package dev.jorel.commandapi.arguments;

import java.util.Collection;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.bukkit.command.CommandSender;

import dev.jorel.commandapi.IStringTooltip;

/**
 * An interface declaring methods required to override argument suggestions
 */
public interface IOverrideableSuggestions {
	
	/////////////////
	// Main getter //
	/////////////////
	
	/**
	 * Returns a function that maps the command sender to a String array of
	 * suggestions for the current command, or <code>null</code> if this is not
	 * overridden.
	 * 
	 * @return a function that provides suggestions, or <code>null</code> if there
	 *         are no overridden suggestions.
	 */
	Optional<BiFunction<CommandSender, Object[], IStringTooltip[]>> getOverriddenSuggestions();
	
	///////////////////////////////////////
	// Override suggestions with strings //
	///////////////////////////////////////
	
	/**
	 * Override the suggestions of this argument with a String array. Typically,
	 * this is the supplier <code>s -&gt; suggestions</code>.
	 * 
	 * @param suggestions the string array to override suggestions with
	 * @return the current argument
	 */
	Argument overrideSuggestions(String... suggestions);
	
	Argument overrideSuggestions(Collection<String> suggestions);

	/**
	 * Override the suggestions of this argument with a function that maps the
	 * command sender to a String array.
	 * 
	 * @param suggestions the function to override suggestions with
	 * @return the current argument
	 */
	Argument overrideSuggestions(Function<CommandSender, String[]> suggestions);
	
	/**
	 * Override the suggestions of this argument with a function that maps the
	 * command sender to a String array.
	 * 
	 * @param suggestions the function to override suggestions with
	 * @return the current argument
	 */
	Argument overrideSuggestions(BiFunction<CommandSender, Object[], String[]> suggestions);
	
	////////////////////////////////////////
	// Override suggestions with tooltips //
	////////////////////////////////////////
	
	/**
	 * Override the suggestions of this argument with an array of StringTooltips,
	 * that represents the String suggestion and a hover tooltip
	 * 
	 * @param suggestions the suggestions and tooltips to override suggestions with 
	 * @return the current argument
	 */
	Argument overrideSuggestionsT(IStringTooltip... suggestions);
	
	/**
	 * Override the suggestions of this argument with a collection of StringTooltips,
	 * that represents the String suggestion and a hover tooltip
	 * 
	 * @param suggestions the suggestions and tooltips to override suggestions with 
	 * @return the current argument
	 */
	Argument overrideSuggestionsT(Collection<IStringTooltip> suggestions);
	
	/**
	 * Override the suggestions of this argument with a function mapping the command
	 * sender to an array of StringTooltips, that represents the String suggestion
	 * and a hover tooltip
	 * 
	 * @param suggestions the suggestions and tooltips to override suggestions with
	 * @return the current argument
	 */
	Argument overrideSuggestionsT(Function<CommandSender, IStringTooltip[]> suggestions);
	
	/**
	 * Override the suggestions of this argument with a function mapping the command
	 * sender and previously declared arguments to an array of StringTooltips, that
	 * represents the String suggestion and a hover tooltip
	 * 
	 * @param suggestions the suggestions and tooltips to override suggestions with
	 * @return the current argument
	 */
	Argument overrideSuggestionsT(BiFunction<CommandSender, Object[], IStringTooltip[]> suggestions);
	
}
