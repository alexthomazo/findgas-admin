package info.thomazo.findgas.service;

import info.thomazo.findgas.config.ElasticConfig;
import org.apache.commons.io.IOUtils;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.client.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

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
	}


	private void initIndex() throws IOException {
		String indexName = esConfig.getIndexName();
		IndicesExistsResponse idxExistsRes = esClient.admin().indices().prepareExists(indexName).get();

		if (idxExistsRes.isExists()) {
			logger.info("Index [{}] already exists", indexName);
			return;
		}

		logger.info("Index [{}] not existing, create with type [{}]", indexName, esConfig.getIndexType());
		//create index
		String mapping = IOUtils.toString(getClass().getResourceAsStream("/type_mapping.json"), StandardCharsets.UTF_8);
		esClient.admin().indices().prepareCreate(indexName)
				.addMapping(esConfig.getIndexType(), mapping)
				.get();

		logger.info("Index [{}] created", indexName);
	}
}
