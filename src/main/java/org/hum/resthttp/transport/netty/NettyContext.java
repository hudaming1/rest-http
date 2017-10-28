package org.hum.resthttp.transport.netty;

import io.netty.handler.codec.http.HttpRequest;

/**
 * 上下文，保存Netty服务器请求信息
 */
public class NettyContext {

	private static ThreadLocal<HttpRequest> requestThreadLocal = new ThreadLocal<>();
	
	public static void set(HttpRequest request) {
		requestThreadLocal.set(request);
	}
	
	public static HttpRequest get() {
		return requestThreadLocal.get();
	}
	
	public static void remove() {
		requestThreadLocal.remove();
	}
}
