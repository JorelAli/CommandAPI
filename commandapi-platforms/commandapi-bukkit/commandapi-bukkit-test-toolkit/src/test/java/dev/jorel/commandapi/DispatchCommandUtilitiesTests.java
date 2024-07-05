package dev.jorel.commandapi;

import be.seeseemelk.mockbukkit.entity.PlayerMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for the methods provided by {@link CommandAPITestUtilities} that handle dispatching commands.
 */
class DispatchCommandUtilitiesTests extends CommandTestBase {
	// Setup
	@BeforeEach
	public void setUp() {
		super.setUp();

		new CommandAPICommand("ping")
			.executes((sender, args) -> {
				sender.sendMessage("pong!");
			})
			.register();
	}

	@AfterEach
	public void tearDown() {
		super.tearDown();
	}

	// Tests
	@Test
	void testAssertCommandSucceeds() {
		PlayerMock player = server.addPlayer();

		// Successful use of `assertCommandSucceeds`
		assertCommandSucceeds(player, "ping");
		player.assertSaid("pong!");

		// Unsuccessful use of `assertCommandSucceeds`
		assertAssertionFails(
			() -> assertCommandSucceeds(player, "pong"),
			"Expected command dispatch to succeed " +
				"==> Unexpected exception thrown: " +
				"com.mojang.brigadier.exceptions.CommandSyntaxException: Unknown command at position 0: <--[HERE]"
		);
		player.assertNoMoreSaid();
	}

	@Test
	void testAssertCommandFails() {
		PlayerMock player = server.addPlayer();

		// Successful use of `assertCommandFails`
		assertCommandFails(
			player, "pong",
			"Unknown command at position 0: <--[HERE]"
		);

		// Unsuccessful uses of `assertCommandFails`
		assertAssertionFails(
			() -> assertCommandFails(player, "ping", "Wrong command"),
			// Command actually did not fail
			"Expected command dispatch to fail ==> " +
				"Expected com.mojang.brigadier.exceptions.CommandSyntaxException to be thrown, but nothing was thrown."
		);
		player.assertSaid("pong!");

		assertAssertionFails(
			() -> assertCommandFails(player, "pong", "Wrong command"),
			// Command failed, but not with the given expected message
			"Expected command dispatch to fail with message <Wrong command>, but got <Unknown command at position 0: <--[HERE]>"
		);

		player.assertNoMoreSaid();
	}
}
