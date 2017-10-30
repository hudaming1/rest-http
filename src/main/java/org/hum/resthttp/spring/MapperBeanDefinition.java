package org.hum.resthttp.spring;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.w3c.dom.Element;

public class MapperBeanDefinition extends AbstractSingleBeanDefinitionParser {

	@Override
	protected Class<?> getBeanClass(Element element) {
		return SpringServerBeanFactory.class;
	}
	
	@Override
    protected void doParse(Element element, BeanDefinitionBuilder bean) {  
        
    }  
}
