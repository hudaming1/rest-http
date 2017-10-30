package org.hum.resthttp.test;

import org.hum.resthttp.transport.Server;
import org.hum.resthttp.transport.ServerFactory;
import org.hum.resthttp.transport.config.ServerConfig;

public class SimpleTest {

	public static void main(String[] args) throws Exception {
		Server server = ServerFactory.get();
		server.start(new ServerConfig(9080));
		System.out.println("333");
	}
}
