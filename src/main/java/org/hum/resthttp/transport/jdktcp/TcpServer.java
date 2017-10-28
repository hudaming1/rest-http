package org.hum.resthttp.transport.jdktcp;

import java.util.concurrent.ExecutionException;

import org.hum.resthttp.invoker.bean.Invocation;
import org.hum.resthttp.invoker.bean.Result;
import org.hum.resthttp.transport.AbstractServer;
import org.hum.resthttp.transport.config.ServerConfig;

public class TcpServer extends AbstractServer {

	@Override
	public void close() {
	}

	@Override
	public void doOpen(ServerConfig serviceConfig) {
		
	}

	@Override
	public Result handler(Invocation invocation) throws InterruptedException, ExecutionException {
		// TODO Auto-generated method stub
		return null;
	}
}
