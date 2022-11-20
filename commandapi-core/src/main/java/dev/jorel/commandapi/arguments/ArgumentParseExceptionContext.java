package dev.jorel.commandapi.arguments;

import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import org.bukkit.command.CommandSender;

/**
 * A record containing information on why an Argument failed to parse.
 *
 * @param exception The CommandSyntaxException that was thrown when the Argument failed to parse
 * @param sender The CommandSender who sent the command that caused the exception
 * @param input The raw object returned by the initial Brigadier parse for the Argument
 */
public record ArgumentParseExceptionContext(WrapperCommandSyntaxException exception, CommandSender sender, Object input) {
}
