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
package dev.jorel.commandapi.arguments;

import java.util.function.Function;

import com.mojang.brigadier.arguments.ArgumentType;

import dev.jorel.commandapi.SuggestionInfo;
import dev.jorel.commandapi.abstractions.AbstractTooltip;

/**
 * An interface declaring methods required to override argument suggestions
 * 
 * @param <T> The type of the underlying object that this argument casts to
 * @param <S> A custom type which is represented by this argument. For example,
 *            a {@link StringArgument} will have a custom type
 *            <code>String</code>
 */
public abstract class SafeOverrideableArgument<T, S> extends Argument<T> {

	private final Function<S, String> mapper;

	/**
	 * Instantiates this argument and assigns the mapper to the provided mapper
	 * 
	 * @param nodeName the node name of this argument
	 * @param rawType  the NMS raw argument type of this argument
	 * @param mapper   the mapping function that maps this argument type to a string
	 *                 for suggestions
	 */
	protected SafeOverrideableArgument(String nodeName, ArgumentType<?> rawType, Function<S, String> mapper) {
		super(nodeName, rawType);
		this.mapper = mapper;
	}

	/**
	 * Replaces the suggestions with a safe {@link SafeSuggestions} object. Use the
	 * static methods in {@link SafeSuggestions} to create safe suggestions.
	 * 
	 * @param suggestions The safe suggestions to use
	 * @return the current argument
	 */
	public final Argument<T> replaceSafeSuggestions(SafeSuggestions<S> suggestions) {
		replaceSuggestions(suggestions.toSuggestions(mapper));
		return this;
	}

	/**
	 * Replaces the suggestions of this argument with an array of suggestions.
	 * 
	 * @param suggestions a function that takes in {@link SuggestionInfo} and
	 *                    returns a {@link S} array of suggestions, where S is your
	 *                    custom type
	 * @return the current argument
	 * @deprecated use {@link #replaceSafeSuggestions(SafeSuggestions)}
	 */
	@Deprecated(forRemoval = true)
	public final Argument<T> replaceWithSafeSuggestions(Function<SuggestionInfo, S[]> suggestions) {
		return replaceSafeSuggestions(SafeSuggestions.suggest(suggestions));
	}

	/**
	 * Replaces the suggestions of this argument with an array of suggestions.
	 * 
	 * @param suggestions a function that takes in {@link SuggestionInfo} and
	 *                    returns an {@link AbstractTooltip} array of suggestions,
	 *                    parameterized over {@link S} where S is your custom type
	 * @return the current argument
	 * @deprecated use {@link #replaceSafeSuggestions(SafeSuggestions)}
	 */
	@Deprecated(forRemoval = true)
	public final Argument<T> replaceWithSafeSuggestionsT(Function<SuggestionInfo, AbstractTooltip<S>[]> suggestions) {
		return replaceSafeSuggestions(SafeSuggestions.tooltips(suggestions));
	}

	/**
	 * Includes the suggestions provided with the existing suggestions for this
	 * argument. Use the static methods in {@link SafeSuggestions} to create safe
	 * suggestions.
	 * 
	 * @param suggestions The safe suggestions to use
	 * @return the current argument
	 */
	public final Argument<T> includeSafeSuggestions(SafeSuggestions<S> suggestions) {
		return this.includeSuggestions(suggestions.toSuggestions(mapper));
	}

	/**
	 * Include suggestions to add to the list of default suggestions represented by
	 * this argument.
	 * 
	 * @param suggestions a function that takes in {@link SuggestionInfo} which
	 *                    includes information about the current state at the time
	 *                    the suggestions are run and returns a {@link S} array of
	 *                    suggestions to add, where S is your custom type
	 * @return the current argument
	 * @deprecated use {@link #includeSafeSuggestions(SafeSuggestions)}
	 */
	@Deprecated(forRemoval = true)
	public final Argument<T> includeWithSafeSuggestions(Function<SuggestionInfo, S[]> suggestions) {
		return includeSafeSuggestions(SafeSuggestions.suggest(suggestions));
	}

	/**
	 * Include suggestions to add to the list of default suggestions represented by
	 * this argument.
	 * 
	 * @param suggestions a function that takes in {@link SuggestionInfo} which
	 *                    includes information about the current state at the time
	 *                    the suggestions are run and returns an {@link AbstractTooltip}
	 *                    array of suggestions to add, parameterized over {@link S}
	 *                    where S is your custom type
	 * @return the current argument
	 * @deprecated use {@link #includeSafeSuggestions(SafeSuggestions)}
	 * 
	 */
	@Deprecated(forRemoval = true)
	public final Argument<T> includeWithSafeSuggestionsT(Function<SuggestionInfo, AbstractTooltip<S>[]> suggestions) {
		return includeSafeSuggestions(SafeSuggestions.tooltips(suggestions));
	}

}
