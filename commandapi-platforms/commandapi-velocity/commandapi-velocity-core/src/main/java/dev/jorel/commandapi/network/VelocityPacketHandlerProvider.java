package dev.jorel.commandapi.network;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.messages.ChannelMessageSource;

/**
 * A {@link CommandAPIPacketHandler} for Velocity.
 * <p>
 * Velocity may receive messages from a Client ({@link Player}) or a Server {@link ServerConnection}. The common
 * interface there is {@link ChannelMessageSource} (coming into Velocity). Packets may be handled differently depending
 * on their direction, so check which of these two sent the packet before continuing.
 */
public class VelocityPacketHandlerProvider implements CommandAPIPacketHandlerProvider<ChannelMessageSource> {
	private final VelocityHandshakePacketHandler handshakePacketHandler = new VelocityHandshakePacketHandler();

	@Override
	public VelocityHandshakePacketHandler getHandshakePacketHandler() {
		return handshakePacketHandler;
	}

	private final VelocityPlayPacketHandler playPacketHandler = new VelocityPlayPacketHandler();

	@Override
	public VelocityPlayPacketHandler getPlayPacketHandler() {
		return playPacketHandler;
	}
}
