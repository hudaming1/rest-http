package org.hum.resthttp.transport.jdktcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TcpUtils {
	
	private static final Logger logger = LoggerFactory.getLogger(TcpUtils.class);

	public static HttpRequest parse(InputStream inputStream) throws IOException {
		
		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
		
		// 其实我只需要request的第一行，即请求行的内容，所以剩下的header与body我都舍弃，不解析了。
		String requestLine = br.readLine();
		
		String url = "";
		Map<String, Object> params = new HashMap<>();
		String[] lineArray = requestLine.split(" ");
		String method = lineArray[0];
		String protocol = lineArray[2];
		String urlAndParams = lineArray[1];
		
		// 如果请求不带参数
		if (urlAndParams.indexOf('?') == -1) {
			return new HttpRequest(protocol, method, urlAndParams, params);
		} else {
			String[] urlAndParamsArray = urlAndParams.split("\\?");
			url = urlAndParamsArray[0];
			// 处理请求参数
			String[] paramsArray = urlAndParamsArray[1].split("&");
			for (String param: paramsArray) {
				String[] p = param.split("=");
				params.put(p[0], p[1]);
			}
			return new HttpRequest(protocol, method, url, params);
		}
	}

	public static void closeSocket(InputStream inputStream, OutputStream outputStream, Socket socket) {
		if (inputStream != null) {
			try {
				inputStream.close();
			} catch (IOException e) {
				logger.error("occured excepiton when close inputstream.", e);
			}
		}
		if (outputStream != null) {
			try {
				outputStream.close();
			} catch (IOException e) {
				logger.error("occured excepiton when close outputstream.", e);
			}
		}
		if (socket != null) {
			try {
				socket.close();
			} catch (IOException e) {
				logger.error("occured excepiton when close socket.", e);
			}
		}
	}
}
