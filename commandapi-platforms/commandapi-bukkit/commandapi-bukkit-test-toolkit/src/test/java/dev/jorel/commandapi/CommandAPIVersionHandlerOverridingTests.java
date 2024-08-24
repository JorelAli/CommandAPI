package dev.jorel.commandapi;

import be.seeseemelk.mockbukkit.MockBukkit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for using {@link CommandAPIVersionHandler#usePlatformImplementation(CommandAPIPlatform)}
 */
class CommandAPIVersionHandlerOverridingTests {
	// Setup
	@BeforeEach
	public void setUp() {
		MockBukkit.mock();
	}

	@AfterEach
	public void tearDown() {
		MockBukkit.unmock();
	}

	private static class CustomMockCommandAPIBukkit extends MockCommandAPIBukkit {

	}

	// Tests
	@Test
	void testDefaultPlatform() {
		MockCommandAPIPlugin.load();

		assertEquals(MockCommandAPIBukkit.class, CommandAPITestUtilities.getCommandAPIPlatform().getClass());
	}

	@Test
	void testChangingPlatform() {
		CommandAPIVersionHandler.usePlatformImplementation(new CustomMockCommandAPIBukkit());
		MockCommandAPIPlugin.load();

		assertEquals(CustomMockCommandAPIBukkit.class, CommandAPITestUtilities.getCommandAPIPlatform().getClass());
	}

	@Test
	void testPlatformDoesNotPersist() {
		CommandAPIVersionHandler.usePlatformImplementation(new CustomMockCommandAPIBukkit());
		MockCommandAPIPlugin.load();

		assertEquals(CustomMockCommandAPIBukkit.class, CommandAPITestUtilities.getCommandAPIPlatform().getClass());

		MockBukkit.unmock();
		MockBukkit.mock();

		MockCommandAPIPlugin.load();
		assertEquals(MockCommandAPIBukkit.class, CommandAPITestUtilities.getCommandAPIPlatform().getClass());
	}
}
