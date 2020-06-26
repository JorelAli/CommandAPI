package dev.jorel.commandapi.arguments;

/**
 * An interface allowing arguments to have built-in suggestion providers based on Minecraft namespaced keys
 */
public interface ICustomProvidedArgument {
			
	/**
	 * Returns the suggestion provider that this argument is populated with
	 * @return the suggestion provider that this argument is populated with
	 */
	SuggestionProviders getSuggestionProvider();
	
	/**
	 * An enum representing the different suggestion providers for arguments based on Minecraft namespaced keys
	 */
	enum SuggestionProviders { 
		/**
		 * A suggestion provider for the FunctionArgument
		 */
		FUNCTION, 
		
		/**
		 * A suggestion provider for the RecipeArgument
		 */
		RECIPES, 
		
		/**
		 * A suggestion provider for the SoundArgument
		 */
		SOUNDS, 
		
		/**
		 * A suggestion provider for the AdvancementArgument
		 */
		ADVANCEMENTS, 
		
		/**
		 * A suggestion provider for the LootTableArgument
		 */
		LOOT_TABLES, 
		
		/**
		 * A suggestion provider for the BiomeArgument
		 */
		BIOMES, 
		
		/**
		 * A suggestion provider for the EntityTypeArgument
		 */
		ENTITIES; 
	}
}
