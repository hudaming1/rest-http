package org.hum.resthttp.transport.netty;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hum.resthttp.common.HttpErrorCode;

import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.MixedAttribute;
import io.netty.util.CharsetUtil;

public class NettyHttpUtils {
	
	private static final String GET = HttpMethod.GET.name();
	private static final String POST = HttpMethod.POST.name();

	public static Map<String, Object> requestParams2Map(HttpRequest request) throws IOException {
		Map<String, Object> parameters = new HashMap<>();
		if (GET.equalsIgnoreCase(request.method().name()) || POST.equalsIgnoreCase(request.method().name())) {
			QueryStringDecoder decoderQuery = new QueryStringDecoder(request.uri());
			Map<String, List<String>> uriAttributes = decoderQuery.parameters();
			for (Entry<String, List<String>> attr : uriAttributes.entrySet()) {
				for (String attrVal : attr.getValue()) {
					parameters.put(attr.getKey(), attrVal);
				}
			}
		} else if (request.headers().contains("Content-Length")) { // request body
			HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(request);
			List<InterfaceHttpData> postList = decoder.getBodyHttpDatas();
			for (InterfaceHttpData data : postList) {
				MixedAttribute value = (MixedAttribute) data;
				value.setCharset(CharsetUtil.UTF_8);
				parameters.put(data.getName(), value.getValue());
			}
		}
		return parameters;
	}

	public static String getUrl(HttpRequest request) {
		String uri = request.uri();
		if (uri.indexOf('?') >= 0) {
			return uri.substring(0, uri.indexOf('?'));
		} else {
			return uri;
		}
	}

	public static HttpResponseStatus parseStatus(String errorCode) {
		if (HttpErrorCode._404.equals(errorCode)) {
			return HttpResponseStatus.NOT_FOUND;
		} else if (HttpErrorCode._405.equals(errorCode)) {
			return HttpResponseStatus.METHOD_NOT_ALLOWED;
		} else {
			return HttpResponseStatus.INTERNAL_SERVER_ERROR;
		}
	}
}
