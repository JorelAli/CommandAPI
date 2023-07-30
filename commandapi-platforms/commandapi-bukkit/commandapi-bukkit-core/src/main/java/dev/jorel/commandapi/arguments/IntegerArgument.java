/*******************************************************************************
 * Copyright 2018, 2020 Jorel Ali (Skepter) - MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *******************************************************************************/
package dev.jorel.commandapi.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.jorel.commandapi.CommandAPIBukkit;
import dev.jorel.commandapi.exceptions.InvalidRangeException;
import dev.jorel.commandapi.executors.CommandArguments;
import dev.jorel.commandapi.wrappers.WrapperStringReader;

/**
 * An argument that represents primitive Java ints
 * 
 * @since 1.1
 */
public class IntegerArgument extends SafeOverrideableArgument<Integer, Integer>
	implements InitialParseExceptionArgument<Integer, IntegerArgumentType,
		IntegerArgument.InitialParseExceptionInformation, Argument<Integer>> {
	/**
	 * An integer argument
	 *
	 * @param nodeName the name of the node for this argument
	 */
	public IntegerArgument(String nodeName) {
		super(nodeName, IntegerArgumentType.integer(), String::valueOf);
	}

	/**
	 * An integer argument with a minimum value
	 *
	 * @param nodeName the name of the node for this argument
	 * @param min      The minimum value this argument can take (inclusive)
	 */
	public IntegerArgument(String nodeName, int min) {
		super(nodeName, IntegerArgumentType.integer(min), String::valueOf);
	}

	/**
	 * An integer argument with a minimum and maximum value
	 *
	 * @param nodeName the name of the node for this argument
	 * @param min      The minimum value this argument can take (inclusive)
	 * @param max      The maximum value this argument can take (inclusive)
	 */
	public IntegerArgument(String nodeName, int min, int max) {
		super(nodeName, IntegerArgumentType.integer(min, max), String::valueOf);
		if (max < min) {
			throw new InvalidRangeException();
		}
	}

	@Override
	public Class<Integer> getPrimitiveType() {
		return int.class;
	}

	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.PRIMITIVE_INTEGER;
	}

	/**
	 * Information why a {@link IntegerArgument} failed to parse.
	 *
	 * @param type     The type of exception that happened.
	 * @param rawInput The String that was being parsed.
	 * @param input    The integer that was input. Defaults to 0 if the exception type was
	 *                 {@link Exceptions#EXPECTED_INTEGER} or {@link Exceptions#INVALID_INTEGER} since an int was not read.
	 * @param minimum  The minimum value set for this Argument.
	 * @param maximum  The maximum value set for this Argument.
	 */
	public record InitialParseExceptionInformation(
			/**
			 * @param type The type of exception that happened.
			 */
			Exceptions type,
			/**
			 * @param rawInput The String that was being parsed.
			 */
			String rawInput,
			/**
			 * @param input The integer that was input. Defaults to 0 if the exception type was
			 * {@link Exceptions#EXPECTED_INTEGER} or {@link Exceptions#INVALID_INTEGER} since an int was not read.
			 */
			int input,
			/**
			 * @param minimum  The minimum value set for this Argument.
			 */
			int minimum,
			/**
			 * @param maximum  The maximum value set for this Argument.
			 */
			int maximum
	) {
		/**
		 * Types of exceptions that might be thrown during the initial Brigadier parse of an int
		 */
		public enum Exceptions {
			/**
			 * Thrown when there are no characters left in the command to be read for this int.
			 */
			EXPECTED_INTEGER,
			/**
			 * Thrown when the String read from the command cannot be parsed into an int by
			 * {@link Integer#parseInt(String)}. The {@link WrapperStringReader} will be placed at the end of the
			 * invalid input.
			 */
			INVALID_INTEGER,
			/**
			 * Thrown when the given int is below the minimum value set for this argument. The
			 * {@link WrapperStringReader} will be placed at the end of the invalid int.
			 */
			INTEGER_TOO_LOW,
			/**
			 * Thrown when the given int is above the maximum value set for this argument. The
			 * {@link WrapperStringReader} will be placed at the end of the invalid int.
			 */
			INTEGER_TOO_HIGH
		}
	}

	private static String getRawIntInput(StringReader reader) {
		// Copied from the first half to StringReader#readInt
		int start = reader.getCursor();
		while (reader.canRead() && StringReader.isAllowedNumber(reader.peek())) {
			reader.skip();
		}
		return reader.getString().substring(start, reader.getCursor());
	}

	@Override
	public InitialParseExceptionInformation parseInitialParseException(
			CommandSyntaxException exception, StringReader reader, IntegerArgumentType baseType
	) {
		String key = CommandAPIBukkit.get().extractTranslationKey(exception);
		if (key == null) {
			throw new IllegalStateException("Unexpected null translation key for IntegerArgument initial parse", exception);
		}
		int min = baseType.getMinimum();
		int max = baseType.getMaximum();
		return switch (key) {
			case "parsing.int.expected" -> new InitialParseExceptionInformation(
					InitialParseExceptionInformation.Exceptions.EXPECTED_INTEGER,
					"", 0, min, max
			);
			case "parsing.int.invalid" -> new InitialParseExceptionInformation(
					InitialParseExceptionInformation.Exceptions.INVALID_INTEGER,
					getRawIntInput(reader), 0, min, max
			);
			case "argument.integer.low" -> {
				String rawInput = getRawIntInput(reader);
				yield new InitialParseExceptionInformation(
						InitialParseExceptionInformation.Exceptions.INTEGER_TOO_LOW,
						rawInput, Integer.parseInt(rawInput), min, max
				);
			}
			case "argument.integer.big" -> {
				String rawInput = getRawIntInput(reader);
				yield new InitialParseExceptionInformation(
						InitialParseExceptionInformation.Exceptions.INTEGER_TOO_HIGH,
						rawInput, Integer.parseInt(rawInput), min, max
				);
			}
			default -> throw new IllegalStateException("Unexpected translation key for IntegerArgument initial parse: " + key, exception);
		};
	}

	@Override
	public <Source> Integer parseArgument(CommandContext<Source> cmdCtx, String key, CommandArguments previousArgs) throws CommandSyntaxException {
		return cmdCtx.getArgument(key, getPrimitiveType());
	}
}
