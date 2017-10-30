package org.hum.resthttp.spring;

import org.w3c.dom.Element;
import org.hum.resthttp.transport.Server;
import org.hum.resthttp.transport.ServerFactory;
import org.hum.resthttp.transport.config.ServerConfig;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;

public class HttpServerBeanDefinition extends AbstractSingleBeanDefinitionParser {

	@Override
	protected Class<?> getBeanClass(Element element) {
		return Server.class;
	}
	
	@Override
    protected void doParse(Element element, BeanDefinitionBuilder bean) {  
        String port = element.getAttribute("port");  
  
        Server server = ServerFactory.get();
		server.start(new ServerConfig(Integer.parseInt(port)));
    }  
}
