package dev.jorel.commandapi.arguments;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.jorel.commandapi.IStringTooltip;
import dev.jorel.commandapi.SuggestionInfo;

import java.util.Collection;
import java.util.Locale;
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
public interface ArgumentSuggestions<CommandSender> {

    /**
     * Create a {@link CompletableFuture} resolving onto a brigadier {@link Suggestions} object.
     * @param info The suggestions info
     * @param builder The Brigadier {@link SuggestionsBuilder} object
     * @return a {@link CompletableFuture} resolving onto a brigadier {@link Suggestions} object.
     *
     * @throws CommandSyntaxException if there is an error making suggestions
     */
    CompletableFuture<Suggestions> suggest(SuggestionInfo<CommandSender> info, SuggestionsBuilder builder)
        throws CommandSyntaxException;
/* ANCHOR_END: Declaration */

	/**
	 * Suggest nothing
	 * @return an {@link ArgumentSuggestions} object suggesting nothing.
	 */
	static <CommandSender> ArgumentSuggestions<CommandSender> empty() {
		return (info, builder) -> builder.buildFuture();
	}

	/**
	 * Suggest hardcoded strings
	 *
	 * @param suggestions array of hardcoded strings
	 *
	 * @return an {@link ArgumentSuggestions} object suggesting hardcoded strings
	 */
	static <CommandSender> ArgumentSuggestions<CommandSender> strings(String... suggestions) {
		return (info, builder) -> future(suggestionsFromStrings(builder, suggestions));
	}

	/**
	 * Suggest hardcoded strings
	 *
	 * @param suggestions collection of hardcoded strings
	 *
	 * @return an {@link ArgumentSuggestions} object suggesting hardcoded strings
	 */
	static <CommandSender> ArgumentSuggestions<CommandSender> strings(Collection<String> suggestions) {
		return (info, builder) -> future(suggestionsFromStrings(builder, suggestions));
	}

	/**
	 * Suggest an array of strings as the result of a function
	 *
	 * @param suggestions function providing the strings as an array
	 *
	 * @return an {@link ArgumentSuggestions} object suggesting the result of the function
	 */
	static <CommandSender> ArgumentSuggestions<CommandSender> strings(Function<SuggestionInfo<CommandSender>, String[]> suggestions) {
		return (info, builder) -> future(suggestionsFromStrings(builder, suggestions.apply(info)));
	}

	/**
	 * Suggest a collection of strings as the result of a function
	 *
	 * @param suggestions function providing the strings as a collection
	 *
	 * @return an {@link ArgumentSuggestions} object suggesting the result of the function
	 */
	static <CommandSender> ArgumentSuggestions<CommandSender> stringCollection(Function<SuggestionInfo<CommandSender>, Collection<String>> suggestions) {
		return (info, builder) -> future(suggestionsFromStrings(builder, suggestions.apply(info)));
	}

	/**
	 * Suggest an array of strings asynchronously
	 *
	 * @param suggestions function providing the array of strings asynchronously
	 *
	 * @return an {@link ArgumentSuggestions} object suggesting the result of the asynchronous function
	 */
	static <CommandSender> ArgumentSuggestions<CommandSender> stringsAsync(Function<SuggestionInfo<CommandSender>, CompletableFuture<String[]>> suggestions) {
		return (info, builder) -> suggestions
			.apply(info)
			.thenApply(strings -> suggestionsFromStrings(builder, strings));
	}

	/**
	 * Suggest a collection of strings asynchronously
	 *
	 * @param suggestions function providing the collection of strings asynchronously
	 *
	 * @return an {@link ArgumentSuggestions} object suggesting the result of the asynchronous function
	 */
	static <CommandSender> ArgumentSuggestions<CommandSender> stringCollectionAsync(Function<SuggestionInfo<CommandSender>, CompletableFuture<Collection<String>>> suggestions) {
		return (info, builder) -> suggestions
			.apply(info)
			.thenApply(strings -> suggestionsFromStrings(builder, strings));
	}

	/**
	 * Suggest hardcoded strings with tooltips
	 *
	 * @param suggestions collection of hardcoded strings with tooltips
	 *
	 * @return an {@link ArgumentSuggestions} object suggesting the hardcoded strings with tooltips
	 */
	static <CommandSender> ArgumentSuggestions<CommandSender> stringsWithTooltips(IStringTooltip... suggestions) {
		return (info, builder) -> future(suggestionsFromTooltips(builder, suggestions));
	}

	/**
	 * Suggest hardcoded strings with tooltips
	 *
	 * @param suggestions collection of hardcoded strings with tooltips
	 *
	 * @return an {@link ArgumentSuggestions} object suggesting the hardcoded strings with tooltips
	 */
	static <CommandSender> ArgumentSuggestions<CommandSender> stringsWithTooltips(Collection<IStringTooltip> suggestions) {
		return (info, builder) -> future(suggestionsFromTooltips(builder, suggestions));
	}

	/**
	 * Suggest an array of strings with tooltips as the result of a function
	 *
	 * @param suggestions function providing the array of strings with tooltips
	 *
	 * @return an {@link ArgumentSuggestions} object suggesting the result of the function
	 */
	static <CommandSender> ArgumentSuggestions<CommandSender> stringsWithTooltips(Function<SuggestionInfo<CommandSender>, IStringTooltip[]> suggestions) {
		return (info, builder) -> future(suggestionsFromTooltips(builder, suggestions.apply(info)));
	}

	/**
	 * Suggest a collection of strings with tooltips as the result of a function
	 *
	 * @param suggestions function providing the collection of strings with tooltips
	 *
	 * @return an {@link ArgumentSuggestions} object suggesting the result of the function
	 */
	static <CommandSender> ArgumentSuggestions<CommandSender> stringsWithTooltipsCollection(Function<SuggestionInfo<CommandSender>, Collection<IStringTooltip>> suggestions) {
		return (info, builder) -> future(suggestionsFromTooltips(builder, suggestions.apply(info)));
	}

