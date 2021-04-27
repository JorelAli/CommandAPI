package dev.jorel.commandapi.arguments;

import java.util.Arrays;

/**
 * An enum that represents the different arguments declared in the Command API
 */
public enum CommandAPIArgumentType {

	/**
	 * The AdvancementArgument
	 */
	ADVANCEMENT("api:advancement"),

	/**
	 * The AxisArgument
	 */
	AXIS("minecraft:swizzle"),

	/**
	 * The BiomeArgument
	 */
	BIOME("api:biome"),
	
	/**
	 * The ChatArgument (with Paper Adventure backend)
	 */
	ADVENTURE_CHAT,

	/**
	 * The ChatArgument (with BaseComponent backend)
	 */
	CHAT("minecraft:message"),

	/**
	 * The ChatComponentArgument (with BaseComponent backend)
	 */
	CHAT_COMPONENT("minecraft:component"),
	
	/**
	 * The ChatComponentArgument (with Paper Adventure backend)
	 */
	ADVENTURE_CHAT_COMPONENT,

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
	LOOT_TABLE("api:loot_table"), 
	
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
	RECIPE("api:recipe"), 
	
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
	PRIMITIVE_BOOLEAN("brigadier:bool"),
	PRIMITIVE_DOUBLE("brigadier:double"),
	PRIMITIVE_FLOAT("brigadier:float"),
	PRIMITIVE_INTEGER("brigadier:integer"),
	PRIMITIVE_LONG("brigadier:long"),
	PRIMITIVE_STRING("brigadier:string"),
	PRIMITIVE_GREEDY_STRING("api:greedy_string"),
	PRIMITIVE_TEXT("api:text"),
	
	/**
	 * The SoundArgument
	 */
	SOUND("api:sound"), 
	
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
	
	private String[] internals;
	
	CommandAPIArgumentType() {
		internals = new String[0];
	}
	
	CommandAPIArgumentType(String internal) {
		this.internals = new String[] {internal};
	}
	
	CommandAPIArgumentType(String[] internals) {
		this.internals = internals;
	}
	
	public static CommandAPIArgumentType fromInternal(String internal) {
		for(CommandAPIArgumentType type : CommandAPIArgumentType.values()) {
			if(Arrays.asList(type.internals).contains(internal)) {
				return type;
			}
		}
		return null;
	}

}
