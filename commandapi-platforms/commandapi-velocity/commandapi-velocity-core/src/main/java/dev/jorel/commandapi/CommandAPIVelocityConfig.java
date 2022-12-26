package dev.jorel.commandapi;

import com.velocitypowered.api.proxy.ProxyServer;

public class CommandAPIVelocityConfig extends CommandAPIConfig {
	ProxyServer server;

	public CommandAPIVelocityConfig(ProxyServer server) {
		this.server = server;
	}
}
