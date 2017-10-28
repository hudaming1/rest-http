package org.hum.resthttp.transport.jdktcp;

public class HttpResponse {

	private String protocolVersion;
	private String code;
	private String content;

	public HttpResponse(String protocolVersion, String code, String content) {
		this.protocolVersion = protocolVersion;
		this.code = code;
		this.content = content;
	}

	public String getProtocolVersion() {
		return protocolVersion;
	}

	public String getCode() {
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
