package org.hum.resthttp.loader;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.hum.resthttp.loader.impl.PropertiesServiceLoader;

/**
 * ServiceLoaderHolder可持有多个类型加载器（默认情况下仅有SpiLoader），在加载类型时，会
 * 根据顺序和当前持有的serviceLoader逐个寻找类实例。
 */
@SuppressWarnings("unchecked")
public class ServiceLoaderHolder {

	private static Map<Class<?>, Object> serviceCache = new ConcurrentHashMap<>();
	private static List<ServiceLoader> serviceLoaders = new CopyOnWriteArrayList<>();
	private static Object lock = new Object();

	static {
		// 弃用SPI加载：配置太零散，一个文件仅相当于一个键值对。
		// createServiceLoader(new SpiLoader());
		
		// 目前还是考虑使用.properties作为配置文件，可以将配置集中管理化
		createServiceLoader(new PropertiesServiceLoader());
	}

	public static <T> T load(Class<T> service) {
		if (service == null) {
			throw new IllegalArgumentException("service loader mustn't be null");
		}
		Object cacheInstance = serviceCache.get(service);
		if (cacheInstance != null) {
			return (T) cacheInstance;
		}
		synchronized (lock) {
			cacheInstance = serviceCache.get(service);
			if (cacheInstance != null) {
				return (T) cacheInstance;
			}
			for (ServiceLoader serviceLoader : serviceLoaders) {
				T instance = serviceLoader.load(service);
				if (instance != null) {
					serviceCache.put(service, instance);
					return instance;
				}
			}
		}
		throw new IllegalArgumentException("can't find service type[" + service.getName() + "]");
	}

	public static synchronized void createServiceLoader(ServiceLoader serviceLoader) {
		if (serviceLoader == null) {
			throw new IllegalArgumentException("service loader mustn't be null");
		}
		// 
		serviceLoaders.add(serviceLoader);
		// make serviceLoaders sort
		Collections.sort(serviceLoaders, new Comparator<ServiceLoader>() {
			@Override
			public int compare(ServiceLoader o1, ServiceLoader o2) {
				if (o1.getSort() == o2.getSort()) {
					return 0;
				} else if (o1.getSort() > o2.getSort()) {
					return 1;
				} else {
					return -1;
				}
			}
		});
	}
}
