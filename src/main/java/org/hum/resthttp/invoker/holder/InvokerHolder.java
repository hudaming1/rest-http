package org.hum.resthttp.invoker.holder;

import java.util.concurrent.Future;

import org.hum.resthttp.invoker.Invoker;
import org.hum.resthttp.invoker.bean.Invocation;
import org.hum.resthttp.invoker.bean.Result;

public interface InvokerHolder {

	public Invoker get(String url);
	
	public Future<Result> invoke(Invocation invocation);
}
