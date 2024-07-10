package io.github.jorelali;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

// Import helper methods from CommandAPITestUtilities
import static dev.jorel.commandapi.CommandAPITestUtilities.assertCommandSucceeds;

class PingPongCommandTests {
	// Setup
	private ServerMock server;

	@BeforeEach
	public void setUp() {
		// Set up MockBukkit server
		server = MockBukkit.mock();

		// Load our plugin
		MockBukkit.load(Main.class);
	}

	@AfterEach
	public void tearDown() {
		MockBukkit.unmock();
	}

	// Tests
	@Test
	void runCommand() {
		PlayerMock player = server.addPlayer();

		assertCommandSucceeds(player, "ping");

		player.assertSaid("pong!");
		player.assertNoMoreSaid();
	}
}
