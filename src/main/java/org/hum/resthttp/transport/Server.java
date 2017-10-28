package org.hum.resthttp.transport;

import java.util.concurrent.ExecutionException;

import org.hum.resthttp.invoker.bean.Invocation;
import org.hum.resthttp.invoker.bean.Result;
import org.hum.resthttp.transport.config.ServerConfig;

public interface Server {

	public void start(ServerConfig serviceConfig);
	
	public Result handler(Invocation invocation) throws InterruptedException, ExecutionException;
	
	public void close();
}
