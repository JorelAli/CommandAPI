package dev.jorel.commandapi.network.packets;

import com.mojang.brigadier.builder.ArgumentBuilder;
import dev.jorel.commandapi.exceptions.ProtocolVersionTooOldException;
import dev.jorel.commandapi.network.CommandAPIMessenger;
import dev.jorel.commandapi.network.CommandAPIPacket;
import dev.jorel.commandapi.network.FriendlyByteBuffer;

import java.util.function.Predicate;

/**
 * A packet that signals a client wants to be sent the <a href="https://wiki.vg/Protocol#Commands">Commands packet</a>.
 * This packet is only intended to be sent in the Client to Server direction.
 * <p>
 * This packet does not contain any data. The receiver should simply update the requirements for the client the packet
 * came from.
 * <p>
 * When the Commands packet is resent, Brigadier reevaluates if that player is able to run each command. If the
 * conditions tested by {@link ArgumentBuilder#requires(Predicate)} have changed, the client is informed of those
 * changes, updating what they know about their requirements.
 */
public class UpdateRequirementsPacket implements CommandAPIPacket {
	/**
	 * Reads the bytes from the given {@link FriendlyByteBuffer} to create a new
	 * {@link UpdateRequirementsPacket}.
	 *
	 * @param ignored This packet has no data, so nothing is read.
	 * @return The {@link UpdateRequirementsPacket} sent to this plugin.
	 */
	public static UpdateRequirementsPacket deserialize(FriendlyByteBuffer ignored) {
		// Nothing to read
		return new UpdateRequirementsPacket();
	}

	/**
	 * Creates a new {@link UpdateRequirementsPacket}, which can be sent using
	 * {@link CommandAPIMessenger#sendPacket(Object, CommandAPIPacket)}.
	 *
	 * @return A new {@link UpdateRequirementsPacket}.
	 */
	public static UpdateRequirementsPacket create() {
		// No parameters to write
		return new UpdateRequirementsPacket();
	}

	private UpdateRequirementsPacket() {

	}

	@Override
	public void write(FriendlyByteBuffer buffer, Object target, int protocolVersion) throws ProtocolVersionTooOldException {
		if (protocolVersion == 0) {
			throw ProtocolVersionTooOldException.whileSending(target, protocolVersion,
				// TODO: If the first released version of the CommandAPI that can receive messages is not 9.0.4, change this message
				"CommandAPI version 9.0.4 or greater is required to receive UpdateRequirementsPacket"
			);
		}
		// Nothing to write
	}

	@Override
	public String toString() {
		return getClass().getSimpleName();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		return obj instanceof UpdateRequirementsPacket;
	}

	@Override
	public int hashCode() {
		return toString().hashCode();
	}
}
