package dev.jorel.commandapi.network;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.messages.ChannelMessageSource;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.network.packets.ClientToServerUpdateRequirementsPacket;

/**
 * A {@link CommandAPIPacketHandler} for handling {@link CommandAPIPacket}s sent to Velocity.
 * <p>
 * Velocity may receive messages from a Client ({@link Player}) or a Server {@link ServerConnection}. The common
 * interface there is {@link ChannelMessageSource} (coming into Velocity). Packets may be handled differently depending
 * on their direction, so check which of these two sent the packet before continuing.
 */
public class VelocityPacketHandler implements CommandAPIPacketHandler<ChannelMessageSource> {
	@Override
	public void handleUpdateRequirementsPacket(ChannelMessageSource sender, ClientToServerUpdateRequirementsPacket packet) {
		// This client-to-server packet should never be sent by the server
		if (sender instanceof ServerConnection) return;

		// This packet may be sent to Velocity when the CommandAPI is a client mod
		// We shall update their requirements as requested
		Player player = (Player) sender;
		CommandAPI.updateRequirements(player);
	}
}
