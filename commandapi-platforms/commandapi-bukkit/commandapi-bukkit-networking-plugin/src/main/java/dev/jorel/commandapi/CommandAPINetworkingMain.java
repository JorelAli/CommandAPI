package dev.jorel.commandapi;

import org.bukkit.plugin.java.JavaPlugin;

import dev.jorel.commandapi.network.BukkitNetworkingCommandAPIMessenger;

/**
 * Main CommandAPI networking plugin entrypoint
 */
public class CommandAPINetworkingMain extends JavaPlugin {
	private BukkitNetworkingCommandAPIMessenger messenger;

	@Override
	public void onEnable() {
		messenger = new BukkitNetworkingCommandAPIMessenger(this);
	}

	/**
	 * @return The {@link BukkitNetworkingCommandAPIMessenger} handling packets.
	 */
	public BukkitNetworkingCommandAPIMessenger getMessenger() {
		return messenger;
	}
}
