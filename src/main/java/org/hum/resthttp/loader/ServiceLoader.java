package org.hum.resthttp.loader;

public interface ServiceLoader {
	
	public int getSort();

	public <T> Class<T> loadImplementsClassType(Class<T> service) throws ClassNotFoundException;
	
	public <T> T load(Class<T> service) throws ClassNotFoundException;
}
