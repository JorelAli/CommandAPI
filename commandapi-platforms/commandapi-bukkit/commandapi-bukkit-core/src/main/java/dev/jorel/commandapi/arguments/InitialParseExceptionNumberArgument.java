package dev.jorel.commandapi.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.jorel.commandapi.CommandAPIBukkit;
import dev.jorel.commandapi.wrappers.WrapperStringReader;

/**
 * An {@link InitialParseExceptionArgument} for Arguments that return a number.
 * @param <N> The type of number returned by this Argument.
 */
public interface InitialParseExceptionNumberArgument<N extends Number>
        extends InitialParseExceptionArgument<N, InitialParseExceptionNumberArgument.ExceptionInformation<N>, Argument<N>>,
        InitialParseExceptionTranslationKeyExtractor<InitialParseExceptionNumberArgument.ExceptionInformation.Exceptions> {
    /**
     * Information why a {@link IntegerArgument} failed to parse.
     *
     * @param type     The type of exception that happened.
     * @param rawInput The String that was being parsed.
     * @param input    The integer that was input. Defaults to 0 if the exception type was
     *                 {@link Exceptions#EXPECTED_NUMBER} or {@link Exceptions#INVALID_NUMBER} since a number was not read.
     * @param minimum  The minimum value set for this Argument.
     * @param maximum  The maximum value set for this Argument.
     */
    record ExceptionInformation<N extends Number>(
            /**
             * @param type The type of exception that happened.
             */
            ExceptionInformation.Exceptions type,
            /**
             * @param rawInput The String that was being parsed.
             */
            String rawInput,
            /**
             * @param input The number that was input. Defaults to 0 if the exception type was
             * {@link #EXPECTED_NUMBER} or {@link #INVALID_NUMBER} since a number was not read.
             */
            N input,
            /**
             * @param minimum  The minimum value set for this Argument.
             */
            N minimum,
            /**
             * @param maximum  The maximum value set for this Argument.
             */
            N maximum
    ) {
        /**
         * Types of exceptions that might be thrown during the initial Brigadier parse of an int
         */
        public enum Exceptions {
            /**
             * Thrown when there are no characters left in the command to be read for this int.
             */
            EXPECTED_NUMBER,
            /**
             * Thrown when the String read from the command cannot be parsed into an int by
             * {@link Integer#parseInt(String)}. The {@link WrapperStringReader} will be placed at the end of the
             * invalid input.
             */
            INVALID_NUMBER,
            /**
             * Thrown when the given int is below the minimum value set for this argument. The
             * {@link WrapperStringReader} will be placed at the end of the invalid int.
             */
            NUMBER_TOO_LOW,
            /**
             * Thrown when the given int is above the maximum value set for this argument. The
             * {@link WrapperStringReader} will be placed at the end of the invalid int.
             */
            NUMBER_TOO_HIGH
        }
    }

    private static String getRawNumberInput(StringReader reader) {
        // Copied from the first half to StringReader#readInt
        int start = reader.getCursor();
        while (reader.canRead() && StringReader.isAllowedNumber(reader.peek())) {
            reader.skip();
        }
        return reader.getString().substring(start, reader.getCursor());
    }

    @Override
    default ExceptionInformation<N> parseInitialParseException(CommandSyntaxException exception, StringReader reader) {
        N min = getMinimum();
        N max = getMaximum();

        return switch (getExceptionType(exception)) {
            case EXPECTED_NUMBER -> new ExceptionInformation<>(
                    ExceptionInformation.Exceptions.EXPECTED_NUMBER,
                    "", getZero(), min, max
            );
            case INVALID_NUMBER -> new ExceptionInformation<>(
                    ExceptionInformation.Exceptions.INVALID_NUMBER,
                    getRawNumberInput(reader), getZero(), min, max
            );
            case NUMBER_TOO_LOW -> {
                String rawInput = getRawNumberInput(reader);
                yield new ExceptionInformation<>(
                        ExceptionInformation.Exceptions.NUMBER_TOO_LOW,
                        rawInput, parseNumber(rawInput), min, max
                );
            }
            case NUMBER_TOO_HIGH -> {
                String rawInput = getRawNumberInput(reader);
                yield new ExceptionInformation<>(
                        ExceptionInformation.Exceptions.NUMBER_TOO_HIGH,
                        rawInput, parseNumber(rawInput), min, max
                );
            }
        };
    }

    /**
     * @return The minimum value assigned for this Argument
     */
    N getMinimum();

    /**
     * @return The maximum value assigned for this Argument
     */
    N getMaximum();

    /**
     * @return The constant 0 in the class handled by this Argument
     */
    N getZero();

    /**
     * @param s The String to parse
     * @return The number represented by the given string as the class handled by this Argument
     */
    N parseNumber(String s);
}
