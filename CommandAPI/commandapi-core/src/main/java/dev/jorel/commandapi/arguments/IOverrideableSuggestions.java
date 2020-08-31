package dev.jorel.commandapi.arguments;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.bukkit.command.CommandSender;

import dev.jorel.commandapi.StringTooltip;

/**
 * An interface declaring methods required to override argument suggestions
 */
public interface IOverrideableSuggestions {

	/**
	 * Override the suggestions of this argument with a String array. Typically,
	 * this is the supplier <code>s -> suggestions</code>.
	 * 
	 * @param suggestions the string array to override suggestions with
	 * @return the current argument
	 */
	Argument overrideSuggestions(String... suggestions);

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

	/**
	 * Returns a function that maps the command sender to a String array of
	 * suggestions for the current command, or <code>null</code> if this is not
	 * overridden.
	 * 
	 * @return a function that provides suggestions, or <code>null</code> if there
	 *         are no overridden suggestions.
	 */
	Optional<BiFunction<CommandSender, Object[], StringTooltip[]>> getOverriddenSuggestions();
	
	Argument overrideSuggestionsT(StringTooltip... suggestions);
	Argument overrideSuggestionsT(Function<CommandSender, StringTooltip[]> suggestions);
	Argument overrideSuggestionsT(BiFunction<CommandSender, Object[], StringTooltip[]> suggestions);
	
}
