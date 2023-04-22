package dev.jorel.commandapi;

import com.velocitypowered.api.proxy.ProxyServer;

/**
 * A class that contains information needed to configure the CommandAPI on Velocity-based servers.
 */
public class CommandAPIVelocityConfig extends CommandAPIConfig<CommandAPIVelocityConfig> {
	ProxyServer server;

	/**
	 * Creates a new CommandAPIVelocityConfig object. Variables in this
	 * constructor are required to load the CommandAPI on Velocity properly.
	 *
	 * @param server The {@link ProxyServer} that the CommandAPI is running on.
	 */
	public CommandAPIVelocityConfig(ProxyServer server) {
		this.server = server;
	}

	@Override
	public CommandAPIVelocityConfig instance() {
		return this;
	}
}
