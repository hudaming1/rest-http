package org.hum.resthttp.spring;

import org.hum.resthttp.transport.config.ServerConfig;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.w3c.dom.Element;

public class HttpServerBeanDefinition extends AbstractSingleBeanDefinitionParser {

	@Override
	protected Class<?> getBeanClass(Element element) {
		return SpringServerBeanFactory.class;
	}
	
	@Override
    protected void doParse(Element element, BeanDefinitionBuilder bean) {  
        String port = element.getAttribute("port");  
		bean.addPropertyValue("serverConfig", new ServerConfig(Integer.parseInt(port)));
    }  
}
