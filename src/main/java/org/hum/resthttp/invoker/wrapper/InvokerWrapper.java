package org.hum.resthttp.invoker.wrapper;

import java.lang.reflect.Method;

import org.hum.resthttp.invoker.Invoker;

public interface InvokerWrapper {

	public Invoker create(Method method, Object instance);
}
