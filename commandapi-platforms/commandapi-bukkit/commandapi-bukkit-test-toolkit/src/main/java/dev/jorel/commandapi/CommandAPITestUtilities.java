package dev.jorel.commandapi;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import org.bukkit.command.CommandSender;

import static org.junit.jupiter.api.Assertions.fail;

public class CommandAPITestUtilities {
	public static MockCommandAPIBukkit getCommandAPIPlatform() {
		return MockCommandAPIBukkit.getInstance();
	}

	public static void dispatchCommand(CommandSender sender, String command) {
		try {
			dispatchThrowableCommand(sender, command);
		} catch (CommandSyntaxException exception) {
			fail(
				"Expected command dispatch to succeed." +
					" If you expected this to fail, use `dispatchFailableCommand` instead.",
				exception
			);
		}
	}

	public static void dispatchThrowableCommand(CommandSender sender, String command) throws CommandSyntaxException {
		getCommandAPIPlatform().getBrigadierDispatcher().execute(command, new MockCommandSource(sender));
	}
}
