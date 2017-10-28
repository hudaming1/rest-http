package org.hum.resthttp.mapper.enumtype;

public enum MethodEnumType {
	
	GET(1, "GET"), POST(2, "POST");

	private int code;
	private String desc;
	
	MethodEnumType(int code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public int getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}
}
