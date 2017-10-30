package org.hum.resthttp.loader;

public interface ServiceLoader {
	
	public int getSort();
	
	public <T> T load(Class<T> service);
}
