package dev.jorel.commandapi.arguments.parseexceptions;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.jorel.commandapi.arguments.AbstractArgument;
import dev.jorel.commandapi.wrappers.WrapperStringReader;

import java.util.Map;

/**
 * An {@link InitialParseExceptionArgument} for Arguments that input a text-based string which may be quoted or unquoted.
 * @param <Impl> The class extending this class, used as the return type in chained calls.
 */
public interface InitialParseExceptionTextArgument<Impl extends AbstractArgument<?, Impl, ?, ?>>
        extends InitialParseExceptionArgument<String, InitialParseExceptionTextArgument.ExceptionTypes, Impl>,
        InitialParseExceptionTranslationKeyExtractor<InitialParseExceptionTextArgument.ExceptionTypes> {
    /**
     * Reasons why a text-based argument failed to parse
     */
    enum ExceptionTypes {
        /**
         * Thrown when reading a quoted String and the escape character {@code \} (backslash) is followed by anything other
         * than another backslash or the same character that started the quoted string. The {@link WrapperStringReader}
         * will be placed so that the next character is the one that could not be escaped.
         */
        INVALID_ESCAPE,
        /**
         * Thrown when reading a quoted String and the input ends before the String is closed. The
         * {@link WrapperStringReader} will be placed at the end of input.
         */
        EXPECTED_QUOTE_END
    }

    @Override
    default ExceptionTypes parseInitialParseException(CommandSyntaxException exception, StringReader reader) {
        return getExceptionType(exception);
    }

    Map<String, ExceptionTypes> keyToExceptionTypeMap = Map.of(
            "parsing.quote.escape", ExceptionTypes.INVALID_ESCAPE,
            "parsing.quote.expected.end", ExceptionTypes.EXPECTED_QUOTE_END
    );

    @Override
    default Map<String, ExceptionTypes> keyToExceptionTypeMap() {
        return keyToExceptionTypeMap;
    }
}
