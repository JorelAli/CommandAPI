package dev.jorel.commandapi.arguments.parser;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import java.util.Optional;
import java.util.function.Function;

interface Branch<T> {
	// Parsing logic inspired by Brigadier
	Result<T> parse(StringReader reader);

	record Argument<T, P>(
		Parser<P> parameterParser, ParameterGetter<P> parameterGetter,
		Function<CommandSyntaxException, Optional<CommandSyntaxException>> parameterExceptionMap,
		Parser<T> continueParser
	) implements Branch<T> {
		@Override
		public Result<T> parse(StringReader reader) {
			Result<P> parameterResult = parameterParser.parse(reader);

			if (parameterResult.exception != null) {
				Optional<CommandSyntaxException> toThrow = parameterExceptionMap.apply(parameterResult.exception);
				return Result.withExceptionAndSuggestions(toThrow.orElse(null), parameterResult.suggestionsStart, parameterResult.suggestions);
			}
			parameterGetter.set(parameterResult.value);

			Result<T> continueResult = continueParser.parse(reader);
			return continueResult.suggestions == null ?
				continueResult.withSuggestions(parameterResult.suggestionsStart, parameterResult.suggestions) :
				continueResult;
		}
	}

	record Literal<T>(
		Parser.VoidParser parser,
		Function<CommandSyntaxException, Optional<CommandSyntaxException>> parameterExceptionMap,
		Parser<T> continueParser
	) implements Branch<T> {
		@Override
		public Result<T> parse(StringReader reader) {
			Result<Void> result = parser.parse(reader);
			if (result.exception != null) {
				Optional<CommandSyntaxException> toThrow = parameterExceptionMap.apply(result.exception);
				return Result.withExceptionAndSuggestions(toThrow.orElse(null), result.suggestionsStart, result.suggestions);
			}

			Result<T> continueResult = continueParser.parse(reader);
			return continueResult.suggestions == null ?
				continueResult.withSuggestions(result.suggestionsStart, result.suggestions) :
				continueResult;
		}
	}
}
