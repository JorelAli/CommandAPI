package dev.jorel.commandapi.arguments;

public interface CustomProvidedArgument {
			
	SuggestionProviders getSuggestionProvider();
	
	enum SuggestionProviders { FUNCTION, RECIPES, SOUNDS, ADVANCEMENTS, LOOT_TABLES; }
}
