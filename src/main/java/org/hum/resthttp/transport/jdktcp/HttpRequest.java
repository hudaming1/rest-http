package org.hum.resthttp.transport.jdktcp;

import java.util.Map;

/**
 * 由于我只打算实现一个简单的Http容器，我就抛弃了复杂的字段，仅保存我需要的内容。
 * <pre>
 * HTTP规范请求结构：
 * 	1.请求行
 *  2.请求头
 *  3.空行
 *  4.请求报文
 * </pre>
 */
public class HttpRequest {

	private String protocolVersion;
	private String method;
	private String url;
	private Map<String, Object> params;

	public HttpRequest(String protocolVersion, String method, String url, Map<String, Object> params) {
		if (protocolVersion == null || protocolVersion.isEmpty()) {
			throw new IllegalArgumentException("request protocolVersion mustn't be null!");
		} else if (method == null || method.isEmpty()) {
			throw new IllegalArgumentException("request method mustn't be null!");
		} else if (url == null || url.isEmpty()) {
			throw new IllegalArgumentException("request url mustn't be null!");
		} else if (params == null || params.isEmpty()) {
			throw new IllegalArgumentException("request params mustn't be null!");
		}
		this.protocolVersion = protocolVersion;
		this.method = method;
		this.url = url;
		this.params = params;
	}

	public String getProtocolVersion() {
		return protocolVersion;
	}

	public String getMethod() {
		return method;
	}

	public String getUrl() {
		return url;
	}

	public Map<String, Object> getParams() {
		return params;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("HttpRequest [protocolVersion=").append(protocolVersion).append(", method=").append(method)
				.append(", url=").append(url).append(", params=").append(params).append("]");
		return builder.toString();
	}
}
