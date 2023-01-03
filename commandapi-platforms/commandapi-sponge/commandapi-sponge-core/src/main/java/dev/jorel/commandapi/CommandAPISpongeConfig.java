package dev.jorel.commandapi;

import org.spongepowered.api.Server;

/**
 * A class that contains information needed to configure the CommandAPI on Sponge-based servers.
 */
public class CommandAPISpongeConfig extends CommandAPIConfig<CommandAPISpongeConfig> {
	Server server;

	/**
	 * Creates a new CommandAPISpongeConfig object. Variables in this
	 * constructor are required to load the CommandAPI on Sponge properly.
	 *
	 * @param server The {@link Server} that the CommandAPI is running on.
	 */
	public CommandAPISpongeConfig(Server server) {
		this.server = server;
	}

	@Override
	public CommandAPISpongeConfig instance() {
		return this;
	}
}
