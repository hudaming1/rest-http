package org.hum.resthttp.transport;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.hum.resthttp.invoker.bean.Invocation;
import org.hum.resthttp.invoker.bean.Result;
import org.hum.resthttp.invoker.holder.InvokerHolder;
import org.hum.resthttp.loader.ServiceLoaderHolder;
import org.hum.resthttp.serialization.Serialization;
import org.hum.resthttp.transport.config.ServerConfig;

/**
 * Server 应该维护URL对Instance的映射表
 */
public abstract class AbstractServer implements Server {
	
	protected volatile boolean serverIsRun = false;
	protected InvokerHolder invokerHolder = ServiceLoaderHolder.load(InvokerHolder.class);
	protected Serialization serialization = ServiceLoaderHolder.load(Serialization.class);

	public void start(ServerConfig serviceConfig) {
		
		// unsafe
		if (serverIsRun) {
			return;
		}
		serverIsRun = true;

		// 1.check port used?
		
		// 2.start server
		doOpen(serviceConfig);
	}
	
	public abstract void doOpen(ServerConfig serviceConfig);

	@Override
	public Result handler(Invocation invocation) throws InterruptedException, ExecutionException {
		Future<Result> future = invokerHolder.invoke(invocation);
		return future.get();
	}
}
