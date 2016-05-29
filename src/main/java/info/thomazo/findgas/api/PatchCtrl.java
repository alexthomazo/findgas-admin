package info.thomazo.findgas.api;

import info.thomazo.findgas.config.ElasticConfig;
import info.thomazo.findgas.dto.Patch;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;
import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.existsQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.PATCH;

@RestController
@RequestMapping("/api/patchs")
public class PatchCtrl {
	private static final Logger logger = LoggerFactory.getLogger(PatchCtrl.class);

	private static final DateTimeFormatter patchFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy HH'h'mm");

	@Autowired
	private Client esClient;

	@Autowired
	private ElasticConfig esConfig;

	@RequestMapping(method = GET)
	public List<Patch> list() {
		SearchResponse res = esClient.prepareSearch(esConfig.getIndexName()).setTypes(esConfig.getPatchType())
				.setQuery(boolQuery()
						.should(boolQuery().mustNot(existsQuery("done")))
						.should(termQuery("done", false))
				)
				.addSort(SortBuilders.fieldSort("date").order(SortOrder.ASC))
				.setSize(10)
				.execute()
				.actionGet();

		List<Patch> patch = new ArrayList<>((int) res.getHits().totalHits());
		res.getHits().forEach(h -> patch.add(map(h)));

		return patch;
	}

	@RequestMapping(path = "/{id}/done",method = PATCH)
	public String doneStation(@PathVariable String id, @RequestBody boolean done) throws IOException {
		esClient.prepareUpdate(esConfig.getIndexName(), esConfig.getPatchType(), id)
				.setDoc(jsonBuilder().startObject()
						.field("done", done)
						.endObject()
				).get();

		logger.info("[PATCH_DONE] [patchId:{}] [done:{}]", id, done);
		return "\"ok\"";
	}


	private Patch map(SearchHit hit) {
		Map<String, Object> fields = hit.getSource();

		String dateStr = null;
		String date = getField(fields, "date");
		if (date != null) {
			dateStr = Instant.parse(date).atZone(ZoneId.systemDefault()).format(patchFormat);
		}

		return Patch.builder()
				.id(hit.getId())
				.date(dateStr)
				.station(getField(fields, "station"))
				.stationName(getField(fields, "stationName"))
				.location(getField(fields, "location"))
				.address(getField(fields, "address"))
				.cp(getField(fields, "cp"))
				.city(getField(fields, "city"))
				.comment(getField(fields, "comment"))
				.name(getField(fields, "name"))
				.build();
	}

	private String getField(Map<String, Object> fields, String name) {
		Object value = fields.get(name);
		if (value instanceof String) return (String) value;
		return null;
	}
}
