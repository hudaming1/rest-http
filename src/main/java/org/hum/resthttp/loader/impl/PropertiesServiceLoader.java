package org.hum.resthttp.loader.impl;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.hum.resthttp.common.util.PropertiesLoader;
import org.hum.resthttp.loader.AbstractServiceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertiesServiceLoader extends AbstractServiceLoader {
	
	private static final Logger logger = LoggerFactory.getLogger(PropertiesServiceLoader.class);
	// 默认读取配置
	private static final String PROPERTIES_FILE = "rest-http-default.properties";
	// 客户端配置(为了提高扩展性，客户端配置会覆盖默认配置)
	private static final String PROPERTIES_EXTEN_FILE = "rest-http.properties";
	
	private static final RestConfig restConfig = new RestConfig();
	
	private static final Map<Class<?>, Object> instancesMap = new ConcurrentHashMap<>();
	
	static {
		try {
			// 1.读取默认配置
			restConfig.server = PropertiesLoader.loadPropertieValue(PROPERTIES_FILE, "server");
			restConfig.serialize = PropertiesLoader.loadPropertieValue(PROPERTIES_FILE, "serialize");
			restConfig.mapper = PropertiesLoader.loadPropertieValue(PROPERTIES_FILE, "mapper");
			restConfig.invoker = PropertiesLoader.loadPropertieValue(PROPERTIES_FILE, "invoker");
			restConfig.invokerHolder = PropertiesLoader.loadPropertieValue(PROPERTIES_FILE, "invokerHolder");
			restConfig.threadpool = PropertiesLoader.loadPropertieValue(PROPERTIES_FILE, "threadpool");
			
			// 2.读取客户端配置
			
			// 3.实例化
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public <T> T load(Class<T> service) {
		return null;
	}
	
	private static class RestConfig {
		public String server;
		public String serialize;
		public String mapper;
		public String invoker;
		public String invokerHolder;
		public String threadpool;
	}
}
