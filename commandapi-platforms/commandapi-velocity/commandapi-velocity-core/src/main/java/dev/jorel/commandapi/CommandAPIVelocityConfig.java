package dev.jorel.commandapi;

import com.velocitypowered.api.proxy.ProxyServer;

public class CommandAPIVelocityConfig extends CommandAPIConfig<CommandAPIVelocityConfig> {
	ProxyServer server;

	public CommandAPIVelocityConfig(ProxyServer server) {
		this.server = server;
	}

	@Override
	public CommandAPIVelocityConfig instance() {
		return null;
	}
}
