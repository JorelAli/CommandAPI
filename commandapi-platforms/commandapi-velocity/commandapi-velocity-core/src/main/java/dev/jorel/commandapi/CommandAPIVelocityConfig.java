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
		super.setNamespace("");
	}

	@Override
	public CommandAPIVelocityConfig setNamespace(String namespace) {
		if (namespace == null) {
			throw new NullPointerException("Default namespace can't be null!");
		}
		if (!CommandAPIHandler.getInstance().namespacePattern.matcher(namespace).matches()) {
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
