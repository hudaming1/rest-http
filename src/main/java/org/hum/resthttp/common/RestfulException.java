package org.hum.resthttp.common;

import org.hum.resthttp.transport.enumtype.HttpStatusEnum;

/**
 * Restful异常使用这个类，如果是服务器内部异常，使用ServerException
 */
public class RestfulException extends RuntimeException {
	
	private static final long serialVersionUID = 6632263251127341599L;
	private HttpStatusEnum httpCode;

	public RestfulException(HttpStatusEnum httpCode, String message) {
		super(message);
		this.httpCode = httpCode;
	}

	public RestfulException(HttpStatusEnum httpCode, String message, Throwable exception) {
		super(message, exception);
		this.httpCode = httpCode;
	}
	
	public HttpStatusEnum getHttpCode() {
		return this.httpCode;
	}
}
