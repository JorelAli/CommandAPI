package dev.jorel.commandapi;

import org.spongepowered.api.Server;

/**
 * Configuration wrapper class for Sponge. The config.yml file used by the CommandAPI is
 * only ever read from, nothing is ever written to it. That's why there's only
 * getter methods.
 */
public class InternalSpongeConfig extends InternalConfig {
	// The server that the CommandAPI is running on
	private final Server server;

	/**
	 * Creates an {@link InternalSpongeConfig} from a {@link CommandAPISpongeConfig}
	 *
	 * @param config The configuration to use to set up this internal configuration
	 */
	public InternalSpongeConfig(CommandAPISpongeConfig config) {
		super(config);
		this.server = config.server;
	}

	/**
	 * @return The {@link Server} the CommandAPI is running on
	 */
	public Server getServer() {
		return server;
	}
}
