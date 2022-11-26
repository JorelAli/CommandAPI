package dev.jorel.commandapi.arguments;

import com.mojang.brigadier.arguments.ArgumentType;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;

/**
 * A record containing information on why a Brigadier {@link ArgumentType} failed to parse its raw input.
 *
 * @param exception The CommandSyntaxException that was thrown when the {@link ArgumentType} failed to parse
 */
public record InitialParseExceptionContext(WrapperCommandSyntaxException exception) {
}
