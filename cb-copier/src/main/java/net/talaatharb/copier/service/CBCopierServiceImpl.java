package net.talaatharb.copier.service;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.StreamSupport;

import org.springframework.data.couchbase.core.CouchbaseTemplate;

import com.couchbase.client.java.query.QueryResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CBCopierServiceImpl implements CBCopierService {

	private final ObjectMapper objectMapper;

	@Override
	public boolean deleteWithFilter(CouchbaseTemplate template, String filter) {
		final QueryResult result = template.getCouchbaseClientFactory().getCluster()
				.query("DELETE FROM " + template.getBucketName() + " WHERE " + filter);
		return result != null;
	}

	@Override
	public String fetchUsingQuery(CouchbaseTemplate template, String query) {
		final QueryResult result = template.getCouchbaseClientFactory().getCluster().query(query);

		// Assuming the result is not null and has content
		if (result != null && !result.rowsAsObject().isEmpty()) {
			// Convert the result to a String for simplicity
			return result.rowsAsObject().toString();
		}

		return "{\"result\":\"No results\"}";
	}

	@Override
	public boolean insert(CouchbaseTemplate template, JsonNode data, String oldBucket) {
		if (data instanceof final ArrayNode arrayData) {
			final List<String> values = StreamSupport
					.stream(Spliterators.spliterator(arrayData.elements(), arrayData.size(), Spliterator.ORDERED),
							false)
					.map(element -> element.findValue(oldBucket)).map(this::mapToEntry).filter(e -> e != null)
					.map(entry -> String.format("('%s', %s)", entry.getKey(), entry.getValue())).toList();

			final int count = values.size();
			final StringBuilder builder = new StringBuilder("INSERT INTO ");
			builder.append(template.getBucketName());
			builder.append(" (KEY,VALUE) \n VALUES ");
			for (int i = 0; i < count - 1; i++) {
				builder.append(values.get(i));
				builder.append(",");
			}
			builder.append(values.get(count - 1));
			builder.append(";");
			template.getCouchbaseClientFactory().getCluster().query(builder.toString());
			return true;
		}
		return false;
	}

	private Entry<String, String> mapToEntry(JsonNode v) {
		try {
			return new AbstractMap.SimpleImmutableEntry<>(v.findValue("id").asText(),
					objectMapper.writeValueAsString(v));
		} catch (final JsonProcessingException e) {
			return null;
		}
	}

	@Override
	public boolean upsert(CouchbaseTemplate template, JsonNode data, String oldBucket) {
		if (data instanceof final ArrayNode arrayData) {
			final List<String> values = StreamSupport
					.stream(Spliterators.spliterator(arrayData.elements(), arrayData.size(), Spliterator.ORDERED),
							false)
					.map(element -> element.findValue(oldBucket)).map(this::mapToEntry).filter(e -> e != null)
					.map(entry -> String.format("('%s', %s)", entry.getKey(), entry.getValue())).toList();

			final int count = values.size();
			final StringBuilder builder = new StringBuilder("UPSERT INTO ");
			builder.append(template.getBucketName());
			builder.append(" (KEY,VALUE) \n VALUES ");
			for (int i = 0; i < count - 1; i++) {
				builder.append(values.get(i));
				builder.append(",");
			}
			builder.append(values.get(count - 1));
			builder.append(";");
			template.getCouchbaseClientFactory().getCluster().query(builder.toString());
			return true;
		}
		return false;
	}

}
