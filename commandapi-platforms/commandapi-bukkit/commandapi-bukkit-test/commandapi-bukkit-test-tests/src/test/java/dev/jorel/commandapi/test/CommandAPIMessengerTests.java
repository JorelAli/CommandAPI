package dev.jorel.commandapi.test;

import be.seeseemelk.mockbukkit.entity.PlayerMock;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import dev.jorel.commandapi.CommandAPIBukkit;
import dev.jorel.commandapi.network.*;
import dev.jorel.commandapi.network.packets.ClientToServerUpdateRequirementsPacket;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.exceptions.base.MockitoException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.spy;

class CommandAPIMessengerTests extends TestBase {

	/*********
	 * Setup *
	 *********/
	private ArgumentCaptor<byte[]> rawBytesCapture;
	private ArgumentCaptor<CommandAPIPacket> packetCapture;

	private byte testPacketId;
	private static TestPacket handledTestPacket = null;

	@BeforeEach
	public void setUp() {
		// Load and enable
		super.setUp();
		disablePaperImplementations();
		assertDoesNotThrow(() -> server.getScheduler().performOneTick());

		// Mock messenger
		BukkitCommandAPIMessenger spyMessenger = spy(CommandAPIBukkit.get().getMessenger());

		rawBytesCapture = ArgumentCaptor.forClass(byte[].class);
		doCallRealMethod().when(spyMessenger).sendRawBytes(any(), rawBytesCapture.capture());

		// Mock packetHandler
		CommandAPIPacketHandler<?> spyPacketHandler = spy(MockPlatform.getFieldAs(
			CommandAPIMessenger.class, "packetHandler", spyMessenger, CommandAPIPacketHandler.class
		));

		packetCapture = ArgumentCaptor.forClass(CommandAPIPacket.class);
		doCallRealMethod().when(spyPacketHandler)
			.handleUpdateRequirementsPacket(any(), (ClientToServerUpdateRequirementsPacket) packetCapture.capture());

		// Insert spies
		MockPlatform.setField(CommandAPIMessenger.class, "packetHandler", spyMessenger, spyPacketHandler);
		MockPlatform.setField(CommandAPIBukkit.class, "messenger", CommandAPIBukkit.get(), spyMessenger);


		// Register test packet for further testing
		testPacketId = (byte) CommandAPIProtocol.getId(TestPacket.class);
		if(testPacketId == -1) {
			testPacketId = (byte) MockPlatform.getFieldAs(CommandAPIProtocol.class, "idToPacket", null, List.class).size();
			try {
				Method registerPacket = CommandAPIProtocol.class.getDeclaredMethod("register", Class.class, Function.class);
				registerPacket.setAccessible(true);
				registerPacket.invoke(null, TestPacket.class, (Function<ByteArrayDataInput, ?>) TestPacket::deserialize);
			} catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
	}

	@AfterEach
	public void tearDown() {
		super.tearDown();
	}

	/***************************************************
	 * Utility methods for sending and receiving bytes *
	 ***************************************************/
	private byte[] getSentBytes(Player target, CommandAPIPacket packet) {
		CommandAPIBukkit.get().getMessenger().sendPacket(target, packet);
		try {
			return rawBytesCapture.getValue();
		} catch (MockitoException ignored) {
			// Exception may be thrown if argument was not captured because the method was not called
			// In that case, return null to signal no bytes were sent
			return null;
		}
	}

	private CommandAPIPacket getHandledPacket(Player source, byte[] message) {
		CommandAPIBukkit.get().getMessenger().onPluginMessageReceived(CommandAPIProtocol.CHANNEL_NAME, source, message);
		try {
			return packetCapture.getValue();
		} catch (MockitoException ignored) {
			// Exception may be thrown if argument was not captured because the method was not called
			// In that case, return null to signal no packet was handled

			// Except... there is also the special case for TestPacket, which doesn't have a method to call in
			//  CommandAPIPacketHandler since it isn't a real packet
			TestPacket packet = handledTestPacket;
			handledTestPacket = null;
			return packet;
		}
	}

	private void assertPacketEncodesAndDecodes(Player player, CommandAPIPacket packet, byte[] bytes) {
		assertArrayEquals(bytes, getSentBytes(player, packet));
		assertEquals(packet, getHandledPacket(player, bytes));
	}

	/***********************************
	 * Special packets used in testing *
	 ***********************************/
	private static class UnregisteredPacket implements CommandAPIPacket {
		@Override
		public void write(ByteArrayDataOutput buffer) {
			// Nothing to do
		}

		@Override
		public <InputChannel> void handle(InputChannel sender, CommandAPIPacketHandler<InputChannel> packetHandler) {
			// Nothing to do
		}
	}
	
	private static class TestPacket implements CommandAPIPacket  {

		public static TestPacket deserialize(ByteArrayDataInput input) {
			int a = input.readInt();
			int b = input.readInt();
			return new TestPacket(a, b);
		}

		public static TestPacket create(int a, int b) {
			return new TestPacket(a, b);
		}

		private final int a;
		private final int b;

		private TestPacket(int a, int b) {
			this.a = a;
			this.b = b;
		}

		@Override
		public void write(ByteArrayDataOutput buffer) {
			buffer.writeInt(a);
			buffer.writeInt(b);
		}

		@Override
		public <InputChannel> void handle(InputChannel sender, CommandAPIPacketHandler<InputChannel> packetHandler) {
			handledTestPacket = this;
		}

		@Override
		public boolean equals(Object obj) {
			if(obj == null) return false;
			if(!(obj instanceof TestPacket other)) return false;
			return this.a == other.a && this.b == other.b;
		}
	}

	/*********
	 * Tests *
	 *********/

	@Test
	void exceptionTestWithCommandAPIMessengerSending() {
		PlayerMock player = server.addPlayer();

		// Unregistered packets cannot be sent
		assertThrowsWithMessage(
			IllegalStateException.class,
			"Packet class \"UnregisteredPacket\" is not registered in the CommandAPIProtocol! This packet cannot be sent.",
			() -> getSentBytes(player, new UnregisteredPacket())
		);
	}

	@Test
	void exceptionTestWithCommandAPIMessengerReceiving() {
		PlayerMock player = server.addPlayer();

		// Empty packet is actually fine, nothing should happen
		assertDoesNotThrow(() -> assertNull(getHandledPacket(player, new byte[0])));

		// Invalid packet id "-1" can't be deserialized
		assertThrowsWithMessage(
			IllegalStateException.class,
			"Unknown packet id: -1",
			() -> getHandledPacket(player, new byte[]{-1, 0, 0})
		);

		// Packet expected more bytes after given data
		IllegalStateException exception = assertThrowsWithMessage(
			IllegalStateException.class,
			"Exception while reading packet",
			// A written int takes up 4 bytes, and TestPacket expects 2 ints
			() -> getHandledPacket(player, new byte[]{testPacketId, 0, 0, 0, 0, 1, 1})
		);
		assertEquals("java.io.EOFException", exception.getCause().getMessage());

		// Extra bytes found after expected packet data
		assertThrowsWithMessage(
			IllegalStateException.class,
			"Packet was larger than expected! Extra bytes found after deserializing.\n" +
				"Given: [0, 0, 0], Read: ClientToServerUpdateRequirementsPacket",
			// ClientToServerUpdateRequirementsPacket expects no data after id (0)
			() -> getHandledPacket(player, new byte[]{0, 0, 0})
		);
	}

	/****************
	 * Packet tests *
	 ****************/
	@Test
	void sendReceiveTestWithTestPacket() {
		// No packets exist yet that contain data, so the TestPacket is temporarily here to make sure all that works
		PlayerMock player = server.addPlayer();

		// Ints are encoded to 4 bytes each
		assertPacketEncodesAndDecodes(player,
			TestPacket.create(0, 0),
			new byte[]{testPacketId, 0, 0, 0, 0, 0, 0, 0, 0}
		);
		assertPacketEncodesAndDecodes(player,
			TestPacket.create(255, 256),
			new byte[]{testPacketId, 0, 0, 0, -1, 0, 0, 1, 0}
		);
		assertPacketEncodesAndDecodes(player,
			TestPacket.create(65535, 65536),
			new byte[]{testPacketId, 0, 0, -1, -1, 0, 1, 0, 0}
		);
		assertPacketEncodesAndDecodes(player,
			TestPacket.create(16777215, 16777216),
			new byte[]{testPacketId, 0, -1, -1, -1, 1, 0, 0, 0}
		);
		assertPacketEncodesAndDecodes(player,
			TestPacket.create(2147483647, -2147483648),
			new byte[]{testPacketId, 127, -1, -1, -1, -128, 0, 0, 0}
		);
	}

	@Test
	void sendReceiveTestWithClientToServerUpdateRequirementsPacket() {
		PlayerMock player = server.addPlayer();

		// Packet should be encoded as just the id since no data is included
		assertPacketEncodesAndDecodes(player, ClientToServerUpdateRequirementsPacket.create(), new byte[]{0});
	}
}
