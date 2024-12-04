package dev.jorel.commandapi.arguments.parser;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

/**
 * A {@link FunctionalInterface} with abstract method {@link #parse(StringReader)}.
 * This extends {@link Parser}{@code <T>}.
 *
 * @param <T> The type of object that {@link #parse(StringReader)} returns.
 */
@FunctionalInterface
public interface ParserArgument<T> extends Parser<T> {
	// Implement parsing logic
	@Override
	T parse(StringReader reader) throws CommandSyntaxException;

	@Override
	default Result<T> getResult(StringReader reader) {
		try {
			return Result.withValue(parse(reader));
		} catch (CommandSyntaxException exception) {
			return Result.withException(exception);
		}
	}

	// Add suggestions

	/**
	 * A {@link ParserArgument} that also places suggestions at the cursor of the {@link StringReader} when invoked.
	 *
	 * @param base        The {@link ParserArgument} that defines the parsing behavior.
	 * @param suggestions The {@link SuggestionProvider} that will generate the suggestions.
	 * @param <T>         The type of object that {@link #parse(StringReader)} returns.
	 */
	record WithSuggestions<T>(ParserArgument<T> base, SuggestionProvider suggestions) implements ParserArgument<T> {
		@Override
		public T parse(StringReader reader) throws CommandSyntaxException {
			return base.parse(reader);
		}

		@Override
		public Result<T> getResult(StringReader reader) {
			int suggestionsStart = reader.getCursor();
			return ParserArgument.super.getResult(reader).withSuggestions(suggestionsStart, suggestions);
		}

		@Override
		public WithSuggestions<T> suggests(SuggestionProvider suggestions) {
			return new WithSuggestions<>(base, suggestions);
		}
	}

	/**
	 * Adds suggestions to the {@link Result} of this {@link ParserArgument}. The suggestions will be placed where the
	 * cursor of the {@link StringReader} was when the parser was invoked.
	 *
	 * @param suggestions The {@link SuggestionProvider} object that will generate the suggestions.
	 * @return A {@link WithSuggestions} object to continue the building process with.
	 */
	default WithSuggestions<T> suggests(SuggestionProvider suggestions) {
		return new WithSuggestions<>(this, suggestions);
	}
}
