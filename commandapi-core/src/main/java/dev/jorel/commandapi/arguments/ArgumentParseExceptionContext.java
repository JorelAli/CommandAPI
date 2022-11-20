package dev.jorel.commandapi.arguments;

import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;

/**
 * A record containing information on why an Argument failed to parse.
 *
 * @param exception The CommandSyntaxException that was thrown when the Argument failed to parse
 */
public record ArgumentParseExceptionContext(WrapperCommandSyntaxException exception) {
}
