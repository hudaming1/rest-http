package org.hum.resthttp.invoker.bean;

import org.hum.resthttp.invoker.enumtype.ResultCodeEum;

public class Result {

	private String code;
	private Throwable error;
	private Object data;
	
	public Result(ResultCodeEum resultCode, Throwable error, Object data) {
		this.code = resultCode.getCode();
		this.error = error;
		this.data = data;
	}

	public String getCode() {
		return code;
	}

	public Object getData() {
		return data;
	}

	public Throwable getError() {
		return error;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Result [code=").append(code).append(", error=").append(error).append(", data=").append(data)
				.append("]");
		return builder.toString();
	}
}
