package info.thomazo.findgas.api;

import info.thomazo.findgas.config.ElasticConfig;
import info.thomazo.findgas.dto.Comment;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/api/comments")
public class CommentsCtrl {
	private static final DateTimeFormatter commentFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy HH'h'mm");


	@Autowired
	private Client esClient;

	@Autowired
	private ElasticConfig esConfig;

	@RequestMapping(method = GET)
	public List<Comment> list() {
		SearchResponse res = esClient.prepareSearch(esConfig.getIndexName()).setTypes(esConfig.getCommentType())
				.setQuery(QueryBuilders.matchAllQuery())
				.addSort(SortBuilders.fieldSort("date").order(SortOrder.DESC))
				.setSize(50)
				.execute()
				.actionGet();

		List<Comment> comments = new ArrayList<>((int) res.getHits().totalHits());
		res.getHits().forEach(h -> comments.add(map(h)));

		return comments;
	}

	private Comment map(SearchHit hit) {
		Map<String, Object> fields = hit.getSource();

		String dateStr = null;
		String date = getField(fields, "date");
		if (date != null) {
			dateStr = Instant.parse(date).atZone(ZoneId.systemDefault()).format(commentFormat);
		}

		return Comment.builder()
				.date(dateStr)
				.comment(getField(fields, "comment"))
				.name(getField(fields, "name"))
				.gas((List<String>) fields.get("gas"))
				.build();
	}

	private String getField(Map<String, Object> fields, String name) {
		Object value = fields.get(name);
		if (value instanceof String) return (String) value;
		return null;
	}


}
