package dev.jorel.commandapi.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

/**
 * Extracts information about an exception that was thrown when an {@link ArgumentType} fails to parse.
 * See {@link InitialParseExceptionArgument#parseInitialParseException(CommandSyntaxException, StringReader, ArgumentType)}.
 *
 * @param <T> The object returned when the wrapped {@link ArgumentType} is parsed.
 * @param <BaseType> The class of the {@link ArgumentType} this object is wrapping.
 * @param <ExceptionInformation> The class that holds information about the exception.
 */
@FunctionalInterface
public interface InitialParseExceptionParser<T, BaseType extends ArgumentType<T>, ExceptionInformation> {
    /**
     * Extracts information about an exception that was thrown when an {@link ArgumentType} fails to parse.
     * See {@link InitialParseExceptionArgument#parseInitialParseException(CommandSyntaxException, StringReader, ArgumentType)}.
     *
     * @param exception The {@link CommandSyntaxException} that was thrown.
     * @param reader The {@link StringReader} that was reading this Argument.
     * @param baseType The {@link ArgumentType} object was parsing.
     * @return An {@link ExceptionInformation} object that holds information about the given exception.
     */
    ExceptionInformation parse(CommandSyntaxException exception, StringReader reader, BaseType baseType);
}
