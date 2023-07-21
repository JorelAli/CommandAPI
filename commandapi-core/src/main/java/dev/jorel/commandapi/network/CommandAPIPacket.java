package dev.jorel.commandapi.network;

import dev.jorel.commandapi.exceptions.ProtocolVersionTooOldException;

/**
 * An interface for formatting messages that can be sent between separate instances of the CommandAPI.
 * <p>
 * The {@link CommandAPIPacket#write(FriendlyByteBuffer, Object, int)} method should be mirrored by a static
 * {@code deserialize(}{@link FriendlyByteBuffer} {@code input)} method. This {@code deserialize} method can assume any
 * bytes written by the {@code write} method will be present to reconstruct this packet from an array of bytes.
 * <p>
 * To set up a packet properly, it needs to be registered to one of the channels of {@link CommandAPIProtocol}. There,
 * the packet's {@code deserialize} method will be linked to a protocol id. An appropriate method should be added
 * to one of the implementations of {@link CommandAPIPacketHandler} to handle this packet when received.
 * <p>
 * It is recommended to implement a static {@code create()} method that takes in any parameters needed for the packet
 * and returns an instance of the packet. Overriding {@link Object#toString()}, {@link Object#equals(Object)}, and
 * {@link Object#hashCode()} is also recommended for compatibility with certain error messages and the testing framework.
 */
public interface CommandAPIPacket {
	/**
	 * Writes the data of this packet to a {@link FriendlyByteBuffer}.
	 *
	 * @param buffer          The byte buffer to write to.
	 * @param target          The OutputChannel this packet is being sent to.
	 * @param protocolVersion The {@link CommandAPIProtocol#PROTOCOL_VERSION} of the CommandAPI instance this packet is
	 *                        being sent to. Implementations of this method should make sure not to send any data the
	 *                        receiving instance can not understand. If this packet simply cannot be sent to the receiving
	 *                        version, it is appropriate to throw an {@link ProtocolVersionTooOldException}.
	 * @throws ProtocolVersionTooOldException if this packet cannot be sent to the receiving protocol version.
	 */
	void write(FriendlyByteBuffer buffer, Object target, int protocolVersion) throws ProtocolVersionTooOldException;
}
