package net.talaatharb.copier.service;

import org.springframework.data.couchbase.core.CouchbaseTemplate;

import com.fasterxml.jackson.databind.JsonNode;

public interface CBCopierService {

	boolean deleteWithFilter(CouchbaseTemplate template, String filter);

	String fetchUsingQuery(CouchbaseTemplate template, String query);

	boolean insert(CouchbaseTemplate template, JsonNode data, String oldBucket);

	boolean upsert(CouchbaseTemplate template, JsonNode data, String oldBucket);
}
