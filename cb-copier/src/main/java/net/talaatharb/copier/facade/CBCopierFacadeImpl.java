package net.talaatharb.copier.facade;

import org.springframework.data.couchbase.core.CouchbaseTemplate;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.RequiredArgsConstructor;
import net.talaatharb.copier.service.CBConnectionService;
import net.talaatharb.copier.service.CBCopierService;

@RequiredArgsConstructor
public class CBCopierFacadeImpl implements CBCopierFacade {

	private final CBConnectionService connectionService;
	private final CBCopierService copierService;

	private CouchbaseTemplate srcTemplate;
	private CouchbaseTemplate dstTemplate;

	@Override
	public boolean connect() {
		srcTemplate = connectionService.connect(CBConnectionService.SRC_CONNECTION_FILE);
		dstTemplate = connectionService.connect(CBConnectionService.DST_CONNECTION_FILE);
		return srcTemplate != null && dstTemplate != null;
	}

	@Override
	public boolean deleteWithFilter(String filter) {
		return copierService.deleteWithFilter(dstTemplate, filter);
	}

	@Override
	public String fetchUsingQuery(String query) {
		return copierService.fetchUsingQuery(srcTemplate, query);
	}

	@Override
	public boolean insert(JsonNode data) {
		return copierService.insert(dstTemplate, data, srcTemplate.getBucketName());
	}

	@Override
	public boolean upsert(JsonNode data) {
		return copierService.upsert(dstTemplate, data, srcTemplate.getBucketName());
	}

}
