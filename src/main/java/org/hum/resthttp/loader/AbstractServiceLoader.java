package org.hum.resthttp.loader;

public abstract class AbstractServiceLoader implements ServiceLoader {

	private int sort;
	private static final int DEFAULT_LOADER_SORT = 100;
	
	public AbstractServiceLoader() {
		this(DEFAULT_LOADER_SORT);
	}
	
	public AbstractServiceLoader(int sort) {
		this.sort = sort;
	}
	
	public int getSort() {
		return sort;
	}
}
