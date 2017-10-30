package org.hum.resthttp.executors;

import java.util.concurrent.ExecutorService;

public interface ThreadPoolFactory {

	public ExecutorService create();
}
