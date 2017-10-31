package org.hum.resthttp.common.util;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class PropertiesLoader {

	private static Map<String, Map<String, String>> propertiesMap = new ConcurrentHashMap<>();
	private static Object lock = new Object();

	public static String loadPropertieValue(String file, String key) throws UnsupportedEncodingException, IOException {
		if (propertiesMap.get(file) != null) {
			return propertiesMap.get(file).get(key);
		}
		synchronized (lock) {
			if (propertiesMap.get(file) != null) {
				return propertiesMap.get(file).get(key);
			}
			Map<String, String> propMap = loadProperties(file);
			propertiesMap.put(file, propMap);
			return propMap.get(key);
		}
	}
	
	public static Map<String, String> loadProperties(String file) throws UnsupportedEncodingException, IOException {
		synchronized (lock) {
			// 1.如果加载过配置，则从内存中直接读取并解析
			if (propertiesMap.get(file) != null) {
				return propertiesMap.get(file);
			}
			// 2.如果没有加载过，则在内存中需要新建一个对象
			Properties properties = new Properties();
			properties.load(new InputStreamReader(PropertiesLoader.class.getResourceAsStream(file), "UTF-8"));
			Map<String, String> propMap = loadProperties(properties);
			propertiesMap.put(file, propMap);
			return propMap;
		}
	}
	
	public static Map<String, String> loadProperties(Properties properties) {
		if (properties == null || properties.isEmpty()) {
			return Collections.emptyMap();
		}
		Map<String, String> propMap = new HashMap<>();
		for (Entry<Object, Object> entry : properties.entrySet()) {
			if (entry.getKey() == null || entry.getKey().toString().isEmpty()) {
				continue;
			}
			String key = entry.getKey().toString();
			String value = entry.getValue() == null ? "" : entry.getValue().toString();
			propMap.put(key, value);
		}
		return propMap;
	}
}
