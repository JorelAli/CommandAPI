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

import com.mojang.brigadier.Message;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.function.Function;

/**
 * This class represents a suggestion for an argument with a hover tooltip text
 * for that suggestion. This class is parameterized over some object S that
 * represents the safe cast type for argument suggestions. This class is to be
 * used with safe suggestion overrides, via the
 * {@link dev.jorel.commandapi.arguments.SafeSuggestions#tooltips(Tooltip[])} family of methods.
 *
 * @param <S> the object that the argument suggestions use
 */
public interface Tooltip<S> {

	/**
	 * The value suggested to the player
	 * @return The value suggested to the player
	 */
	S getSuggestion();

	/**
	 * The tooltip shown to the player
	 * @return The tooltip shown to the player
	 */
	Message getTooltip();

	/**
	 * Constructs a {@link Tooltip<S>} with a suggestion and no tooltip
	 *
	 * @param <S> the object that the argument suggestions use
	 * @param suggestion the suggestion to provide to the user
	 * @return a {@link Tooltip<S>} representing this suggestion
	 */
	static <S> Tooltip<S> none(S suggestion) {
		return of(suggestion, (Message) null);
	}

	/**
	 * Constructs a {@link Tooltip<S>} with a suggestion and a tooltip
	 *
	 * @param <S> the object that the argument suggestions use
	 * @param suggestion the suggestion to provide to the user
	 * @param tooltip    the tooltip to show to the user when they hover over the
	 *                   suggestion
	 * @return a {@link Tooltip<S>} representing this suggestion and tooltip
	 */
	static <S> Tooltip<S> of(S suggestion, String tooltip) {
		return of(suggestion, messageFrom(tooltip));
	}

	/**
	 * Constructs a {@link Tooltip<S>} with a suggestion and a formatted tooltip
	 *
	 * @param <S> the object that the argument suggestions use
	 * @param suggestion the suggestion to provide to the user
	 * @param tooltip    the formatted tooltip to show to the user when they hover over the
	 *                   suggestion
	 * @return a {@link Tooltip<S>} representing this suggestion and tooltip
	 */
	static <S> Tooltip<S> of(S suggestion, Message tooltip) {
		return new Tooltip<>() {

			@Override
			public S getSuggestion() {
				return suggestion;
			}

			@Override
			public Message getTooltip() {
				return tooltip;
			}
		};
	}

	/**
	 * Constructs a {@link Tooltip<S>} with a suggestion and a formatted tooltip
	 *
	 * @param <S> the object that the argument suggestions use
	 * @param suggestion the suggestion to provide to the user
	 * @param components bungee chat components for the formatted tooltip
	 * @return a {@link Tooltip<S>} representing this suggestion and tooltip
	 */
	static <S> Tooltip<S> of(S suggestion, BaseComponent... components) {
		return of(suggestion, messageFrom(components));
	}

	/**
	 * Constructs a {@link Tooltip<S>}[] with no tooltips from an array of suggestions
	 *
	 * @param <S> the object that the argument suggestions use
	 * @param suggestions the suggestions to provide to the user
	 * @return a {@link Tooltip<S>}[] representing the suggestions with no tooltips
	 */
	@SafeVarargs
	@SuppressWarnings("unchecked")
	static <S> Tooltip<S>[] none(S... suggestions) {
		Tooltip<?>[] tooltips = new Tooltip<?>[suggestions.length];
		for(int i = 0; i < suggestions.length; i++) {
			tooltips[i] = none(suggestions[i]);
		}
		return (Tooltip<S>[]) tooltips;
	}

	/**
	 * Constructs a {@link Tooltip<S>}[] from an array of suggestions,
	 * and a function mapping a suggestion to a string tooltip.
	 *
	 * @param <S> the object that the argument suggestions use
	 * @param suggestions the suggestions to provide to the user
	 * @param mapper a function mapping a suggestion onto its string tooltip
	 * @return a {@link Tooltip<S>}[] representing the suggestions with tooltips
	 */
	@SafeVarargs
	@SuppressWarnings("unchecked")
	static <S> Tooltip<S>[] ofStrings(Function<S, String> mapper, S... suggestions) {
		Tooltip<?>[] tooltips = new Tooltip<?>[suggestions.length];
		for(int i = 0; i < suggestions.length; i++) {
			tooltips[i] = of(suggestions[i], mapper.apply(suggestions[i]));
		}
		return (Tooltip<S>[]) tooltips;
	}

