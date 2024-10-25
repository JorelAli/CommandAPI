package dev.jorel.commandapi.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import dev.jorel.commandapi.MockCommandSource;
import dev.jorel.commandapi.arguments.parser.Parser;
import dev.jorel.commandapi.arguments.parser.ParserArgument;
import dev.jorel.commandapi.arguments.parser.ParserLiteral;
import dev.jorel.commandapi.arguments.parser.Result;
import dev.jorel.commandapi.wrappers.IntegerRange;

import java.util.function.Function;
import java.util.function.Predicate;

public class IntegerRangeArgumentType implements ArgumentType<IntegerRange> {
	// No internal state is necessary
	public static final IntegerRangeArgumentType INSTANCE = new IntegerRangeArgumentType();

	private IntegerRangeArgumentType() {

	}

	// ArgumentType implementation
	public static final SimpleCommandExceptionType EMPTY_INPUT = new SimpleCommandExceptionType(
		ArgumentUtilities.translatedMessage("argument.range.empty")
	);
	public static final SimpleCommandExceptionType RANGE_SWAPPED = new SimpleCommandExceptionType(
		ArgumentUtilities.translatedMessage("argument.range.swapped")
	);

	private static final Predicate<CommandSyntaxException> throwInvalidIntExceptions = exception ->
		exception.getType().equals(CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerInvalidInt());

	private static final ParserLiteral rangeIndicator = ArgumentUtilities.literal("..");

	private static final ParserArgument<Integer> readHigh = StringReader::readInt;

	private static final ParserArgument<Integer> readLow = reader -> {
		// Acts like `StringReader#readInt`, but avoids reading `..` indicator for range as part of a number
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

	public static final Parser<IntegerRange> parser = reader -> {
		if (!reader.canRead()) {
			return Result.withException(EMPTY_INPUT.createWithContext(reader));
		}

		Function<CommandSyntaxException, Result<IntegerRange>> handleNumberReadFailure = Result.wrapFunctionResult(exception -> {
			if (throwInvalidIntExceptions.test(exception)) {
				// Tried to input a number, but it was not a valid int
				throw exception;
			}

			// Nothing looking like a number was found, empty input
			throw EMPTY_INPUT.createWithContext(reader);
		});

		int start = reader.getCursor();
		return rangeIndicator.getResult(reader).continueWith(
			// Input ..
			// Try to read ..high
			success -> readHigh.getResult(reader).continueWith(
				// Successfully input ..high
				Result.wrapFunctionResult(IntegerRange::integerRangeLessThanOrEq),
				// Either input a high that was not an int, or just an empty .. input
				handleNumberReadFailure
			),
			// No range indicator yet
			// Try to read low
			failure -> readLow.getResult(reader).continueWith(
				// Successfully read low
				// Try to read low..
				low -> rangeIndicator.getResult(reader).continueWith(
					// Successfully read low..
					// Try to read low..high
					success -> readHigh.getResult(reader).continueWith(
						// Successfully read low..high
						Result.wrapFunctionResult(high -> {
							if (low > high) {
								throw RANGE_SWAPPED.createWithContext(reader);
							}
							return new IntegerRange(low, high);
						}),
						// Either input a high that was not an int, or just low..
						Result.wrapFunctionResult(exception -> {
							if (throwInvalidIntExceptions.test(exception)) {
								// Tried to input low..high, but high was not an int
								throw exception;
							}

							// Input low..
							return IntegerRange.integerRangeGreaterThanOrEq(low);
						})
					),
					// Didn't find the range indicator
					// Input is just low
					Result.wrapFunctionResult(failure2 -> new IntegerRange(low, low))
				),
				// Either input a low that was not an int, or just an empty input
				handleNumberReadFailure
			)
		).continueWith(
			// If we return an IntegerRange, keep that
			Result.wrapFunctionResult(success -> success),
			// For some reason, Minecraft explicitly maps all exceptions to underline the start of the reader input
			// Even something like `..1.0`, which by my intuition should underline `1.0` to show it is not an integer
			Result.wrapFunctionResult(exception -> {
				throw new CommandSyntaxException(exception.getType(), exception.getRawMessage(), exception.getInput(), start);
			})
		);
	};

	@Override
	public IntegerRange parse(StringReader reader) throws CommandSyntaxException {
		return parser.parse(reader);
	}

	public static IntegerRange getRange(CommandContext<MockCommandSource> cmdCtx, String key) {
		return cmdCtx.getArgument(key, IntegerRange.class);
	}
}
