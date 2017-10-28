package org.hum.resthttp.invoker.bean;

import java.util.Map;

public class Invocation {

	private String url;
	private String method;
	private Map<String, Object> params;

	public Invocation(String method, String url, Map<String, Object> params) {
		this.method = method;
		this.url = url;
		this.params = params;
	}

	public String getUrl() {
		return url;
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public String getMethod() {
		return method;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Invocation [url=").append(url).append(", method=").append(method).append(", params=")
				.append(params).append("]");
		return builder.toString();
	}
}
