package dev.jorel.commandapi;

import com.velocitypowered.api.proxy.ProxyServer;

public class InternalVelocityConfig extends InternalConfig {
	private final ProxyServer server;

	public InternalVelocityConfig(CommandAPIVelocityConfig config) {
		super(config);
		this.server = config.server;
	}

	public ProxyServer getServer() {
		return server;
	}
}
