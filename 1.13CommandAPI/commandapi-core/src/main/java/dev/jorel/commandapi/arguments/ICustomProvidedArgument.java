package dev.jorel.commandapi.arguments;

/**
 * An interface allowing arguments to have built-in suggestion providers
 */
public interface ICustomProvidedArgument {
			
	SuggestionProviders getSuggestionProvider();
	
	enum SuggestionProviders { FUNCTION, RECIPES, SOUNDS, ADVANCEMENTS, LOOT_TABLES, BIOMES, ENTITIES; }
}
