package org.hum.resthttp.mapper.annotation;

import java.lang.reflect.Method;

import org.hum.resthttp.mapper.AbstractMapper;
import org.hum.resthttp.mapper.MethodHolder;
import org.hum.resthttp.mapper.enumtype.MethodEnumType;

public class AnnotationMapper extends AbstractMapper {
	
	public void process(Object instance) throws InstantiationException, IllegalAccessException {
		process(instance.getClass(), instance);
	}
	
	public void process(Class<?> clazz, Object instance) throws InstantiationException, IllegalAccessException {
		for (Method method : clazz.getDeclaredMethods()) {
			processHttpMethodAnnotation(method, instance);
		}
	}
	
	private void processHttpMethodAnnotation(Method method, Object instance) throws InstantiationException, IllegalAccessException {
		if (method.getAnnotation(Get.class) != null) {
			Get getAnnotation = method.getAnnotation(Get.class);
			put(getAnnotation.url(), new MethodHolder(MethodEnumType.GET, method, instance));
		} else if (method.getAnnotation(Post.class) != null) {
			Post getAnnotation = method.getAnnotation(Post.class);
			put(getAnnotation.url(), new MethodHolder(MethodEnumType.POST, method, instance));
		}
	}
}
	
