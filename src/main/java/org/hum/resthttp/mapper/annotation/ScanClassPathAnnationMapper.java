package org.hum.resthttp.mapper.annotation;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.hum.resthttp.common.ServerException;
import org.hum.resthttp.conf.Configure;

public class ScanClassPathAnnationMapper extends AnnotationMapper {

	private String scanDirectory = "";
	private final String CONFIG_FILE = "/rest-http-mapper.properties";
	private final String SCAN_PACKAGES_FIELD = "scan-packages";
	private final String CLASS_EXTEN = ".class";

	public ScanClassPathAnnationMapper() throws FileNotFoundException {
		try {
			scanDirectory = Configure.loadPropertieValue(CONFIG_FILE, SCAN_PACKAGES_FIELD);
			if (scanDirectory == null) {
				throw new ServerException("can't find field [" + SCAN_PACKAGES_FIELD + "] in file [" + CONFIG_FILE + "], please check field is exists.");
			}
			scan();
		} catch (FileNotFoundException e) {
			throw new FileNotFoundException("cann't find [" + CONFIG_FILE + "], please check file is exists!");
		} catch (Exception e) {
			throw new ServerException("load [" + CONFIG_FILE + "] error!", e);
		}
	}

	public void scan() throws Exception {
		if (scanDirectory == null || scanDirectory.isEmpty()) {
			throw new IllegalArgumentException("scan packages mustn't be null, please check [" + CONFIG_FILE + "], key: " + SCAN_PACKAGES_FIELD);
		}
		
		// 1.加载scanDirectory目录下的所有class文件
		String scanPakcages = AnnotationMapper.class.getResource("/" + scanDirectory.replaceAll("\\.", File.separator)).getPath();
		List<Class<?>> classes = scanClassFromDirectory(scanPakcages);

		// 2.遍历classes
		for (Class<?> clazz : classes) {

			// 3.遍历每个class文件的方法
			process(clazz, clazz.newInstance());
		}
	}

	// recursion load directory or .class file
	private List<Class<?>> scanClassFromDirectory(String directory) throws ClassNotFoundException {
		File fileObj = new File(directory);
		List<Class<?>> classes = new ArrayList<>();
		File[] listFiles = fileObj.listFiles();
		for (File file : listFiles) {
			if (file.isDirectory()) {
				classes.addAll(scanClassFromDirectory(file.getPath()));
			} else if (file.getName().endsWith(CLASS_EXTEN)) {
				classes.add(classFile2ClassType(file.getPath()));
			}
		}
		return classes;
	}

	private final String CLASS_PATH = AnnotationMapper.class.getResource("/").getPath();
	private Class<?> classFile2ClassType(String path) throws ClassNotFoundException {
		// classFilePath:org/hum/resthttp/common/RestfulException.class
		String classFilePath = path.substring(CLASS_PATH.length(), path.length());
		// classJavaName:org.hum.resthttp.common.RestfulException
		String classJavaName = classFilePath.replaceAll(File.separator, "\\.").substring(0, classFilePath.length() - CLASS_EXTEN.length());
		// file -> ClassType
		return Class.forName(classJavaName);
	}

}
