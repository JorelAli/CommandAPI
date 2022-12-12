package dev.jorel.commandapi;

import com.velocitypowered.api.proxy.ProxyServer;

/**
 * A class for wrapping the values provided by a Velocity plugin into a known object
 */
public interface CommandAPIVelocityPluginWrapper {
	ProxyServer getServer();
}
