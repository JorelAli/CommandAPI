package dev.jorel.commandapi;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import org.bukkit.command.CommandSender;
import org.opentest4j.AssertionFailedError;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CommandAPITestUtilities {
	public static MockCommandAPIBukkit getCommandAPIPlatform() {
		return MockCommandAPIBukkit.getInstance();
	}

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
}
