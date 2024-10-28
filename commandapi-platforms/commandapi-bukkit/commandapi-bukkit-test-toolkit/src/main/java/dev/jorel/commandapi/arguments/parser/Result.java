package dev.jorel.commandapi.arguments.parser;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

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
 *
 *     <li>{@link #withVoidValue()}</li>
 *     <li>{@link #withVoidValueAndSuggestions(int, SuggestionProvider)}</li>
 *
 *     <li>{@link #withException(CommandSyntaxException)}</li>
 *     <li>{@link #withExceptionAndSuggestions(CommandSyntaxException, int, SuggestionProvider)}</li>
 * </ul>
 * Additionally, a {@link Result} object with the same value but different
 * suggestions can be created using {@link #withSuggestions(int, SuggestionProvider)}.
 * <p>
 * The information of a {@link Result} can be accessed directly using {@link #value()}, {@link #exception()},
 * {@link #suggestionsStart()}, or {@link #suggestions()}. Additionally, the information can be interacted with
 * using {@link #throwOrReturn()} or {@link #continueWith(Function, Function)}.
 *
 * @param <T>              The type of object held as a return result.
 * @param value            The result of parsing input, if the parse was successful.
 * @param exception        The {@link CommandSyntaxException} thrown, if the parse failed.
 * @param suggestionsStart The point in the input {@link StringReader} where the suggestions should be placed.
 * @param suggestions      The {@link SuggestionProvider} object that will generate the suggestions.
 */
public record Result<T>(
	/**
	 * @param The result of parsing input, if the parse was successful.
	 */
	T value,
	/**
	 * @param The {@link CommandSyntaxException} thrown, if the parse failed.
	 */
	CommandSyntaxException exception,
	/**
	 * @param The point in the input {@link StringReader} where the suggestions should be placed.
	 */
	int suggestionsStart,
	/**
	 * @param The {@link SuggestionProvider} object that will generate the suggestions.
	 */
	SuggestionProvider suggestions
) {
	/**
	 * A placeholder object that represents a successful parse that didn't return a specific object.
	 * This allows a null {@link Result} value to indicate an unsuccessful parse.
	 */
	public static class Void {
		// Not using `java.lang.Void` since that class can never be instantiated.
		private static final Void INSTANCE = new Void();

		private Void() {

		}
	}

	/**
	 * @param value The object resulting from parsing input.
	 * @param <T>   The type of object held as a return value.
	 * @return A new {@link Result} object with the given information.
	 */
	public static <T> Result<T> withValue(@NotNull T value) {
		return new Result<>(value, null, 0, null);
	}

	/**
	 * @return A new {@link Result} with a {@link Void} value.
	 */
	public static Result<Void> withVoidValue() {
		return withValue(Void.INSTANCE);
	}

	/**
	 * @param exception The {@link CommandSyntaxException} explaining why a value object could not be created from the given input.
	 * @param <T>       The type of object held as a return value.
	 * @return A new {@link Result} object with the given information.
	 */
	public static <T> Result<T> withException(@NotNull CommandSyntaxException exception) {
		return new Result<>(null, exception, 0, null);
	}

	/**
	 * @param value            The object resulting from parsing input.
	 * @param suggestionsStart The point in the input where suggestions should be placed.
	 * @param suggestions      The {@link SuggestionProvider} object that will generate the suggestions.
	 * @param <T>              The type of object held as a return value.
	 * @return A new {@link Result} object with the given information.
	 */
	public static <T> Result<T> withValueAndSuggestions(@NotNull T value, int suggestionsStart, SuggestionProvider suggestions) {
		return new Result<>(value, null, suggestionsStart, suggestions);
	}

	/**
	 * @param suggestionsStart The point in the input where suggestions should be placed.
	 * @param suggestions      The {@link SuggestionProvider} object that will generate the suggestions.
	 * @return A new {@link Result} object with a {@link Void} value and the given suggestions.
	 */
	public static Result<Void> withVoidValueAndSuggestions(int suggestionsStart, SuggestionProvider suggestions) {
		return withValueAndSuggestions(Void.INSTANCE, suggestionsStart, suggestions);
	}

	/**
	 * @param exception        The {@link CommandSyntaxException} explaining why a value object could not be created from the given input.
	 * @param suggestionsStart The point in the input where suggestions should be placed.
	 * @param suggestions      The {@link SuggestionProvider} object that will generate the suggestions.
	 * @param <T>              The type of object held as a return value.
	 * @return A new {@link Result} object with the given information.
	 */
	public static <T> Result<T> withExceptionAndSuggestions(@NotNull CommandSyntaxException exception, int suggestionsStart, SuggestionProvider suggestions) {
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

	/**
	 * @return {@link #value()} if the parse was successful.
	 * @throws CommandSyntaxException {@link #exception()} if the parse failed.
	 */
	public T throwOrReturn() throws CommandSyntaxException {
		if (this.value == null) {
			// No value, parsing failed
			throw this.exception;
		}

		// Parsing succeeded
		return this.value;
	}

	/**
	 * Uses this {@link Result} to generate a final {@link Result} with a value of type {@code R}.
	 * If this parse was successful, then the given {@code success} {@link Function} is used to generate the new {@link Result}.
	 * If this parse failed, then the given {@code failure} {@link Function} is used to generate the new {@link Result}.
	 * <p>
	 * If the new {@link Result} does not have any {@link #suggestions()}, then the final {@link Result} returned by
	 * this method will have the {@link #suggestionsStart()} and {@link #suggestions()} of this {@link Result}.
	 * <p>
	 * Note that the {@link #wrapFunctionResult(ThrowableFunction)} method may be useful for defining the {@code success}
	 * and {@code failure} {@link Function}s.
	 *
	 * @param success The {@link Function} to use to generate the new {@link Result} when this parse was successful.
	 *                The argument passed to {@link Function#apply(Object)} will be the {@link #value()} of this {@link Result}.
	 * @param failure The {@link Function} to use to generate the new {@link Result} when this parse failed.
	 *                The argument passed to {@link Function#apply(Object)} will be the {@link #exception()} of this {@link Result}.
	 * @return The final {@link Result} object.
	 * @param <R> The type of object held as the {@link #value()} of the final {@link Result}.
	 */
	public <R> Result<R> continueWith(Function<T, Result<R>> success, Function<CommandSyntaxException, Result<R>> failure) {
		Result<R> result;
		if (this.value == null) {
			// No value, parsing failed
			result = failure.apply(this.exception);
		} else {
			// Continue with value of parse
			result = success.apply(this.value);
		}

		// Merge suggestions
		if (result.suggestions == null) {
			return result.withSuggestions(suggestionsStart, suggestions);
		}
		return result;
	}

	/**
	 * This method works the same as {@link #continueWith(Function, Function)}, but the {@code failure} {@link Function}
	 * is given as {@code Result::withException}. This has the effect of passing the exception and suggestions of this
	 * {@link Result} without changes if this parse failed. If this parse succeeded, the {@code success}  {@link Function}
	 * is applied as usual.
	 *
	 * @param success The {@link Function} to use to generate the new {@link Result} when this parse was successful.
	 *                The argument passed to {@link Function#apply(Object)} will be the {@link #value()} of this {@link Result}.
	 * @return The final {@link Result} object.
	 * @param <R> The type of object held as the {@link #value()} of the final {@link Result}.
	 */
	public <R> Result<R> continueWith(Function<T, Result<R>> success) {
		return continueWith(success, Result::withException);
	}

	/**
	 * A {@link FunctionalInterface} with abstract method {@link #apply(Object)}. Implementations of this interface
	 * can be wrapped into a {@link Function}{@code <T, Result<R>>} using {@link #wrapFunctionResult(ThrowableFunction)}.
	 *
	 * @param <T> The type of the function parameter.
	 * @param <R> The function return type.
	 */
	@FunctionalInterface
	public interface ThrowableFunction<T, R> {
		/**
		 * Applies this function to the given argument.
		 *
		 * @param arg The function argument.
		 * @return The function result.
		 * @throws CommandSyntaxException Some exception that caused this to fail.
		 */
		R apply(T arg) throws CommandSyntaxException;
	}

	/**
	 * Converts a {@link ThrowableFunction}{@code <T, R>} into a {@link Function}{@code <T, Result<R>>}.
	 * The returned {@link Function} will pass its argument unchanged to the given {@link ThrowableFunction}. The outcome
	 * of the {@link ThrowableFunction} - either returning an object of type {@code R} or throwing a {@link CommandSyntaxException}
	 * - will be wrapped into a {@link Result}{@code <R>} as appropriate.
	 *
	 * @param original The {@link ThrowableFunction} being wrapped.
	 * @return A {@link Function} that wraps the outcome of the given {@link ThrowableFunction} in a {@link Result}.
	 * @param <T> The type of the function parameter.
	 * @param <R> The function return type.
	 */
	public static <T, R> Function<T, Result<R>> wrapFunctionResult(ThrowableFunction<T, R> original) {
		return arg -> {
			R result;
			try {
				result = original.apply(arg);
			} catch (CommandSyntaxException exception) {
				return Result.withException(exception);
			}
			return Result.withValue(result);
		};
	}
}
