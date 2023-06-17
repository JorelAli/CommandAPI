package dev.jorel.commandapi.network;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

// Based on https://www.spigotmc.org/wiki/bukkit-bungee-plugin-messaging-channel/
/**
 * A {@link CommandAPIMessenger} for sending and receiving messages to and from CommandAPIBukkit and {@link Player}s
 * connected to the Bukkit server.
 */
public class BukkitCommandAPIMessenger extends CommandAPIMessenger<Player, Player> implements PluginMessageListener {
	private final String channelName;
	private final JavaPlugin plugin;

	/**
	 * Creates a new {@link BukkitCommandAPIMessenger}.
	 *
	 * @param channelName The name of the channel to communicate on.
	 * @param plugin      The plugin sending and receiving messages.
	 */
	public BukkitCommandAPIMessenger(String channelName, JavaPlugin plugin) {
		super(new BukkitPacketHandler());
		this.channelName = channelName;
		this.plugin = plugin;

		// Register to listen for plugin messages
		Messenger messenger = Bukkit.getServer().getMessenger();
		messenger.registerIncomingPluginChannel(this.plugin, channelName, this);
	}

	@Override
	public void close() {
		// Unregister this listener
		Messenger messenger = Bukkit.getServer().getMessenger();
		messenger.unregisterIncomingPluginChannel(this.plugin, channelName);
	}

	@Override
	public void onPluginMessageReceived(String channel, @NotNull Player player, byte[] message) {
		// A plugin message was sent to Bukkit, check if it is for us
		if (!channel.equals(this.channelName)) return;

		// Handle the message
		messageReceived(player, message);
	}

	@Override
	public void sendRawBytes(Player target, byte[] bytes) {
		target.sendPluginMessage(this.plugin, channelName, bytes);
	}
}
