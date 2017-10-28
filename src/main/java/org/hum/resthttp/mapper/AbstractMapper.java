package org.hum.resthttp.mapper;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractMapper implements Mapper {

	private Map<String, MethodHolder> methodHolderMapper = new ConcurrentHashMap<>();
	
	public void put(String url, MethodHolder holder) {
		methodHolderMapper.put(url, holder);
	}
	
	@Override
	public MethodHolder get(String url) {
		return methodHolderMapper.get(url);
	}
}
