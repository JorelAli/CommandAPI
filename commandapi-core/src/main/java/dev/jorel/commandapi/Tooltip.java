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

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.Message;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * This class represents a suggestion for an argument with a hover tooltip text
 * for that suggestion. This class is parameterized over some object S that
 * represents the safe cast type for argument suggestions. This class is to be
 * used with safe suggestion overrides, via the
 * SafeOverrideableArgument.safeOverrideSuggestionsT method.
 *
 * @param <S> the object that the argument suggestions use
 */
public class Tooltip<S> {

	private final S object;
	private final Message tooltipMessage;

	protected Tooltip(S object, Message tooltip) {
		this.object = object;
		this.tooltipMessage = tooltip;
	}

	/**
	 * Gets the suggestion for this object
	 * @return the suggestion for this object
	 */
	public S getSuggestion() {
		return object;
	}

	/**
	 * Gets the formatted tooltip for this object
	 * @return the formatted tooltip for this object
	 */
	public Message getTooltip() {
		return tooltipMessage;
	}

	/**
	 * Constructs a <code>Tooltip&lt;S&gt;</code> with a suggestion and a tooltip
	 *
	 * @param <S> the object that the argument suggestions use
	 * @param object the suggestion to provide to the user
	 * @param tooltip    the tooltip to show to the user when they hover over the
	 *                   suggestion
	 * @return a <code>Tooltip&lt;S&gt;</code> representing this suggestion and tooltip
	 */
	public static <S> Tooltip<S> ofString(S object, String tooltip) {
		return ofMessage(object, messageFromString(tooltip));
	}

	/**
	 * Constructs a <code>Tooltip&lt;S&gt;</code> with a suggestion and a tooltip
	 * 
	 * @param <S> the object that the argument suggestions use
	 * @param object the suggestion to provide to the user
	 * @param tooltip    the tooltip to show to the user when they hover over the
	 *                   suggestion
	 * @return a <code>Tooltip&lt;S&gt;</code> representing this suggestion and tooltip
	 */
	public static <S> Tooltip<S> ofMessage(S object, Message tooltip) {
		return new Tooltip<>(object, tooltip);
	}

	/**
	 * Constructs a <code>Tooltip&lt;S&gt;</code> with a suggestion and no tooltip
	 * 
	 * @param <S> the object that the argument suggestions use
	 * @param object the suggestion to provide to the user
	 * @return a <code>Tooltip&lt;S&gt;</code> representing this suggestion
	 */
	public static <S> Tooltip<S> none(S object) {
		return new Tooltip<>(object, null);
	}

	/**
	 * Constructs a collection of {@link Tooltip <S>} objects from an array of suggestions, and no tooltips
	 *
	 * @param <S> the object that the argument suggestions use
	 * @param suggestions array of suggestions to provide to the user
	 *
	 * @return a collection of {@link Tooltip <S>} objects from the suggestions, with no tooltips
	 */
	@SafeVarargs
	public static <S> Collection<Tooltip<S>> none(S... suggestions) {
		return generate(S::toString, (s, t) -> Tooltip.none(s), suggestions);
	}

	/**
	 * Constructs a collection of {@link Tooltip <S>} objects from a collection of suggestions, and no tooltips
	 *
	 * @param <S> the object that the argument suggestions use
	 * @param suggestions collection of suggestions to provide to the user
	 *
	 * @return a collection of {@link Tooltip <S>} objects from the suggestions, with no tooltips
	 */
	public static <S> Collection<Tooltip<S>> none(Collection<S> suggestions) {
		return generate(S::toString, (s, t) -> Tooltip.none(s), suggestions);

	}

	/**
	 * Constructs a collection of {@link Tooltip <S>} objects from an array of suggestions, and a function which generates a
	 * string tooltip for each suggestion
	 *
	 * @param <S> the object that the argument suggestions use
	 * @param tooltipGenerator function which returns a string tooltip for the suggestion
	 * @param suggestions array of suggestions to provide to the user
	 *
	 * @return a collection of {@link Tooltip <S>} objects from the provided suggestions, with the generated string tooltips
	 */
	@SafeVarargs
	public static <S> Collection<Tooltip<S>> generateStrings(Function<S, String> tooltipGenerator, S... suggestions) {
		return generate(tooltipGenerator, Tooltip::ofString, suggestions);
	}

	/**
	 * Constructs a collection of {@link Tooltip <S>} objects from a collection of suggestions, and a function which generates a
	 * string tooltip for each suggestion
	 *
	 * @param <S> the object that the argument suggestions use
	 * @param tooltipGenerator function which returns a string tooltip for the suggestion
	 * @param suggestions collection of suggestions to provide to the user
	 *
	 * @return a collection of {@link Tooltip <S>} objects from the provided suggestions, with the generated string tooltips
	 */
	public static <S> Collection<Tooltip<S>> generateStrings(Function<S, String> tooltipGenerator, Collection<S> suggestions) {
		return generate(tooltipGenerator, Tooltip::ofString, suggestions);
	}

	/**
	 * Constructs a collection of {@link Tooltip <S>} objects from an array of suggestions, and a function which generates a
	 * formatted tooltip for each suggestion
	 *
	 * @param <S> the object that the argument suggestions use
	 * @param tooltipGenerator function which returns a formatted tooltip for the suggestion
	 * @param suggestions array of suggestions to provide to the user
	 *
	 * @return a collection of {@link Tooltip <S>} objects from the provided suggestions, with the generated formatted
	 * 	tooltips
	 */
	@SafeVarargs
	public static <S> Collection<Tooltip<S>> generateMessages(Function<S, Message> tooltipGenerator, S... suggestions) {
		return generate(tooltipGenerator, Tooltip::ofMessage, suggestions);
	}

