package dev.jorel.commandapi.network;

/**
 * An interface for handling {@link CommandAPIPacket}s. Direct children of this interface handles packets for each of the
 * {@link CommandAPIProtocol}s, and those children should be implemented to handle packets for each platform.
 *
 * @param <InputChannel> The type of objects that might send a packet to this plugin on this platform
 */
public interface CommandAPIPacketHandler<InputChannel> {
	/**
	 * Handles a {@link CommandAPIPacket} according to the implementation of {@link CommandAPIPacketHandler}. This
	 * should be implemented for each {@link CommandAPIProtocol} to handle packets on that channel.
	 *
	 * @param sender The source of the packet.
	 * @param packet The data for the packet.
	 */
	void handlePacket(InputChannel sender, CommandAPIPacket packet);
}
