package info.thomazo.findgas.service;

import info.thomazo.findgas.config.ElasticConfig;
import info.thomazo.findgas.dto.pdv.PdvListe;
import org.apache.commons.io.IOUtils;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.client.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXB;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

/**
 * Import gas station from http://www.prix-carburants.economie.gouv.fr/rubrique/opendata/
 */
@Service
public class ImportService {
	private static final Logger logger = LoggerFactory.getLogger(ImportService.class);

	@Autowired
	private ElasticConfig esConfig;

	@Autowired
	private Client esClient;

	public void importStation(String file) throws IOException {
		initIndex();
		PdvListe stations = JAXB.unmarshal(new File(file), PdvListe.class);

		List<PdvListe.Pdv> toSave = new ArrayList<>(500);
		//adding test station
		toSave.add(createTestStation());

		int nbStations = 0;
		for (PdvListe.Pdv station : stations.getPdv()) {
			toSave.add(station);
			nbStations++;

			if (toSave.size() >= 500) {
				persistStations(toSave);
				toSave.clear();
			}
		}

		persistStations(toSave);
		logger.info("[{}] stations saved", nbStations);

	}

	private void persistStations(List<PdvListe.Pdv> stations) throws IOException {
		if (stations == null || stations.isEmpty()) return;
		BulkRequestBuilder bulkRequest = esClient.prepareBulk();

		for (PdvListe.Pdv station : stations) {
			try {
				String geo = extractGeo(station.getLatitude(), station.getLongitude());

				bulkRequest.add(esClient.prepareIndex(esConfig.getIndexName(), esConfig.getStationType(), station.getId())
						.setSource(jsonBuilder()
								.startObject()
								.field("address", station.getAdresse())
								.field("city", station.getVille())
								.field("cp", station.getCp())
								.field("location", geo)
								.endObject()
						)
				);
			} catch (NumberFormatException e) {
				logger.error("Unable to convert [{}] station geo into numbers : [{}], [{}] at {} {} {}", station.getId(),
						station.getLatitude(), station.getLongitude(), station.getAdresse(), station.getCp(), station.getVille());
			}
		}

		bulkRequest.get();
	}

	private String extractGeo(String latitude, String longitude) {
		if (latitude.isEmpty() || latitude.equals("0") || longitude.isEmpty() || longitude.equals("0")) {
			//do a call to https://adresse.data.gouv.fr/api/
			throw new NumberFormatException();
		} else {
			BigDecimal lat = new BigDecimal(latitude).movePointLeft(5);
			BigDecimal lng = new BigDecimal(longitude).movePointLeft(5);
			return lat.toString() + ", " + lng.toString();
		}

	}

	private PdvListe.Pdv createTestStation() {
		PdvListe.Pdv s = new PdvListe.Pdv();
		s.setId("TEST01");
		s.setAdresse("200 Central Park S");
		s.setCp("NY 10019");
		s.setVille("New York");
		s.setLatitude("40.766937");
		s.setLongitude("-73.979173");
		return s;
	}


	private void initIndex() throws IOException {
		String indexName = esConfig.getIndexName();
		IndicesExistsResponse idxExistsRes = esClient.admin().indices().prepareExists(indexName).get();

		if (idxExistsRes.isExists()) {
			logger.info("Index [{}] already exists", indexName);
			return;
		}

		logger.info("Index [{}] not existing, let's create it", indexName);
		//create index
		String stationMapping = IOUtils.toString(getClass().getResourceAsStream("/station_mapping.json"), StandardCharsets.UTF_8);
		String commentMapping = IOUtils.toString(getClass().getResourceAsStream("/comment_mapping.json"), StandardCharsets.UTF_8);
		esClient.admin().indices().prepareCreate(indexName)
				.addMapping(esConfig.getStationType(), stationMapping)
				.addMapping(esConfig.getCommentType(), commentMapping)
				.get();

		logger.info("Index [{}] created", indexName);
	}
}
