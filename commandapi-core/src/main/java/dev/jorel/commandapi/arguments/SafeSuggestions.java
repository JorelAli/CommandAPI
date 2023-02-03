package dev.jorel.commandapi.arguments;

import dev.jorel.commandapi.Tooltip;
import dev.jorel.commandapi.IStringTooltip;
import dev.jorel.commandapi.SuggestionInfo;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * This class represents safe suggestions. These are parameterized suggestions which can be converted
 * into a string under a mapping function.
 * {@link SafeSuggestions} instances are best created using the static methods as opposed to functionally.
 * @param <S> the type of the suggestions
 */
@FunctionalInterface
public interface SafeSuggestions<S, CommandSender> {

	/**
	 * Convert this {@link SafeSuggestions} object into an {@link ArgumentSuggestions} by mapping the values with a string
	 * mapping function.
	 *
	 * @param mapper a function which maps an instance of {@link S} to a string.
	 *
	 * @return an {@link ArgumentSuggestions} object resulting from this mapping.
	 */
	ArgumentSuggestions<CommandSender> toSuggestions(Function<S, String> mapper);

	/**
	 * Create an empty SafeSuggestions object.
	 *
	 * @param <T> type parameter of the SafeSuggestions object
	 *
	 * @return a SafeSuggestions object resulting in empty suggestions
	 */
	static <T, CommandSender> SafeSuggestions<T, CommandSender> empty() {
		return mapper -> ArgumentSuggestions.empty();
	}

	/**
	 * Hardcode an array of values to suggest
	 *
	 * @param suggestions array of hardcoded values
	 * @param <T> type of the values
	 *
	 * @return a SafeSuggestions object suggesting the hardcoded suggestions
	 */
	@SafeVarargs
	static <T, CommandSender> SafeSuggestions<T, CommandSender> suggest(T... suggestions) {
		return mapper -> ArgumentSuggestions.strings(toStrings(mapper, suggestions));
	}

	/**
	 * Hardcode a collection of values to suggest
	 *
	 * @param suggestions collection of hardcoded values
	 * @param <T> type of the values
	 *
	 * @return a SafeSuggestions object suggesting the hardcoded suggestions
	 */
	static <T, CommandSender> SafeSuggestions<T, CommandSender> suggest(Collection<T> suggestions) {
		return mapper -> ArgumentSuggestions.strings(toStrings(mapper, suggestions));
	}

	/**
	 * Suggest an array of values as the result of a function
	 *
	 * @param suggestions function providing the array of values
	 * @param <T> type of the values
	 *
	 * @return a SafeSuggestion object suggesting the result of the function
	 */
	static <T, CommandSender> SafeSuggestions<T, CommandSender> suggest(Function<SuggestionInfo<CommandSender>, T[]> suggestions) {
		return mapper -> ArgumentSuggestions.stringCollection(info -> toStrings(mapper, suggestions.apply(info)));
	}

	/**
	 * Suggest a collection of values as the result of a function
	 *
	 * @param suggestions function providing the collection of values
	 * @param <T> type of the values
	 *
	 * @return a SafeSuggestion object suggesting the result of the function
	 */
	static <T, CommandSender> SafeSuggestions<T, CommandSender> suggestCollection(Function<SuggestionInfo<CommandSender>, Collection<T>> suggestions) {
		return mapper -> ArgumentSuggestions.stringCollection(info -> toStrings(mapper, suggestions.apply(info)));
	}

	/**
	 * Suggest an array of values provided asynchronously
	 *
	 * @param suggestions function providing the array of values asynchronously
	 * @param <T> type of the values
	 *
	 * @return a SafeSuggestion object suggestion the result of the asynchronous function
	 */
	static <T, CommandSender> SafeSuggestions<T, CommandSender> suggestAsync(Function<SuggestionInfo<CommandSender>, CompletableFuture<T[]>> suggestions) {
		return mapper -> ArgumentSuggestions.stringCollectionAsync(info -> suggestions
			.apply(info)
			.thenApply(items -> toStrings(mapper, items)));
	}

	/**
	 * Suggest a collection of values provided asynchronously
	 *
	 * @param suggestions function providing the collection of values asynchronously
	 * @param <T> type of the values
	 *
	 * @return a SafeSuggestion object suggestion the result of the asynchronous function
	 */
	static <T, CommandSender> SafeSuggestions<T, CommandSender> suggestCollectionAsync(Function<SuggestionInfo<CommandSender>, CompletableFuture<Collection<T>>> suggestions) {
		return mapper -> ArgumentSuggestions.stringCollectionAsync(info -> suggestions
			.apply(info)
			.thenApply(items -> toStrings(mapper, items)));
	}

	/**
	 * Suggest an array of hardcoded values with tooltips
	 *
	 * @param suggestions array of hardcoded values with their tooltips
	 * @param <T> type of the values
	 *
	 * @return a SafeSuggestion object suggesting the hardcoded values
	 */
	@SafeVarargs
	static <T, CommandSender> SafeSuggestions<T, CommandSender> tooltips(Tooltip<T>... suggestions) {
		return mapper -> ArgumentSuggestions.stringsWithTooltips(toStringsWithTooltips(mapper, suggestions));
	}

	/**
	 * Suggest a collection of hardcoded values with tooltips
	 *
	 * @param suggestions collection of hardcoded values with their tooltips
	 * @param <T> type of the values
	 *
	 * @return a SafeSuggestion object suggesting the hardcoded values
	 */
	static <T, CommandSender> SafeSuggestions<T, CommandSender> tooltips(Collection<Tooltip<T>> suggestions) {
		return mapper -> ArgumentSuggestions.stringsWithTooltips(toStringsWithTooltips(mapper, suggestions));
	}

