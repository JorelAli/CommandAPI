package dev.jorel.commandapi.network;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import java.util.Arrays;

/**
 * Handles sending and receiving {@link CommandAPIPacket}s between instances of the CommandAPI plugin on different servers.
 * This class should be extended on each platform to hook into its specific plugin-messaging scheme.
 *
 * @param <InputChannel> The type of objects that might send a packet to this plugin
 * @param <OutputChannel> The type of objects this plugin might send messages to
 */
public abstract class CommandAPIMessenger<InputChannel, OutputChannel> {
	private final CommandAPIPacketHandler<InputChannel> packetHandler;

	/**
	 * Creates a new {@link CommandAPIMessenger}.
	 *
	 * @param packetHandler The {@link CommandAPIPacketHandler} that should handle incoming packets.
	 */
	protected CommandAPIMessenger(CommandAPIPacketHandler<InputChannel> packetHandler) {
		this.packetHandler = packetHandler;
	}

	/**
	 * Disables this messenger, unregistering any events and such that let it send/receive packets before the plugin disables.
	 */
	public abstract void close();

	/**
	 * Handles a message sent to this plugin. The given byte array will be decoded into a {@link CommandAPIPacket}
	 * according to {@link CommandAPIProtocol} and handled appropriately. Nothing happens if the given byte array is empty.
	 *
	 * @param sender The source of this message.
	 * @param input  The byte array that makes up this message.
	 * @throws IllegalStateException if the packet is malformed and cannot be read correctly.
	 */
	protected void messageReceived(InputChannel sender, byte[] input) {
		// Inspired by net.minecraft.PacketDecoder

		// Empty packet. Kinda weird, but nothing to do.
		if (input.length == 0) return;

		ByteArrayDataInput buffer = ByteStreams.newDataInput(input);

		int id;
		CommandAPIPacket packet;
		try {
			// Read the id
			id = buffer.readByte();
			// Use the id to find and use the correct deserialize method
			packet = CommandAPIProtocol.createPacket(id, buffer);
		} catch (IllegalStateException e) {
			throw new IllegalStateException("Exception while reading packet", e);
		}
		if (packet == null) throw new IllegalStateException("Unknown packet id: " + id);

		boolean extraBytes;
		try {
			buffer.readByte();
			extraBytes = true;
		} catch (IllegalStateException ignored) {
			// Buffer is out of bytes, as expected
			// There isn't a method to check this for some reason?
			// https://github.com/google/guava/issues/937
			extraBytes = false;
		}
		if (extraBytes) {
			throw new IllegalStateException(
				"Packet was larger than expected! Extra bytes found after deserializing.\n" +
					"Given: " + Arrays.toString(input) + ", Read: " + packet
			);
		}

		// Handle the packet
		packet.handle(sender, packetHandler);
	}

	/**
	 * Sends a packet to the given target. The given packet will be converted into a byte array and sent using
	 * {@link CommandAPIMessenger#sendRawBytes(Object, byte[])}. The packet must be registered in
	 * {@link CommandAPIProtocol} to be sent.
	 *
	 * @param target The connection to send the packet to.
	 * @param packet The {@link CommandAPIPacket} to send.
	 * @throws IllegalStateException if the given packet is not registered with {@link CommandAPIProtocol}.
	 */
	public void sendPacket(OutputChannel target, CommandAPIPacket packet) {
		// Inspired by net.minecraft.PacketEncoder
		int id = CommandAPIProtocol.getId(packet.getClass());
		if (id == -1) {
			throw new IllegalStateException(
				"Packet class \"" + packet.getClass().getSimpleName() +
					"\" is not registered in the CommandAPIProtocol! This packet cannot be sent."
			);
		}

		ByteArrayDataOutput output = ByteStreams.newDataOutput();

		// Write packet's id
		output.writeByte(id);
		// Write packet's data
		packet.write(output);

		// Send the bytes
		sendRawBytes(target, output.toByteArray());
	}

	/**
	 * Sends an array of bytes to the given target.
	 *
	 * @param target The connection to send this packet to.
	 * @param bytes  The array of bytes to send.
	 */
	protected abstract void sendRawBytes(OutputChannel target, byte[] bytes);
}
