package org.hum.resthttp.transport.jdktcp;

import org.hum.resthttp.transport.enumtype.HttpStatusEnum;

/**
 * 由于我只打算实现一个简单的Http容器，我就抛弃了复杂的字段，仅保存我需要的内容。
 * <pre>
 * HTTP规范响应结构：
 * 	1.响应行
 *  2.响应头
 *  3.空行
 *  4.响应报文
 * </pre>
 */
public class HttpResponse {

	private String protocolVersion;
	private HttpStatusEnum code;
	private String content;

	public HttpResponse(String protocolVersion, HttpStatusEnum code, String content) {
		this.protocolVersion = protocolVersion;
		this.code = code;
		this.content = content;
	}

	public String getProtocolVersion() {
		return protocolVersion;
	}

	public HttpStatusEnum getCode() {
		return code;
	}

	public String getContent() {
		return content;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("HttpResponse [protocolVersion=").append(protocolVersion).append(", code=").append(code)
				.append(", content=").append(content).append("]");
		return builder.toString();
	}
}
