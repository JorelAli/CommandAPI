package dev.jorel.commandapi.arguments.parser;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import java.util.concurrent.CompletableFuture;

/**
 * A {@link FunctionalInterface} that reads input from a {@link StringReader} and interprets it as an object.
 * <p>
 * Use {@link #parse(StringReader)} or {@link #listSuggestions(CommandContext, SuggestionsBuilder)} to process input.
 * <p>
 * Parsers can be directly defined by implementing the abstract method {@link #getResult(StringReader)} method. They may
 * also be defined with a slightly different interface by implementing {@link ParserArgument#parse(StringReader)}
 * or {@link ParserLiteral#parse(StringReader)}. If you are trying to define one of these using a lambda and Java needs
 * extra hints to infer the lambda signature, you can use one of the following static methods:
 *
 * <ul>
 *     <li>{@link #parse(Parser)}</li>
 *     <li>{@link #argument(ParserArgument)}</li>
 *     <li>{@link #read(ParserLiteral)}</li>
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
		return result.throwOrReturn();
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
		if (result.suggestions() == null) {
			return builder.buildFuture();
		}
		builder = builder.createOffset(result.suggestionsStart());
		result.suggestions().addSuggestions(context, builder);
		return builder.buildFuture();
	}

	// Helper methods for letting Java infer the signature of a lambda

	/**
	 * Directly returns the {@link Parser} given. This method can help Java infer
	 * the lambda signature of {@code (StringReader) -> Result<T>}.
	 *
	 * @param parser The {@link Parser} lambda.
	 * @return The {@link Parser} object.
	 * @param <T> The type of object that {@link #parse(StringReader)} returns.
	 */
	static <T> Parser<T> parse(Parser<T> parser) {
		return parser;
	}

	/**
	 * Directly returns the {@link ParserArgument} given. This method can help Java infer
	 * the lambda signature of {@code (StringReader) -> T throws CommandSyntaxException}.
	 *
	 * @param parser The {@link ParserArgument} lambda.
	 * @return The {@link ParserArgument} object.
	 * @param <T> The type of object that {@link #parse(StringReader)} returns.
	 */
	static <T> ParserArgument<T> argument(ParserArgument<T> parser) {
		return parser;
	}

	/**
	 * Directly returns the {@link ParserLiteral} given. This method can help Java infer
	 * the lambda signature of {@code (StringReader) -> void throws CommandSyntaxException}.
	 *
	 * @param reader The {@link ParserLiteral} lambda.
	 * @return The {@link ParserLiteral} object.
	 */
	static ParserLiteral read(ParserLiteral reader) {
		return reader;
	}
}
