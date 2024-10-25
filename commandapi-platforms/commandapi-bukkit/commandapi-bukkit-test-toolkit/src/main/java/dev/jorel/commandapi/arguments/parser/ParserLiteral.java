package dev.jorel.commandapi.arguments.parser;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

/**
 * A {@link FunctionalInterface} with abstract method {@link #read(StringReader)}.
 * This extends {@link Parser}{@code <}{@link Result.Void}{@code >}, so it only
 * reads from the input {@link StringReader} and doesn't return any specific object
 * when successful.
 */
@FunctionalInterface
public interface ParserLiteral extends Parser<Result.Void> {
	// Implement parsing logic

	/**
	 * Reads from the given input and either returns successfully or throws
	 * an exception explaining why the input could not be interpreted.
	 *
	 * @param reader The {@link StringReader} that holds the input to parse.
	 * @throws CommandSyntaxException If the input is malformed.
	 */
	void read(StringReader reader) throws CommandSyntaxException;

	@Override
	default Result<Result.Void> getResult(StringReader reader) {
		try {
			read(reader);
			return Result.withVoidValue();
		} catch (CommandSyntaxException exception) {
			return Result.withException(exception);
		}
	}

	// Add suggestions

	/**
	 * A {@link ParserLiteral} that also places suggestions at the cursor of the {@link StringReader} when invoked.
	 *
	 * @param base        The {@link ParserLiteral} that defines the parsing behavior.
	 * @param suggestions The {@link SuggestionProvider} that will generate the suggestions.
	 */
	record WithSuggestions(ParserLiteral base, SuggestionProvider suggestions) implements ParserLiteral {
		@Override
		public void read(StringReader reader) throws CommandSyntaxException {
			base.read(reader);
		}

		@Override
		public Result<Result.Void> getResult(StringReader reader) {
			int suggestionsStart = reader.getCursor();
			return ParserLiteral.super.getResult(reader).withSuggestions(suggestionsStart, suggestions);
		}

		@Override
		public WithSuggestions suggests(SuggestionProvider suggestions) {
			return new WithSuggestions(base, suggestions);
		}
	}

	/**
	 * Adds suggestions to the {@link Result} of this {@link ParserLiteral}. The suggestions will be placed where the
	 * cursor of the {@link StringReader} was when the parser was invoked.
	 *
	 * @param suggestions The {@link SuggestionProvider} object that will generate the suggestions.
	 * @return A {@link WithSuggestions} object to continue the building process with.
	 */
	default WithSuggestions suggests(SuggestionProvider suggestions) {
		return new WithSuggestions(this, suggestions);
	}
}
