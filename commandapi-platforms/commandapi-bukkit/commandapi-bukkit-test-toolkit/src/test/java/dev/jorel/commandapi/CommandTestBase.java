package dev.jorel.commandapi;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import dev.jorel.commandapi.executors.CommandExecutionInfo;
import org.junit.jupiter.api.function.Executable;
import org.opentest4j.AssertionFailedError;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class CommandTestBase extends CommandAPITestUtilities {
	// Useful objects
	public static CommandExecutionInfo DEFAULT_EXECUTOR = info -> {};

	// Setup
	protected ServerMock server;
	protected MockCommandAPIPlugin plugin;

	public void setUp() {
		server = MockBukkit.mock();
		plugin = MockBukkit.load(MockCommandAPIPlugin.class);
	}

	public void tearDown() {
		MockBukkit.unmock();
	}

	// Additional test utilities
	@CanIgnoreReturnValue
	public <T extends Throwable> T assertThrowsWithMessage(Class<T> exceptionClass, Executable executable, String expectedMessage) {
		T exception = assertThrows(exceptionClass, executable);
		assertEquals(expectedMessage, exception.getMessage());
		return exception;
	}

	@CanIgnoreReturnValue
	public AssertionFailedError assertAssertionFails(Executable executable, String expectedMessage) {
		return assertThrowsWithMessage(AssertionFailedError.class, executable, expectedMessage);
	}
}
