package dev.jorel.commandapi.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

/**
 * Extracts information about an exception that was thrown when an {@link ArgumentType} fails to parse.
 * See {@link InitialParseExceptionArgument#parseInitialParseException(CommandSyntaxException, StringReader)}.
 *
 * @param <T> The object returned when the wrapped {@link ArgumentType} is parsed.
 * @param <ExceptionInformation> The class that holds information about the exception.
 */
@FunctionalInterface
public interface InitialParseExceptionParser<T, ExceptionInformation> {
    /**
     * Extracts information about an exception that was thrown when an {@link ArgumentType} fails to parse.
     * See {@link InitialParseExceptionArgument#parseInitialParseException(CommandSyntaxException, StringReader)}.
     *
     * @param exception The {@link CommandSyntaxException} that was thrown.
     * @param reader The {@link StringReader} that was reading this Argument.
     * @return An {@link ExceptionInformation} object that holds information about the given exception.
     */
    ExceptionInformation parse(CommandSyntaxException exception, StringReader reader);
}
