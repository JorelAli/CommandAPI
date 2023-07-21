package dev.jorel.commandapi.network.packets;

import dev.jorel.commandapi.exceptions.ProtocolVersionTooOldException;
import dev.jorel.commandapi.network.CommandAPIMessenger;
import dev.jorel.commandapi.network.CommandAPIPacket;
import dev.jorel.commandapi.network.CommandAPIProtocol;
import dev.jorel.commandapi.network.FriendlyByteBuffer;

import java.util.Objects;

/**
 * A packet that informs a connected instance of the CommandAPI their {@link CommandAPIProtocol#PROTOCOL_VERSION} is too old
 * to receive a different packet this instance wanted to send.
 * <p>
 * This packet may be sent in any direction. It is automatically sent whenever a {@link ProtocolVersionTooOldException}
 * is thrown while writing another packet. When received, a {@link ProtocolVersionTooOldException} should be thrown on the
 * receiving side that mimics the original exception.
 */
public class ProtocolVersionTooOldPacket implements CommandAPIPacket {
	/**
	 * Reads the bytes from the given {@link FriendlyByteBuffer} to create a new {@link ProtocolVersionTooOldPacket}.
	 *
	 * @param buffer The buffer to read bytes from.
	 * @return The {@link ProtocolVersionTooOldPacket} sent to this plugin.
	 */
	public static ProtocolVersionTooOldPacket deserialize(FriendlyByteBuffer buffer) {
		int protocolVersion = buffer.readVarInt();
		String reason = buffer.readString();
		return new ProtocolVersionTooOldPacket(protocolVersion, reason);
	}

	/**
	 * Creates a new {@link ProtocolVersionTooOldPacket}, which can be sent using
	 * {@link CommandAPIMessenger#sendPacket(Object, CommandAPIPacket)}.
	 *
	 * @param protocolVersion The {@link CommandAPIProtocol#PROTOCOL_VERSION} to send in this packet.
	 * @param reason          The reason why this packet is being sent.
	 * @return A new {@link ProtocolVersionTooOldPacket}.
	 */
	public static ProtocolVersionTooOldPacket create(int protocolVersion, String reason) {
		return new ProtocolVersionTooOldPacket(protocolVersion, reason);
	}

	private final int protocolVersion;
	private final String reason;

	private ProtocolVersionTooOldPacket(int protocolVersion, String reason) {
		this.protocolVersion = protocolVersion;
		this.reason = reason;
	}

	/**
	 * @return The {@link CommandAPIProtocol#PROTOCOL_VERSION} of the CommandAPI instance this packet is being sent from.
	 */
	public int getProtocolVersion() {
		return protocolVersion;
	}

	/**
	 * @return The reason why this packet is being sent.
	 */
	public String getReason() {
		return reason;
	}

	@Override
	public void write(FriendlyByteBuffer buffer, Object target, int protocolVersion) {
		// No need to check `int protocolVersion`, we'll always send
		// If we did throw an exception, that would trigger another packet to be sent, causing infinite recursion anyway
		buffer.writeVarInt(this.protocolVersion);
		buffer.writeString(this.reason);
	}

	@Override
	public String toString() {
		return "ProtocolVersionTooOldPacket{" +
			"protocolVersion=" + protocolVersion +
			", reason='" + reason + '\'' +
			'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ProtocolVersionTooOldPacket that = (ProtocolVersionTooOldPacket) o;
		return getProtocolVersion() == that.getProtocolVersion() && Objects.equals(getReason(), that.getReason());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getProtocolVersion(), getReason());
	}
}
