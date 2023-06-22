package dev.jorel.commandapi.test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.bukkit.command.CommandSender;

import com.mojang.brigadier.exceptions.CommandSyntaxException;

public class Assertions {

	/**
	 * Checks if the command has invalid syntax (would show up as red in the client)
	 * @param server the server instance
	 * @param sender the command sender
	 * @param command the command that the command sender would run (without the leading {@code /})
	 */
	public static void assertInvalidSyntax(CommandAPIServerMock server, CommandSender sender, String command) {
		assertThrows(CommandSyntaxException.class, () -> assertTrue(server.dispatchThrowableCommand(sender,command)));
	}

}
