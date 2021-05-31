/*******************************************************************************
 * Copyright 2018, 2021 Jorel Ali (Skepter) - MIT License
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

import java.util.Collection;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.bukkit.command.CommandSender;

import dev.jorel.commandapi.IStringTooltip;
import dev.jorel.commandapi.SuggestionInfo;

/**
 * An interface declaring methods required to override argument suggestions
 */
public interface IOverrideableSuggestions {

	/**
	 * Override the suggestions of this argument with a String array. Typically,
	 * this is the supplier <code>s -&gt; suggestions</code>.
	 * 
	 * @param suggestions the string array to override suggestions with
	 * @return the current argument
	 * @deprecated use {@link Argument#overrideSuggestions(Function)} 
	 */
	@Deprecated
	Argument overrideSuggestions(String... suggestions);
	
	/**
	 * @deprecated use {@link Argument#overrideSuggestions(Function)} 
	 * @param suggestions a collection of strings to override suggestions with
	 */
	@Deprecated
	Argument overrideSuggestions(Collection<String> suggestions);

	/**
	 * Override the suggestions of this argument with a function that maps the
	 * command sender to a String array.
	 * 
	 * @param suggestions the function to override suggestions with
	 * @return the current argument
	 */
	@Deprecated
	Argument overrideSuggestions(Function<CommandSender, String[]> suggestions);
	
	/**
	 * Override the suggestions of this argument with a function that maps the
	 * command sender to a String array.
	 * 
	 * @param suggestions the function to override suggestions with
	 * @return the current argument
	 */
	@Deprecated
	Argument overrideSuggestions(BiFunction<CommandSender, Object[], String[]> suggestions);

	/**
	 * Returns a function that maps the command sender to a String array of
	 * suggestions for the current command, or <code>null</code> if this is not
	 * overridden.
	 * 
	 * @return a function that provides suggestions, or <code>null</code> if there
	 *         are no overridden suggestions.
	 */
	Optional<Function<SuggestionInfo, IStringTooltip[]>> getOverriddenSuggestions();
	
	/**
	 * Override the suggestions of this argument with an array of StringTooltips,
	 * that represents the String suggestion and a hover tooltip
	 * 
	 * @param suggestions the suggestions and tooltips to override suggestions with 
	 * @return the current argument
	 * @deprecated use {@link Argument#overrideSuggestionsT(Function)}
	 */
	@Deprecated
	Argument overrideSuggestionsT(IStringTooltip... suggestions);
	
	/**
	 * Override the suggestions of this argument with a collection of StringTooltips,
	 * that represents the String suggestion and a hover tooltip
	 * 
	 * @param suggestions the suggestions and tooltips to override suggestions with 
	 * @return the current argument
	 * @deprecated use {@link Argument#overrideSuggestionsT(Function)}
	 */
	@Deprecated
	Argument overrideSuggestionsT(Collection<IStringTooltip> suggestions);
	
	/**
	 * Override the suggestions of this argument with a function mapping the command
	 * sender to an array of StringTooltips, that represents the String suggestion
	 * and a hover tooltip
	 * 
	 * @param suggestions the suggestions and tooltips to override suggestions with
	 * @return the current argument
	 */
	@Deprecated
	Argument overrideSuggestionsT(Function<CommandSender, IStringTooltip[]> suggestions);
	
	/**
	 * Override the suggestions of this argument with a function mapping the command
	 * sender and previously declared arguments to an array of StringTooltips, that
	 * represents the String suggestion and a hover tooltip
	 * 
	 * @param suggestions the suggestions and tooltips to override suggestions with
	 * @return the current argument
	 */
	@Deprecated
	Argument overrideSuggestionsT(BiFunction<CommandSender, Object[], IStringTooltip[]> suggestions);
	

	Argument replaceSuggestions(Function<SuggestionInfo, String[]> suggestions);
	
	Argument replaceSuggestionsT(Function<SuggestionInfo, IStringTooltip[]> suggestions);
	
}
