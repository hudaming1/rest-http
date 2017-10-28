package org.hum.resthttp.common;

public class ServiceLoader {

	public static <T> T load(Class<T> service) {
		if (service == null) {
			throw new NullPointerException("service param mustn't be null");
		}
		java.util.ServiceLoader<T> serviceLoader = java.util.ServiceLoader.load(service);
		if (serviceLoader == null || serviceLoader.iterator() == null) {
			throw new IllegalArgumentException("service " + service.getName() + " not found!");
		}
		try {
			return serviceLoader.iterator().next();
		} catch (Exception ce) {
			throw new IllegalArgumentException("service " + service.getName() + " not found!", ce);
		}
	}
}
