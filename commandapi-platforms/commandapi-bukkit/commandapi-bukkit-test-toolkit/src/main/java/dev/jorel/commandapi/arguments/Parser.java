package dev.jorel.commandapi.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

@FunctionalInterface
public interface Parser<T> {
	//////////////////////
	// Define interface //
	//////////////////////
	T parse(StringReader reader) throws CommandSyntaxException;

	// Version without return value
	@FunctionalInterface
	interface Void extends Parser<java.lang.Void> {
		void read(StringReader reader) throws CommandSyntaxException;

		@Override
		default java.lang.Void parse(StringReader reader) throws CommandSyntaxException {
			read(reader);
			return null;
		}
	}

	//////////////////
	// Build parser //
	//////////////////
	// Idea for type safe parameter retrieval from https://github.com/JorelAli/CommandAPI/issues/544
	class ParameterGetter<T> {
		private T value;

		private void set(T value) {
			this.value = value;
		}

		public T get() {
			return value;
		}
	}

	// These methods simply forward to the real builder implementation
	//  Nice for not having to specify `Parser.Builder.whatever~`
	static <T, P> Builder.ExceptionHandler<T> tryParse(
		Parser<P> parameterParser,
		BiFunction<ParameterGetter<P>, Builder.ResultTypeUnknown, Parser<T>> buildContinue
	) {
		return new Builder.ResultTypeUnknown().tryParse(parameterParser, buildContinue);
	}

	static <T> Builder.ExceptionHandler<T> tryParse(Void reader, Function<Builder.ResultTypeUnknown, Parser<T>> buildContinue) {
		return new Builder.ResultTypeUnknown().tryParse(reader, buildContinue);
	}

	static <T> Parser<T> conclude(Parser<T> conclude) {
		return conclude;
	}

	class Builder<T> {
		private interface Branch<T> {
			// Parsing logic inspired by Brigadier
			Optional<T> parse(StringReader reader) throws CommandSyntaxException;

			record Argument<T, P>(
				Parser<P> parameterParser, ParameterGetter<P> parameterGetter,
				Function<CommandSyntaxException, Optional<CommandSyntaxException>> parameterExceptionMap,
				Parser<T> continueParser
			) implements Branch<T> {
				@Override
				public Optional<T> parse(StringReader reader) throws CommandSyntaxException {
					P parameter;
					try {
						parameter = parameterParser.parse(reader);
					} catch (CommandSyntaxException exception) {
						Optional<CommandSyntaxException> toThrow = parameterExceptionMap.apply(exception);
						if (toThrow.isPresent()) throw toThrow.get();
						return Optional.empty();
					}
					parameterGetter.set(parameter);
					return Optional.of(continueParser.parse(reader));
				}
			}

			record Literal<T>(
				Void parser,
				Function<CommandSyntaxException, Optional<CommandSyntaxException>> parameterExceptionMap,
				Parser<T> continueParser
			) implements Branch<T> {
				@Override
				public Optional<T> parse(StringReader reader) throws CommandSyntaxException {
					try {
						parser.parse(reader);
					} catch (CommandSyntaxException exception) {
						Optional<CommandSyntaxException> toThrow = parameterExceptionMap.apply(exception);
						if (toThrow.isPresent()) throw toThrow.get();
						return Optional.empty();
					}
					return Optional.of(continueParser.parse(reader));
				}
			}
		}

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

			public <T> ExceptionHandler<T> tryParse(Void reader, Function<Builder.ResultTypeUnknown, Parser<T>> buildContinue) {
				ResultTypeUnknown continueBuilder = new ResultTypeUnknown();

				Parser<T> continueParser = buildContinue.apply(continueBuilder);

				return exceptionMap -> {
					Builder<T> builder = new Builder<>();
					builder.branches.add(new Branch.Literal<>(reader, exceptionMap, continueParser));
					return builder;
				};
			}

			public <T> Parser<T> conclude(Parser<T> conclude) {
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

		public ExceptionHandler<T> tryParse(Void reader, Function<Builder<T>, Parser<T>> buildContinue) {
			Builder<T> continueBuilder = new Builder<>();

			Parser<T> continueParser = buildContinue.apply(continueBuilder);

			return test -> {
				branches.add(new Branch.Literal<>(reader, test, continueParser));
				return this;
			};
		}

		public Parser<T> conclude(Parser<T> conclude) {
			List<Branch<T>> branches = this.branches;
			return reader -> {
				int start = reader.getCursor();
				for (Branch<T> potential : branches) {
					Optional<T> result = potential.parse(reader);
					if (result.isPresent()) {
						// Update input reader to match final result
						return result.get();
					}

					// Reset cursor for next try
					reader.setCursor(start);
				}

				return conclude.parse(reader);
			};
		}
	}

	////////////////////
	// Common parsers //
	////////////////////
	static Void assertCanRead(Function<StringReader, CommandSyntaxException> exception) {
		return reader -> {
			if (!reader.canRead()) throw exception.apply(reader);
		};
	}

	static Void literal(String literal) {
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
}
