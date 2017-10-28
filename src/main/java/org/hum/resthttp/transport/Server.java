package org.hum.resthttp.transport;

import org.hum.resthttp.transport.config.ServerConfig;

public interface Server {

	public void start(ServerConfig serviceConfig);
	
	public void close();
}
