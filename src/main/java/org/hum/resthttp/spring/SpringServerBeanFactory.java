package org.hum.resthttp.spring;

import org.hum.resthttp.transport.Server;
import org.hum.resthttp.transport.ServerFactory;
import org.hum.resthttp.transport.config.ServerConfig;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

public class SpringServerBeanFactory implements ApplicationListener<ContextRefreshedEvent> {

	private ServerConfig serverConfig;

	public void setServerConfig(ServerConfig serverConfig) {
		this.serverConfig = serverConfig;
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		Server server = ServerFactory.get();
		server.start(serverConfig);
	}
}
