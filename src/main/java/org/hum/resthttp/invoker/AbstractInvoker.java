package org.hum.resthttp.invoker;

import java.lang.reflect.Method;

public abstract class AbstractInvoker implements Invoker {

	protected Method method;
	protected Object instance;
	
	public AbstractInvoker(Method method, Object instance) {
		this.method = method;
		this.instance = instance;
	}
}
