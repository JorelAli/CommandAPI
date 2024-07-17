package dev.jorel.commandapi.arguments.parser;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Predicate;

@FunctionalInterface
public interface Parser<T> {
	//////////////////////
	// Define interface //
	//////////////////////
	Result<T> getResult(StringReader reader);

	default T parse(StringReader reader) throws CommandSyntaxException {
		Result<T> result = getResult(reader);
		if (result.exception != null) throw result.exception;
		return result.value;
	}

	default <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
		StringReader reader = new StringReader(builder.getInput());
		reader.setCursor(builder.getStart());

		Result<T> result = getResult(reader);
		if (result.suggestions == null) {
			return builder.buildFuture();
		}
		builder = builder.createOffset(result.suggestionsStart);
		result.suggestions.addSuggestions(context, builder);
		return builder.buildFuture();
	}

	///////////////////
	// Build parsers //
	///////////////////
	// Start builder by reading from input
	static <T> Argument<T> parse(Parser<T> parser) {
		return parser::parse;
	}

	static <T> Argument<T> parse(Argument<T> parser) {
		return parser;
	}

	static Literal read(Literal reader) {
		return reader;
	}

	@FunctionalInterface
	interface Argument<T> extends ExceptionHandler<T, TerminalArgument<T>, NonTerminal.Argument<T>> {
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

		// Optionally continue build
		record WithSuggestions<T>(Argument<T> base, SuggestionProvider suggestions) implements Argument<T> {
			@Override
			public T parse(StringReader reader) throws CommandSyntaxException {
				return base.parse(reader);
			}

			@Override
			public Result<T> getResult(StringReader reader) {
				int suggestionsStart = reader.getCursor();
				return Argument.super.getResult(reader).withSuggestions(suggestionsStart, suggestions);
			}
		}

		default Argument<T> suggests(SuggestionProvider suggestions) {
			return new WithSuggestions<>(this, suggestions);
		}

		@Override
		default TerminalArgument<T> alwaysMapException(Function<CommandSyntaxException, CommandSyntaxException> map) {
			return new TerminalArgument<>(this, map);
		}

		@Override
		default NonTerminal.Argument<T> mapExceptions(Function<CommandSyntaxException, Optional<CommandSyntaxException>> map) {
			return new NonTerminal.Argument<>(this, map);
		}
	}

	@FunctionalInterface
	interface Literal extends ExceptionHandler<Void, TerminalLiteral, NonTerminal.Literal> {
		// Implement parsing logic
		void read(StringReader reader) throws CommandSyntaxException;

		@Override
		default Result<Void> getResult(StringReader reader) {
			try {
				read(reader);
				return Result.withValue(null);
			} catch (CommandSyntaxException exception) {
				return Result.withException(exception);
			}
		}

		// Optionally continue build
		record WithSuggestions(Literal base, SuggestionProvider suggestions) implements Literal {
			@Override
			public void read(StringReader reader) throws CommandSyntaxException {
				base.read(reader);
			}

			@Override
			public Result<Void> getResult(StringReader reader) {
				int suggestionsStart = reader.getCursor();
				return Literal.super.getResult(reader).withSuggestions(suggestionsStart, suggestions);
			}
		}

		default Literal suggests(SuggestionProvider suggestions) {
			return new WithSuggestions(this, suggestions);
		}

		@Override
		default TerminalLiteral alwaysMapException(Function<CommandSyntaxException, CommandSyntaxException> map) {
			return new TerminalLiteral(this, map);
		}

		@Override
		default NonTerminal.Literal mapExceptions(Function<CommandSyntaxException, Optional<CommandSyntaxException>> map) {
			return new NonTerminal.Literal(this, map);
		}
	}

	// Define special handling with an exception
	interface ExceptionHandler<T, NextTerminal extends Parser<T>, NextNonTerminal extends NonTerminal<T>> extends Parser<T> {
		// Either throw it (or substitute with another exception) to match Parser interface
		NextTerminal alwaysMapException(Function<CommandSyntaxException, CommandSyntaxException> map);

		default NextTerminal alwaysThrowException() {
			return alwaysMapException(Function.identity());
		}

		// Or catch it to leave the path unresolved
		NextNonTerminal mapExceptions(Function<CommandSyntaxException, Optional<CommandSyntaxException>> map);

		default NextNonTerminal throwExceptionIfTrue(Predicate<CommandSyntaxException> test) {
			return mapExceptions(exception -> Optional.ofNullable(test.test(exception) ? exception : null));
		}

		default NextNonTerminal neverThrowException() {
			return mapExceptions(exception -> Optional.empty());
		}
	}

	private static <T> Result<T> mergeResultSuggestions(Result<T> newResult, Result<?> oldResult) {
		return newResult.suggestions == null ?
			newResult.withSuggestions(oldResult.suggestionsStart, oldResult.suggestions) :
			newResult;
	}

	record TerminalArgument<T>(
		Argument<T> parser,
		Function<CommandSyntaxException, CommandSyntaxException> map
	) implements Parser<T> {
		// Implement parsing logic
		@Override
		public Result<T> getResult(StringReader reader) {
			try {
				return Result.withValue(parser.parse(reader));
			} catch (CommandSyntaxException exception) {
				CommandSyntaxException toThrow = map.apply(exception);
				return Result.withException(toThrow);
			}
		}

		// Optionally continue build
		// In the case where this returns a value, provide that value as context and keep parsing
		public <P> Parser<P> continueWith(Function<ParameterGetter<T>, Parser<P>> continueBuild) {
			ParameterGetter<T> getParameter = new ParameterGetter<>();

			Parser<P> continueParser = continueBuild.apply(getParameter);

			return reader -> {
				Result<T> parameterResult = parser.getResult(reader);

				if (parameterResult.exception != null) {
					CommandSyntaxException toThrow = map.apply(parameterResult.exception);
					return mergeResultSuggestions(Result.withException(toThrow), parameterResult);
				}
				getParameter.set(parameterResult.value);

				return mergeResultSuggestions(continueParser.getResult(reader), parameterResult);
			};
		}
	}

	record TerminalLiteral(
		Literal parser,
		Function<CommandSyntaxException, CommandSyntaxException> map
	) implements Parser<Void> {
		// Implement parsing logic
		@Override
		public Result<Void> getResult(StringReader reader) {
			try {
				parser.read(reader);
				return Result.withValue(null);
			} catch (CommandSyntaxException exception) {
				CommandSyntaxException toThrow = map.apply(exception);
				return Result.withException(toThrow);
			}
		}

		// Optionally continue build
		// In the case where the read does not throw an exception, keep parsing
		public <P> Parser<P> continueWith(Parser<P> continueParser) {
			return reader -> {
				Result<Void> parameterResult = parser.getResult(reader);

				if (parameterResult.exception != null) {
					CommandSyntaxException toThrow = map.apply(parameterResult.exception);
					return mergeResultSuggestions(Result.withException(toThrow), parameterResult);
				}

				return mergeResultSuggestions(continueParser.getResult(reader), parameterResult);
			};
		}
	}

	// Not a Parser since it may not finish with a definite value or exception
	interface NonTerminal<T> {
		Result<T> getResult(StringReader reader);

		record Argument<T>(
			Parser.Argument<T> parser,
			Function<CommandSyntaxException, Optional<CommandSyntaxException>> map
		) implements NonTerminal<T> {
			// Implement parsing logic
			@Override
			public Result<T> getResult(StringReader reader) {
				try {
					return Result.withValue(parser.parse(reader));
				} catch (CommandSyntaxException exception) {
					Optional<CommandSyntaxException> toThrow = map.apply(exception);
					return Result.withException(toThrow.orElse(null));
				}
			}

			// Optionally continue build
			// In the case where this returns a value, provide that value as context and try new branches
			public <P> NonTerminal<P> continueWith(Function<ParameterGetter<T>, Parser<P>> continueBuild) {
				ParameterGetter<T> getParameter = new ParameterGetter<>();

				Parser<P> continueParser = continueBuild.apply(getParameter);

				return reader -> {
					Result<T> parameterResult = parser.getResult(reader);

					if (parameterResult.exception != null) {
						Optional<CommandSyntaxException> toThrow = map.apply(parameterResult.exception);
						return mergeResultSuggestions(Result.withException(toThrow.orElse(null)), parameterResult);
					}
					getParameter.set(parameterResult.value);

					return mergeResultSuggestions(continueParser.getResult(reader), parameterResult);
				};
			}
		}

		record Literal(
			Parser.Literal parser,
			Function<CommandSyntaxException, Optional<CommandSyntaxException>> map
		) implements NonTerminal<Void> {
			// Implement parsing logic
			@Override
			public Result<Void> getResult(StringReader reader) {
				try {
					parser.read(reader);
					return Result.withValue(null);
				} catch (CommandSyntaxException exception) {
					Optional<CommandSyntaxException> toThrow = map.apply(exception);
					return Result.withException(toThrow.orElse(null));
				}
			}

			// Optionally continue build
			// In the case where the read does not throw an exception, try new branches
			public <P> NonTerminal<P> continueWith(Parser<P> continueParser) {
				return reader -> {
					Result<Void> parameterResult = parser.getResult(reader);

					if (parameterResult.exception != null) {
						Optional<CommandSyntaxException> toThrow = map.apply(parameterResult.exception);
						return mergeResultSuggestions(Result.withException(toThrow.orElse(null)), parameterResult);
					}

					return mergeResultSuggestions(continueParser.getResult(reader), parameterResult);
				};
			}
		}
	}

	// Try different possibilities
	static <T> Branches<T> tryParse(NonTerminal<T> branch) {
		return new Branches<T>().thenTryParse(branch);
	}

	class Branches<T> {
		private final List<NonTerminal<T>> possibilities = new ArrayList<>();

		// Check through possibly non-terminal results
		public Branches<T> thenTryParse(NonTerminal<T> branch) {
			this.possibilities.add(branch);

			return this;
		}

		// Conclude with a parser that always resolves
		public Parser<T> then(Parser<T> parser) {
			List<NonTerminal<T>> possibilities = this.possibilities;

			return reader -> {
				Result<T> suggestionsResult = Result.withValue(null);

				int start = reader.getCursor();
				for (NonTerminal<T> potential : possibilities) {
					Result<T> result = potential.getResult(reader);

					if (result.value != null || result.exception != null) {
						return result;
					}
					if (result.suggestions != null) {
						suggestionsResult = result;
					}

					// Reset cursor for next try
					reader.setCursor(start);
				}

				return mergeResultSuggestions(parser.getResult(reader), suggestionsResult);
			};
		}
	}

	//////////////////////////////////
	// Common parsers and utilities //
	//////////////////////////////////
	CommandSyntaxException NEXT_BRANCH = new SimpleCommandExceptionType(
		() -> "This branch did not match"
	).create();

	static Literal assertCanRead(Function<StringReader, CommandSyntaxException> exception) {
		return reader -> {
			if (!reader.canRead()) throw exception.apply(reader);
		};
	}

	static Literal literal(String literal) {
		return reader -> {
			if (reader.canRead(literal.length())) {
				int start = reader.getCursor();
				int end = start + literal.length();

				if (reader.getString().substring(start, end).equals(literal)) {
					reader.setCursor(end);
					return;
				}
			}
			throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.literalIncorrect().createWithContext(reader, literal);
		};
	}

	static Argument<String> readUntilWithoutEscapeCharacter(char terminator) {
		return reader -> {
			int start = reader.getCursor();
			while (reader.canRead() && reader.peek() != terminator) {
				reader.skip();
			}
			return reader.getString().substring(start, reader.getCursor());
		};
	}
}
