package dev.jorel.commandapi.test;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.bukkit.Bukkit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import dev.jorel.commandapi.exceptions.GreedyArgumentException;
import dev.jorel.commandapi.exceptions.InvalidCommandNameException;
import dev.jorel.commandapi.exceptions.MissingCommandExecutorException;

/**
 * Run CommandAPI.onEnable()'s scheduler
 */
@SuppressWarnings("unused")
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
		assertDoesNotThrow(() -> server.getScheduler().performOneTick());
	}

}
