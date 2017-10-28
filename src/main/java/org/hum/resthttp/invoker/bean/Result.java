package org.hum.resthttp.invoker.bean;

import org.hum.resthttp.invoker.enumtype.ResultCodeEum;

public class Result {

	private String code;
	private String message;
	private Object data;
	
	public Result() { }

	public Result(ResultCodeEum resultCode, Object data) {
		this.code = resultCode.getCode();
		this.message = resultCode.getMessage();
		this.data = data;
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

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Result [code=").append(code).append(", message=").append(message).append(", data=").append(data).append("]");
		return builder.toString();
	}
}
