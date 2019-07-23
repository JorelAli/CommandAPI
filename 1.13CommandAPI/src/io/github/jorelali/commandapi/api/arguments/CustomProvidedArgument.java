package io.github.jorelali.commandapi.api.arguments;

public interface CustomProvidedArgument {
			
	enum SuggestionProviders {
		FUNCTION, RECIPES, SOUNDS, ADVANCEMENTS, LOOT_TABLES
	}
	
	SuggestionProviders getSuggestionProvider();
}
