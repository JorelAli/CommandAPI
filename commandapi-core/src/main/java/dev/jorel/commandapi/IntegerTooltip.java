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
 * A integer-based tooltip interface that includes a string suggestion and a string tooltip text to display when hovering
 * over the suggestion
 */
public interface IntegerTooltip extends Tooltip<Integer> {

	/**
	 * Suggest an integer with no tooltip
	 * @param suggestion
	 * @return
	 */
	static IntegerTooltip none(int suggestion) {
		return of(suggestion, (Message) null);
	}

	static IntegerTooltip of(int suggestion, String tooltip) {
		return of(suggestion, Tooltip.messageFrom(tooltip));
	}
	static IntegerTooltip of(int suggestion, Message message) {
		return new IntegerTooltip() {

			@Override
			public Integer getSuggestion() {
				return suggestion;
			}

			@Override
			public Message getTooltip() {
				return message;
			}
		};
	}

	/**
	 * Constructs a {@link IntegerTooltip} with a suggestion and a formatted tooltip
	 *
	 * @param suggestion the suggestion to provide to the user
	 * @param components bungee chat components for the formatted tooltip
	 *
	 * @return a {@link IntegerTooltip} representing this suggestion and tooltip
	 */
	static IntegerTooltip of(int suggestion, BaseComponent... components) {
		return of(suggestion, Tooltip.messageFrom(components));
	}

	static IntegerTooltip[] none(int... suggestions) {
		IntegerTooltip[] tooltips = new IntegerTooltip[suggestions.length];
		for(int i = 0; i < suggestions.length; i++) {
			tooltips[i] = none(suggestions[i]);
		}
		return tooltips;
	}

	static IntegerTooltip[] ofStrings(Function<Integer, String> mapper, int... suggestions) {
		IntegerTooltip[] tooltips = new IntegerTooltip[suggestions.length];
		for(int i = 0; i < suggestions.length; i++) {
			tooltips[i] = of(suggestions[i], mapper.apply(suggestions[i]));
		}
		return tooltips;
	}
	static IntegerTooltip[] ofMessages(Function<Integer, Message> mapper, int... suggestions) {
		IntegerTooltip[] tooltips = new IntegerTooltip[suggestions.length];
		for(int i = 0; i < suggestions.length; i++) {
			tooltips[i] = of(suggestions[i], mapper.apply(suggestions[i]));
		}
		return tooltips;
	}

	/**
	 * Constructs a {@link IntegerTooltip}[] from an array of suggestions, and a function mapping a suggestion to a
	 * formatted tooltip using bungee chat components.
	 *
	 * @param suggestions the suggestions to provide to the user
	 * @param mapper a function mapping a suggestion onto its formatted tooltip using bungee components
	 *
	 * @return a {@link IntegerTooltip}[] representing the suggestions with tooltips
	 */
	static IntegerTooltip[] ofComponents(Function<Integer, BaseComponent[]> mapper, int... suggestions) {
		IntegerTooltip[] tooltips = new IntegerTooltip[suggestions.length];
		for(int i = 0; i < suggestions.length; i++) {
			tooltips[i] = of(suggestions[i], mapper.apply(suggestions[i]));
		}
		return tooltips;

	}

}
