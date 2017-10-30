package org.hum.resthttp.transport;

import org.hum.resthttp.loader.ServiceLoaderHolder;

public class ServerFactory {

	private static Server transporter;
	
	public static synchronized Server get() {
		if (transporter == null) {
			transporter = ServiceLoaderHolder.load(Server.class);
		}
		return transporter;
	}
}
