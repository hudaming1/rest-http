package org.hum.resthttp.common;

/**
 * Restful异常使用这个类，如果是服务器内部异常，使用ServerException
 */
public class RestfulException extends RuntimeException {
	
	private static final long serialVersionUID = 6632263251127341599L;
	private String code;

	public RestfulException(String code, String message) {
		super(message);
		this.code = code;
	}

	public RestfulException(String code, String message, Throwable exception) {
		super(message, exception);
		this.code = code;
	}
	
	public String getCode() {
		return this.code;
	}
}
