package dev.jorel.commandapi.network;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

/**
 * An interface for formatting messages that can be sent between instances of the CommandAPI plugin on different servers.
 * <p>
 * The {@link CommandAPIPacket#write(ByteArrayDataOutput)} method should be mirrored by a static
 * {@code deserialize(}{@link ByteArrayDataInput} {@code input)} method. This {@code deserialize} method can assume any
 * bytes written by the {@code write} method will be present to reconstruct this packet from an array of bytes. The
 * deserialize method should be registered with {@link CommandAPIProtocol} to give this packet a protocol id.
 */
public interface CommandAPIPacket {
	/**
	 * Writes the data of this packet to a {@link ByteArrayDataOutput}.
	 *
	 * @param buffer The byte buffer to write to.
	 */
	void write(ByteArrayDataOutput buffer);

	/**
	 * Handles this packet according to the given {@link CommandAPIPacketHandler}. This should be implemented by calling
	 * the corresponding method in {@link CommandAPIPacketHandler} for this packet.
	 *
	 * @param sender         The source of this packet.
	 * @param packetHandler  The implementation of {@link CommandAPIPacketHandler} being used.
	 * @param <InputChannel> The type of objects that might send a packet to this plugin.
	 */
	<InputChannel> void handle(InputChannel sender, CommandAPIPacketHandler<InputChannel> packetHandler);
}
