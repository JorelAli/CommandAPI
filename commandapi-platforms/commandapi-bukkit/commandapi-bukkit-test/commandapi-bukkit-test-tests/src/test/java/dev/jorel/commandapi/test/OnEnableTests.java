package dev.jorel.commandapi.test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

}
