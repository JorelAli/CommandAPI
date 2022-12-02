package dev.jorel.commandapi.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.wrappers.WrapperStringReader;

/**
 * A record containing information on why a Brigadier {@link ArgumentType} failed to parse its raw input.
 *
 * @param exception The {@link CommandSyntaxException} that was thrown when the ArgumentType failed to parse.
 * @param stringReader The {@link StringReader} used for reading in the command.
 *                     The cursor is at the beginning of the argument.
 */
public record InitialParseExceptionContext(WrapperCommandSyntaxException exception, WrapperStringReader stringReader) {
}
