package dev.jorel.commandapi;

import com.velocitypowered.api.proxy.ProxyServer;

/**
 * A class that contains information needed to configure the CommandAPI on Velocity-based servers.
 */
public class CommandAPIVelocityConfig extends CommandAPIConfig<CommandAPIVelocityConfig> {
	ProxyServer server;

	String namespace = "";

	/**
	 * Creates a new CommandAPIVelocityConfig object. Variables in this
	 * constructor are required to load the CommandAPI on Velocity properly.
	 *
	 * @param server The {@link ProxyServer} that the CommandAPI is running on.
	 */
	public CommandAPIVelocityConfig(ProxyServer server) {
		this.server = server;
	}

	/**
	 * Configures the default namespace for commands registered with the CommandAPI
	 * <p>
	 * This defaults to an empty namespace if not set
	 *
	 * @param namespace The namespace to use for commands
	 * @return this CommandAPIVelocityConfig
	 */
	public CommandAPIVelocityConfig setNamespace(String namespace) {
		this.namespace = namespace;
		return instance();
	}

	@Override
	public CommandAPIVelocityConfig instance() {
		return this;
	}
}
