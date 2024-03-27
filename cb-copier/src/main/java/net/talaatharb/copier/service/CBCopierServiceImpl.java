package net.talaatharb.copier.service;

import org.springframework.data.couchbase.core.CouchbaseTemplate;

import com.couchbase.client.java.query.QueryResult;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CBCopierServiceImpl implements CBCopierService {

	@Override
	public boolean deleteWithFilter(CouchbaseTemplate template, String filter) {
		final QueryResult result = template.getCouchbaseClientFactory().getCluster()
				.query("delete from " + template.getBucketName() + " where " + filter);
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
	public boolean insert(CouchbaseTemplate template, JsonNode data) {
		return false;
	}

	@Override
	public boolean upsert(CouchbaseTemplate template, JsonNode data) {
		return false;
	}

}
