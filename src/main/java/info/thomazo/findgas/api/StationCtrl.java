package info.thomazo.findgas.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import info.thomazo.findgas.config.ElasticConfig;
import info.thomazo.findgas.dto.Station;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping("/api/stations")
public class StationCtrl {

	@Autowired
	private Client esClient;

	@Autowired
	private ElasticConfig esConfig;

	@Autowired
	private ObjectMapper mapper;

	@RequestMapping(method = POST)
	public Station post(@RequestBody Station station) throws JsonProcessingException, ExecutionException, InterruptedException {
		esClient.prepareIndex(esConfig.getIndexName(), esConfig.getStationType())
				.setSource(mapper.writeValueAsBytes(station))
				.execute()
				.get();

		return station;
	}

	@RequestMapping(path = "/{id}", method = GET)
	public Station get(@PathVariable String id) throws IOException {
		GetResponse res = esClient.prepareGet(esConfig.getIndexName(), esConfig.getStationType(), id)
				.get();

		if (res.isSourceEmpty()) return null;
		Station station = mapper.readValue(res.getSourceAsString(), Station.class);
		station.setId(res.getId());
		return station;
	}

	@RequestMapping(path = "/{id}", method = PUT)
	public Station update(@PathVariable String id, @RequestBody Station station) throws JsonProcessingException, ExecutionException, InterruptedException {
		esClient.prepareUpdate(esConfig.getIndexName(), esConfig.getStationType(), id)
				.setDoc(mapper.writeValueAsBytes(station))
				.execute()
				.get();

		return station;
	}

	@RequestMapping(path = "/{id}", method = DELETE)
	public boolean delete(@PathVariable String id) throws IOException, ExecutionException, InterruptedException {
		esClient.prepareDelete(esConfig.getIndexName(), esConfig.getStationType(), id)
				.execute()
				.get();

		return true;
	}

}
