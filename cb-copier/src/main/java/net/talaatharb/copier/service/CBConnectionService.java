package net.talaatharb.copier.service;

import java.io.IOException;
import java.util.Properties;

import org.springframework.data.couchbase.core.CouchbaseTemplate;

public interface CBConnectionService {

	String SCOPE = "scope";
	String BUCKET = "bucket";
	String PASS = "pass";
	String USER = "user";
	String CONNECTION = "connection";

	String SRC_CONNECTION_FILE = "./src.properties";

	String DST_CONNECTION_FILE = "./dst.properties";

	CouchbaseTemplate connect(String connectionFile);

	void editConnectionDetails(Properties properties, String connectionFile);

	Properties loadConnectionDetails(String string) throws IOException;
}
