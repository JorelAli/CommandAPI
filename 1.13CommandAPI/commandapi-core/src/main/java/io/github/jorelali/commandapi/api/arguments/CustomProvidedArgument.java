package io.github.jorelali.commandapi.api.arguments;

public interface CustomProvidedArgument {
			
	SuggestionProviders getSuggestionProvider();
	
	enum SuggestionProviders { FUNCTION, RECIPES, SOUNDS, ADVANCEMENTS, LOOT_TABLES; }
}
