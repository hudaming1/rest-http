package org.hum.resthttp.transport;

import org.hum.resthttp.common.ServiceLoader;

public class ServerFactory {

	private static Server transporter;
	
	public static synchronized Server get() {
		if (transporter == null) {
			transporter = ServiceLoader.load(Server.class);
		}
		return transporter;
	}
}
