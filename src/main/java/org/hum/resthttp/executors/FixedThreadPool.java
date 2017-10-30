package org.hum.resthttp.executors;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FixedThreadPool implements ThreadPoolFactory {

	@Override
	public ExecutorService create() {
		return Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);
	}
}