	/**
	 * Suggest an array of values with tooltips as the result of a function
	 *
	 * @param suggestions function providing the array of values with tooltips
	 * @param <T> type of the values
	 *
	 * @return a SafeSuggestion object suggesting the result of the function
	 */
	static <T, CommandSender> SafeSuggestions<T, CommandSender> tooltips(Function<SuggestionInfo<CommandSender>, Tooltip<T>[]> suggestions) {
		return mapper -> ArgumentSuggestions.stringsWithTooltipsCollection(info -> toStringsWithTooltips(mapper,
			suggestions.apply(info)
		));
	}

	/**
	 * Suggest a collection of values with tooltips as the result of a function
	 *
	 * @param suggestions function providing the collection of values with tooltips
	 * @param <T> type of the values
	 *
	 * @return a SafeSuggestion object suggesting the result of the function
	 */
	static <T, CommandSender> SafeSuggestions<T, CommandSender> tooltipCollection(Function<SuggestionInfo<CommandSender>, Collection<Tooltip<T>>> suggestions) {
		return mapper -> ArgumentSuggestions.stringsWithTooltipsCollection(info -> toStringsWithTooltips(mapper,
			suggestions.apply(info)
		));
	}

	/**
	 * Suggest an array of values with tooltips asynchronously
	 *
	 * @param suggestions function providing the collection of values with tooltips asynchronously
	 * @param <T> type of the values
	 *
	 * @return a SafeSuggestion suggesting the result of the asynchronous function
	 */
	static <T, CommandSender> SafeSuggestions<T, CommandSender> tooltipsAsync(Function<SuggestionInfo<CommandSender>, CompletableFuture<Tooltip<T>[]>> suggestions) {
		return mapper -> ArgumentSuggestions.stringsWithTooltipsCollectionAsync(info -> suggestions
			.apply(info)
			.thenApply(items -> toStringsWithTooltips(mapper, items)));
	}

	/**
	 * Suggest a collection of values with tooltips asynchronously
	 *
	 * @param suggestions function providing the array of values with tooltips asynchronously
	 * @param <T> type of the values
	 *
	 * @return a SafeSuggestion suggesting the result of the asynchronous function
	 */
	static <T, CommandSender> SafeSuggestions<T, CommandSender> tooltipCollectionAsync(Function<SuggestionInfo<CommandSender>, CompletableFuture<Collection<Tooltip<T>>>> suggestions) {
		return mapper -> ArgumentSuggestions.stringsWithTooltipsCollectionAsync(info -> suggestions
			.apply(info)
			.thenApply(items -> toStringsWithTooltips(mapper, items)));
	}

	/**
	 * Convert an array of values into a collection of strings using the mapping function
	 *
	 * @param mapper the mapping function
	 * @param suggestions the array of values
	 * @param <T> type of the values
	 *
	 * @return array of strings representing the array of values under the mapping function
	 */
	@SafeVarargs
	private static <T> Collection<String> toStrings(Function<T, String> mapper, T... suggestions) {
		return Arrays.stream(suggestions).map(mapper).toList();
	}

	/**
	 * Convert a collection of values into a collection of strings using the mapping function
	 *
	 * @param mapper the mapping function
	 * @param suggestions the collection of values
	 * @param <T> type of the values
	 *
	 * @return collection of strings representing the collection of values under the mapping function
	 */
	private static <T> Collection<String> toStrings(Function<T, String> mapper, Collection<T> suggestions) {
		return suggestions.stream().map(mapper).toList();
	}


	/**
	 * Convert an array of values with tooltips into an array of strings with tooltips using the mapping function
	 *
	 * @param mapper the mapping function
	 * @param suggestions the array of values with tooltips
	 * @param <T> type of the values
	 *
	 * @return array of strings with tooltips representing the array of values with tooltips under the mapping function
	 */
	@SafeVarargs
	private static <T> Collection<IStringTooltip> toStringsWithTooltips(Function<T, String> mapper, Tooltip<T>... suggestions) {
		return toStringsWithTooltips(mapper, Arrays.stream(suggestions));
	}

	/**
	 * Convert a collection of values with tooltips into a collection of strings with tooltips using the mapping function
	 *
	 * @param mapper the mapping function
	 * @param suggestions the collection of values with tooltips
	 * @param <T> type of the values
	 *
	 * @return collection of strings with tooltips representing the collection of values with tooltips under the mapping function
	 */

	private static <T> Collection<IStringTooltip> toStringsWithTooltips(Function<T, String> mapper, Collection<Tooltip<T>> suggestions) {
		return toStringsWithTooltips(mapper, suggestions.stream());
	}

	/**
	 * Convert a stream of values with tooltips into a collection of strings with tooltips using the mapping function
	 *
	 * @param mapper the mapping function
	 * @param suggestions the stream of values with tooltips
	 * @param <T> type of the values
	 *
	 * @return collection of strings with tooltips representing the collection of values with tooltips under the mapping function
	 */

	private static <T> Collection<IStringTooltip> toStringsWithTooltips(Function<T, String> mapper, Stream<Tooltip<T>> suggestions) {
		//Note the ::apply is required to allow the return type to be IStringTooltip instead of StringTooltip
		Function<Tooltip<T>, IStringTooltip> builder = Tooltip.build(mapper)::apply;
		return suggestions.map(builder).toList();
	}

}
