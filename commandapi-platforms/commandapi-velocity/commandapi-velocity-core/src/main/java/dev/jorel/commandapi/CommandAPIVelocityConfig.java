package dev.jorel.commandapi;

import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;

/**
 * A class that contains information needed to configure the CommandAPI on Velocity-based servers.
 */
public class CommandAPIVelocityConfig extends CommandAPIConfig<CommandAPIVelocityConfig> {
	Object plugin;
	ProxyServer server;

	/**
	 * Creates a new CommandAPIVelocityConfig object. Variables in this
	 * constructor are required to load the CommandAPI on Velocity properly.
	 *
	 * @param plugin The plugin object (annotated by {@link Plugin}) loading the CommandAPI.
	 *               This is used when registering events.
	 * @param server The {@link ProxyServer} that the CommandAPI is running on.
	 */
	public CommandAPIVelocityConfig(Object plugin, ProxyServer server) {
		this.plugin = plugin;
		this.server = server;
		super.setNamespace("");
	}

	@Override
	public CommandAPIVelocityConfig setNamespace(String namespace) {
		if (namespace == null) {
			throw new NullPointerException("Default namespace can't be null!");
		}
		if (!CommandAPIHandler.NAMESPACE_PATTERN.matcher(namespace).matches()) {
			CommandAPI.logNormal("Did not set namespace to the provided '" + namespace + "' namespace because only 0-9, a-z, underscores, periods and hyphens are allowed!");
			return this;
		}
		return super.setNamespace(namespace);
	}

	@Override
	public CommandAPIVelocityConfig instance() {
		return this;
	}
}
