package org.hum.resthttp.loader;

@SuppressWarnings("unchecked")
public abstract class AbstractServiceLoader implements ServiceLoader {

	private int sort;
	private static final int DEFAULT_LOADER_SORT = 100;
	
	public AbstractServiceLoader() {
		this(DEFAULT_LOADER_SORT);
	}
	
	public AbstractServiceLoader(int sort) {
		this.sort = sort;
	}

	public <T> Class<T> loadImplementsClassType(Class<T> service) throws ClassNotFoundException {
		return (Class<T>) load(service).getClass();
	}
	
	public int getSort() {
		return sort;
	}
}
