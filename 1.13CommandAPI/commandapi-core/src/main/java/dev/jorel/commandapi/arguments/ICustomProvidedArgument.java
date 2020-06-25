package dev.jorel.commandapi.arguments;

public interface ICustomProvidedArgument {
			
	SuggestionProviders getSuggestionProvider();
	
	enum SuggestionProviders { FUNCTION, RECIPES, SOUNDS, ADVANCEMENTS, LOOT_TABLES, BIOMES, ENTITIES; }
}
