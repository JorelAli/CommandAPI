package dev.jorel.commandapi.network.packets;

import dev.jorel.commandapi.network.CommandAPIMessenger;
import dev.jorel.commandapi.network.CommandAPIPacket;
import dev.jorel.commandapi.network.CommandAPIProtocol;
import dev.jorel.commandapi.network.FriendlyByteBuffer;

import java.util.Objects;

/**
 * A packet that conveys what {@link CommandAPIProtocol#PROTOCOL_VERSION} a connected instance of the CommandAPI is using.
 * <p>
 * This packet may be sent in any direction. It should be sent whenever a Player connects to a Server from the CommandAPI
 * instances on both sides (if they exist). If this packet is never received, the protocol version defaults to 0.
 */
public class SetVersionPacket implements CommandAPIPacket {
	/**
	 * Reads the bytes from the given {@link FriendlyByteBuffer} to create a new {@link SetVersionPacket}.
	 *
	 * @param buffer The buffer to read bytes from.
	 * @return The {@link SetVersionPacket} sent to this plugin.
	 */
	public static SetVersionPacket deserialize(FriendlyByteBuffer buffer) {
		int protocolVersion = buffer.readVarInt();
		return new SetVersionPacket(protocolVersion);
	}

	/**
	 * Creates a new {@link SetVersionPacket}, which can be sent using
	 * {@link CommandAPIMessenger#sendPacket(Object, CommandAPIPacket)}.
	 *
	 * @param protocolVersion The {@link CommandAPIProtocol#PROTOCOL_VERSION} to send in this packet.
	 * @return A new {@link SetVersionPacket}.
	 */
	public static SetVersionPacket create(int protocolVersion) {
		return new SetVersionPacket(protocolVersion);
	}

	private final int protocolVersion;

	private SetVersionPacket(int protocolVersion) {
		this.protocolVersion = protocolVersion;
	}

	/**
	 * @return The {@link CommandAPIProtocol#PROTOCOL_VERSION} held by this packet.
	 */
	public int getProtocolVersion() {
		return protocolVersion;
	}

	@Override
	public void write(FriendlyByteBuffer buffer, Object target, int protocolVersion) {
		// No need to check `int protocolVersion`, we'll always send
		// We probably have not received the other instance's version yet, so it might incorrectly be 0 anyway
		buffer.writeVarInt(this.protocolVersion);
	}

	@Override
	public String toString() {
		return "SetVersionPacket{" +
			"protocolVersion=" + protocolVersion +
			'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		SetVersionPacket packet = (SetVersionPacket) o;
		return getProtocolVersion() == packet.getProtocolVersion();
	}

	@Override
	public int hashCode() {
		return Objects.hash(getProtocolVersion());
	}
}
