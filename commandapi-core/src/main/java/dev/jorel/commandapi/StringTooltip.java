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

import java.util.function.Function;

/**
 * A string-based tooltip interface that includes a string suggestion and a string tooltip text to display when hovering
 * over the suggestion
 */
public interface StringTooltip extends Tooltip<String> {

	static StringTooltip none(String suggestion) {
		return of(suggestion, (Message) null);
	}

	static StringTooltip of(String suggestion, String tooltip) {
		return of(suggestion, Tooltip.toMessage(tooltip));
	}
	static StringTooltip of(String suggestion, Message message) {
		return new StringTooltip() {

			@Override
			public String getSuggestion() {
				return suggestion;
			}

			@Override
			public Message getTooltip() {
				return message;
			}
		};
	}

	/**
	 * Constructs a {@link StringTooltip} with a suggestion and a formatted tooltip
	 *
	 * @param suggestion the suggestion to provide to the user
	 * @param components bungee chat components for the formatted tooltip
	 *
	 * @return a {@link StringTooltip} representing this suggestion and tooltip
	 */
	static StringTooltip of(String suggestion, BaseComponent... components) {
		return of(suggestion, Tooltip.toMessage(components));
	}

	static StringTooltip[] none(String... suggestions) {
		StringTooltip[] tooltips = new StringTooltip[suggestions.length];
		for(int i = 0; i < suggestions.length; i++) {
			tooltips[i] = none(suggestions[i]);
		}
		return tooltips;
	}

	static StringTooltip[] ofStrings(Function<String, String> mapper, String... suggestions) {
		StringTooltip[] tooltips = new StringTooltip[suggestions.length];
		for(int i = 0; i < suggestions.length; i++) {
			tooltips[i] = of(suggestions[i], mapper.apply(suggestions[i]));
		}
		return tooltips;
	}
	static StringTooltip[] ofMessages(Function<String, Message> mapper, String... suggestions) {
		StringTooltip[] tooltips = new StringTooltip[suggestions.length];
		for(int i = 0; i < suggestions.length; i++) {
			tooltips[i] = of(suggestions[i], mapper.apply(suggestions[i]));
		}
		return tooltips;
	}

	/**
	 * Constructs a {@link StringTooltip}[] from an array of suggestions, and a function mapping a suggestion to a
	 * formatted tooltip using bungee chat components.
	 *
	 * @param suggestions the suggestions to provide to the user
	 * @param mapper a function mapping a suggestion onto its formatted tooltip using bungee components
	 *
	 * @return a {@link StringTooltip}[] representing the suggestions with tooltips
	 */
	static StringTooltip[] ofComponents(Function<String, BaseComponent[]> mapper, String... suggestions) {
		StringTooltip[] tooltips = new StringTooltip[suggestions.length];
		for(int i = 0; i < suggestions.length; i++) {
			tooltips[i] = of(suggestions[i], mapper.apply(suggestions[i]));
		}
		return tooltips;

	}

}
