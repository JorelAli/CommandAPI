package dev.jorel.commandapi;

import org.spongepowered.api.Server;

public class CommandAPISpongeConfig extends CommandAPIConfig<CommandAPISpongeConfig> {
	Server server;

	public CommandAPISpongeConfig(Server server) {
		this.server = server;
	}

	@Override
	public CommandAPISpongeConfig instance() {
		return this;
	}
}
