package dev.jorel.commandapi.network;

import dev.jorel.commandapi.CommandAPINetworkingMain;
import dev.jorel.commandapi.network.packets.UpdateRequirementsPacket;
import org.bukkit.entity.Player;

/**
 * A {@link PlayPacketHandler} for handling {@link CommandAPIPacket}s sent to Bukkit by {@link Player} connections.
 */
public class BukkitNetworkingPlayPacketHandler implements PlayPacketHandler<Player> {
	private final CommandAPINetworkingMain plugin;

	protected BukkitNetworkingPlayPacketHandler(CommandAPINetworkingMain plugin) {
		this.plugin = plugin;
	}

	@Override
	public void handleUpdateRequirementsPacket(Player sender, UpdateRequirementsPacket packet) {
		sender.updateCommands();
	}
}
