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
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

@FunctionalInterface
public interface Parser<T> {
	//////////////////////
	// Define interface //
	//////////////////////
	Result<T> parse(StringReader reader);

	default T parseValueOrThrow(StringReader reader) throws CommandSyntaxException {
		Result<T> tResult = parse(reader);
		if (tResult.exception != null) throw tResult.exception;
		return tResult.value;
	}

	default <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
		StringReader reader = new StringReader(builder.getInput());
		reader.setCursor(builder.getStart());

		Result<T> result = parse(reader);
		if (result.suggestions == null) {
			return builder.buildFuture();
		}
		builder = builder.createOffset(result.suggestionsStart);
		result.suggestions.addSuggestions(context, builder);
		return builder.buildFuture();
	}

	// Version that matches `ArgumentType#parse`
	@FunctionalInterface
	interface NoSuggestions<T> extends Parser<T> {
		T parseNoSuggestions(StringReader reader) throws CommandSyntaxException;

		@Override
		default Result<T> parse(StringReader reader) {
			try {
				return Result.withValue(parseNoSuggestions(reader));
			} catch (CommandSyntaxException exception) {
				return Result.withException(exception);
			}
		}

		// Can be adapted to include suggestions
		default Parser<T> suggests(SuggestionProvider suggestions) {
			return reader -> {
				int suggestionsStart = reader.getCursor();
				return parse(reader).withSuggestions(suggestionsStart, suggestions);
			};
		}
	}

	// Versions without return value
	@FunctionalInterface
	interface VoidParser extends Parser<Void> {
	}

	@FunctionalInterface
	interface VoidNoSuggestions extends VoidParser {
		void parseNoSuggestions(StringReader reader) throws CommandSyntaxException;

		@Override
		default Result<Void> parse(StringReader reader) {
			try {
				parseNoSuggestions(reader);
				return Result.withValue(null);
			} catch (CommandSyntaxException exception) {
				return Result.withException(exception);
			}
		}

		// Can be adapted to include suggestions
		default VoidParser suggests(SuggestionProvider suggestions) {
			return reader -> {
				int suggestionsStart = reader.getCursor();
				return parse(reader).withSuggestions(suggestionsStart, suggestions);
			};
		}
	}

	///////////////////
	// Build Parsers //
	///////////////////

	// These methods simply forward to the real builder implementation
	//  Nice for not having to specify `Parser.Builder.whatever~`
	static <T, P> Builder.ExceptionHandler<T> tryParse(
		Parser<P> parameterParser,
		BiFunction<ParameterGetter<P>, Builder.ResultTypeUnknown, Parser<T>> buildContinue
	) {
		return new Builder.ResultTypeUnknown().tryParse(parameterParser, buildContinue);
	}

	static <T, P> Builder.ExceptionHandler<T> tryParseNoSuggestions(
		NoSuggestions<P> parameterParser,
		BiFunction<ParameterGetter<P>, Builder.ResultTypeUnknown, Parser<T>> buildContinue
	) {
		return tryParse(parameterParser, buildContinue);
	}

	static <T> Builder.ExceptionHandler<T> tryParse(VoidParser reader, Function<Builder.ResultTypeUnknown, Parser<T>> buildContinue) {
		return new Builder.ResultTypeUnknown().tryParse(reader, buildContinue);
	}

	static <T> Builder.ExceptionHandler<T> tryParse(
		VoidNoSuggestions reader,
		Function<Builder.ResultTypeUnknown, Parser<T>> buildContinue
	) {
		return tryParse((VoidParser) reader, buildContinue);
	}

	static <T> Parser<T> conclude(Parser<T> conclude) {
		return conclude;
	}

	static <T> Parser<T> concludeNoSuggestions(Parser.NoSuggestions<T> conclude) {
		return conclude;
	}

	class Builder<T> {

		// Determine whether exceptions are thrown or we pass onto the next branch
		@FunctionalInterface
		public interface ExceptionHandler<T> {
			Builder<T> mapExceptions(Function<CommandSyntaxException, Optional<CommandSyntaxException>> map);

			default Builder<T> throwExceptionIfTrue(Predicate<CommandSyntaxException> test) {
				return mapExceptions(exception -> Optional.ofNullable(test.test(exception) ? exception : null));
			}

			default Builder<T> neverThrowException() {
				return mapExceptions(exception -> Optional.empty());
			}

			// These methods can conclude the parser, since we'll always throw before needing to do more processing
			default Parser<T> alwaysMapException(Function<CommandSyntaxException, CommandSyntaxException> map) {
				return mapExceptions(map.andThen(Optional::of)).conclude(null);
			}

			default Parser<T> alwaysThrowException() {
				return mapExceptions(Optional::of).conclude(null);
			}
		}

		public static class ResultTypeUnknown {
			// Some Java type-inference magic is happening here :)
			//  With the syntax of this tree builder, there will only be one call to `ResultTypeUnknown#conclude`
			//  The type of Parser there can be inferred from the return expression of the functional interface
			//  This defines T, which is then propagated backward to determine the type of Builder<T>
			public <T, P> ExceptionHandler<T> tryParse(
				Parser<P> parameterParser,
				BiFunction<ParameterGetter<P>, ResultTypeUnknown, Parser<T>> buildContinue
			) {
				ParameterGetter<P> getParameter = new ParameterGetter<>();
				ResultTypeUnknown continueBuilder = new ResultTypeUnknown();

				Parser<T> continueParser = buildContinue.apply(getParameter, continueBuilder);

				return exceptionMap -> {
					Builder<T> builder = new Builder<>();
					builder.branches.add(new Branch.Argument<>(parameterParser, getParameter, exceptionMap, continueParser));
					return builder;
				};
			}

			public <T, P> ExceptionHandler<T> tryParseNoSuggestions(
				NoSuggestions<P> parameterParser,
				BiFunction<ParameterGetter<P>, ResultTypeUnknown, Parser<T>> buildContinue
			) {
				return tryParse(parameterParser, buildContinue);
			}

			public <T> ExceptionHandler<T> tryParse(
				VoidParser reader,
				Function<Builder.ResultTypeUnknown, Parser<T>> buildContinue
			) {
				ResultTypeUnknown continueBuilder = new ResultTypeUnknown();

				Parser<T> continueParser = buildContinue.apply(continueBuilder);

				return exceptionMap -> {
					Builder<T> builder = new Builder<>();
					builder.branches.add(new Branch.Literal<>(reader, exceptionMap, continueParser));
					return builder;
				};
			}

			public <T> Builder.ExceptionHandler<T> tryParse(
				VoidNoSuggestions reader,
				Function<Builder.ResultTypeUnknown, Parser<T>> buildContinue
			) {
				return tryParse((VoidParser) reader, buildContinue);
			}

			public <T> Parser<T> conclude(Parser<T> conclude) {
				return conclude;
			}

			public <T> Parser<T> concludeNoSuggestions(Parser.NoSuggestions<T> conclude) {
				return conclude;
			}
		}

		// Sane builder Impl since we know T now
		private final List<Branch<T>> branches = new ArrayList<>();

		public <P> ExceptionHandler<T> tryParse(
			Parser<P> parameterParser,
			BiFunction<ParameterGetter<P>, Builder<T>, Parser<T>> buildContinue
		) {
			ParameterGetter<P> getParameter = new ParameterGetter<>();
			Builder<T> continueBuilder = new Builder<>();

			Parser<T> continueParser = buildContinue.apply(getParameter, continueBuilder);

			return exceptionMap -> {
				branches.add(new Branch.Argument<>(parameterParser, getParameter, exceptionMap, continueParser));
				return this;
			};
		}

		public <P> ExceptionHandler<T> tryParseNoSuggestions(
			NoSuggestions<P> parameterParser,
			BiFunction<ParameterGetter<P>, Builder<T>, Parser<T>> buildContinue
		) {
			return tryParse(parameterParser, buildContinue);
		}

		public ExceptionHandler<T> tryParse(VoidParser reader, Function<Builder<T>, Parser<T>> buildContinue) {
			Builder<T> continueBuilder = new Builder<>();

			Parser<T> continueParser = buildContinue.apply(continueBuilder);

			return test -> {
				branches.add(new Branch.Literal<>(reader, test, continueParser));
				return this;
			};
		}

		public ExceptionHandler<T> tryParse(VoidNoSuggestions reader, Function<Builder<T>, Parser<T>> buildContinue) {
			return tryParse((VoidParser) reader, buildContinue);
		}

		public Parser<T> conclude(Parser<T> conclude) {
			List<Branch<T>> branches = this.branches;
			if (branches.isEmpty()) return conclude;

			return reader -> {
				int suggestionsStart = 0;
				SuggestionProvider suggestions = null;

				int start = reader.getCursor();
				for (Branch<T> potential : branches) {
					Result<T> result = potential.parse(reader);

					if (result.value != null || result.exception != null) {
						return result;
					}
					if (result.suggestions != null) {
						suggestionsStart = result.suggestionsStart;
						suggestions = result.suggestions;
					}

					// Reset cursor for next try
					reader.setCursor(start);
				}

				Result<T> concludeResult = conclude.parse(reader);
				return concludeResult.suggestions == null ?
					concludeResult.withSuggestions(suggestionsStart, suggestions) :
					concludeResult;
			};
		}

		public Parser<T> concludeNoSuggestions(Parser.NoSuggestions<T> conclude) {
			return conclude(conclude);
		}
	}

	////////////////////
	// Common parsers //
	////////////////////
	CommandSyntaxException NEXT_BRANCH = new SimpleCommandExceptionType(
		() -> "This branch did not match"
	).create();

	static VoidNoSuggestions assertCanRead(Function<StringReader, CommandSyntaxException> exception) {
		return reader -> {
			if (!reader.canRead()) throw exception.apply(reader);
		};
	}

	static VoidNoSuggestions literal(String literal) {
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

	static NoSuggestions<String> readUntilWithoutEscapeCharacter(char terminator) {
		return reader -> {
			int start = reader.getCursor();
			while (reader.canRead() && reader.peek() != terminator) {
				reader.skip();
			}
			return reader.getString().substring(start, reader.getCursor());
		};
	}
}
