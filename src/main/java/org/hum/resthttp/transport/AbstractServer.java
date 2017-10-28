package org.hum.resthttp.transport;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.hum.resthttp.common.ServiceLoader;
import org.hum.resthttp.invoker.bean.Invocation;
import org.hum.resthttp.invoker.bean.Result;
import org.hum.resthttp.invoker.holder.InvokerHolder;
import org.hum.resthttp.serialization.Serialization;
import org.hum.resthttp.transport.config.ServerConfig;

/**
 * Server 应该维护URL对Instance的映射表
 */
public abstract class AbstractServer implements Server {
	
	private volatile boolean isRun = false;
	protected InvokerHolder invokerHolder = ServiceLoader.load(InvokerHolder.class);
	protected Serialization serialization = ServiceLoader.load(Serialization.class);

	public void start(ServerConfig serviceConfig) {
		
		// unsafe
		if (isRun) {
			return;
		}
		isRun = true;
		
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
