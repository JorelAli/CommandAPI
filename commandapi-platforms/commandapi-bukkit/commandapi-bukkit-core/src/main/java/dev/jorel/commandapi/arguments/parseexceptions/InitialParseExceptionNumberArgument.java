package dev.jorel.commandapi.arguments.parseexceptions;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.wrappers.WrapperStringReader;

/**
 * An {@link InitialParseExceptionArgument} for Arguments that return a number.
 * @param <N> The type of number returned by this Argument.
 */
public interface InitialParseExceptionNumberArgument<N extends Number>
        extends InitialParseExceptionArgument<N, InitialParseExceptionNumberArgument.ExceptionInformation<N>, Argument<N>>,
        InitialParseExceptionTranslationKeyExtractor<InitialParseExceptionNumberArgument.ExceptionInformation.Exceptions> {
    /**
     * Information why an Argument that returns a number failed to parse.
     *
     * @param type     The type of exception that happened.
     * @param rawInput The String that was being parsed.
     * @param input    The number that was input. Defaults to 0 if the exception type was
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
         * Types of exceptions that might be thrown during the initial Brigadier parse of a number
         */
        public enum Exceptions {
            /**
             * Thrown when the first character read for this Argument is not a digit ({@code 0 to 9}), decimal point
             * ({@code '.'}), or negative sign ({@code '-'}), which are the only characters allowed in numbers. The
             * {@link WrapperStringReader} will be placed just before that first character.
             */
            EXPECTED_NUMBER,
            /**
             * Thrown when the String read from the command cannot be parsed into a number by
             * {@link InitialParseExceptionNumberArgument#parseNumber(String)}. The {@link WrapperStringReader} will be
             * placed at the end of the invalid input String.
             */
            INVALID_NUMBER,
            /**
             * Thrown when the given number is below the minimum value set for this argument. The
             * {@link WrapperStringReader} will be placed at the end of the invalid number.
             */
            NUMBER_TOO_LOW,
            /**
             * Thrown when the given number is above the maximum value set for this argument. The
             * {@link WrapperStringReader} will be placed at the end of the invalid number.
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
