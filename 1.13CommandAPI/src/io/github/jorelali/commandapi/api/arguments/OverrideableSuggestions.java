package io.github.jorelali.commandapi.api.arguments;

public interface OverrideableSuggestions {
			
	/**
	 * Override the suggestions of this argument with a String array
	 * @param suggestions The string array to override suggestions with
	 * @return The argument
	 */
	public <T extends Argument> T overrideSuggestions(String... suggestions);
		
	public default String[] getOverriddenSuggestions() {
		return null;
	}
}
