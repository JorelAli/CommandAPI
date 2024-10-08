package dev.jorel.commandapi.arguments.parser;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * A {@link FunctionalInterface} that reads input from a {@link StringReader} and interprets it as an object.
 * <p>
 * Use {@link #parse(StringReader)} or {@link #listSuggestions(CommandContext, SuggestionsBuilder)} to process input.
 * <p>
 * Parsers may be directly defined by implementing the abstract method {@link #getResult(StringReader)} method. More
 * complex parsers can be created with a builder syntax that starts with one of the following methods:
 * <ul>
 *     <li>{@link #parse(Parser)}</li>
 *     <li>{@link #parse(Argument)}</li>
 *     <li>{@link #read(Literal)}</li>
 *     <li>{@link #tryParse(NonTerminal)}</li>
 * </ul>
 *
 * @param <T> The type of object that {@link #parse(StringReader)} returns.
 */
@FunctionalInterface
public interface Parser<T> {
	//////////////////////
	// Define interface //
	//////////////////////
	/**
	 * Parses the given input. The returned {@link Result} must either have a value or an exception.
	 *
	 * @param reader The {@link StringReader} that holds the input to parse.
	 * @return A {@link Result} object holding information about the parse.
	 */
	Result<T> getResult(StringReader reader);

	/**
	 * Parses the given input and either returns the corresponding object or throws
	 * an exception explaining why the input could not be interpreted.
	 *
	 * @param reader The {@link StringReader} that holds the input to parse.
	 * @return The object represented by the given input.
	 * @throws CommandSyntaxException If the input is malformed.
	 */
	default T parse(StringReader reader) throws CommandSyntaxException {
		Result<T> result = getResult(reader);
		if (result.exception != null) throw result.exception;
		return result.value;
	}

	/**
	 * Attempts to suggest how the remaining input could be completed or fixed to produce valid input for this parser.
	 *
	 * @param context The {@link CommandContext} that holds information about the command that needs suggestions.
	 * @param builder The {@link SuggestionsBuilder} that holds the input and position where suggestions should be given.
	 * @return A {@link CompletableFuture} that holds the resulting {@link Suggestions}.
	 * @param <S> The type of object returned by {@link CommandContext#getSource()}.
	 */
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
	/**
	 * Starts building a {@link Parser}. The first action of the built parser will be to read an object or throw
	 * an exception according to the given {@link Parser}.
	 *
	 * @param parser A {@link Parser} object representing the first parse action that should be taken by the final built parser.
	 * @return A {@link Argument} object to continue the building process with.
	 * @param <T> The type of object returned by the first parse action.
	 */
	static <T> Argument<T> parse(Parser<T> parser) {
		return parser::parse;
	}

	/**
	 * Starts building a {@link Parser}. The first action of the built parser will be to read an object or throw
	 * an exception according to the given {@link Argument}. This will typically be defined as a lambda of the form
	 * {@code (StringReader reader) -> new T()}.
	 * <p>
	 * Note that this method returns exactly the object passed as its parameter. If you already have an {@link Argument}
	 * object, you don't need to call this method. However, if you are defining a new {@link Argument} object using a
	 * lambda, this method can be used to help Java understand the intended signature of the lambda and properly interpret
	 * it as an {@link Argument} object.
	 *
	 * @param parser A {@link Argument} object representing the first parse action that should be taken by the final built parser.
	 * @return A {@link Argument} object to continue the building process with.
	 * @param <T> The type of object returned by the first parse action.
	 */
	static <T> Argument<T> parse(Argument<T> parser) {
		return parser;
	}

	/**
	 * Starts building a {@link Parser}. The first action of the built parser will be to read from the input or throw
	 * an exception according to the given {@link Literal}. This will typically be defined as a lambda of the form
	 * {@code (StringReader reader) -> {}}.
	 * <p>
	 * Note that this method returns exactly the object passed as its parameter. If you already have a {@link Literal}
	 * object, you don't need to call this method. However, if you are defining a new {@link Literal} object using a
	 * lambda, this method can be used to help Java understand the intended signature of the lambda and properly interpret
	 * it as a {@link Literal} object.
	 *
	 * @param reader A {@link Literal} object representing the first parse action that should be taken by the final built parser.
	 * @return A {@link Literal} object to continue the building process with.
	 */
	static Literal read(Literal reader) {
		return reader;
	}

	/**
	 * A {@link FunctionalInterface} used for building a {@link Parser}. You can create an object for this by implementing
	 * the abstract {@link #parse(StringReader)} method. This interface extends {@code Parser<T>}, so it can be directly
	 * used as a {@link Parser}. It also provides methods to continue the building process.
	 * <p>
	 * The build process continues with the methods detailed in {@link ExceptionHandler}.
	 * You can optionally add suggestions for this step of the parsing process using {@link #suggests(SuggestionProvider)}.
	 *
	 * @param <T> The type of object that {@link #parse(StringReader)} returns.
	 */
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

		/**
		 * An {@link Argument} that also places suggestions at the cursor of the {@link StringReader} when invoked. This
		 * object can be used as a {@link Parser}, or the building process can continue with the methods detailed in
		 * {@link ExceptionHandler}.
		 *
		 * @param base        The {@link Argument} that defines the parsing behavior.
		 * @param suggestions The {@link SuggestionProvider} that will generate the suggestions.
		 * @param <T>         The type of object that {@link #parse(StringReader)} returns.
		 */
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

			@Override
			public WithSuggestions<T> suggests(SuggestionProvider suggestions) {
				return new WithSuggestions<>(base, suggestions);
			}
		}

		/**
		 * Adds suggestions to this step of the parsing process. The suggestions will be placed where the cursor
		 * of the {@link StringReader} was when the parser was invoked.
		 *
		 * @param suggestions The {@link SuggestionProvider} object that will generate the suggestions.
		 * @return A {@link WithSuggestions} object to continue the building process with.
		 */
		default WithSuggestions<T> suggests(SuggestionProvider suggestions) {
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

	/**
	 * A {@link FunctionalInterface} used for building a {@link Parser}. You can create an object for this by implementing
	 * the abstract {@link #read(StringReader)} method. This interface extends {@code Parser<Void>}, so it can be directly
	 * used as a {@link Parser}. It also provides methods to continue the building process.
	 * <p>
	 * The build process continues with the methods detailed in {@link ExceptionHandler}.
	 * You can optionally add suggestions for this step of the parsing process using {@link #suggests(SuggestionProvider)}.
	 */
	@FunctionalInterface
	interface Literal extends ExceptionHandler<Void, TerminalLiteral, NonTerminal.Literal> {
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
		default Result<Void> getResult(StringReader reader) {
			try {
				read(reader);
				return Result.withValue(null);
			} catch (CommandSyntaxException exception) {
				return Result.withException(exception);
			}
		}

		// Optionally continue build
		/**
		 * An {@link Literal} that also places suggestions at the cursor of the {@link StringReader} when invoked. This
		 * object can be used as a {@link Parser}, or the building process can continue with the methods detailed in
		 * {@link ExceptionHandler}.
		 *
		 * @param base        The {@link Literal} that defines the parsing behavior.
		 * @param suggestions The {@link SuggestionProvider} that will generate the suggestions.
		 */
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

			@Override
			public WithSuggestions suggests(SuggestionProvider suggestions) {
				return new WithSuggestions(base, suggestions);
			}
		}

		/**
		 * Adds suggestions to this step of the parsing process. The suggestions will be placed where the cursor
		 * of the {@link StringReader} was when the parser was invoked.
		 *
		 * @param suggestions The {@link SuggestionProvider} object that will generate the suggestions.
		 * @return A {@link WithSuggestions} object to continue the building process with.
		 */
		default WithSuggestions suggests(SuggestionProvider suggestions) {
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

	/**
	 * An interface used for building a {@link Parser}. A {@link Parser} usually either returns a value or throws a
	 * {@link CommandSyntaxException}. The methods in this class can be used to define more specific behavior when an
	 * exception is thrown.
	 * <p>
	 * These methods will return a {@link Parser} since the resulting object still always either returns a value or
	 * throws a {@link CommandSyntaxException}:
	 * <ul>
	 *     <li>{@link #alwaysMapException(Function)}</li>
	 *     <li>{@link #alwaysThrowException()}</li>
	 * </ul>
	 * These objects will always terminate the parsing process with a return value or an exception that will bubble up
	 * and appear in the final result.
	 * <p>
	 * These methods will return a {@link NonTerminal} because the resulting object may sometimes not return a value
	 * or throw an exception, and so does not match the interface of a {@link Parser}:
	 * <ul>
	 *     <li>{@link #mapExceptions(Function)}</li>
	 *     <li>{@link #throwExceptionIfTrue(Predicate)}</li>
	 *     <li>{@link #neverThrowException()}</li>
	 * </ul>
	 * {@link NonTerminal} is intended for use in branching structures (See {@link #tryParse(NonTerminal)}). If a
	 * {@link NonTerminal} ends up not returning a value or throwing an exception, then the next branch will be
	 * evaluated.
	 *
	 * @param <T> The type of object that {@link #parse(StringReader)} returns.
	 * @param <NextTerminal> The specific type of {@link Parser} returned by the terminal methods to continue the building process.
	 * @param <NextNonTerminal> The specific type of {@link NonTerminal} returned by the non-terminal methods to continue the building process.
	 */
	interface ExceptionHandler<T, NextTerminal extends Parser<T>, NextNonTerminal extends NonTerminal<T>> extends Parser<T> {
		// Either throw it (or substitute with another exception) to match Parser interface

		/**
		 * Returns a new {@link Parser} that always throws an exception when this {@link Parser} throws
		 * an exception. The {@link CommandSyntaxException} thrown is determined by the given mapping {@link Function}.
		 *
		 * @param map A {@link Function} that takes the original {@link CommandSyntaxException} and returns the
		 *            {@link CommandSyntaxException} that should be thrown by the returned {@link Parser}.
		 * @return A new {@link Parser} to continue the building process with.
		 */
		NextTerminal alwaysMapException(Function<CommandSyntaxException, CommandSyntaxException> map);

		/**
		 * Returns a new {@link Parser} that always throws the exception this {@link Parser} throws.
		 *
		 * @return A new {@link Parser} to continue the building process with.
		 */
		default NextTerminal alwaysThrowException() {
			return alwaysMapException(Function.identity());
		}

		// Or catch it to leave the path unresolved

		/**
		 * Returns a new {@link NonTerminal} that may not throw an exception when this {@link Parser} throws
		 * an exception. The {@link CommandSyntaxException} thrown is determined by the given mapping {@link Function}.
		 *
		 * @param map A {@link Function} that takes the original {@link CommandSyntaxException} and returns a {@link Optional}
		 *            containing the {@link CommandSyntaxException} that should be thrown by the returned {@link Parser}.
		 *            If the {@link Optional} is empty, then no exception is thrown.
		 * @return A new {@link NonTerminal} to continue the building process with.
		 */
		NextNonTerminal mapExceptions(Function<CommandSyntaxException, Optional<CommandSyntaxException>> map);

		/**
		 * Returns a new {@link NonTerminal} that may not throw an exception when this {@link Parser} throws
		 * an exception. Whether a {@link CommandSyntaxException} is thrown is determined by the given {@link Predicate}.
		 *
		 * @param test A {@link Predicate} that takes the original {@link CommandSyntaxException} and returns true
		 *             if the exception should be thrown and false is the exception should be caught.
		 * @return A new {@link NonTerminal} to continue the building process with.
		 */
		default NextNonTerminal throwExceptionIfTrue(Predicate<CommandSyntaxException> test) {
			return mapExceptions(exception -> Optional.ofNullable(test.test(exception) ? exception : null));
		}

		/**
		 * Returns a new {@link NonTerminal} that never throws an exception when this {@link Parser} throws
		 * a {@link CommandSyntaxException}.
		 *
		 * @return A new {@link NonTerminal} to continue the building process with.
		 */
		default NextNonTerminal neverThrowException() {
			return mapExceptions(exception -> Optional.empty());
		}
	}

	private static <T> Result<T> mergeResultSuggestions(Result<T> newResult, Result<?> oldResult) {
		return newResult.suggestions == null ?
			newResult.withSuggestions(oldResult.suggestionsStart, oldResult.suggestions) :
			newResult;
	}

	/**
	 * A class used for building a {@link Parser}. This class implements {@code Parser<T>}, so it can be used
	 * as a {@link Parser} that always either returns an object or throws a {@link CommandSyntaxException}.
	 * <p>
	 * The build process may also be continued using the {@link #continueWith(Function)} method.
	 *
	 * @param parser The {@link Argument} that defines the parsing behavior.
	 * @param map A mapping {@link Function} to apply to the exceptions thrown by the given parser.
	 * @param <T> The type of object that {@link #parse(StringReader)} returns.
	 */
	record TerminalArgument<T>(
		Argument<T> parser,
		Function<CommandSyntaxException, CommandSyntaxException> map
	) implements Parser<T> {
		// Implement parsing logic
		@Override
		public Result<T> getResult(StringReader reader) {
			Result<T> originalResult = parser.getResult(reader);
			if (originalResult.exception != null) {
				// If an exception was thrown, apply our mapping function
				CommandSyntaxException toThrow = map.apply(originalResult.exception);
				// Keep the original suggestions
				return Result.withExceptionAndSuggestions(toThrow, originalResult.suggestionsStart, originalResult.suggestions);
			}
			return originalResult;
		}

		// Optionally continue build

		/**
		 * Returns a new {@link Parser} that continues parsing with the result of this {@link Parser} as context.
		 * <p>
		 * If this {@link Parser} fails, the resulting {@link CommandSyntaxException} will be passed up to be the
		 * final result. If this {@link Parser} succeeds, its value will be accessible through the provided
		 * {@link ParameterGetter}, and the {@link Parser} returned by the {@code continueBuild} {@link Function}
		 * will be invoked to continue parsing the input {@link StringReader}.
		 * <p>
		 * This method is intended for combining the results of {@link Parser}s into more complex composite objects.
		 * For example:
		 * <pre>
		 * {@code
		 * Parser<A> aParser;
		 * Parser<B> bParser;
		 *
		 * // Expected input to create C: AB
		 * Parser<C> cParser = Parser
		 * 	// Parse A object from the input
		 * 	.parse(aParser)
		 * 	.alwaysThrowException()
		 * 	.continueWith(aGetter -> Parser
		 * 		// Parse B object from the input
		 * 		.parse(bParser)
		 * 		.alwaysThrowException()
		 * 		.continueWith(bGetter -> Parser
		 * 			// Get A and B to create C
		 * 			.parse(reader -> {
		 * 				return new C(aGetter.get(), bGetter.get());
		 * 			})
		 * 		)
		 * 	);
		 * }
		 * </pre>
		 *
		 * @param continueBuild A {@link Function} that takes a {@link ParameterGetter} and returns a {@link Parser}
		 *                      that will perform the next step in the parsing process.
		 * @return A new {@link Parser} that attempts to invoke this {@link Parser}, and if successful invokes the
		 * {@link Parser} returned by the {@code continueBuild} {@link Function}.
		 * @param <P> The type of object returned by the new {@link Parser}.
		 */
		public <P> Parser<P> continueWith(Function<ParameterGetter<T>, Parser<P>> continueBuild) {
			ParameterGetter<T> getParameter = new ParameterGetter<>();

			Parser<P> continueParser = continueBuild.apply(getParameter);

			return reader -> {
				Result<T> parameterResult = this.getResult(reader);

				// If the parser failed, bubble that up as our result
				if (parameterResult.exception != null) {
					// Convert Result<T> to Result<P> with same exception and suggestions
					return Result.withExceptionAndSuggestions(
						parameterResult.exception, 
						parameterResult.suggestionsStart, parameterResult.suggestions
					);
				}
				// Otherwise, provide the parsed value as context and keep parsing
				getParameter.set(parameterResult.value);

				return mergeResultSuggestions(continueParser.getResult(reader), parameterResult);
			};
		}
	}

	/**
	 * A class used for building a {@link Parser}. This class implements {@code Parser<Void>}, so it can be used
	 * as a {@link Parser} that always either succeeds or throws a {@link CommandSyntaxException}.
	 * <p>
	 * The build process may also be continued using the {@link #continueWith(Parser)} method.
	 *
	 * @param parser The {@link Literal} that defines the parsing behavior.
	 * @param map A mapping {@link Function} to apply to the exceptions thrown by the given parser.
	 */
	record TerminalLiteral(
		Literal parser,
		Function<CommandSyntaxException, CommandSyntaxException> map
	) implements Parser<Void> {
		// Implement parsing logic
		@Override
		public Result<Void> getResult(StringReader reader) {
			Result<Void> originalResult = parser.getResult(reader);
			if (originalResult.exception != null) {
				// If an exception was thrown, apply our mapping function
				CommandSyntaxException toThrow = map.apply(originalResult.exception);
				// Keep the original suggestions
				return Result.withExceptionAndSuggestions(toThrow, originalResult.suggestionsStart, originalResult.suggestions);
			}
			return originalResult;
		}

		// Optionally continue build
		// In the case where the read does not throw an exception, keep parsing

		/**
		 * Returns a new {@link Parser} that invokes the given {@link Parser} after running this {@link Parser}.
		 * <p>
		 * If this {@link Parser} fails, the resulting {@link CommandSyntaxException} will be passed up to be the
		 * final result. If this {@link Parser} succeeds, the final result is determined by the given {@link Parser}.
		 *
		 * @param continueParser The {@link Parser} to continue with if this {@link Parser} succeeds.
		 * @return A new {@link Parser} that attempts to invoke this {@link Parser}, and if successful invokes the given {@link Parser}.
		 * @param <P> The type of object returned by the new {@link Parser}.
		 */
		public <P> Parser<P> continueWith(Parser<P> continueParser) {
			return reader -> {
				Result<Void> parameterResult = this.getResult(reader);

				// If the parser failed, bubble that up as our result
				if (parameterResult.exception != null) {
					// Convert Result<Void> to Result<P> with same exception and suggestions
					return Result.withExceptionAndSuggestions(
						parameterResult.exception,
						parameterResult.suggestionsStart, parameterResult.suggestions
					);
				}

				// Otherwise, keep parsing
				return mergeResultSuggestions(continueParser.getResult(reader), parameterResult);
			};
		}
	}

	// Not a Parser since it may not finish with a definite value or exception
	/**
	 * An interface used for building a {@link Parser}. Its abstract method, {@link #getResult(StringReader)} is
	 * almost identical to {@link Parser#getResult(StringReader)}, except that the {@link Result} may not have a
	 * final value or exception. This is intended for use in branching {@link Parser} structures (See
	 * {@link Parser#tryParse(NonTerminal)}), where an inconclusive result means that parsing continues to the next branch.
	 *
	 * @param <T> The type of object returned as the value of {@link #getResult(StringReader)}.
	 */
	interface NonTerminal<T> {
		/**
		 * Parses the given input. The returned {@link Result} may not define a value or exception.
		 *
		 * @param reader The {@link StringReader} that holds the input to parse.
		 * @return A {@link Result} object holding information about the parse.
		 */
		Result<T> getResult(StringReader reader);

		/**
		 * A class used for building a {@link Parser}. This class implements {@code NonTerminal<T>}, so it can be used
		 * as a {@link NonTerminal}.
		 * <p>
		 * The build process may also be continued using the {@link #continueWith(Function)} method.
		 *
		 * @param parser The {@link Parser.Argument} that defines the parsing behavior.
		 * @param map A mapping {@link Function} to apply to the exceptions thrown by the given parser, which may
		 *            choose to not throw an exception and resolve this {@link NonTerminal} with no value or exception.
		 * @param <T> The type of object returned as the value of {@link #getResult(StringReader)}.
		 */
		record Argument<T>(
			Parser.Argument<T> parser,
			Function<CommandSyntaxException, Optional<CommandSyntaxException>> map
		) implements NonTerminal<T> {
			// Implement parsing logic
			@Override
			public Result<T> getResult(StringReader reader) {
				Result<T> originalResult = parser.getResult(reader);
				if (originalResult.exception != null) {
					// If an exception was thrown, apply our mapping function
					//  We may choose to resolve with no final result, in which case value and exception are both null
					CommandSyntaxException toThrow = map.apply(originalResult.exception).orElse(null);
					// Keep the original suggestions
					return Result.withExceptionAndSuggestions(toThrow, originalResult.suggestionsStart, originalResult.suggestions);
				}
				return originalResult;
			}

			// Optionally continue build
			// In the case where this returns a value, provide that value as context and try new branches

			/**
			 * Returns a new {@link NonTerminal} that continues parsing with the result of this {@link NonTerminal} as context.
			 * <p>
			 * If this {@link NonTerminal} fails, the resulting {@link CommandSyntaxException} will be passed up to be
			 * the final result. If this {@link NonTerminal} resolves without a value or exception, that is the final
			 * result. If this {@link NonTerminal} succeeds, its value will be accessible through the provided
			 * {@link ParameterGetter}, and the {@link Parser} returned by the {@code continueBuild} {@link Function}
			 * will be invoked to continue parsing the input {@link StringReader}.
			 * <p>
			 * See also {@link TerminalArgument#continueWith(Function)}.
			 *
			 * @param continueBuild A {@link Function} that takes a {@link ParameterGetter} and returns a {@link Parser}
			 *                      that will perform the next step in the parsing process.
			 * @return A new {@link NonTerminal} that attempts to invoke this {@link NonTerminal}, and if successful
			 * invokes the {@link Parser} returned by the {@code continueBuild} {@link Function}.
			 * @param <P> The type of object returned by the new {@link NonTerminal}.
			 */
			public <P> NonTerminal<P> continueWith(Function<ParameterGetter<T>, Parser<P>> continueBuild) {
				ParameterGetter<T> getParameter = new ParameterGetter<>();

				Parser<P> continueParser = continueBuild.apply(getParameter);

				return reader -> {
					Result<T> parameterResult = parser.getResult(reader);

					if (parameterResult.exception != null) {
						// If an exception was thrown, apply our mapping function
						//  We may choose to resolve with no final result, in which case value and exception are both null
						CommandSyntaxException toThrow = map.apply(parameterResult.exception).orElse(null);
						// Keep the original suggestions
						return Result.withExceptionAndSuggestions(toThrow, parameterResult.suggestionsStart, parameterResult.suggestions);
					}
					// Otherwise, provide the parsed value as context and keep parsing
					getParameter.set(parameterResult.value);

					return mergeResultSuggestions(continueParser.getResult(reader), parameterResult);
				};
			}
		}

		/**
		 * A class used for building a {@link Parser}. This class implements {@code NonTerminal<Void>}, so it can be used
		 * as a {@link NonTerminal}.
		 * <p>
		 * The build process may also be continued using the {@link #continueWith(Parser)} method.
		 *
		 * @param parser The {@link Parser.Literal} that defines the parsing behavior.
		 * @param map A mapping {@link Function} to apply to the exceptions thrown by the given parser, which may
		 *            choose to not throw an exception and resolve this {@link NonTerminal} with no value or exception.
		 */
		record Literal(
			Parser.Literal parser,
			Function<CommandSyntaxException, Optional<CommandSyntaxException>> map
		) implements NonTerminal<Void> {
			// Implement parsing logic
			@Override
			public Result<Void> getResult(StringReader reader) {
				Result<Void> originalResult = parser.getResult(reader);
				if (originalResult.exception != null) {
					// If an exception was thrown, apply our mapping function
					//  We may choose to resolve with no final result, in which case value and exception are both null
					CommandSyntaxException toThrow = map.apply(originalResult.exception).orElse(null);
					// Keep the original suggestions
					return Result.withExceptionAndSuggestions(toThrow, originalResult.suggestionsStart, originalResult.suggestions);
				}
				return originalResult;
			}

			// Optionally continue build
			// In the case where the read does not throw an exception, try new branches

			/**
			 * Returns a new {@link NonTerminal} that invokes the given {@link Parser} after running this {@link NonTerminal}.
			 * <p>
			 * If this {@link NonTerminal} fails, the resulting {@link CommandSyntaxException} will be passed up to be
			 * the final result. If this {@link NonTerminal} resolves without a value or exception, that is the final
			 * result. If this {@link NonTerminal} succeeds, the final result is determined by the given {@link Parser}.
			 *
			 * @param continueParser The {@link Parser} to continue with if this {@link NonTerminal} succeeds.
			 * @return A new {@link NonTerminal} that attempts to invoke this {@link NonTerminal}, and if successful invokes the given {@link Parser}.
			 * @param <P> The type of object returned by the new {@link Parser}.
			 */
			public <P> NonTerminal<P> continueWith(Parser<P> continueParser) {
				return reader -> {
					Result<Void> parameterResult = parser.getResult(reader);

					if (parameterResult.exception != null) {
						// If an exception was thrown, apply our mapping function
						//  We may choose to resolve with no final result, in which case value and exception are both null
						CommandSyntaxException toThrow = map.apply(parameterResult.exception).orElse(null);
						return Result.withExceptionAndSuggestions(toThrow, parameterResult.suggestionsStart, parameterResult.suggestions);
					}

					// Otherwise, keep parsing
					return mergeResultSuggestions(continueParser.getResult(reader), parameterResult);
				};
			}
		}
	}

	// Try different possibilities
	/**
	 * Starts building a {@link Parser}. The first action of the built parser will be to read an object or throw
	 * an exception according to the given {@link NonTerminal}. If the {@link NonTerminal} does not return a value or
	 * throw an exception, then the next branch given will be evaluated.
	 *
	 * @param branch A {@link NonTerminal} object representing the first parse action that should be taken by the final built parser.
	 * @return A new {@link Branches} object to continue the building process with.
	 * @param <T> The type of object returned by the final built {@link Parser}.
	 */
	static <T> Branches<T> tryParse(NonTerminal<T> branch) {
		return new Branches<T>().thenTryParse(branch);
	}

	/**
	 * A class used for building a {@link Parser}. The {@link #thenTryParse(NonTerminal)} method adds {@link NonTerminal}
	 * steps that should be attempted. The first {@link NonTerminal} that throws a {@link CommandSyntaxException} or
	 * returns a value determines the final {@link Result} for the final built {@link Parser}. If none of the {@link NonTerminal}
	 * return a final result, then the {@link Parser} given by {@link #then(Parser)} will determine the final result.
	 *
	 * @param <T> The type of object returned by the final build {@link Parser}.
	 */
	class Branches<T> {
		private final List<NonTerminal<T>> possibilities = new ArrayList<>();

		// Check through possibly non-terminal results

		/**
		 * Adds a {@link NonTerminal} branch at this step in the parsing process. If the given {@link NonTerminal} returns
		 * a final value or exception, that will be the final result of the final built {@link Parser}. If the given
		 * {@link NonTerminal} does not give a final result, then the next branch will be evaluated.
		 *
		 * @param branch The {@link NonTerminal} branch to test.
		 * @return This {@link Branches} object to continue the building process with.
		 */
		public Branches<T> thenTryParse(NonTerminal<T> branch) {
			this.possibilities.add(branch);

			return this;
		}

		// Conclude with a parser that always resolves

		/**
		 * Finishes building the branching {@link Parser}. The returned {@link Parser} will check each {@link NonTerminal}
		 * given by {@link #thenTryParse(NonTerminal)}. If none of those give a final result, the {@link Parser} given here
		 * will be invoked to determine the final {@link Result}.
		 *
		 * @param parser The {@link Parser} that concludes this branching step.
		 * @return A new {@link Parser} that evaluates different branches.
		 */
		public Parser<T> then(Parser<T> parser) {
			// Putting the list into a local variable means that the lambda can hold onto the
			//  list directly, rather than tracking the `this` object as well.
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
}
