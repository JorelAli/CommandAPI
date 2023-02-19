package dev.jorel.commandapi.test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.logging.Logger;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;

/**
 * Run CommandAPI.onEnable()'s scheduler
 */
class OnEnableTests extends TestBase {

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
	void testOnEnableExecution() {
		disablePaperImplementations();
		assertDoesNotThrow(() -> server.getScheduler().performOneTick());
	}

	@Test
	void testOnEnableRegisterCommand() {
		disablePaperImplementations();
		assertDoesNotThrow(() -> server.getScheduler().performOneTick());
		
		assertFalse(CommandAPI.canRegister());

		// Should log a warning
		new CommandAPICommand("mycommand")
			.executes((sender, args) -> {
			})
			.register();
	}

}
