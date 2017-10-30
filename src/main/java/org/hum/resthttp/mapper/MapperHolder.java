package org.hum.resthttp.mapper;

import org.hum.resthttp.loader.ServiceLoaderHolder;

public class MapperHolder {

	private static Mapper mapper = ServiceLoaderHolder.load(Mapper.class);
	
	public static MethodHolder get(String url) {
		return mapper.get(url);
	}
	
	// TODO 这里设计的不好，object escape
	public static final Mapper getMapper() {
		return mapper;
	}
}
