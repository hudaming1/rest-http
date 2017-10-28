package org.hum.resthttp.common;

public class ServerException extends RuntimeException {

	private static final long serialVersionUID = 6632263251127341599L;

	public ServerException(String message) {
		super(message);
	}

	public ServerException(String message, Throwable exception) {
		super(message, exception);
	}
}
