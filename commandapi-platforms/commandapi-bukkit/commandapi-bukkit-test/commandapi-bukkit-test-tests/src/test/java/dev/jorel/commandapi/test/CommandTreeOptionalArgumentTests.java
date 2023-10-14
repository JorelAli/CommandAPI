package dev.jorel.commandapi.test;

import be.seeseemelk.mockbukkit.entity.PlayerMock;
import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import dev.jorel.commandapi.executors.PlayerCommandExecutor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Tests for using optional arguments in CommandTrees
 */
class CommandTreeOptionalArgumentTests extends TestBase {

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
	void testNormalArgument() {
		Mut<String> results = Mut.of();

		new CommandTree("test")
			.then(new StringArgument("string").executesPlayer((player, args) -> {
				results.set(args.getUnchecked("string"));
			}))
			.register();

		PlayerMock player = server.addPlayer();

		assertCommandFailsWith(player, "test", "Unknown or incomplete command, see below for error at position 4: test<--[HERE]");

		assertStoresResult(player, "test hello", results, "hello");
		assertStoresResult(player, "test world", results, "world");

		assertNoMoreResults(results);
	}

	@Test
	void testOptionalArgument() {
		Mut<String> results = Mut.of();

		new CommandTree("test")
			.withOptionalArguments(new StringArgument("string"))
			.executesPlayer((player, args) -> {
				results.set(args.getUnchecked("string"));
			})
			.register();

		PlayerMock player = server.addPlayer();

		assertStoresResult(player, "test", results, null);

		assertStoresResult(player, "test hello", results, "hello");
		assertStoresResult(player, "test world", results, "world");

		assertNoMoreResults(results);
	}

	@Test
	void testTwoOptionalArguments() {
		Mut<String> arg1 = Mut.of();
		Mut<String> arg2 = Mut.of();

		new CommandTree("test")
			.withOptionalArguments(
				new StringArgument("string1"),
				new StringArgument("string2")
			)
			.executesPlayer((player, args) -> {
				arg1.set(args.getUnchecked("string1"));
				arg2.set(args.getUnchecked("string2"));
			})
			.register();

		PlayerMock player = server.addPlayer();

		// "/test" should return null, null
		assertStoresResult(player, "test", arg1, null);
		assertNull(arg2.get());

		// "/test hello" should return "hello", null
		assertStoresResult(player, "test hello", arg1, "hello");
		assertNull(arg2.get());

		// "/test hello world" should return "hello", "world"
		assertStoresResult(player, "test hello world", arg1, "hello");
		assertEquals("world", arg2.get());

		assertNoMoreResults(arg1);
		assertNoMoreResults(arg2);
	}

	@Test
	void testCombinedOptionalArguments() {
		Mut<String> results = Mut.of();

		new CommandTree("test")
			.withOptionalArguments(
				new LiteralArgument("1").combineWith(new LiteralArgument("2"), new LiteralArgument("3")),
				new LiteralArgument("4"),
				new LiteralArgument("5").combineWith(new LiteralArgument("6"))
			)
			.executesPlayer((player, args) -> {
				results.set(args.fullInput());
			})
			.register();

		PlayerMock player = server.addPlayer();

		assertStoresResult(player, "test", results, "/test");

		assertCommandFailsWith(player, "test 1", "Unknown or incomplete command, see below for error at position 6: test 1<--[HERE]");
		assertCommandFailsWith(player, "test 1 2", "Unknown or incomplete command, see below for error at position 8: test 1 2<--[HERE]");
		assertStoresResult(player, "test 1 2 3", results, "/test 1 2 3");

		assertStoresResult(player, "test 1 2 3 4", results, "/test 1 2 3 4");

		assertCommandFailsWith(player, "test 1 2 3 4 5", "Unknown or incomplete command, see below for error at position 14: ... 1 2 3 4 5<--[HERE]");
		assertStoresResult(player, "test 1 2 3 4 5 6", results, "/test 1 2 3 4 5 6");

		assertNoMoreResults(results);
	}

	@Test
	void testBranchesWithOptionalArguments() {
		Mut<String> executedPath = Mut.of();
		Mut<String> results = Mut.of();

		Function<String, PlayerCommandExecutor> executor = path -> (player, args) -> {
			executedPath.set(path);
			results.set(args.fullInput());
		};

		new CommandTree("test")
			.then(new LiteralArgument("1")
				.then(new LiteralArgument("1")
					.withOptionalArguments(new LiteralArgument("optional"))
					.executesPlayer(executor.apply("1 1"))
				)
				.then(new LiteralArgument("2")
					.withOptionalArguments(new LiteralArgument("optional"))
					.executesPlayer(executor.apply("1 2"))
				)
				.withOptionalArguments(new LiteralArgument("optional"))
				.executesPlayer(executor.apply("1"))
			)
			.then(new LiteralArgument("2")
				.then(new LiteralArgument("1")
					.withOptionalArguments(new LiteralArgument("optional"))
					.executesPlayer(executor.apply("2 1"))
				)
				.then(new LiteralArgument("2")
					.withOptionalArguments(new LiteralArgument("optional"))
					.executesPlayer(executor.apply("2 2"))
				)
				.withOptionalArguments(new LiteralArgument("optional"))
				.executesPlayer(executor.apply("2"))
			)
			.withOptionalArguments(new LiteralArgument("optional"))
			.executesPlayer(executor.apply(""))
			.register();

		PlayerMock player = server.addPlayer();

		// Base command path
		assertStoresResult(player, "test", executedPath, "");
		assertEquals("/test", results.get());
		assertStoresResult(player, "test optional", executedPath, "");
		assertEquals("/test optional", results.get());

		// "1" path
		assertStoresResult(player, "test 1", executedPath, "1");
		assertEquals("/test 1", results.get());
		assertStoresResult(player, "test 1 optional", executedPath, "1");
		assertEquals("/test 1 optional", results.get());

		// "1 1" path
		assertStoresResult(player, "test 1 1", executedPath, "1 1");
		assertEquals("/test 1 1", results.get());
		assertStoresResult(player, "test 1 1 optional", executedPath, "1 1");
		assertEquals("/test 1 1 optional", results.get());

		// "1 2" path
		assertStoresResult(player, "test 1 2", executedPath, "1 2");
		assertEquals("/test 1 2", results.get());
		assertStoresResult(player, "test 1 2 optional", executedPath, "1 2");
		assertEquals("/test 1 2 optional", results.get());

		// "2" path
		assertStoresResult(player, "test 1", executedPath, "1");
		assertEquals("/test 1", results.get());
		assertStoresResult(player, "test 1 optional", executedPath, "1");
		assertEquals("/test 1 optional", results.get());

		// "2 1" path
		assertStoresResult(player, "test 2 1", executedPath, "2 1");
		assertEquals("/test 2 1", results.get());
		assertStoresResult(player, "test 2 1 optional", executedPath, "2 1");
		assertEquals("/test 2 1 optional", results.get());

		// "2 2" path
		assertStoresResult(player, "test 2 2", executedPath, "2 2");
		assertEquals("/test 2 2", results.get());
		assertStoresResult(player, "test 2 2 optional", executedPath, "2 2");
		assertEquals("/test 2 2 optional", results.get());


		assertNoMoreResults(executedPath);
		assertNoMoreResults(results);
	}
}
