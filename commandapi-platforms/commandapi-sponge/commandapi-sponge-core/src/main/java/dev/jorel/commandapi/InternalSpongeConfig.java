package dev.jorel.commandapi;

import org.spongepowered.api.Server;

public class InternalSpongeConfig extends InternalConfig {
	private final Server server;

	public InternalSpongeConfig(CommandAPISpongeConfig config) {
		super(config);
		this.server = config.server;
	}

	public Server getServer() {
		return server;
	}
}
