package org.hum.resthttp.transport.config;

public class ServerConfig {

	private int port;

	public ServerConfig(int port) {
		this.port = port;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ServerConfig [port=").append(port).append("]");
		return builder.toString();
	}
}
