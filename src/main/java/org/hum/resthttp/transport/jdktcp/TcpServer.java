package org.hum.resthttp.transport.jdktcp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutionException;

import org.hum.resthttp.common.ServerException;
import org.hum.resthttp.invoker.bean.Invocation;
import org.hum.resthttp.invoker.bean.Result;
import org.hum.resthttp.transport.AbstractServer;
import org.hum.resthttp.transport.config.ServerConfig;
import org.hum.resthttp.transport.context.ServerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * BIO实现：
 *    阻塞式Server仿NIO模式，单线程负责监听，然后解码、序列化成Invocation，然后交给线程池处理，以此减少阻塞时间。 
 */
public class TcpServer extends AbstractServer {

	private ServerContext<HttpRequest> serverContext = new ServerContext<>();
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
			
			// 2.listening port
			while (isRun) {

				Socket socket = server.accept();
				InputStream inputStream = null;
				OutputStream outputStream = null;
				try {
					// 3.handle request
					Result result = handlerSocket(inputStream);	
					
					// 4.parse to response
					HttpResponse response = TcpUtils.parse(result);
					
					// 5.output response 
				} catch (Exception e) {
					// x.output exception
					logger.error("handler request occured exception", e);
				} finally {
					closeSocket(inputStream, outputStream, socket);
					serverContext.remove();
				}
			}
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
	
	private void closeSocket(InputStream inputStream, OutputStream outputStream, Socket socket) {
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
	
	private void flushResponse(Result result) {
		
	}
}
