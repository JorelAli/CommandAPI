package dev.jorel.commandapi.network;

import dev.jorel.commandapi.CommandAPINetworkingMain;
import dev.jorel.commandapi.network.packets.SetVersionPacket;
import org.bukkit.entity.Player;

/**
 * A {@link HandshakePacketHandler} for handling {@link CommandAPIPacket}s sent to Bukkit by {@link Player} connections.
 */
public class BukkitHandshakePacketHandler implements HandshakePacketHandler<Player> {
	private final CommandAPINetworkingMain plugin;

	protected BukkitHandshakePacketHandler(CommandAPINetworkingMain plugin) {
		this.plugin = plugin;
	}

	@Override
	public void handleSetVersionPacket(Player sender, SetVersionPacket packet) {
		plugin.getMessenger().setProtocolVersion(sender, packet.protocolVersion());
	}
}
