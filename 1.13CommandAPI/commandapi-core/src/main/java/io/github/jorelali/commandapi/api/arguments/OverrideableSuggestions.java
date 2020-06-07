package io.github.jorelali.commandapi.api.arguments;

import org.bukkit.command.CommandSender;

public interface OverrideableSuggestions {
			
	/**
	 * Override the suggestions of this argument with a String array
	 * @param suggestions The string array to override suggestions with
	 * @return The argument
	 */
	<T extends Argument> T overrideSuggestions(String... suggestions);	
	<T extends Argument> T overrideSuggestions(DynamicSuggestions suggestions);
	default DynamicSuggestions getOverriddenSuggestions() { return null; }
	
	default DynamicSuggestions mkSuggestions(String... suggestions) {
		return (s) -> suggestions;
	}
	
	@FunctionalInterface
	public interface DynamicSuggestions {
		String[] getSuggestions(CommandSender sender);
	}
}
