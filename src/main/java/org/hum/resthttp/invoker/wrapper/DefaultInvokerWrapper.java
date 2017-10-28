package org.hum.resthttp.invoker.wrapper;

import java.lang.reflect.Method;

import org.hum.resthttp.invoker.DefaultInvoker;
import org.hum.resthttp.invoker.Invoker;

public class DefaultInvokerWrapper implements InvokerWrapper {

	@Override
	public Invoker create(Method method, Object instance) {
		return new DefaultInvoker(method, instance);
	}
}