	/**
	 * Constructs a collection of {@link Tooltip <S>} objects from an collection of suggestions, and a function which generates a
	 * formatted tooltip for each suggestion
	 *
	 * @param <S> the object that the argument suggestions use
	 * @param tooltipGenerator function which returns a formatted tooltip for the suggestion
	 * @param suggestions collection of suggestions to provide to the user
	 *
	 * @return a collection of {@link Tooltip <S>} objects from the provided suggestions, with the generated formatted
	 * 	tooltips
	 */
	public static <S> Collection<Tooltip<S>> generateMessages(Function<S, Message> tooltipGenerator, Collection<S> suggestions) {
		return generate(tooltipGenerator, Tooltip::ofMessage, suggestions);
	}

	/**
	 * Internal base method for the other generation types, for processing arrays
	 *
	 * @param <S> the object that the argument suggestions use
	 * @param <T> the type of the tooltip
	 * @param tooltipGenerator tooltip generation function
	 * @param tooltipWrapper function which wraps suggestion and tooltip into a {@link Tooltip <S>} object
	 * @param suggestions array of suggestions to provide to the user
	 *
	 * @return a collection of {@link Tooltip <S>} objects from the provided suggestion, wrapped using the above functions
	 */
	@SafeVarargs
	protected
	static <S, T> Collection<Tooltip<S>> generate(Function<S, T> tooltipGenerator, BiFunction<S, T, Tooltip<S>> tooltipWrapper, S... suggestions) {
		return generate(tooltipGenerator, tooltipWrapper, Arrays.stream(suggestions));
	}

	/**
	 * Internal base method for the other generation types, for processing collections
	 *
	 * @param <S> the object that the argument suggestions use
	 * @param <T> the type of the tooltip
	 * @param tooltipGenerator tooltip generation function
	 * @param tooltipWrapper function which wraps suggestion and tooltip into a {@link Tooltip <S>} object
	 * @param suggestions collection of suggestions to provide to the user
	 *
	 * @return a collection of {@link Tooltip <S>} objects from the provided suggestion, wrapped using the above functions
	 */
	protected static <S, T> Collection<Tooltip<S>> generate(Function<S, T> tooltipGenerator, BiFunction<S, T, Tooltip<S>> tooltipWrapper, Collection<S> suggestions) {
		return generate(tooltipGenerator, tooltipWrapper, suggestions.stream());
	}

	/**
	 * Internal base method for the other generation types, for processing streams
	 *
	 * @param <S> the object that the argument suggestions use
	 * @param <T> the type of the tooltip
	 * @param tooltipGenerator tooltip generation function
	 * @param tooltipWrapper function which wraps suggestion and tooltip into a {@link Tooltip <S>} object
	 * @param suggestions stream of suggestions to provide to the user
	 *
	 * @return a collection of {@link Tooltip <S>} objects from the provided suggestion, wrapped using the above functions
	 */
	private static <S, T> Collection<Tooltip<S>> generate(Function<S, T> tooltipGenerator, BiFunction<S, T, Tooltip<S>> tooltipWrapper, Stream<S> suggestions) {
		return suggestions.map(suggestion -> tooltipWrapper.apply(suggestion,tooltipGenerator.apply(suggestion))).toList();
	}

	/**
	 * Constructs a <code>Tooltip&lt;S&gt;</code>[] from an array of <code>Tooltip&lt;S&gt;</code> via varargs. This
	 * method takes advantage of Java's varargs to construct a generic array
	 * parameterised over S for the purpose of type safety for the
	 * safeOverrideSuggestionsT method, because Java doesn't allow you to create generic arrays.
	 * 
	 * @param <S> the object that the argument suggestions use
	 * @param tooltips an array of <code>Tooltip&lt;S&gt;</code> to be converted into <code>Tooltip&lt;S&gt;</code>[]
	 * @return a <code>Tooltip&lt;S&gt;</code>[] from the provided <code>Tooltip&lt;S&gt;</code>
	 */
	@SafeVarargs
	public static <S> Tooltip<S>[] arrayOf(Tooltip<S>... tooltips) {
		return tooltips;
	}
	
	/**
	 * Constructs a function that maps the current <code>Tooltip&lt;S&gt;</code> into a StringTooltip,
	 * using a standard mapping function which is defined for a given argument. This
	 * method is used internally by the CommandAPI.
	 * 
	 * @param <S> the object that the argument suggestions use
	 * @param mapper a mapping function that converts an S instance into a String
	 * @return the mapping function from this tooltip into a StringTooltip
	 */
	public static <S> Function<Tooltip<S>, StringTooltip> build(Function<S, String> mapper) {
		return t -> StringTooltip.ofMessage(mapper.apply(t.object), t.tooltipMessage);
	}

	/**
	 * Converts an unformatted string to an unformatted tooltip by wrapping as with a {@link LiteralMessage}.
	 *
	 * If formatting is required, please see the various other {@code messageFromXXX} methods.
	 *
	 * @param string unformatted string tooltip
	 * @return wrapped tooltip as a {@link LiteralMessage}
	 */
	public static Message messageFromString(String string) {
		return new LiteralMessage(string);
	}

}
