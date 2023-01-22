package dev.jorel.commandapi.test.arguments;

import be.seeseemelk.mockbukkit.entity.PlayerMock;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;
import dev.jorel.commandapi.exceptions.GreedyArgumentException;
import dev.jorel.commandapi.test.Mut;
import dev.jorel.commandapi.test.TestBase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for the {@link GreedyStringArgument}
 */
public class ArgumentGreedyStringTests extends TestBase {

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
	public void executionTestWithGreedyStringArgument() {
		Mut<String> results = Mut.of();

		new CommandAPICommand("test")
			.withArguments(new GreedyStringArgument("value"))
			.executesPlayer((player, args) -> {
				results.set((String) args.get(0));
			})
			.register();

		PlayerMock player = server.addPlayer();

		// /test Hello World
		server.dispatchCommand(player, "test Hello World");
		assertEquals("Hello World", results.get());

		// /test Hello
		server.dispatchCommand(player, "test Hello");
		assertEquals("Hello", results.get());

		// /test ""*`?#+*!"§
		server.dispatchCommand(player, "test \"\"*`?#+*!\"§");
		assertEquals("\"\"*`?#+*!\"§", results.get());

		// /test こんにちは
		server.dispatchCommand(player, "test こんにちは");
		assertEquals("こんにちは", results.get());

		assertNoMoreResults(results);

	}

	@Test
	public void exceptionTestWithGreedyStringArgument() {

		assertThrows(GreedyArgumentException.class, () -> new CommandAPICommand("test")
			.withArguments(new GreedyStringArgument("greedy"))
			.withArguments(new PlayerArgument("player"))
			.executesPlayer(P_EXEC)
			.register());

	}

}
