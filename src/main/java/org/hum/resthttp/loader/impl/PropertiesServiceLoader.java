package org.hum.resthttp.loader.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.hum.resthttp.common.util.PropertiesLoader;
import org.hum.resthttp.conf.PropertiesConfigKey;
import org.hum.resthttp.executors.ThreadPoolFactory;
import org.hum.resthttp.invoker.holder.InvokerHolder;
import org.hum.resthttp.invoker.wrapper.InvokerWrapper;
import org.hum.resthttp.loader.AbstractServiceLoader;
import org.hum.resthttp.mapper.Mapper;
import org.hum.resthttp.serialization.Serialization;
import org.hum.resthttp.transport.Server;
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
	// key-interfaceClass, value-implementsClass
	private static final Map<Class<?>, Class<?>> IMPLEMENTS_CLASS_MAP = new ConcurrentHashMap<Class<?>, Class<?>>();
	
	private final Object LOAD_LOCK = new Object();
	
	{
		// 1.读取默认配置
		try {
			loadProperties(PROPERTIES_FILE);
			logger.info("load properties[" + PROPERTIES_FILE + "] complete");
		} catch (IOException e) {
			logger.error("load properties [" + PROPERTIES_FILE + "] occured exception", e);
		}
		// 2.读取客户端配置(覆盖框架默认配置)
		try {
			loadProperties(PROPERTIES_EXTEN_FILE);
			logger.info("load properties[" + PROPERTIES_EXTEN_FILE + "] complete");
		} catch (IOException e) {
			logger.error("load properties [" + PROPERTIES_EXTEN_FILE + "] occured exception", e);
		}
	}
	
	private void loadProperties(String file) throws UnsupportedEncodingException, IOException {
		if (PropertiesLoader.class.getResource(file) == null) {
			logger.info("cann't find file[" + file + "], ignore...");
			return ;
		}
		
		String serverClassName = PropertiesLoader.loadPropertieValue(file, PropertiesConfigKey.server);
		if (serverClassName != null && !serverClassName.isEmpty()) {
			registImplementsClass(Server.class, serverClassName);
		}
		String serializeClassName = PropertiesLoader.loadPropertieValue(file, PropertiesConfigKey.serialize);
		if (serializeClassName != null && !serverClassName.isEmpty()) {
			registImplementsClass(Serialization.class, serializeClassName);
		}
		String mapperClassName = PropertiesLoader.loadPropertieValue(file, PropertiesConfigKey.mapper);
		if (mapperClassName != null && !mapperClassName.isEmpty()) {
			registImplementsClass(Mapper.class, mapperClassName);
		}
		String invokerClassName = PropertiesLoader.loadPropertieValue(file, PropertiesConfigKey.invoker);
		if (invokerClassName != null && !invokerClassName.isEmpty()) {
			registImplementsClass(InvokerWrapper.class, invokerClassName);
		}
		String invokerHolderClassName = PropertiesLoader.loadPropertieValue(file, PropertiesConfigKey.invokerHolder);
		if (invokerHolderClassName != null && !invokerHolderClassName.isEmpty()) {
			registImplementsClass(InvokerHolder.class, invokerHolderClassName);
		}
		String threadpoolClassName = PropertiesLoader.loadPropertieValue(file, PropertiesConfigKey.threadpool);
		if (threadpoolClassName != null && !threadpoolClassName.isEmpty()) {
			registImplementsClass(ThreadPoolFactory.class, threadpoolClassName);
		}
	}
	
	private void registImplementsClass(Class<?> interfaceType, String implementsClassName) {
		try {
			IMPLEMENTS_CLASS_MAP.put(interfaceType, Class.forName(implementsClassName));
		} catch (ClassNotFoundException e) {
			logger.error("find interface type[" + interfaceType.getName() + "] occured exception, class not found:" + implementsClassName);
		}
	}

	@Override
	public <T> T load(Class<T> service) throws ClassNotFoundException {
		Object instances = INSTANCES_MAP.get(service);
		if (instances != null) {
			return (T) instances;
		}
		synchronized (LOAD_LOCK) {
			try {
				
				// 1.先找到对应实现类
				Class<?> implementsClass = IMPLEMENTS_CLASS_MAP.get(service);
				
				if (implementsClass == null) {
					throw new ClassNotFoundException("cann't load class type[" + service.getName() + "], because no implements class found ");
				}

				// 2.根据实现类进行实例化
				instances = implementsClass.newInstance();

				// 3.再放到Map中
				INSTANCES_MAP.put(service, instances);
				return (T) instances;
			} catch (InstantiationException | IllegalAccessException e) {
				logger.error("load class[" + service.getName() + "] and create instances occured exception", e);
			}
			return (T) INSTANCES_MAP.get(service);
		}
	}
}
