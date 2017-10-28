package org.hum.resthttp.conf;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class Configure {

	private static Map<String, Properties> propertiesMap = new ConcurrentHashMap<>();

	// TODO
	public static String loadPropertieValue(String file, String key) throws UnsupportedEncodingException, IOException {
		if (propertiesMap.get(file) != null) {
			return propertiesMap.get(file).getProperty(key);
		}
		Properties properties = new Properties();
		properties.load(new InputStreamReader(Configure.class.getResourceAsStream(file), "UTF-8"));
		propertiesMap.putIfAbsent(file, properties);
		return properties.getProperty(key);
	}
}