	/**
	 * Suggest an array of strings with tooltips asynchronously
	 *
	 * @param suggestions function providing the array of strings with tooltips asynchronously
	 *
	 * @return an {@link ArgumentSuggestions} object suggesting the result of the asynchronous function
	 */
	static <CommandSender> ArgumentSuggestions<CommandSender> stringsWithTooltipsAsync(Function<SuggestionInfo<CommandSender>, CompletableFuture<IStringTooltip[]>> suggestions) {
		return (info, builder) -> suggestions
			.apply(info)
			.thenApply(stringsWithTooltips -> suggestionsFromTooltips(builder, stringsWithTooltips));
	}

	/**
	 * Suggest a collection of strings with tooltips asynchronously
	 *
	 * @param suggestions function providing the collection of strings with tooltips asynchronously
	 *
	 * @return an {@link ArgumentSuggestions} object suggesting the result of the asynchronous function
	 */
	static <CommandSender> ArgumentSuggestions<CommandSender> stringsWithTooltipsCollectionAsync(Function<SuggestionInfo<CommandSender>, CompletableFuture<Collection<IStringTooltip>>> suggestions) {
		return (info, builder) -> suggestions
			.apply(info)
			.thenApply(stringsWithTooltips -> suggestionsFromTooltips(builder, stringsWithTooltips));
	}

	/**
	 * Merge suggestions from multiple {@link ArgumentSuggestions} together
	 * @param suggestions The {@link ArgumentSuggestions} to be merged
	 * @return an {@link ArgumentSuggestions} object suggesting everything suggested by the input suggestions
	 */
	@SafeVarargs
	static <CommandSender> ArgumentSuggestions<CommandSender> merge(ArgumentSuggestions<CommandSender>... suggestions) {
		return (info, builder) -> {
			for(ArgumentSuggestions<CommandSender> suggestion : suggestions) {
				suggestion.suggest(info, builder);
			}
			return builder.buildFuture();
		};
	}

	/**
	 * Convert an array of strings into a brigadier {@link Suggestions} object
	 *
	 * @param builder brigadier {@link SuggestionsBuilder} object for building the suggestions
	 * @param suggestions array of strings
	 *
	 * @return a brigadier {@link Suggestions} object suggesting the array of strings
	 */
	private static Suggestions suggestionsFromStrings(SuggestionsBuilder builder, String... suggestions) {
		for(String suggestion : suggestions) {
			if(shouldSuggest(builder, suggestion)) {
				builder.suggest(suggestion);
			}
		}
		return builder.build();
	}

	/**
	 * Convert a collection of strings into a brigadier {@link Suggestions} object
	 *
	 * @param builder brigadier {@link SuggestionsBuilder} object for building the suggestions
	 * @param suggestions collection of strings
	 *
	 * @return a brigadier {@link Suggestions} object suggesting the collection of strings
	 */
	private static Suggestions suggestionsFromStrings(SuggestionsBuilder builder, Collection<String> suggestions) {
		for(String suggestion : suggestions) {
			if(shouldSuggest(builder, suggestion)) {
				builder.suggest(suggestion);
			}
		}
		return builder.build();
	}

	/**
	 * Convert an array of strings with tooltips into a brigadier {@link Suggestions} object
	 *
	 * @param builder brigadier {@link SuggestionsBuilder} object for building the suggestions
	 * @param suggestions array of strings with tooltips
	 *
	 * @return a brigadier {@link Suggestions} object suggesting the array of strings with tooltips
	 */
	private static Suggestions suggestionsFromTooltips(SuggestionsBuilder builder, IStringTooltip... suggestions) {
		for(IStringTooltip suggestion : suggestions) {
			processSuggestion(builder, suggestion);
		}
		return builder.build();
	}

	/**
	 * Convert a collection of strings with tooltips into a brigadier {@link Suggestions} object
	 *
	 * @param builder brigadier {@link SuggestionsBuilder} object for building the suggestions
	 * @param suggestions collection of strings with tooltips
	 *
	 * @return a brigadier {@link Suggestions} object suggesting the collection of strings with tooltips
	 */
	private static Suggestions suggestionsFromTooltips(SuggestionsBuilder builder, Collection<IStringTooltip> suggestions) {
		for(IStringTooltip suggestion : suggestions) {
			processSuggestion(builder, suggestion);
		}
		return builder.build();
	}

	private static void processSuggestion(SuggestionsBuilder builder, IStringTooltip suggestion) {
		if(!shouldSuggest(builder, suggestion.getSuggestion())) {
			return;
		}

		if(suggestion.getTooltip() == null) {
			builder.suggest(suggestion.getSuggestion());
		} else {
			builder.suggest(suggestion.getSuggestion(), suggestion.getTooltip());
		}
	}

	/**
	 * Returns whether the typed text should be suggested by the current suggestion
	 *
	 * @param builder SuggestionsBuilder object
	 * @param suggestion string suggestion
	 *
	 * @return true if the current input is a prefix of the suggestion, false otherwise
	 */
	private static boolean shouldSuggest(SuggestionsBuilder builder, String suggestion) {
		return suggestion.toLowerCase(Locale.ROOT).startsWith(builder.getRemaining().toLowerCase(Locale.ROOT));
	}

	/**
	 * Wrap a value in a {@link CompletableFuture}
	 *
	 * @param value the value
	 * @param <T> type of the value
	 *
	 * @return a {@link CompletableFuture} resolving instantly in the value
	 */
	private static <T> CompletableFuture<T> future(T value) {
		return CompletableFuture.completedFuture(value);
	}

}
