package dev.jorel.commandapi.test.network;

import be.seeseemelk.mockbukkit.entity.PlayerMock;
import dev.jorel.commandapi.exceptions.ProtocolVersionTooOldException;
import dev.jorel.commandapi.network.packets.ClientToServerUpdateRequirementsPacket;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ClientToServerUpdateRequirementsPacketTests extends NetworkTestBase {

	/*********
	 * Setup *
	 *********/

	@BeforeEach
	public void setUp() {
		super.setUp();
	}

	@AfterEach
	public void tearDown() {
		super.tearDown();
	}

	/*********
	 * Tests *
	 *********/

	@Test
	void sendReceiveTestWithClientToServerUpdateRequirementsPacket() {
		PlayerMock player = server.addPlayer(); // Protocol version currently 0

		// Error when sending packet to target that has not declared they understand CommandAPI plugin messages
		assertThrowsWithMessage(
			ProtocolVersionTooOldException.class,
			"Tried to send a packet to " + player + ", which is using protocol version 0. " +
				"This system is using version 1. That version is too old to receive the packet. " +
				"CommandAPI version 9.0.4 or greater is required to receive ClientToServerUpdateRequirementsPacket",
			() -> getSentBytes(player, ClientToServerUpdateRequirementsPacket.create())
		);

		setProtocolVersion(player, 1); // Protocol version now 1

		// Packet should be encoded as just the id since no data is included
		assertPacketEncodesAndDecodes(player,
			ClientToServerUpdateRequirementsPacket.create(),
			new byte[]{0}
		);
	}
}
