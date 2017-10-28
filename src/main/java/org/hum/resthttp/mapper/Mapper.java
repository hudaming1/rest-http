package org.hum.resthttp.mapper;

public interface Mapper {
	
	public void scan() throws Exception;

	public MethodHolder get(String url);
}
