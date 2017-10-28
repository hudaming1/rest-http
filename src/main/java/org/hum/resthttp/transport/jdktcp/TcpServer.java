package org.hum.resthttp.transport.jdktcp;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutionException;

import org.hum.resthttp.common.ServerException;
import org.hum.resthttp.common.ServiceLoader;
import org.hum.resthttp.invoker.bean.Invocation;
import org.hum.resthttp.invoker.bean.Result;
import org.hum.resthttp.invoker.enumtype.ResultCodeEum;
import org.hum.resthttp.serialization.Serialization;
import org.hum.resthttp.transport.AbstractServer;
import org.hum.resthttp.transport.config.ServerConfig;
import org.hum.resthttp.transport.context.ServerContext;
import org.hum.resthttp.transport.enumtype.HttpStatusEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * BIO实现：
 *    阻塞式Server仿NIO模式，单线程负责监听，然后解码、序列化成Invocation，然后交给线程池处理，以此减少阻塞时间。 
 */
public class TcpServer extends AbstractServer {

	private ServerContext<HttpRequest> serverContext = new ServerContext<>();
	private Serialization serialization = ServiceLoader.load(Serialization.class);
	private static final Logger logger = LoggerFactory.getLogger(TcpServer.class);
	private volatile boolean isRun = false;

	@Override
	public void close() {
		isRun = false;
	}

	@Override
	public void doOpen(ServerConfig serverConfig) {
		ServerSocket server = null;
		try {
			if (isRun) {
				return;
			}
			// 1.start server
			isRun = true;
			server = new ServerSocket(serverConfig.getPort());
			logger.info("Tcp server start, listening on port: " + serverConfig.getPort());
			
			// 2.listening 
			listening(server);
		} catch (IOException e) {
			throw new ServerException("start TcpServer error!", e);
		} finally {
			if (server != null) {
				try {
					server.close();
				} catch (IOException e) {
					logger.error("occured excepiton when TcpServer closed.", e);
				}
			}
		}
	}
	
	private void listening(ServerSocket server) throws IOException {
		while (isRun) {
			Socket socket = server.accept();
			InputStream inputStream = null;
			OutputStream outputStream = null;
			try {
				inputStream = socket.getInputStream();
				outputStream = socket.getOutputStream();
				
				// 3.handle request
				Result result = handlerSocket(inputStream);	
				
				// 4.parse to response
				HttpResponse response = parseResponse(result);
				
				// 5.output response 
				flushResponse(outputStream, response);
			} catch (Exception e) {
				// x.output exception
				logger.error("handler request occured exception", e);
				try {
					flushResponse(outputStream, parseResponse(new Result(ResultCodeEum.ERROR, e, null)));
				} catch (IOException ignore) {
					logger.error("flash response occured exception", ignore);
				}
			} finally {
				TcpUtils.closeSocket(inputStream, outputStream, socket);
				serverContext.remove();
			}
		}
	}
	
	// TODO 我想把这个方法也移到TcpUtils里去，由于依赖TcpServer对象，所以只能放到这里了，有什么好的解决办法吗？
	private HttpResponse parseResponse(Result result) {
		HttpRequest httpRequest = serverContext.get();
		HttpStatusEnum httpCode = ResultCodeEum.SUCCESS.getCode().equals(result.getCode())? HttpStatusEnum.OK: HttpStatusEnum.INTERNAL_ERROR;
		String content = serialization.serialize(result.getData());
		return new HttpResponse(httpRequest.getProtocolVersion(), httpCode, content);
	}
	
	private Result handlerSocket(InputStream inputStream) throws IOException, InterruptedException, ExecutionException {

		// 1.deserialize request
		HttpRequest request = TcpUtils.parse(inputStream);
		
		// 2.save into context
		serverContext.set(request);
		
		// 3.parse request to invocation
		Invocation invocation = new Invocation(request.getMethod(), request.getUrl(), request.getParams());

		// 4.non-block invoke
		return handler(invocation);
	}
	
	private void flushResponse(OutputStream outputStream, HttpResponse response) throws IOException {
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream));
		// TODO 研究一下，响应头为什么当做内容输出了
//		HttpStatusEnum httpCode = response.getCode();
//		bw.write(response.getProtocolVersion() + " " + httpCode.getCode() + " " + httpCode.getMsg() + "\n");
//		bw.write("\n"); // header我就不输出了(不知道这样行不行，http规范中header是必须的吗？)
//		bw.write("\n");
		bw.write(response.getContent());
		bw.flush();
	}
}
