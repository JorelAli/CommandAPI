package io.github.jorelali.commandapi.api.arguments;

import java.util.function.Function;

import org.bukkit.command.CommandSender;

public interface OverrideableSuggestions<T extends Argument> {
			
	/**
	 * Override the suggestions of this argument with a String array
	 * @param suggestions The string array to override suggestions with
	 * @return The argument
	 */
	T overrideSuggestions(String... suggestions);	
	T overrideSuggestions(Function<CommandSender, String[]> suggestions);
	Function<CommandSender, String[]> getOverriddenSuggestions();
}
