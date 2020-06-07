package io.github.jorelali.commandapi.api.arguments;

import org.bukkit.command.CommandSender;

public interface OverrideableSuggestions<T extends Argument> {
			
	/**
	 * Override the suggestions of this argument with a String array
	 * @param suggestions The string array to override suggestions with
	 * @return The argument
	 */
	T overrideSuggestions(String... suggestions);	
	T overrideSuggestions(DynamicSuggestions suggestions);
	default DynamicSuggestions getOverriddenSuggestions() { return null; }
	
	default DynamicSuggestions mkSuggestions(String... suggestions) {
		return (s) -> suggestions;
	}
	
	@FunctionalInterface
	public interface DynamicSuggestions {
		String[] getSuggestions(CommandSender sender);
	}
}