	/**
	 * Constructs a {@link Tooltip<S>}[] from an array of suggestions,
	 * and a function mapping a suggestion to a formatted tooltip.
	 *
	 * @param <S> the object that the argument suggestions use
	 * @param suggestions the suggestions to provide to the user
	 * @param mapper a function mapping a suggestion onto its formatted tooltip
	 * @return a {@link Tooltip<S>}[] representing the suggestions with tooltips
	 */
	@SafeVarargs
	@SuppressWarnings("unchecked")
	static <S> Tooltip<S>[] ofMessages(Function<S, Message> mapper, S... suggestions) {
		Tooltip<?>[] tooltips = new Tooltip<?>[suggestions.length];
		for(int i = 0; i < suggestions.length; i++) {
			tooltips[i] = of(suggestions[i], mapper.apply(suggestions[i]));
		}
		return (Tooltip<S>[]) tooltips;
	}

	/**
	 * Constructs a {@link Tooltip<S>}[] from an array of suggestions,
	 * and a function mapping a suggestion to a formatted tooltip using bungee chat components.
	 *
	 * @param <S> the object that the argument suggestions use
	 * @param suggestions the suggestions to provide to the user
	 * @param mapper a function mapping a suggestion onto its formatted tooltip using bungee components
	 * @return a {@link Tooltip<S>}[] representing the suggestions with tooltips
	 */
	@SafeVarargs
	@SuppressWarnings("unchecked")
	static <S> Tooltip<S>[] ofComponents(Function<S, BaseComponent[]> mapper, S... suggestions) {
		Tooltip<?>[] tooltips = new Tooltip<?>[suggestions.length];
		for(int i = 0; i < suggestions.length; i++) {
			tooltips[i] = of(suggestions[i], mapper.apply(suggestions[i]));
		}
		return (Tooltip<S>[]) tooltips;
	}

	/**
	 * Constructs a {@link Tooltip<S>}[] from an array of {@link Tooltip<S>} via varargs. This
	 * method takes advantage of Java's varargs to construct a generic array
	 * parameterised over S for the purpose of type safety for the
	 * safeOverrideSuggestionsT method, because Java doesn't allow you to create generic arrays.
	 *
	 * @param <S> the object that the argument suggestions use
	 * @param tooltips an array of {@link Tooltip<S>} to be converted into {@link Tooltip<S>}[]
	 * @return a {@link Tooltip<S>}[] from the provided {@link Tooltip<S>}
	 */
	@SafeVarargs
	static <S> Tooltip<S>[] arrayOf(Tooltip<S>... tooltips) {
		return tooltips;
	}

	/**
	 * Constructs a function that maps the current {@link Tooltip<S>} into a StringTooltip,
	 * using a standard mapping function which is defined for a given argument. This
	 * method is used internally by the CommandAPI.
	 *
	 * @param <S> the object that the argument suggestions use
	 * @param mapper a mapping function that converts an S instance into a String
	 * @return the mapping function from this tooltip into a StringTooltip
	 */
	static <S> Function<Tooltip<S>, StringTooltip> build(Function<S, String> mapper) {
		return t -> StringTooltip.of(mapper.apply(t.getSuggestion()), t.getTooltip());
	}

	/**
	 * Constructs a formatted {@link Message} from the input string.
	 * This {@link Message} can then be used directly in a tooltip.
	 * This string supports formatting codes
	 *
	 * @return a {@link Message} from the input string.
	 */
	static Message messageFrom(String string) {
		return messageFrom(TextComponent.fromLegacyText(string));
	}

	/**
	 * Constructs a formatted {@link Message} from bungee chat components.
	 * This {@link Message} can then be used directly in a tooltip.
	 *
	 * @return a {@link Message} from the bungee chat components.
	 */
	static Message messageFrom(BaseComponent... components) {
		return CommandAPIHandler.getInstance().getNMS().toBrigadierMessage(components);
	}

}
