package dev.jorel.commandapi.network.packets;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.mojang.brigadier.builder.ArgumentBuilder;
import dev.jorel.commandapi.network.CommandAPIMessenger;
import dev.jorel.commandapi.network.CommandAPIPacket;
import dev.jorel.commandapi.network.CommandAPIPacketHandler;

import java.util.function.Predicate;

/**
 * A packet that signals a client wants to be sent the <a href="https://wiki.vg/Protocol#Commands">Commands packet</a>.
 * <p>
 * This packet does not contain any data. The receiver should simply update the requirements for the client the packet
 * came from.
 * <p>
 * When the Commands packet is resent, Brigadier reevaluates if that player is able to run each command. If the
 * conditions tested by {@link ArgumentBuilder#requires(Predicate)} have changed, the client is informed of those
 * changes, updating what they know about their requirements.
 */
public class ClientToServerUpdateRequirementsPacket implements CommandAPIPacket {
	/**
	 * Reads the bytes from the given {@link ByteArrayDataInput} to create a new
	 * {@link ClientToServerUpdateRequirementsPacket}.
	 *
	 * @param ignored This packet has no data, so nothing is read.
	 * @return The {@link ClientToServerUpdateRequirementsPacket} sent to this plugin.
	 */
	public static ClientToServerUpdateRequirementsPacket deserialize(ByteArrayDataInput ignored) {
		// Nothing to read
		return new ClientToServerUpdateRequirementsPacket();
	}

	/**
	 * Creates a new {@link ClientToServerUpdateRequirementsPacket}, which can be sent using
	 * {@link CommandAPIMessenger#sendPacket(Object, CommandAPIPacket)}.
	 *
	 * @return A new {@link ClientToServerUpdateRequirementsPacket}.
	 */
	public static ClientToServerUpdateRequirementsPacket create() {
		// No parameters to write
		return new ClientToServerUpdateRequirementsPacket();
	}

	private ClientToServerUpdateRequirementsPacket() {

	}

	@Override
	public void write(ByteArrayDataOutput buffer) {
		// Nothing to write
	}

	@Override
	public <InputChannel> void handle(InputChannel sender, CommandAPIPacketHandler<InputChannel> packetHandler) {
		packetHandler.handleUpdateRequirementsPacket(sender, this);
	}

	@Override
	public String toString() {
		return getClass().getSimpleName();
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		return obj instanceof ClientToServerUpdateRequirementsPacket;
	}
}
