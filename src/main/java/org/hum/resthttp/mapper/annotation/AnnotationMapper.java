package org.hum.resthttp.mapper.annotation;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.hum.resthttp.mapper.AbstractMapper;
import org.hum.resthttp.mapper.MethodHolder;
import org.hum.resthttp.mapper.enumtype.MethodEnumType;

public class AnnotationMapper extends AbstractMapper {

	private String scanDirectory = "org.hum";
	
	private final String CONFIG_FILE = "/rest-http-mapper.properties";
	private final String CLASS_EXTEN = ".class";
	
	public void scan() throws Exception {
		// 1.加载scanDirectory目录下的所有class文件
		String scanPakcages = AnnotationMapper.class.getResource("/" + scanDirectory.replaceAll("\\.", File.separator)).getPath();
		List<Class<?>> classes = scanClassFromDirectory(scanPakcages);
		
		// 2.遍历每个class文件的方法
		for (Class<?> clazz : classes) {
			
			for (Method method : clazz.getDeclaredMethods()) {
				// 3.如果方法有httpMethod注解，则进行处理
				processHttpMethodAnnotation(method);
			}
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
	
	private void processHttpMethodAnnotation(Method method) throws InstantiationException, IllegalAccessException {
		if (method.getAnnotation(Get.class) != null) {
			Get getAnnotation = method.getAnnotation(Get.class);
			put(getAnnotation.url(), new MethodHolder(MethodEnumType.GET, method, method.getDeclaringClass().newInstance()));
		} else if (method.getAnnotation(Post.class) != null) {
			Post getAnnotation = method.getAnnotation(Post.class);
			put(getAnnotation.url(), new MethodHolder(MethodEnumType.POST, method, method.getDeclaringClass().newInstance()));
		}
	}
}
	
