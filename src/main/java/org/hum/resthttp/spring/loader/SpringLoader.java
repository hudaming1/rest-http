package org.hum.resthttp.spring.loader;

import org.hum.resthttp.loader.AbstractServiceLoader;
import org.hum.resthttp.loader.ServiceLoaderHolder;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringLoader extends AbstractServiceLoader implements ApplicationContextAware {
	
	private ApplicationContext applicationContext;

	public SpringLoader() { }
	public SpringLoader(int sort) {
		super(sort);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
		// 将自己写好的Loader放到holder中
		ServiceLoaderHolder.createServiceLoader(this);
	}

	@Override
	public <T> T load(Class<T> service) {
		return applicationContext.getBean(service);
	}
}
