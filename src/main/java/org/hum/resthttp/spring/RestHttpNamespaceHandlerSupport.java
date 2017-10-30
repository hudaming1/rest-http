package org.hum.resthttp.spring;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class RestHttpNamespaceHandlerSupport extends NamespaceHandlerSupport {

	@Override
	public void init() {
		registerBeanDefinitionParser("server", new HttpServerBeanDefinition());
		registerBeanDefinitionParser("mapper", new MapperBeanDefinition());
	}
}
