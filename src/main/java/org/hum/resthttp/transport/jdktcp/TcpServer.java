package org.hum.resthttp.transport.jdktcp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutionException;

import org.hum.resthttp.common.RestfulException;
import org.hum.resthttp.common.ServerException;
import org.hum.resthttp.invoker.bean.Invocation;
import org.hum.resthttp.invoker.bean.Result;
import org.hum.resthttp.invoker.enumtype.ResultCodeEum;
import org.hum.resthttp.loader.ServiceLoaderHolder;
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
	private Serialization serialization = ServiceLoaderHolder.load(Serialization.class);
	private static final Logger logger = LoggerFactory.getLogger(TcpServer.class);
	private ServerSocket server = null;

	@Override
	public void close() {
		// 1.请求入口先关闭
		serverIsRun = false;
		
		// 2.等待正在执行中的请求完成
		while (currentRequest.get() > 0) {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void doOpen(ServerConfig serverConfig) {
		try {
			// 1.start server
			server = new ServerSocket(serverConfig.getPort());
			logger.info("Tcp server start, listening on port: " + serverConfig.getPort());
			
			// 2.listening (asyn)
			asynListening(server);
			
		} catch (IOException e) {
			throw new ServerException("start TcpServer error!", e);
		} 
	}
	
	private void asynListening(final ServerSocket server) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					listening(server);					
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
		}).start();
	}
	
	private void listening(ServerSocket server) {
		while (serverIsRun) {
			Socket socket = null;
			InputStream inputStream = null;
			OutputStream outputStream = null;
			try {
				socket = server.accept();
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
		
		if (ResultCodeEum.SUCCESS.getCode().equals(result.getCode())) {
			String content = serialization.serialize(result.getData());
			return new HttpResponse(httpRequest.getProtocolVersion(), HttpStatusEnum.OK, content);
		} else if (result.getError() instanceof RestfulException) {
			return new HttpResponse(httpRequest.getProtocolVersion(), ((RestfulException)result.getError()).getHttpCode(), result.getError().getMessage());
		} else {
			return new HttpResponse(httpRequest.getProtocolVersion(), HttpStatusEnum.INTERNAL_ERROR, result.getError().toString());
		}
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
		PrintWriter pw = new PrintWriter(new OutputStreamWriter(outputStream));
		HttpStatusEnum httpCode = response.getCode();
		// 简单实现，就省略header不打印了
		pw.println(response.getProtocolVersion() + " " + httpCode.getCode() + " " + httpCode.getMsg());
		pw.println("");  // 打印空行，取分响应头和报文
		pw.println(response.getContent());
		pw.flush();
	}
}
