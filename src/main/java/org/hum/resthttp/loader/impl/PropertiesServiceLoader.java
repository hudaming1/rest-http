package org.hum.resthttp.loader.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.hum.resthttp.common.util.PropertiesLoader;
import org.hum.resthttp.conf.PropertiesConfigKey;
import org.hum.resthttp.loader.AbstractServiceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("unchecked")
public class PropertiesServiceLoader extends AbstractServiceLoader {
	
	private static final Logger logger = LoggerFactory.getLogger(PropertiesServiceLoader.class);
	// 默认读取配置
	private static final String PROPERTIES_FILE = "/rest-http-default.properties";
	// 客户端配置(为了提高扩展性，客户端配置会覆盖默认配置)
	private static final String PROPERTIES_EXTEN_FILE = "/rest-http.properties";
	// instance map
	private static final Map<Class<?>, Object> INSTANCES_MAP = new ConcurrentHashMap<>();
	
	{
		RestConfig restConfig = new RestConfig();
		// 1.读取默认配置
		try {
			restConfig = loadProperties(restConfig, PROPERTIES_FILE);
			logger.info("load properties[" + PROPERTIES_FILE + "] complete");
		} catch (IOException e) {
			logger.error("load properties [" + PROPERTIES_FILE + "] occured exception", e);
		}
		// 2.读取客户端配置(覆盖框架默认配置)
		try {
			restConfig = loadProperties(restConfig, PROPERTIES_EXTEN_FILE);
			logger.info("load properties[" + PROPERTIES_EXTEN_FILE + "] complete");
		} catch (IOException e) {
			logger.error("load properties [" + PROPERTIES_EXTEN_FILE + "] occured exception", e);
		}
		// 3.实例化 (目前Properties方式和SPI实例化一个类时使用的方式一样，都是只能调用无参构造方法 bad smell)
		//loadClassAndInstance(restConfig);
		//logger.info("load class and instances complete");
		
		// 4.验证必要加载项
		//validate(INSTANCES_MAP);
		//logger.info("load instances is ok");
	}
	
	private void validate(Map<Class<?>, Object> instancesMap) {
		for (Entry<Class<?>, Object> entry : instancesMap.entrySet()) {
			logger.info("[" + entry.getKey().getName() + "] class has been load");
		}
	}
	
	private void loadClassAndInstance(RestConfig restConfig) {
		if (restConfig == null) {
			return;
		}
		loadClassAndInstance(restConfig.threadpoolClass);
		loadClassAndInstance(restConfig.invokerHolderClass);
		loadClassAndInstance(restConfig.serializeClass);
		loadClassAndInstance(restConfig.invokerClass);
		loadClassAndInstance(restConfig.mapperClass);
		loadClassAndInstance(restConfig.serverClass);
	}
	
	private void loadClassAndInstance(String className) {
		if (className == null || className.isEmpty()) {
			logger.info("class name is null, ignore load..");
			return;
		}
		try {
			Class<?>serviceClassType = Class.forName(className);
			load(serviceClassType);
			logger.info("load class and instance [" + serviceClassType.getName() + "]");
		} catch (ClassNotFoundException e) {
			logger.error("load class[" + className + "] occured exception, class not found!", e);
		}
	}
	
	private RestConfig loadProperties(RestConfig restConfig, String file) throws UnsupportedEncodingException, IOException {
		if (PropertiesLoader.class.getResource(file) == null) {
			logger.info("cann't find file[" + file + "], ignore...");
			return restConfig;
		}
		
		String serverClassName = PropertiesLoader.loadPropertieValue(file, PropertiesConfigKey.server);
		if (serverClassName != null & !serverClassName.isEmpty()) {
			restConfig.serverClass = serverClassName;
		}
		String serializeClassName = PropertiesLoader.loadPropertieValue(file, PropertiesConfigKey.serialize);
		if (serializeClassName != null & !serverClassName.isEmpty()) {
			restConfig.serializeClass = serializeClassName;
		}
		String mapperClassName = PropertiesLoader.loadPropertieValue(file, PropertiesConfigKey.mapper);
		if (mapperClassName != null && !mapperClassName.isEmpty()) {
			restConfig.mapperClass = PropertiesLoader.loadPropertieValue(file, PropertiesConfigKey.mapper);
		}
		String invokerClassName = PropertiesLoader.loadPropertieValue(file, PropertiesConfigKey.invoker);
		if (invokerClassName != null && !invokerClassName.isEmpty()) {
			restConfig.invokerClass = PropertiesLoader.loadPropertieValue(file, PropertiesConfigKey.invoker);
		}
		String invokerHolderClassName = PropertiesLoader.loadPropertieValue(file, PropertiesConfigKey.invokerHolder);
		if (invokerHolderClassName != null && !invokerHolderClassName.isEmpty()) {
			restConfig.invokerHolderClass = PropertiesLoader.loadPropertieValue(file, PropertiesConfigKey.invokerHolder);
		}
		String threadpoolClassName = PropertiesLoader.loadPropertieValue(file, PropertiesConfigKey.threadpool);
		if (threadpoolClassName != null && !threadpoolClassName.isEmpty()) {
			restConfig.threadpoolClass = PropertiesLoader.loadPropertieValue(file, PropertiesConfigKey.threadpool);
		}
		return restConfig;
	}

	@Override
	public <T> T load(Class<T> service) {
		Object instances = INSTANCES_MAP.get(service);
		if (instances == null) {
			try {
				instances = service.newInstance();
				INSTANCES_MAP.put(service, instances);
				return (T) instances;
			} catch (InstantiationException | IllegalAccessException e) {
				logger.error("load class[" + service.getName() + "] and create instances occured exception", e);
			}
		}
		return (T) INSTANCES_MAP.get(service);
	}
	
	// bad smell 只能这么设计吗？没有更好的办法了吗？多么繁琐...如果一旦增加一个字段，上面代码都要改....
	private static class RestConfig {
		public String serverClass;
		public String serializeClass;
		public String mapperClass;
		public String invokerClass;
		public String invokerHolderClass;
		public String threadpoolClass;
	}
}
