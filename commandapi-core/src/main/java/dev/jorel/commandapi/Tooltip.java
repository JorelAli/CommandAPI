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
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;

import java.util.function.BiFunction;
import java.util.function.Function;

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
	private final Message tooltip;

	 protected Tooltip(S object, Message tooltip) {
		this.object = object;
		this.tooltip = tooltip;
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
		return tooltip;
	}

	/**
	 * Constructs a <code>Tooltip&lt;S&gt;</code> with a suggestion and a tooltip
	 *
	 * @param <S> the object that the argument suggestions use
	 * @param object the suggestion to provide to the user
	 * @param tooltip    the tooltip to show to the user when they hover over the
	 *                   suggestion
	 * @return a <code>Tooltip&lt;S&gt;</code> representing this suggestion and tooltip
	 *
	 * @deprecated Please use {@link Tooltip#ofString(Object, String)} instead
	 */
	@Deprecated
	public static <S> Tooltip<S> of(S object, String tooltip) {
		return ofString(object, tooltip);
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
		return new Tooltip<S>(object, tooltip);
	}

	/**
	 * Constructs a <code>Tooltip&lt;S&gt;</code> with a suggestion and a formatted tooltip
	 *
	 * @param <S> the object that the argument suggestions use
	 * @param object the suggestion to provide to the user
	 * @param tooltip    the formatted tooltip to show to the user when they hover over the
	 *                   suggestion
	 * @return a <code>Tooltip&lt;S&gt;</code> representing this suggestion and tooltip
	 */
	public static <S> Tooltip<S> ofBaseComponents(S object, BaseComponent... tooltip) {
		return ofMessage(object, messageFromBaseComponents(tooltip));
	}

	/**
	 * Constructs a <code>Tooltip&lt;S&gt;</code> with a suggestion and a formatted tooltip
	 *
	 * @param <S> the object that the argument suggestions use
	 * @param object the suggestion to provide to the user
	 * @param tooltip    the formatted tooltip to show to the user when they hover over the
	 *                   suggestion
	 * @return a <code>Tooltip&lt;S&gt;</code> representing this suggestion and tooltip
	 */
	public static <S> Tooltip<S> ofAdventureComponent(S object, Component tooltip) {
		return ofMessage(object, messageFromAdventureComponent(tooltip));
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
	 * Constructs an array of {@link Tooltip<S>} objects from an array of suggestions, and no tooltips
	 *
	 * @param <S> the object that the argument suggestions use
	 * @param suggestions array of suggestions to provide to the user
	 * @return an array of {@link Tooltip<S>} objects from the suggestions, with no tooltips
	 */
	@SafeVarargs
	public static <S> Tooltip<S>[] none(S... suggestions) {
		return generate(S::toString, (s, t) -> Tooltip.none(s), suggestions);
	}

	/**
	 * Constructs an array of {@link Tooltip<S>} objects from an array of suggestions,
	 * and a function which generates a string tooltip for each suggestion
	 *
	 * @param <S> the object that the argument suggestions use
	 * @param tooltipGenerator function which returns a string tooltip for the suggestion
	 * @param suggestions array of suggestions to provide to the user
	 * @return an array of {@link Tooltip<S>} objects from the provided suggestions, with the generated string tooltips
	 */
	@SafeVarargs
	public static <S> Tooltip<S>[] generateStrings(Function<S, String> tooltipGenerator, S... suggestions) {
		return generate(tooltipGenerator, Tooltip::ofString, suggestions);
	}

	/**
	 * Constructs an array of {@link Tooltip<S>} objects from an array of suggestions,
	 * and a function which generates a formatted tooltip for each suggestion
	 *
	 * @param <S> the object that the argument suggestions use
	 * @param tooltipGenerator function which returns a formatted tooltip for the suggestion
	 * @param suggestions array of suggestions to provide to the user
	 * @return an array of {@link Tooltip<S>} objects from the provided suggestions, with the generated formatted tooltips
	 */
	@SafeVarargs
	public static <S> Tooltip<S>[] generateMessages(Function<S, Message> tooltipGenerator, S... suggestions) {
		return generate(tooltipGenerator, Tooltip::ofMessage, suggestions);
	}

	/**
	 * Constructs an array of {@link Tooltip<S>} objects from an array of suggestions,
	 * and a function which generates a formatted tooltip for each suggestion
	 *
	 * @param <S> the object that the argument suggestions use
	 * @param tooltipGenerator function which returns a formatted tooltip for the suggestion
	 * @param suggestions array of suggestions to provide to the user
	 * @return an array of {@link Tooltip<S>} objects from the provided suggestions, with the generated formatted tooltips
	 */
	@SafeVarargs
	public static <S> Tooltip<S>[] generateBaseComponents(Function<S, BaseComponent[]> tooltipGenerator, S... suggestions) {
		return generate(tooltipGenerator, Tooltip::ofBaseComponents, suggestions);
	}

	/**
	 * Constructs an array of {@link Tooltip<S>} objects from an array of suggestions,
	 * and a function which generates a formatted tooltip for each suggestion
	 *
	 * @param <S> the object that the argument suggestions use
	 * @param tooltipGenerator function which returns a formatted tooltip for the suggestion
	 * @param suggestions array of suggestions to provide to the user
	 * @return an array of {@link Tooltip<S>} objects from the provided suggestions, with the generated formatted tooltips
	 */
	@SafeVarargs
	public static <S> Tooltip<S>[] generateAdvenureComponents(Function<S, Component> tooltipGenerator, S... suggestions) {
		return generate(tooltipGenerator, Tooltip::ofAdventureComponent, suggestions);
	}

	/**
	 * Internal base method for the other generation types
	 *
	 * @param <S> the object that the argument suggestions use
	 * @param <T> the type of the tooltip
	 * @param tooltipGenerator tooltip generation function
	 * @param tooltipWrapper function which wraps suggestion and tooltip into a {@link Tooltip<S>} object
	 * @param suggestions array of suggestions to provide to the user
	 * @return an array of {@link Tooltip<S>} objects from the provided suggestion, wrapped using the above functions
	 */
	@SafeVarargs
	private static <S, T> Tooltip<S>[] generate(Function<S, T> tooltipGenerator, BiFunction<S, T, Tooltip<S>> tooltipWrapper, S... suggestions) {
		Tooltip<?>[] tooltips = new Tooltip<?>[suggestions.length];
		for(int i = 0; i < suggestions.length; i++) {
			S suggestion = suggestions[i];
			tooltips[i] = tooltipWrapper.apply(suggestion, tooltipGenerator.apply(suggestion));
		}
		return (Tooltip<S>[]) tooltips;
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
		return t -> StringTooltip.ofMessage(mapper.apply(t.object), t.tooltip);
	}

	/**
	 * Converts an unformatted string to an unformatted tooltip by wrapping as with a {@link LiteralMessage}.
	 *
	 * If formatting is required, please see {@link #messageFromBaseComponents(BaseComponent...)},
	 * or consider using the more modern adventure text api.
	 *
	 * @param string unformatted string tooltip
	 * @return wrapped tooltip as a {@link LiteralMessage}
	 */
	public static Message messageFromString(String string) {
		return new LiteralMessage(string);
	}

	/**
	 * Converts a formatted bungee text component to a native minecraft text component which can be used natively by brigadier.
	 *
	 * This supports all forms of formatting including entity selectors, scores,
	 * click &amp; hover events, translations, keybinds and more.
	 *
	 * Note: the bungee component api is deprecated, and the adventure text component api should be used instead
	 *
	 * @param components array of bungee text components
	 * @return native minecraft message object which can be used natively by brigadier.
	 */
	public static Message messageFromBaseComponents(BaseComponent... components) {
		return CommandAPIHandler.getInstance().getNMS().generateMessageFromJson(ComponentSerializer.toString(components));
	}

	/**
	 * Converts a formatted adventure text component to a native minecraft text component which can be used natively by brigadier.
	 *
	 * This supports all forms of formatting including entity selectors, scores,
	 * click &amp; hover events, translations, keybinds and more.
	 **
	 * @param component adventure text component
	 * @return native minecraft message object which can be used natively by brigadier.
	 */
	public static Message messageFromAdventureComponent(Component component) {
		return CommandAPIHandler.getInstance().getNMS().generateMessageFromJson(GsonComponentSerializer.gson().serialize(component));
	}

}
