package io.github.jorelali.commandapi.api.arguments;

public interface OverrideableSuggestions {
			
	public <T extends Argument> T overrideSuggestions(String... suggestions);
		
	public default String[] getOverriddenSuggestions() {
		return null;
	}
}
