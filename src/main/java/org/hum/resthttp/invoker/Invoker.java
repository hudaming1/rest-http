package org.hum.resthttp.invoker;

import java.util.Map;

import org.hum.resthttp.invoker.bean.Result;

public interface Invoker {

	public Result invoke(Map<String, Object> params);
}
