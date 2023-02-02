package dev.jorel.commandapi.arguments;

import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;

/**
 * A record containing information on why an Argument failed to parse.
 *
 * @param exception The CommandSyntaxException that was thrown when the Argument failed to parse
 * @param sender The CommandSender who sent the command that caused the exception
 * @param input The raw object returned by the initial Brigadier parse for the Argument
 * @param previousArguments The previously parsed arguments that came before this argument
 */
public record ArgumentParseExceptionContext<CommandSender>(
	/**
	 * @param exception The CommandSyntaxException that was thrown when the Argument failed to parse
	 */
	WrapperCommandSyntaxException exception,
	/**
	 * @param sender The CommandSender who sent the command that caused the exception
	 */
	CommandSender sender,
	/**
	 * @param input The raw object returned by the initial Brigadier parse for the Argument
	 */
	Object input,
	/**
	 * @param previousArguments The previously parsed arguments that came before this argument
	 */
	Object[] previousArguments) {
}
