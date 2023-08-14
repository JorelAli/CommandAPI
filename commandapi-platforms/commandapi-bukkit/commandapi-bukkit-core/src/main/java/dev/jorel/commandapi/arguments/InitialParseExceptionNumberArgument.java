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
        extends InitialParseExceptionArgument<N, InitialParseExceptionNumberArgument.ExceptionInformation<N>, Argument<N>> {
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
        String key = CommandAPIBukkit.get().extractTranslationKey(exception);
        if (key == null) {
            throw new IllegalStateException("Unexpected null translation key for IntegerArgument initial parse", exception);
        }

        N min = getMinimum();
        N max = getMaximum();

        // We expect translation keys in the form (parsing/argument).(name of this number).(expected/invalid/low/big)
        String[] keyParts = key.split("\\.");
        if (keyParts.length == 3) {
            String type = keyParts[0];
            if(type.equals("parsing")) {
                if(keyParts[1].equals(getParsingName())) {
                    String subType = keyParts[2];
                    if(subType.equals("expected")) {
                        return new ExceptionInformation<>(
                                ExceptionInformation.Exceptions.EXPECTED_NUMBER,
                                "", getZero(), min, max
                        );
                    } else if(subType.equals("invalid")) {
                        return new ExceptionInformation<>(
                                ExceptionInformation.Exceptions.INVALID_NUMBER,
                                getRawNumberInput(reader), getZero(), min, max
                        );
                    }
                }
            } else if(type.equals("argument")) {
                if(keyParts[1].equals(getSizingName())) {
                    String subType = keyParts[2];
                    if(subType.equals("low")) {
                        String rawInput = getRawNumberInput(reader);
                        return new ExceptionInformation<>(
                                ExceptionInformation.Exceptions.NUMBER_TOO_LOW,
                                rawInput, parseNumber(rawInput), min, max
                        );
                    } else if(subType.equals("big")) {
                        String rawInput = getRawNumberInput(reader);
                        return new ExceptionInformation<>(
                                ExceptionInformation.Exceptions.NUMBER_TOO_HIGH,
                                rawInput, parseNumber(rawInput), min, max
                        );
                    }
                }
            }
        }

        throw new IllegalStateException("Unexpected translation key for " + getClass().getSimpleName() + " initial parse: " + key, exception);
    }

    N getMinimum();

    N getMaximum();

    String getParsingName();

    String getSizingName();

    N getZero();

    N parseNumber(String s);
}
