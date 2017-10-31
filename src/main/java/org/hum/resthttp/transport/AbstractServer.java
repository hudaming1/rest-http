package org.hum.resthttp.transport;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

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
	protected AtomicInteger currentRequest = new AtomicInteger(0);
	
	public void start(ServerConfig serviceConfig) {
		
		// unsafe
		if (serverIsRun) {
			return;
		}
		serverIsRun = true;

		// 1.check port used? TODO
		
		// 2.start server
		doOpen(serviceConfig);
	}
	
	public abstract void doOpen(ServerConfig serviceConfig);

	@Override
	public Result handler(Invocation invocation) throws InterruptedException, ExecutionException {
		// TODO 这里最好做一个filter
		currentRequest.incrementAndGet();
		Future<Result> future = invokerHolder.invoke(invocation);
		Result result = future.get();
		currentRequest.decrementAndGet();
		return result;
	}
}
