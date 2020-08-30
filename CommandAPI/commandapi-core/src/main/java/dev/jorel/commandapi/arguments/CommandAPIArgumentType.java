package dev.jorel.commandapi.arguments;

/**
 * An enum that represents the different arguments declared in the Command API
 */
public enum CommandAPIArgumentType {

	/**
	 * The AdvancementArgument
	 */
	ADVANCEMENT,

	/**
	 * The AxisArgument
	 */
	AXIS,

	/**
	 * The BiomeArgument
	 */
	BIOME,

	/**
	 * The ChatArgument
	 */
	CHAT,

	/**
	 * The ChatComponentArgument
	 */
	CHAT_COMPONENT,

	/**
	 * The ChatColorArgument
	 */
	CHATCOLOR, 
	
	/**
	 * The CustomArgument
	 */
	CUSTOM, 
	
	/**
	 * The EnchantmentArgument
	 * 
	 */
	ENCHANTMENT, 
	
	/**
	 * The EntitySelectorArgument
	 */
	ENTITY_SELECTOR, 
	
	/**
	 * The EntityTypeArgument
	 */
	ENTITY_TYPE, 
	
	/**
	 * The EnvironmentArgument
	 */
	ENVIRONMENT, 
	
	/**
	 * The FloatRangeArgument
	 */
	FLOAT_RANGE, 
	
	/**
	 * The FunctionArgument
	 */
	FUNCTION, 
	
	/**
	 * The IntegerRangeArgument
	 */
	INT_RANGE,
	
	/**
	 * The ItemStackArgument
	 */
	ITEMSTACK, 
	
	/**
	 * The LiteralArgument
	 */
	LITERAL, 
	
	/**
	 * The LocationArgument
	 */
	LOCATION, 
	
	/**
	 * The Location2dArgument
	 */
	LOCATION_2D, 
	
	/**
	 * The LootTableArgument
	 */
	LOOT_TABLE, 
	
	/**
	 * The MathOperationArgument
	 */
	MATH_OPERATION, 
	
	/**
	 * The NBTCompoundArgument
	 */
	NBT_COMPOUND, 
	
	/**
	 * The ObjectiveArgument
	 */
	OBJECTIVE, 
	
	/**
	 * The ObjectiveCriteriaArgument
	 */
	OBJECTIVE_CRITERIA,
	
	/**
	 * The ParticleArgument
	 */
	PARTICLE, 
	
	/**
	 * The PlayerArgument
	 */
	PLAYER, 
	
	/**
	 * The PotionEffectArgument
	 */
	POTION_EFFECT, 
	
	/**
	 * The RecipeArgument	
	 */
	RECIPE, 
	
	/**
	 * The RotationArgument
	 */
	ROTATION, 
	
	/**
	 * The ScoreHolderArgument
	 */
	SCORE_HOLDER, 
	
	/**
	 * The ScoreboardSlotArgument
	 */
	SCOREBOARD_SLOT, 
	
	/**
	 * Primitive arguments BooleanArgument, DoubleArgument, FloatArgument, IntegerArgument, LongArgument, StringArgument, GreedyStringArgument, TextArgument
	 */
	SIMPLE_TYPE, 
	
	/**
	 * The SoundArgument
	 */
	SOUND, 
	
	/**
	 * The TeamArgument
	 */
	TEAM, 
	
	/**
	 * The TimeArgument
	 */
	TIME, 
	
	/**
	 * The BlockStateArgument
	 */
	BLOCKSTATE, 
	
	/**
	 * The UUIDArgument
	 */
	UUID, 
	
	/**
	 * The ItemStackPredicateArgument
	 */
	ITEMSTACK_PREDICATE, 
	
	/**
	 * The BlockPredicateArgument
	 */
	BLOCK_PREDICATE, MULTI_LITERAL;

}
