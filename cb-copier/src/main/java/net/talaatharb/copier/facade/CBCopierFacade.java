package net.talaatharb.copier.facade;

import com.fasterxml.jackson.databind.JsonNode;

public interface CBCopierFacade {

	boolean connect();

	boolean deleteWithFilter(String filter);

	String fetchUsingQuery(String query);

	boolean insert(JsonNode data);

	boolean upsert(JsonNode data);
}
