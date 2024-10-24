package dev.jorel.commandapi.network;

import org.bukkit.entity.Player;

import dev.jorel.commandapi.CommandAPINetworkingMain;

/**
 * A {@link CommandAPIPacketHandlerProvider} for the barebones networking plugin for Bukkit.
 */
public class BukkitNetworkingPacketHandlerProvider implements CommandAPIPacketHandlerProvider<Player> {
	private final BukkitNetworkingHandshakePacketHandler handshakePacketHandler;
	private final BukkitNetworkingPlayPacketHandler playPacketHandler;

	protected BukkitNetworkingPacketHandlerProvider(CommandAPINetworkingMain plugin) {
		handshakePacketHandler = new BukkitNetworkingHandshakePacketHandler(plugin);
		playPacketHandler = new BukkitNetworkingPlayPacketHandler(plugin);
	}

	@Override
	public BukkitNetworkingHandshakePacketHandler getHandshakePacketHandler() {
		return handshakePacketHandler;
	}

	@Override
	public BukkitNetworkingPlayPacketHandler getPlayPacketHandler() {
		return playPacketHandler;
	}
}
