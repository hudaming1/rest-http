package org.hum.resthttp.transport.netty;

import java.util.concurrent.ExecutionException;

import org.hum.resthttp.invoker.bean.Invocation;
import org.hum.resthttp.invoker.bean.Result;

public interface NettyHandleCallback {
	Result handler(Invocation invocation) throws InterruptedException, ExecutionException;
}
