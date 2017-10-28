package org.hum.resthttp.mapper;

import java.lang.reflect.Method;

import org.hum.resthttp.mapper.enumtype.MethodEnumType;

public class MethodHolder {

	private MethodEnumType httpMethod;
	private Method method;
	private Object instance;

	public MethodHolder(MethodEnumType httpMethod, Method method, Object instance) {
		this.httpMethod = httpMethod;
		this.method = method;
		this.instance = instance;
	}

	public Method getMethod() {
		return method;
	}

	public Object getInstance() {
		return instance;
	}

	public MethodEnumType getHttpMethod() {
		return httpMethod;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MethodHolder [httpMethod=").append(httpMethod).append(", method=").append(method)
				.append(", instance=").append(instance).append("]");
		return builder.toString();
	}
}
