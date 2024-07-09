package dev.jorel.commandapi;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.jorel.commandapi.commandsenders.AbstractCommandSender;
import dev.jorel.commandapi.executors.ExecutionInfo;
import dev.jorel.commandapi.spying.ExecutionQueue;
import org.bukkit.command.CommandSender;
import org.opentest4j.AssertionFailedError;

import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class CommandAPITestUtilities {
	public static MockCommandAPIBukkit getCommandAPIPlatform() {
		return MockCommandAPIBukkit.getInstance();
	}

	// Running commands
	public static void dispatchCommand(CommandSender sender, String command) throws CommandSyntaxException {
		getCommandAPIPlatform().getBrigadierDispatcher().execute(command, new MockCommandSource(sender));
	}

	public static void assertCommandSucceeds(CommandSender sender, String command) {
		assertDoesNotThrow(
			() -> dispatchCommand(sender, command),
			"Expected command dispatch to succeed"
		);
	}

	@CanIgnoreReturnValue
	public static CommandSyntaxException assertCommandFails(CommandSender sender, String command, String expectedMessage) {
		CommandSyntaxException exception = assertThrows(
			CommandSyntaxException.class,
			() -> dispatchCommand(sender, command),
			"Expected command dispatch to fail"
		);

		String actualMessage = exception.getMessage();
		if (!Objects.equals(expectedMessage, actualMessage)) {
			throw new AssertionFailedError(
				"Expected command dispatch to fail with message <" + expectedMessage + ">, but got <" + actualMessage + ">"
			);
		}
		return exception;
	}

	// Verifying arguments
	public static ExecutionInfo<CommandSender, AbstractCommandSender<? extends CommandSender>> getExecutionInfoOfCommand(
		CommandSender sender, String command
	) {
		ExecutionQueue executions = getCommandAPIPlatform().getCommandAPIHandlerSpy().getExecutionQueue();
		executions.clear();

		assertCommandSucceeds(sender, command);

		ExecutionInfo<CommandSender, AbstractCommandSender<? extends CommandSender>> execution = executions.poll();
		assertNotNull(execution);
		executions.assertNoMoreCommandsWereRun();

		return execution;
	}

	public static void assertCommandRunsWithArguments(CommandSender sender, String command, Object... argumentsArray) {
		assertArrayEquals(argumentsArray, getExecutionInfoOfCommand(sender, command).args().args(),
			"Argument arrays are not equal"
		);
	}

	public static void assertCommandRunsWithArguments(CommandSender sender, String command, Map<String, Object> argumentsMap) {
		assertEquals(argumentsMap, getExecutionInfoOfCommand(sender, command).args().argsMap(),
			"Argument maps are not equal"
		);
	}
}
