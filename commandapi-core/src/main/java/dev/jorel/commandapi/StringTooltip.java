/*******************************************************************************
 * Copyright 2018, 2020 Jorel Ali (Skepter) - MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *******************************************************************************/
package dev.jorel.commandapi;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import com.mojang.brigadier.Message;

/**
 * Represents a suggestion for an argument with a hover tooltip text for that
 * suggestion. This class is to be used with normal suggestion overrides, via
 * the <code>Argument.overrideSuggestionsT</code> method.
 */
public class StringTooltip implements IStringTooltip {

	private final String suggestion;
	private final Message tooltip;

	/**
	 * Constructs a StringTooltip with a suggestion and a string tooltip
	 *
	 * @param suggestion the suggestion to provide to the user
	 * @param tooltip    the string tooltip to show to the user when they hover over the
	 *                   suggestion
	 * @return a StringTooltip representing this suggestion and tooltip
	 */
	public static StringTooltip ofString(String suggestion, String tooltip) {
		return ofMessage(suggestion, Tooltip.messageFromString(tooltip));
	}

	/**
	 * Constructs a StringTooltip with a suggestion and a formatted tooltip
	 *
	 * @param suggestion the suggestion to provide to the user
	 * @param tooltip    the formatted tooltip to show to the user when they hover over the
	 *                   suggestion
	 * @return a StringTooltip representing this suggestion and tooltip
	 */
	public static StringTooltip ofMessage(String suggestion, Message tooltip) {
		return tooltip == null ? none(suggestion) : new StringTooltip(suggestion, tooltip);
	}

	/**
	 * Constructs a StringTooltip with a suggestion and no tooltip
	 * 
	 * @param suggestion the suggestion to provide to the user
	 * @return a StringTooltip representing this suggestion
	 */
	public static StringTooltip none(String suggestion) {
		return new StringTooltip(suggestion, null);
	}
	
	/**
	 * Constructs a collection of {@link StringTooltip} objects from an array of suggestions, and no tooltips
	 *
	 * @param suggestions array of suggestions to provide to the user
	 *
	 * @return a collection of {@link StringTooltip} objects from the suggestions, with no tooltips
	 */
	public static Collection<StringTooltip> none(String... suggestions) {
		return generate(String::toString, (s, t) -> StringTooltip.none(s), suggestions);
	}

	/**
	 * Constructs a collection of {@link StringTooltip} objects from a collection of suggestions, and no tooltips
	 *
	 * @param suggestions collection of suggestions to provide to the user
	 *
	 * @return a collection of {@link StringTooltip} objects from the suggestions, with no tooltips
	 */
	public static Collection<StringTooltip> none(Collection<String> suggestions) {
		return generate(String::toString, (s, t) -> StringTooltip.none(s), suggestions);
	}

	/**
	 * Constructs a collection of {@link StringTooltip} objects from an array of suggestions, and a function which generates
	 * a string tooltip for each suggestion
	 *
	 * @param tooltipGenerator function which returns a string tooltip for the suggestion
	 * @param suggestions array of suggestions to provide to the user
	 *
	 * @return a collection of {@link StringTooltip} objects from the provided suggestions, with the generated string
	 * 	tooltips
	 */
	public static Collection<StringTooltip> generateStrings(UnaryOperator<String> tooltipGenerator, String... suggestions) {
		return generate(tooltipGenerator, StringTooltip::ofString, suggestions);
	}

	/**
	 * Constructs a collection of {@link StringTooltip} objects from a collection of suggestions, and a function which generates
	 * a string tooltip for each suggestion
	 *
	 * @param tooltipGenerator function which returns a string tooltip for the suggestion
	 * @param suggestions collection of suggestions to provide to the user
	 *
	 * @return a collection of {@link StringTooltip} objects from the provided suggestions, with the generated string
	 * 	tooltips
	 */
	public static Collection<StringTooltip> generateStrings(UnaryOperator<String> tooltipGenerator, Collection<String> suggestions) {
		return generate(tooltipGenerator, StringTooltip::ofString, suggestions);
	}

