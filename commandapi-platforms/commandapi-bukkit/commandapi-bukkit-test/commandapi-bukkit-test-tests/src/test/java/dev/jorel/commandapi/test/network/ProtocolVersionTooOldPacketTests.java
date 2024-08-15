package dev.jorel.commandapi.test.network;

import be.seeseemelk.mockbukkit.entity.PlayerMock;
import dev.jorel.commandapi.exceptions.ProtocolVersionTooOldException;
import dev.jorel.commandapi.network.packets.ProtocolVersionTooOldPacket;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ProtocolVersionTooOldPacketTests  extends NetworkTestBase {

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
	void sendTestWithProtocolVersionTooOldPacket() {
		PlayerMock player = server.addPlayer();

		// Packet is encoded as id, VarInt protocol version, then String for the reason inside
		assertArrayEquals(
			new byte[]{1, 0, 9, 'M', 'e', 's', 's', 'a', 'g', 'e', ' ', '1'},
			getSentBytes(player, ProtocolVersionTooOldPacket.create(0, "Message 1"))
		);

		assertArrayEquals(
			new byte[]{1, (byte) 0x80, 0x01, 11, 'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd'},
			getSentBytes(player, ProtocolVersionTooOldPacket.create(128, "Hello World"))
		);
	}

	@Test
	void receiveTestWithProtocolVersionTooOldPacket() {
		PlayerMock player = server.addPlayer();

		// When this packet is received, an appropriate ProtocolVersionTooOldException should be thrown
		assertThrowsWithMessage(
			ProtocolVersionTooOldException.class,
			player + " tried to send a packet here using protocol version 0. This system is using version 1. " +
				"This version is too old to receive the packet. Message 1",
			() -> getHandledPacket(player, "commandapi:handshake",
				new byte[]{1, 0, 9, 'M', 'e', 's', 's', 'a', 'g', 'e', ' ', '1'})
		);
		assertEquals(ProtocolVersionTooOldPacket.create(0, "Message 1"), packetCapture.getValue());
	}
}
