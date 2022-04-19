package dev.jorel.commandapi.arguments;

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.jorel.commandapi.IStringTooltip;
import dev.jorel.commandapi.SuggestionInfo;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * This class represents suggestions for an argument. {@link ArgumentSuggestions} objects are best
 * created using the static methods as opposed to functionally.
 */
// Yes, the following block has spaces instead of tabs. This is by design: DO NOT
// change the spaces into tabs!
/* ANCHOR: Declaration */
@FunctionalInterface
public non-sealed interface ArgumentSuggestions extends ISuggestions {

    /**
     * Create a {@link CompletableFuture} resolving onto a brigadier {@link Suggestions} object.
     * @param info The suggestions info
     * @param builder The Brigadier {@link SuggestionsBuilder} object
     * @return a {@link CompletableFuture} resolving onto a brigadier {@link Suggestions} object.
     *
     * @throws CommandSyntaxException if there is an error making suggestions
     */
    CompletableFuture<Suggestions> suggest(SuggestionInfo info, SuggestionsBuilder builder)
        throws CommandSyntaxException;
/* ANCHOR_END: Declaration */

	/**
	 * Suggest nothing
	 * @return an {@link ArgumentSuggestions} object suggesting nothing.
	 */
	static ArgumentSuggestions empty() {
		return (info, builder) -> builder.buildFuture();
	}

	/**
	 * Suggest hardcoded strings
	 * @param suggestions array of hardcoded strings
	 * @return an {@link ArgumentSuggestions} object suggesting hardcoded strings
	 */
	static ArgumentSuggestions strings(String... suggestions) {
		return (info, builder) -> future(toSuggestions(builder, suggestions));
	}

	/**
	 * Suggest strings as the result of a function
	 * @param suggestions function providing the strings
	 * @return an {@link ArgumentSuggestions} object suggesting the result of the function
	 */
	static ArgumentSuggestions strings(Function<SuggestionInfo, String[]> suggestions) {
		return (info, builder) -> future(toSuggestions(builder, suggestions.apply(info)));
	}

	/**
	 * Suggest strings asynchronously
	 * @param suggestions function providing the strings asynchronously
	 * @return an {@link ArgumentSuggestions} object suggesting the result of the asynchronous function
	 */
	static ArgumentSuggestions stringsAsync(Function<SuggestionInfo, CompletableFuture<String[]>> suggestions) {
		return (info, builder) -> suggestions
			.apply(info)
			.thenApply(strings -> toSuggestions(builder, strings));
	}

	/**
	 * Suggest hardcoded strings with tooltips
	 * @param suggestions array of hardcoded strings with tooltips
	 * @return an {@link ArgumentSuggestions} object suggesting the hardcoded strings with tooltips
	 */
	static ArgumentSuggestions stringsWithTooltips(IStringTooltip... suggestions) {
		return (info, builder) -> future(toSuggestions(builder, suggestions));
	}

	/**
	 * Suggest strings with tooltips as the result of a function
	 * @param suggestions function providing the strings with tooltips
	 * @return an {@link ArgumentSuggestions} object suggesting the result of the function
	 */
	static ArgumentSuggestions stringsWithTooltips(Function<SuggestionInfo, IStringTooltip[]> suggestions) {
		return (info, builder) -> future(toSuggestions(builder, suggestions.apply(info)));
	}

	/**
	 * Suggest strings with tooltips asynchronously
	 * @param suggestions function providing the strings with tooltips asynchronously
	 * @return an {@link ArgumentSuggestions} object suggesting the result of the asynchronous function
	 */
	static ArgumentSuggestions stringsWithTooltipsAsync(Function<SuggestionInfo, CompletableFuture<IStringTooltip[]>> suggestions) {
		return (info, builder) -> suggestions
			.apply(info)
			.thenApply(stringsWithTooltips -> toSuggestions(builder, stringsWithTooltips));
	}

	/**
	 * Convert an array of strings into a brigadier {@link Suggestions} object
	 * @param builder brigadier {@link SuggestionsBuilder} object for building the suggestions
	 * @param suggestions array of strings
	 * @return a brigadier {@link Suggestions} object suggesting the array of strings
	 */
	private static Suggestions toSuggestions(SuggestionsBuilder builder, String... suggestions) {
		for(String suggestion : suggestions) {
			builder.suggest(suggestion);
		}
		return builder.build();
	}

	/**
	 * Convert an array of strings with tooltips into a brigadier {@link Suggestions} object
	 * @param builder brigadier {@link SuggestionsBuilder} object for building the suggestions
	 * @param suggestions array of strings with tooltips
	 * @return a brigadier {@link Suggestions} object suggesting the array of strings with tooltips
	 */
	private static Suggestions toSuggestions(SuggestionsBuilder builder, IStringTooltip... suggestions) {
		for(IStringTooltip suggestion : suggestions) {
			if(suggestion.getTooltip() == null) {
				builder.suggest(suggestion.getSuggestion());
			} else {
				builder.suggest(suggestion.getSuggestion(), new LiteralMessage(suggestion.getTooltip()));
			}
		}
		return builder.build();
	}

	/**
	 * Wrap a value in a {@link CompletableFuture}
	 * @param value the value
	 * @param <T> type of the value
	 * @return a {@link CompletableFuture} resolving instantly in the value
	 */
	private static <T> CompletableFuture<T> future(T value) {
		return CompletableFuture.completedFuture(value);
	}

}
