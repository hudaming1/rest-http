package org.hum.resthttp.spring.mapper;

import org.hum.resthttp.mapper.MapperHolder;
import org.hum.resthttp.mapper.annotation.AnnotationMapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

public class SpringBeanMapper implements BeanPostProcessor {

	private static AnnotationMapper mapper = (AnnotationMapper) MapperHolder.getMapper();
	
	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		try {
			mapper.process(bean);
			System.out.println(bean);
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return bean;
	}
}
