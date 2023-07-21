package dev.jorel.commandapi.network;

/**
 * An interface for defining the platform-level implementations of {@link CommandAPIPacketHandler}. Each method returns a
 * packet handler for a different {@link CommandAPIProtocol}. These methods should be implemented on each platform to
 * determine the class that handles each packet channel.
 *
 * @param <InputChannel> The type of objects that might send a packet to this plugin on this platform.
 */
public interface CommandAPIPacketHandlerProvider<InputChannel> {
	/**
	 * Gets the {@link CommandAPIPacketHandler} for the given {@link CommandAPIProtocol}.
	 *
	 * @param protocol The protocol channel
	 * @return The packet handler
	 */
	default CommandAPIPacketHandler<InputChannel> getHandlerForProtocol(CommandAPIProtocol protocol) {
		return switch (protocol) {
			case HANDSHAKE -> getHandshakePacketHandler();
			case PLAY -> getPlayPacketHandler();
		};
	}

	/**
	 * @return The implementation of {@link HandshakePacketHandler} for this platform.
	 */
	HandshakePacketHandler<InputChannel> getHandshakePacketHandler();

	/**
	 * @return The implementation of {@link PlayPacketHandler} for this platform.
	 */
	PlayPacketHandler<InputChannel> getPlayPacketHandler();
}
