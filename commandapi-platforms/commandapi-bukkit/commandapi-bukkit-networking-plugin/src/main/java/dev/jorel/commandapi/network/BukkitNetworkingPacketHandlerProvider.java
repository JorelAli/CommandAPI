package dev.jorel.commandapi.network;

import org.bukkit.entity.Player;

import dev.jorel.commandapi.CommandAPINetworkingMain;

/**
 * A {@link CommandAPIPacketHandlerProvider} for the barebones netowrking plugin for Bukkit.
 */
public class BukkitNetworkingPacketHandlerProvider implements CommandAPIPacketHandlerProvider<Player> {
	private final BukkitHandshakePacketHandler handshakePacketHandler;
	private final BukkitPlayPacketHandler playPacketHandler;

	protected BukkitNetworkingPacketHandlerProvider(CommandAPINetworkingMain plugin) {
		handshakePacketHandler = new BukkitHandshakePacketHandler(plugin);
		playPacketHandler = new BukkitPlayPacketHandler(plugin);
	}

	@Override
	public BukkitHandshakePacketHandler getHandshakePacketHandler() {
		return handshakePacketHandler;
	}

	@Override
	public BukkitPlayPacketHandler getPlayPacketHandler() {
		return playPacketHandler;
	}
}
