package dev.jorel.commandapi;

import org.spongepowered.api.Server;

public class CommandAPISpongeConfig extends CommandAPIConfig {
	Server server;

	public CommandAPISpongeConfig(Server server) {
		this.server = server;
	}
}
