package dev.jorel.commandapi.arguments;


/**
 * An enum representing the different suggestion providers for arguments based on Minecraft namespaced keys
 */
public enum SuggestionProviders { 
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
	ENTITIES,

	/**
	 * A suggestion provider for the PotionEffectArgument
	 */
	POTION_EFFECTS;
}