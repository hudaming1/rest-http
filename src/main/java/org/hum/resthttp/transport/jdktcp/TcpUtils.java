package org.hum.resthttp.transport.jdktcp;

import java.io.InputStream;
import java.util.Map;

import org.hum.resthttp.invoker.bean.Result;

public class TcpUtils {

	public static HttpRequest parse(InputStream inputStream) {

		String protocol = "";
		String method = "";
		String url = "";
		Map<String, Object> params = null;

		return new HttpRequest(protocol, method, url, params);
	}

	public static HttpResponse parse(Result result) {
		String protocolVersion = "";
		String code = "";
		String content = "";
		return new HttpResponse(protocolVersion, code, content);
	}
}
