package dev.jorel.commandapi.arguments.parser;

import com.mojang.brigadier.exceptions.CommandSyntaxException;

/**
 * Holds the information that results from a {@link Parser} parsing input. A parse either creates an object
 * or throws a {@link CommandSyntaxException} explaining why it couldn't create its object. The result may
 * optionally provide suggestions, which includes the point in the string where the suggestions should be
 * placed and the {@link SuggestionProvider} that will generate the suggestions.
 * <p>
 * {@link Result}s can be constructed with the following methods:
 * <ul>
 *     <li>{@link #withValue(Object)}</li>
 *     <li>{@link #withValueAndSuggestions(Object, int, SuggestionProvider)}</li>
 *     <li>{@link #withException(CommandSyntaxException)}</li>
 *     <li>{@link #withExceptionAndSuggestions(CommandSyntaxException, int, SuggestionProvider)}</li>
 * </ul>
 * Additionally, a {@link Result} object with the same value but different
 * suggestions can be created using {@link #withSuggestions(int, SuggestionProvider)}.
 *
 * @param <T> The type of object held as a return result.
 */
public class Result<T> {
	protected final T value;
	protected final CommandSyntaxException exception;
	protected final int suggestionsStart;
	protected final SuggestionProvider suggestions;

	private Result(T value, CommandSyntaxException exception, int suggestionsStart, SuggestionProvider suggestions) {
		this.value = value;
		this.exception = exception;
		this.suggestionsStart = suggestionsStart;
		this.suggestions = suggestions;
	}

	/**
	 * @param value The object resulting from parsing input.
	 * @param <T>   The type of object held as a return value.
	 * @return A new {@link Result} object with the given information.
	 */
	public static <T> Result<T> withValue(T value) {
		return new Result<>(value, null, 0, null);
	}

	/**
	 * @param exception The {@link CommandSyntaxException} explaining why a value object could not be created from the given input.
	 * @param <T>       The type of object held as a return value.
	 * @return A new {@link Result} object with the given information.
	 */
	public static <T> Result<T> withException(CommandSyntaxException exception) {
		return new Result<>(null, exception, 0, null);
	}

	/**
	 * @param value            The object resulting from parsing input.
	 * @param suggestionsStart The point in the input where suggestions should be placed.
	 * @param suggestions      The {@link SuggestionProvider} object that will generate the suggestions.
	 * @param <T>              The type of object held as a return value.
	 * @return A new {@link Result} object with the given information.
	 */
	public static <T> Result<T> withValueAndSuggestions(T value, int suggestionsStart, SuggestionProvider suggestions) {
		return new Result<>(value, null, suggestionsStart, suggestions);
	}

	/**
	 * @param exception        The {@link CommandSyntaxException} explaining why a value object could not be created from the given input.
	 * @param suggestionsStart The point in the input where suggestions should be placed.
	 * @param suggestions      The {@link SuggestionProvider} object that will generate the suggestions.
	 * @param <T>              The type of object held as a return value.
	 * @return A new {@link Result} object with the given information.
	 */
	public static <T> Result<T> withExceptionAndSuggestions(CommandSyntaxException exception, int suggestionsStart, SuggestionProvider suggestions) {
		return new Result<>(null, exception, suggestionsStart, suggestions);
	}

	/**
	 * Keeps the same value or exception but overrides any suggestions information.
	 *
	 * @param suggestionsStart The point in the input where suggestions should be placed.
	 * @param suggestions      The {@link SuggestionProvider} object that will generate the suggestions.
	 * @return A new {@link Result} object with the given information.
	 */
	public Result<T> withSuggestions(int suggestionsStart, SuggestionProvider suggestions) {
		return new Result<>(this.value, this.exception, suggestionsStart, suggestions);
	}
}
