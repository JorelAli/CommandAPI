package dev.jorel.commandapi.network;

import org.bukkit.entity.Player;

/**
 * A {@link CommandAPIPacketHandlerProvider} for Bukkit.
 */
public class BukkitPacketHandlerProvider implements CommandAPIPacketHandlerProvider<Player> {
	private final BukkitHandshakePacketHandler handshakePacketHandler = new BukkitHandshakePacketHandler();

	@Override
	public BukkitHandshakePacketHandler getHandshakePacketHandler() {
		return handshakePacketHandler;
	}

	private final BukkitPlayPacketHandler playPacketHandler = new BukkitPlayPacketHandler();

	@Override
	public BukkitPlayPacketHandler getPlayPacketHandler() {
		return playPacketHandler;
	}
}
