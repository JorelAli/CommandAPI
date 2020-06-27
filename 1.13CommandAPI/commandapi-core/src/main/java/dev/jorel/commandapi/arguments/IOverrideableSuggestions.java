package dev.jorel.commandapi.arguments;

import java.util.function.Function;

import org.bukkit.command.CommandSender;

/**
 * An interface declaring methods required to override argument suggestions
 */
public interface IOverrideableSuggestions<T extends Argument> {

	/**
	 * Override the suggestions of this argument with a String array. Typically,
	 * this is the supplier <code>s -> suggestions</code>.
	 * 
	 * @param suggestions the string array to override suggestions with
	 * @return the current argument
	 */
	T overrideSuggestions(String... suggestions);

	/**
	 * Override the suggestions of this argument with a function that maps the
	 * command sender to a String array.
	 * 
	 * @param suggestions the function to override suggestions with
	 * @return the current argument
	 */
	T overrideSuggestions(Function<CommandSender, String[]> suggestions);

	/**
	 * Returns a function that maps the command sender to a String array of
	 * suggestions for the current command, or <code>null</code> if this is not
	 * overridden.
	 * 
	 * @return a function that provides suggestions, or <code>null</code> if there
	 *         are no overridden suggestions.
	 */
	Function<CommandSender, String[]> getOverriddenSuggestions();
	
}
