package dev.jorel.commandapi;

import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;

/**
 * Configuration wrapper class for Velocity. The config.yml file used by the CommandAPI is
 * only ever read from, nothing is ever written to it. That's why there's only
 * getter methods.
 */
public class InternalVelocityConfig extends InternalConfig {
	// The plugin that loaded the CommandAPI
	private final Object plugin;
	// The server that the CommandAPI is running on
	private final ProxyServer server;

	/**
	 * Creates an {@link InternalVelocityConfig} from a {@link CommandAPIVelocityConfig}
	 *
	 * @param config The configuration to use to set up this internal configuration
	 */
	public InternalVelocityConfig(CommandAPIVelocityConfig config) {
		super(config);
		this.plugin = config.plugin;
		this.server = config.server;
	}

	/**
	 * @return The plugin object (annotated by {@link Plugin}) loading the CommandAPI
	 */
	public Object getPlugin() {
		return plugin;
	}

	/**
	 * @return The {@link ProxyServer} the CommandAPI is running on
	 */
	public ProxyServer getServer() {
		return server;
	}
}
