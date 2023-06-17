package dev.jorel.commandapi.network;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.network.packets.ClientToServerUpdateRequirementsPacket;
import org.bukkit.entity.Player;

/**
 * A {@link CommandAPIPacketHandler} for handling {@link CommandAPIPacket}s sent to Bukkit by {@link Player} connections.
 */
public class BukkitPacketHandler implements CommandAPIPacketHandler<Player> {
	@Override
	public void handleUpdateRequirementsPacket(Player sender, ClientToServerUpdateRequirementsPacket packet) {
		CommandAPI.updateRequirements(sender);
	}
}
