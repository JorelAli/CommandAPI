package dev.jorel.commandapi.test.arguments;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.entity.PlayerMock;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.StringArgument;
import dev.jorel.commandapi.exceptions.OptionalArgumentException;
import dev.jorel.commandapi.test.Mut;
import dev.jorel.commandapi.test.TestBase;

/**
 * Tests for optional arguments
 */
public class OptionalArgumentTests extends TestBase {

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
	public void testNormalArgument() {
		Mut<String> type = Mut.of();

		new CommandAPICommand("test")
			.withArguments(new StringArgument("string"))
			.executesPlayer((player, args) -> {
				type.set((String) args.get("string"));
			})
			.register();

		PlayerMock player = server.addPlayer();
		server.dispatchCommand(player, "test hello");
		server.dispatchCommand(player, "test world");

		assertEquals("hello", type.get());
		assertEquals("world", type.get());
	}

	@Test
	public void testOptionalArgument() {
		Mut<String> type = Mut.of();

		new CommandAPICommand("test")
			.withOptionalArguments(new StringArgument("string"))
			.executesPlayer((player, args) -> {
				type.set((String) args.get("string"));
			})
			.register();

		PlayerMock player = server.addPlayer();
		server.dispatchCommand(player, "test");
		server.dispatchCommand(player, "test hello");

		assertNull(type.get());
		assertEquals("hello", type.get());
	}

	@Test
	public void testTwoOptionalArguments() {
		Mut<String> type = Mut.of();

		new CommandAPICommand("test")
			.withOptionalArguments(new StringArgument("string1"))
			.withOptionalArguments(new StringArgument("string2"))
			.executesPlayer((player, args) -> {
				type.set((String) args.get("string1"));
				type.set((String) args.get("string2"));
			})
			.register();

		PlayerMock player = server.addPlayer();

		// "/test" should return null, null
		server.dispatchCommand(player, "test");
		assertNull(type.get());
		assertNull(type.get());

		// "/test hello" should return "hello", null
		server.dispatchCommand(player, "test hello");
		assertEquals("hello", type.get());
		assertNull(type.get());

		// "/test hello world" should return "hello", "world"
		server.dispatchCommand(player, "test hello world");
		assertEquals("hello", type.get());
		assertEquals("world", type.get());
	}

	@Test()
	public void testOptionalArgumentException() {
		Mut<String> type = Mut.of();

		// An optional argument followed by a required argument should throw
		// an OptionalArgumentException
		assertThrows(OptionalArgumentException.class, () -> {
			new CommandAPICommand("test")
				.withOptionalArguments(new StringArgument("string1"))
				.withArguments(new StringArgument("string2"))
				.executesPlayer((player, args) -> {
					type.set((String) args.get("string1"));
					type.set((String) args.get("string2"));
				})
				.register();
		});
	}

	@Test
	public void testOptionalArgumentDefault() {
		Mut<String> type = Mut.of();

		new CommandAPICommand("test")
			.withOptionalArguments(new StringArgument("string"))
			.executesPlayer((player, args) -> {
				type.set((String) args.getOrDefault("string", "hello"));
			})
			.register();

		PlayerMock player = server.addPlayer();

		server.dispatchCommand(player, "test");
		server.dispatchCommand(player, "test world");

		assertEquals("hello", type.get());
		assertEquals("world", type.get());
	}

	@Test
	public void testOptionalArgumentDefaultWithSupplier() {
		Mut<String> type = Mut.of();

		new CommandAPICommand("test")
			.withOptionalArguments(new StringArgument("string"))
			.executesPlayer((player, args) -> {
				type.set((String) args.getOrDefault("string", () -> "hello"));
			})
			.register();

		PlayerMock player = server.addPlayer();

		server.dispatchCommand(player, "test");
		server.dispatchCommand(player, "test world");

		assertEquals("hello", type.get());
		assertEquals("world", type.get());
	}

	@Test
	public void testOptionalArgumentWithIndex() {
		Mut<String> type = Mut.of();

		new CommandAPICommand("test")
			.withOptionalArguments(new StringArgument("string"))
			.executesPlayer((player, args) -> {
				type.set((String) args.get(0));
			})
			.register();

		PlayerMock player = server.addPlayer();
		server.dispatchCommand(player, "test");
		server.dispatchCommand(player, "test hello");

		assertNull(type.get());
		assertEquals("hello", type.get());
	}

	@Test
	public void testOptionalArgumentDefaultWithIndex() {
		Mut<String> type = Mut.of();

		new CommandAPICommand("test")
			.withOptionalArguments(new StringArgument("string"))
			.executesPlayer((player, args) -> {
				type.set((String) args.getOrDefault(0, "hello"));
			})
			.register();

		PlayerMock player = server.addPlayer();
		server.dispatchCommand(player, "test");
		server.dispatchCommand(player, "test world");

		assertEquals("hello", type.get());
		assertEquals("world", type.get());
	}

	@Test
	public void testOptionalArgumentDefaultWithIndexWithSupplier() {
		Mut<String> type = Mut.of();

		new CommandAPICommand("test")
			.withOptionalArguments(new StringArgument("string"))
			.executesPlayer((player, args) -> {
				type.set((String) args.getOrDefault(0, () -> "hello"));
			})
			.register();

		PlayerMock player = server.addPlayer();
		server.dispatchCommand(player, "test");
		server.dispatchCommand(player, "test world");

		assertEquals("hello", type.get());
		assertEquals("world", type.get());
	}

}
