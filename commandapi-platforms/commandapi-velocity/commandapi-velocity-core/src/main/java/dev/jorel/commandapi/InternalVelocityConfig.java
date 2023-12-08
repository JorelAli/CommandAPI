package dev.jorel.commandapi;

import com.velocitypowered.api.proxy.ProxyServer;

/**
 * Configuration wrapper class for Velocity. The config.yml file used by the CommandAPI is
 * only ever read from, nothing is ever written to it. That's why there's only
 * getter methods.
 */
public class InternalVelocityConfig extends InternalConfig {
	// The server that the CommandAPI is running on
	private final ProxyServer server;

	// The default command namespace
	private final String namespace;

	/**
	 * Creates an {@link InternalVelocityConfig} from a {@link CommandAPIVelocityConfig}
	 *
	 * @param config The configuration to use to set up this internal configuration
	 */
	public InternalVelocityConfig(CommandAPIVelocityConfig config) {
		super(config);
		this.server = config.server;
		this.namespace = config.namespace;
	}

	/**
	 * @return The {@link ProxyServer} the CommandAPI is running on
	 */
	public ProxyServer getServer() {
		return server;
	}

	/**
	 * @return the default namespace used to register commands
	 */
	public String getNamespace() {
		return namespace;
	}
}
