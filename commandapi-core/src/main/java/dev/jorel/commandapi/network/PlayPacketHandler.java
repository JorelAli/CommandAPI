package dev.jorel.commandapi.network;

import dev.jorel.commandapi.network.packets.UpdateRequirementsPacket;

/**
 * An interface for handling {@link CommandAPIPacket}s on the {@link CommandAPIProtocol#PLAY} channel. Each method
 * handles a different packet. These methods should be implemented on each platform to define what happens when each
 * packet is received.
 *
 * @param <InputChannel> The type of objects that might send a packet to this plugin
 */
public interface PlayPacketHandler<InputChannel> extends CommandAPIPacketHandler<InputChannel> {
	@Override
	default void handlePacket(InputChannel sender, CommandAPIPacket packet) {
		if (packet instanceof UpdateRequirementsPacket p) handleUpdateRequirementsPacket(sender, p);
		else throw new IllegalStateException("Tried to handle " + packet + " with PlayPacketHandler. " +
				"PlayPacketHandler can't handle this packet.");
	}

	/**
	 * Handles a {@link UpdateRequirementsPacket}.
	 *
	 * @param sender The source of the packet.
	 * @param packet The data for the packet.
	 */
	void handleUpdateRequirementsPacket(InputChannel sender, UpdateRequirementsPacket packet);
}
