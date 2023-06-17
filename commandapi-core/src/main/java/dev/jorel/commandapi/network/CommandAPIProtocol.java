package dev.jorel.commandapi.network;

import com.google.common.io.ByteArrayDataInput;
import dev.jorel.commandapi.network.packets.ClientToServerUpdateRequirementsPacket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * A utility class that defines the network protocol of the CommandAPI. It handles converting bytes off the network into
 * {@link CommandAPIPacket}s.
 */
public final class CommandAPIProtocol {
	// Utility class should never be instantiated
	private CommandAPIProtocol() {}

	/**
	 * The identifier for the channel used by the CommandAPI to communicate across the network.
	 */
	public static final String CHANNEL_NAME = "commandapi:plugin";

	private static final List<Function<ByteArrayDataInput, ? extends CommandAPIPacket>> idToPacket = new ArrayList<>();
	private static final Map<Class<? extends CommandAPIPacket>, Integer> packetToId = new HashMap<>();

	static {
		register(ClientToServerUpdateRequirementsPacket.class, ClientToServerUpdateRequirementsPacket::deserialize);
	}

	/**
	 * Registers a {@link CommandAPIPacket} so that it can be used on the network. This method assigns ids to each class
	 * of packet sequentially.
	 *
	 * @param clazz        The class of the packet being registered.
	 * @param deserializer The method that reads this packet from a {@link ByteArrayDataInput}.
	 */
	private static <Packet extends CommandAPIPacket> void register(Class<Packet> clazz, Function<ByteArrayDataInput, Packet> deserializer) {
		if (packetToId.containsKey(clazz)) {
			throw new IllegalStateException("Packet class \"" + clazz.getSimpleName() + "\" was already registered!");
		}

		int id = idToPacket.size();
		idToPacket.add(deserializer);
		packetToId.put(clazz, id);
	}

	/**
	 * Reads a {@link ByteArrayDataInput} to create a {@link CommandAPIPacket}.
	 *
	 * @param id     The id of this packet read from the network. Used to select the appropriate deserialize method.
	 * @param buffer The bytes of this packet.
	 * @return The deserialized packet, or null if the given id could not be found.
	 * @throws IllegalStateException when there is a problem deserializing the packet.
	 */
	public static CommandAPIPacket createPacket(int id, ByteArrayDataInput buffer) {
		if (id < 0 || id >= idToPacket.size()) return null;
		Function<ByteArrayDataInput, ? extends CommandAPIPacket> deserializer = idToPacket.get(id);
		return deserializer == null ? null : deserializer.apply(buffer);
	}

	/**
	 * Gets the id for the given {@link CommandAPIPacket} class.
	 *
	 * @param clazz The class of the packet to get the id for.
	 * @return The id of the given packet, or -1 if the packet could not be found.
	 */
	public static int getId(Class<? extends CommandAPIPacket> clazz) {
		return packetToId.getOrDefault(clazz, -1);
	}
}
