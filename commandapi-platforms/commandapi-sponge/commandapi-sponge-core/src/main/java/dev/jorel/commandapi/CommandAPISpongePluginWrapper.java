package dev.jorel.commandapi;

import org.spongepowered.api.Server;

/**
 * A class for wrapping the values provided by a Sponge plugin into a known
 * object
 */
public interface CommandAPISpongePluginWrapper {
	Server getServer();
}
