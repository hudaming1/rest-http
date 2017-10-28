package org.hum.resthttp.transport.enumtype;

public enum HttpStatusEnum {
	
	OK("200", "OK"), 
	URL_NOT_FOUND("404", "URL_NOT_FOUND"), 
	METHOD_NOT_ALLOW("405", "METHOD_NOT_ALLOW"), 
	INTERNAL_ERROR("500", "INTERNAL_ERROR");

	private String code;
	private String msg;

	HttpStatusEnum(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public String getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}
}
