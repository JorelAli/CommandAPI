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
	AXIS("minecraft:swizzle"),

	/**
	 * The BiomeArgument
	 */
	BIOME,

	/**
	 * The ChatArgument
	 */
	CHAT("minecraft:message"),

	/**
	 * The ChatComponentArgument
	 */
	CHAT_COMPONENT("minecraft:component"),

	/**
	 * The ChatColorArgument
	 */
	CHATCOLOR("minecraft:color"), 
	
	/**
	 * The CustomArgument
	 */
	CUSTOM, 
	
	/**
	 * The EnchantmentArgument
	 * 
	 */
	ENCHANTMENT("minecraft:item_enchantment"), 
	
	/**
	 * The EntitySelectorArgument
	 */
	ENTITY_SELECTOR("minecraft:entity"), 
	
	/**
	 * The EntityTypeArgument
	 */
	ENTITY_TYPE("minecraft:entity_summon"), 
	
	/**
	 * The EnvironmentArgument
	 */
	ENVIRONMENT("minecraft:dimension"), 
	
	/**
	 * The FloatRangeArgument
	 */
	FLOAT_RANGE("minecraft:float_range"), 
	
	/**
	 * The FunctionArgument
	 */
	FUNCTION("minecraft:function"), 
	
	/**
	 * The IntegerRangeArgument
	 */
	INT_RANGE("minecraft:int_range"),
	
	/**
	 * The ItemStackArgument
	 */
	ITEMSTACK("minecraft:item_stack"), 
	
	/**
	 * The LiteralArgument
	 */
	LITERAL, 
	
	/**
	 * The LocationArgument
	 */
	LOCATION(new String[] {"minecraft:vec3", "minecraft:block_pos"}), 
	
	/**
	 * The Location2dArgument
	 */
	LOCATION_2D(new String[] {"minecraft:vec2", "minecraft:column_pos"}), 
	
	/**
	 * The LootTableArgument
	 */
	LOOT_TABLE, 
	
	/**
	 * The MathOperationArgument
	 */
	MATH_OPERATION("minecraft:operation"), 
	
	/**
	 * The NBTCompoundArgument
	 */
	NBT_COMPOUND("minecraft:nbt_compound_tag"), 
	
	/**
	 * The ObjectiveArgument
	 */
	OBJECTIVE("minecraft:objective"), 
	
	/**
	 * The ObjectiveCriteriaArgument
	 */
	OBJECTIVE_CRITERIA("minecraft:objective_criteria"),
	
	/**
	 * The ParticleArgument
	 */
	PARTICLE("minecraft:particle"), 
	
	/**
	 * The PlayerArgument
	 */
	PLAYER("minecraft:game_profile"), 
	
	/**
	 * The PotionEffectArgument
	 */
	POTION_EFFECT("minecraft:mob_effect"), 
	
	/**
	 * The RecipeArgument	
	 */
	RECIPE, 
	
	/**
	 * The RotationArgument
	 */
	ROTATION("minecraft:rotation"), 
	
	/**
	 * The ScoreHolderArgument
	 */
	SCORE_HOLDER("minecraft:score_holder"), 
	
	/**
	 * The ScoreboardSlotArgument
	 */
	SCOREBOARD_SLOT("minecraft:scoreboard_slot"), 
	
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
	TEAM("minecraft:team"), 
	
	/**
	 * The TimeArgument
	 */
	TIME("minecraft:time"), 
	
	/**
	 * The BlockStateArgument
	 */
	BLOCKSTATE("minecraft:block_state"), 
	
	/**
	 * The UUIDArgument
	 */
	UUID("minecraft:uuid"), 
	
	/**
	 * The ItemStackPredicateArgument
	 */
	ITEMSTACK_PREDICATE("minecraft:item_predicate"), 
	
	/**
	 * The BlockPredicateArgument
	 */
	BLOCK_PREDICATE("minecraft:block_predicate"), 
	
	/**
	 * The MultiLiteralArgument
	 */
	MULTI_LITERAL, 
	
	/**
	 * The AngleArgument
	 */
	ANGLE("minecraft:angle");
	
	CommandAPIArgumentType(String internal) {
		
	}
	
	CommandAPIArgumentType(String[] internal) {
		
	}
	
	public static CommandAPIArgumentType fromInternal(String internal) {
		return null;
	}

}
