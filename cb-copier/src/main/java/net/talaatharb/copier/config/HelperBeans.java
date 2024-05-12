package net.talaatharb.copier.config;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import net.talaatharb.copier.facade.CBCopierFacade;
import net.talaatharb.copier.facade.CBCopierFacadeImpl;
import net.talaatharb.copier.service.CBConnectionService;
import net.talaatharb.copier.service.CBConnectionServiceImpl;
import net.talaatharb.copier.service.CBCopierService;
import net.talaatharb.copier.service.CBCopierServiceImpl;

public class HelperBeans {

	private HelperBeans() {
	}

	public static final ObjectMapper buildObjectMapper() {
		return JsonMapper.builder().enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS) // ignore case
				.enable(SerializationFeature.INDENT_OUTPUT) // pretty format for json
				.addModule(new JavaTimeModule()) // time module
				.build();
	}

	public static final CBConnectionService buildConnectionService() {
		return new CBConnectionServiceImpl();
	}

	public static final CBCopierService buildCopierService() {
		return new CBCopierServiceImpl(buildObjectMapper());
	}

	public static final CBCopierFacade buildCopierFacade(CBConnectionService connectionService,
			CBCopierService copierService) {
		return new CBCopierFacadeImpl(connectionService, copierService);
	}

}
