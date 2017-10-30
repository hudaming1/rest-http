package org.hum.resthttp.invoker.holder;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.hum.resthttp.common.RestfulException;
import org.hum.resthttp.executors.ThreadPoolFactory;
import org.hum.resthttp.invoker.Invoker;
import org.hum.resthttp.invoker.bean.Invocation;
import org.hum.resthttp.invoker.bean.Result;
import org.hum.resthttp.invoker.wrapper.InvokerWrapper;
import org.hum.resthttp.loader.ServiceLoaderHolder;
import org.hum.resthttp.mapper.MapperHolder;
import org.hum.resthttp.mapper.MethodHolder;
import org.hum.resthttp.transport.enumtype.HttpStatusEnum;

public class DefaultInvokerHolder implements InvokerHolder {
	
	private InvokerWrapper invokerWrapper = ServiceLoaderHolder.load(InvokerWrapper.class);
	private ThreadPoolFactory threadPoolFactory = ServiceLoaderHolder.load(ThreadPoolFactory.class);
	private ExecutorService executorService;
	
	public DefaultInvokerHolder() {
		// init thread-pool
		executorService = threadPoolFactory.create();
	}

	@Override
	public Invoker get(String url) {
		MethodHolder methodHolder = MapperHolder.get(url);
		if (methodHolder == null) {
			throw new RestfulException(HttpStatusEnum.URL_NOT_FOUND, "url [" + url + "] not mappered");
		}
		return invokerWrapper.create(methodHolder.getMethod(), methodHolder.getInstance());
	}

	@Override
	public Future<Result> invoke(final Invocation invocation) {
		// 1.校验请求是否合法
		validate(invocation);
		
		// 2.发起调用
		return executorService.submit(new Callable<Result>() {
			@Override
			public Result call() throws Exception {
				return get(invocation.getUrl()).invoke(invocation.getParams());
			}
		});
	}
	
	private void validate(Invocation invocation) {
		MethodHolder methodHolder = MapperHolder.get(invocation.getUrl());
		if (!methodHolder.getHttpMethod().getDesc().equalsIgnoreCase(invocation.getMethod())) {
			throw new RestfulException(HttpStatusEnum.METHOD_NOT_ALLOW, "method don't support");
		}
	}
}
