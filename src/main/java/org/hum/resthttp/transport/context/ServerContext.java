package org.hum.resthttp.transport.context;

public class ServerContext<T> {
	
	private ThreadLocal<T> requestThreadLocal = new ThreadLocal<T>();
	
	public void set(T request) {
		requestThreadLocal.set(request);
	}
	
	public T get() {
		return requestThreadLocal.get();
	}
	
	public void remove() {
		requestThreadLocal.remove();
	}
}
