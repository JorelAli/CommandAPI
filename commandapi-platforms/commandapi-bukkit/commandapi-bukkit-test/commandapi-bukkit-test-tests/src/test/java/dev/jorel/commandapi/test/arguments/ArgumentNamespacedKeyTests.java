package dev.jorel.commandapi.test.arguments;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.bukkit.NamespacedKey;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.entity.PlayerMock;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.NamespacedKeyArgument;
import dev.jorel.commandapi.test.Mut;
import dev.jorel.commandapi.test.TestBase;

/**
 * Tests for the {@link NamespacedKeyArgument}
 */
class ArgumentNamespacedKeyTests extends TestBase {

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

	@SuppressWarnings("deprecation")
	@Test
	void executionTestWithNamespacedKeyArgument() {
		Mut<NamespacedKey> results = Mut.of();

		new CommandAPICommand("test")
			.withArguments(new NamespacedKeyArgument("namespace"))
			.executesPlayer((player, args) -> {
				results.set((NamespacedKey) args.get("namespace"));
			})
			.register();

		PlayerMock player = server.addPlayer();

		// /test key
		server.dispatchCommand(player, "test key");
		assertEquals(new NamespacedKey("minecraft", "key"), results.get());
		
		// /test mynamespace:key
		server.dispatchCommand(player, "test mynamespace:key");
		assertEquals(new NamespacedKey("mynamespace", "key"), results.get());

		assertNoMoreResults(results);
	}

	/********************
	 * Suggestion tests *
	 ********************/

	@Test
	void suggestionTestWithNamespacedKeyArgument() {
		new CommandAPICommand("test")
			.withArguments(new NamespacedKeyArgument("namespace"))
			.executesPlayer(P_EXEC)
			.register();

		PlayerMock player = server.addPlayer();

		// /test
		// Should suggest nothing
		assertEquals(List.of(), server.getSuggestions(player, "test "));
	}

}
