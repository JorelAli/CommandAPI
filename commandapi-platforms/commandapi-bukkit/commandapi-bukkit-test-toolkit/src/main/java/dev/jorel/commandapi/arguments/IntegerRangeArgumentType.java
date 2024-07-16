package dev.jorel.commandapi.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import dev.jorel.commandapi.MockCommandSource;
import dev.jorel.commandapi.arguments.parser.Parser;
import dev.jorel.commandapi.wrappers.IntegerRange;

import java.util.function.Predicate;

public class IntegerRangeArgumentType implements ArgumentType<IntegerRange> {
	// No internal state is necessary
	public static final IntegerRangeArgumentType INSTANCE = new IntegerRangeArgumentType();

	private IntegerRangeArgumentType() {

	}

	// ArgumentType implementation
	//  I'd like to use the translation keys for these error messages,
	//  but MockBukkit doesn't seem to currently support that: https://github.com/MockBukkit/MockBukkit/issues/1040.
	//  For now, just grabbing the literal strings from https://gist.github.com/sppmacd/82af47c83b225d4ffd33bb0c27b0d932.
	public static final SimpleCommandExceptionType EMPTY_INPUT = new SimpleCommandExceptionType(
		() -> "Expected value or range of values"
	);
	public static final SimpleCommandExceptionType RANGE_SWAPPED = new SimpleCommandExceptionType(
		() -> "Min cannot be bigger than max"
	);
	private static final Parser.NoSuggestions<Integer> READ_INT_BEFORE_RANGE = reader -> {
		// Custom parser avoids reading `..` indicator for range as part of a number
		int start = reader.getCursor();

		while (reader.canRead()) {
			char c = reader.peek();
			if (!(c == '-' // Negative sign
				|| (c >= '0' && c <= '9') // Digit
				|| c == '.' && !(reader.canRead(2) && reader.peek(1) == '.') // Decimal point, but not `..`
			)) break;
			reader.skip();
		}

		String number = reader.getString().substring(start, reader.getCursor());
		if (number.isEmpty()) {
			reader.setCursor(start);
			throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerExpectedInt().createWithContext(reader);
		}
		try {
			return Integer.parseInt(number);
		} catch (NumberFormatException ignored) {
			reader.setCursor(start);
			throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerInvalidInt().createWithContext(reader, number);
		}
	};

	private static final Predicate<CommandSyntaxException> PASS_INVALID_INT_EXCEPTIONS = exception ->
		exception.getType().equals(CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerInvalidInt());

	private static final Parser<IntegerRange> PARSER = Parser
		.tryParse(Parser.assertCanRead(EMPTY_INPUT::createWithContext), builder -> builder
			.tryParse(Parser.literal(".."), builder1 -> builder1
				.tryParseNoSuggestions(StringReader::readInt, (high, builder2) -> builder2
					// Input ..high
					.concludeNoSuggestions(reader -> IntegerRange.integerRangeLessThanOrEq(high.get()))
				)
				// It looks like they tried to input ..high, but high was not a valid int
				.throwExceptionIfTrue(PASS_INVALID_INT_EXCEPTIONS)
				// Input just ..
				.concludeNoSuggestions(reader -> {
					// Move cursor to start of ..
					reader.setCursor(reader.getCursor() - 2);
					throw EMPTY_INPUT.createWithContext(reader);
				})
			).neverThrowException()
			.tryParseNoSuggestions(StringReader::getCursor, (start, builder1) -> builder1
				.tryParse(READ_INT_BEFORE_RANGE, (getLow, builder2) -> builder2
					.tryParse(Parser.literal(".."), builder3 -> builder3
						.tryParseNoSuggestions(StringReader::readInt, (getHigh, builder4) -> builder4
							// Input low..high
							.concludeNoSuggestions(reader -> {
								int low = getLow.get();
								int high = getHigh.get();
								if (low > high) {
									// Reset to start of input
									reader.setCursor(start.get());
									throw RANGE_SWAPPED.createWithContext(reader);
								}
								return new IntegerRange(low, high);
							})
						)
						// It looked like they tried to input low..high, but high was not a valid int
						.throwExceptionIfTrue(PASS_INVALID_INT_EXCEPTIONS)
						// Input low..
						.concludeNoSuggestions(reader -> IntegerRange.integerRangeGreaterThanOrEq(getLow.get()))
					).neverThrowException()
					// Input low
					.concludeNoSuggestions(reader -> {
						int exact = getLow.get();
						return new IntegerRange(exact, exact);
					})
				).alwaysMapException(exception -> {
					if (exception.getType().equals(CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerExpectedInt())) {
						// If we didn't find any int to input, this range is empty
						StringReader context = new StringReader(exception.getInput());
						context.setCursor(exception.getCursor());
						return EMPTY_INPUT.createWithContext(context);
					}
					// Otherwise throw original exception
					return exception;
				})
			).alwaysThrowException()
		).alwaysThrowException();

	@Override
	public IntegerRange parse(StringReader reader) throws CommandSyntaxException {
		return PARSER.parseValueOrThrow(reader);
	}

	public static IntegerRange getRange(CommandContext<MockCommandSource> cmdCtx, String key) {
		return cmdCtx.getArgument(key, IntegerRange.class);
	}
}
