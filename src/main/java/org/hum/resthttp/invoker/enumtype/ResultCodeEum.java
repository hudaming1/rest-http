package org.hum.resthttp.invoker.enumtype;

public enum ResultCodeEum {
	
	SUCCESS("1", "执行成功"),
	ERROR("2", "执行失败");

	private String code;
	private String message;

	ResultCodeEum(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
