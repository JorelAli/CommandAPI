package io.github.jorelali.commandapi.api.arguments;

public interface CustomProvidedArgument {
			
	public enum SuggestionProviders {
		FUNCTION, RECIPES, SOUNDS, ADVANCEMENTS, LOOT_TABLES;
	}
	
	public SuggestionProviders getSuggestionProvider();
}
