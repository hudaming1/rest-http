package org.hum.resthttp.invoker.holder;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.hum.resthttp.common.HttpErrorCode;
import org.hum.resthttp.common.RestfulException;
import org.hum.resthttp.common.ServiceLoader;
import org.hum.resthttp.invoker.Invoker;
import org.hum.resthttp.invoker.bean.Invocation;
import org.hum.resthttp.invoker.bean.Result;
import org.hum.resthttp.invoker.wrapper.InvokerWrapper;
import org.hum.resthttp.mapper.Mapper;
import org.hum.resthttp.mapper.MethodHolder;

public class DefaultInvokerHolder implements InvokerHolder {
	
	private Mapper mapper = ServiceLoader.load(Mapper.class);
	private InvokerWrapper invokerWrapper = ServiceLoader.load(InvokerWrapper.class);
	private ExecutorService executorService;
	
	public DefaultInvokerHolder() {
		// init thread-pool
		executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);
		// scan restful class
		try {
			mapper.scan();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Invoker get(String url) {
		MethodHolder methodHolder = mapper.get(url);
		if (methodHolder == null) {
			throw new RestfulException(HttpErrorCode._404, "url [" + url + "] not mappered");
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
		MethodHolder methodHolder = mapper.get(invocation.getUrl());
		if (!methodHolder.getHttpMethod().getDesc().equalsIgnoreCase(invocation.getMethod())) {
			throw new RestfulException(HttpErrorCode._405, "method don't support");
		}
	}
}