	/**
	 * Constructs a collection of {@link StringTooltip} objects from an array of suggestions, and a function which generates
	 * a formatted tooltip for each suggestion
	 *
	 * @param tooltipGenerator function which returns a formatted tooltip for the suggestion
	 * @param suggestions array of suggestions to provide to the user
	 *
	 * @return a collection of {@link StringTooltip} objects from the provided suggestions, with the generated formatted
	 * 	tooltips
	 */
	public static Collection<StringTooltip> generateMessages(Function<String, Message> tooltipGenerator, String... suggestions) {
		return generate(tooltipGenerator, StringTooltip::ofMessage, suggestions);
	}

	/**
	 * Constructs a collection of {@link StringTooltip} objects from a collection of suggestions, and a function which generates
	 * a formatted tooltip for each suggestion
	 *
	 * @param tooltipGenerator function which returns a formatted tooltip for the suggestion
	 * @param suggestions collection of suggestions to provide to the user
	 *
	 * @return a collection of {@link StringTooltip} objects from the provided suggestions, with the generated formatted
	 * 	tooltips
	 */
	public static Collection<StringTooltip> generateMessages(Function<String, Message> tooltipGenerator, Collection<String> suggestions) {
		return generate(tooltipGenerator, StringTooltip::ofMessage, suggestions);
	}

	/**
	 * Internal base method for the other generation types
	 *
	 * @param <T> the type of the tooltip
	 * @param tooltipGenerator tooltip generation function
	 * @param tooltipWrapper function which wraps suggestion and tooltip into a {@link StringTooltip} object
	 * @param suggestions array of suggestions to provide to the user
	 * @return a collection of {@link StringTooltip} objects from the provided suggestion, wrapped using the above functions
	 */
	protected static <T> Collection<StringTooltip> generate(Function<String, T> tooltipGenerator, BiFunction<String, T, StringTooltip> tooltipWrapper, String... suggestions) {
		return generate(tooltipGenerator, tooltipWrapper, Arrays.stream(suggestions));
	}

	/**
	 * Internal base method for the other generation types
	 *
	 * @param <T> the type of the tooltip
	 * @param tooltipGenerator tooltip generation function
	 * @param tooltipWrapper function which wraps suggestion and tooltip into a {@link StringTooltip} object
	 * @param suggestions collection of suggestions to provide to the user
	 * @return a collection of {@link StringTooltip} objects from the provided suggestion, wrapped using the above functions
	 */
	protected static <T> Collection<StringTooltip> generate(Function<String, T> tooltipGenerator, BiFunction<String, T, StringTooltip> tooltipWrapper, Collection<String> suggestions) {
		return generate(tooltipGenerator, tooltipWrapper, suggestions.stream());
	}

	/**
	 * Internal base method for the other generation types
	 *
	 * @param <T> the type of the tooltip
	 * @param tooltipGenerator tooltip generation function
	 * @param tooltipWrapper function which wraps suggestion and tooltip into a {@link StringTooltip} object
	 * @param suggestions stream of suggestions to provide to the user
	 * @return a collection of {@link StringTooltip} objects from the provided suggestion, wrapped using the above functions
	 */
	protected static <T> Collection<StringTooltip> generate(Function<String, T> tooltipGenerator, BiFunction<String, T, StringTooltip> tooltipWrapper, Stream<String> suggestions) {
		Function<String, StringTooltip> builder = suggestion -> tooltipWrapper.apply(suggestion, tooltipGenerator.apply(suggestion));
		return suggestions.map(builder).toList();
	}

	protected StringTooltip(String suggestion, Message tooltip) {
		this.suggestion = suggestion;
		this.tooltip = tooltip;
	}
	
	/**
	 * Returns the current suggestion that this class holds
	 * @return the current suggestion that this class holds
	 */
	public String getSuggestion() {
		return this.suggestion;
	}
	
	/**
	 * Returns the current tooltip text that this class holds
	 * @return the current tooltip text that this class holds
	 */
	public Message getTooltip() {
		return this.tooltip;
	}
	
}
