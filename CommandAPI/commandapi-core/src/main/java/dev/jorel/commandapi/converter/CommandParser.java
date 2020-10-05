package dev.jorel.commandapi.converter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.AdvancementArgument;
import dev.jorel.commandapi.arguments.AngleArgument;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.AxisArgument;
import dev.jorel.commandapi.arguments.BiomeArgument;
import dev.jorel.commandapi.arguments.BlockPredicateArgument;
import dev.jorel.commandapi.arguments.BlockStateArgument;
import dev.jorel.commandapi.arguments.BooleanArgument;
import dev.jorel.commandapi.arguments.ChatArgument;
import dev.jorel.commandapi.arguments.ChatColorArgument;
import dev.jorel.commandapi.arguments.ChatComponentArgument;
import dev.jorel.commandapi.arguments.CommandAPIArgumentType;
import dev.jorel.commandapi.arguments.EnchantmentArgument;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.EntitySelectorArgument.EntitySelector;
import dev.jorel.commandapi.arguments.EntityTypeArgument;
import dev.jorel.commandapi.arguments.EnvironmentArgument;
import dev.jorel.commandapi.arguments.FloatRangeArgument;
import dev.jorel.commandapi.arguments.FunctionArgument;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import dev.jorel.commandapi.arguments.IntegerRangeArgument;
import dev.jorel.commandapi.arguments.ItemStackArgument;
import dev.jorel.commandapi.arguments.ItemStackPredicateArgument;
import dev.jorel.commandapi.arguments.LootTableArgument;
import dev.jorel.commandapi.arguments.MathOperationArgument;
import dev.jorel.commandapi.arguments.MultiLiteralArgument;
import dev.jorel.commandapi.arguments.NBTCompoundArgument;
import dev.jorel.commandapi.arguments.ObjectiveArgument;
import dev.jorel.commandapi.arguments.ObjectiveCriteriaArgument;
import dev.jorel.commandapi.arguments.ParticleArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;
import dev.jorel.commandapi.arguments.PotionEffectArgument;
import dev.jorel.commandapi.arguments.RecipeArgument;
import dev.jorel.commandapi.arguments.RotationArgument;
import dev.jorel.commandapi.arguments.ScoreboardSlotArgument;
import dev.jorel.commandapi.arguments.SoundArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import dev.jorel.commandapi.arguments.TeamArgument;
import dev.jorel.commandapi.arguments.TextArgument;
import dev.jorel.commandapi.arguments.TimeArgument;
import dev.jorel.commandapi.arguments.UUIDArgument;

public class CommandParser {

	static Pattern argumentPattern = Pattern.compile("<(\\w+)>\\[([a-z:_]+|(?:[0-9]+)?\\.\\.(?:[0-9]+)?)\\]");
	static Pattern literalPattern = Pattern.compile("\\((\\w+(?:\\|\\w+)*)\\)");
	
	/*
	 * plugins-to-convert: 
  - Essentials: 
    - speed <speed>[0..10]
    - speed <target>[minecraft:game_profile]
    - speed (walk|fly) <speed>[0..10]
    - speed (walk|fly) <speed>[0..10] <target>[minecraft:game_profile]
	 */
	public void parse(String command) {
		String[] parts = command.split(" ");
		String commandName = parts[0];
		
		CommandAPICommand cmdCommand = new CommandAPICommand(commandName);
		if(parts.length == 1) {
			//done
		} else {
			for(int i = 1; i < parts.length; i++) {
				cmdCommand.withArguments(parseArgument(parts[i]));
			}
		}
	}
	
	public Argument parseArgument(String argument) {
		Matcher literalMatcher = literalPattern.matcher(argument);
		Matcher argumentMatcher = argumentPattern.matcher(argument);
		if(literalMatcher.matches()) {
			String literals = literalMatcher.group(1);
			return new MultiLiteralArgument(literals.split("\\|"));
		} else if(argumentMatcher.matches()) {
			String nodeName = literalMatcher.group(1);
			String argumentType = literalMatcher.group(2);
			
			Argument resultArgument = null; //TODO: Remove
			switch(CommandAPIArgumentType.fromInternal(argumentType)) {
			case ADVANCEMENT:
				resultArgument = new AdvancementArgument(nodeName);
				break;
			case ANGLE:
				resultArgument = new AngleArgument(nodeName);
				break;
			case AXIS:
				resultArgument = new AxisArgument(nodeName);
				break;
			case BIOME:
				resultArgument = new BiomeArgument(nodeName);
				break;
			case BLOCKSTATE:
				resultArgument = new BlockStateArgument(nodeName);
				break;
			case BLOCK_PREDICATE:
				resultArgument = new BlockPredicateArgument(nodeName);
				break;
			case CHAT:
				resultArgument = new ChatArgument(nodeName);
				break;
			case CHATCOLOR:
				resultArgument = new ChatColorArgument(nodeName);
				break;
			case CHAT_COMPONENT:
				resultArgument = new ChatComponentArgument(nodeName);
				break;
			case CUSTOM:
				break;
			case ENCHANTMENT:
				resultArgument = new EnchantmentArgument(nodeName);
				break;
			case ENTITY_SELECTOR:
				resultArgument = new EntitySelectorArgument(nodeName, EntitySelector.ONE_ENTITY);
				break;
			case ENTITY_TYPE:
				resultArgument = new EntityTypeArgument(nodeName);
				break;
			case ENVIRONMENT:
				resultArgument = new EnvironmentArgument(nodeName);
				break;
			case FLOAT_RANGE:
				resultArgument = new FloatRangeArgument(nodeName);
				break;
			case FUNCTION:
				resultArgument = new FunctionArgument(nodeName);
				break;
			case INT_RANGE:
				resultArgument = new IntegerRangeArgument(nodeName);
				break;
			case ITEMSTACK:
				resultArgument = new ItemStackArgument(nodeName);
				break;
			case ITEMSTACK_PREDICATE:
				resultArgument = new ItemStackPredicateArgument(nodeName);
				break;
			case LITERAL:
				break;
			case LOCATION:
				break;
			case LOCATION_2D:
				break;
			case LOOT_TABLE:
				resultArgument = new LootTableArgument(nodeName);
				break;
			case MATH_OPERATION:
				resultArgument = new MathOperationArgument(nodeName);
				break;
			case MULTI_LITERAL:
				break;
			case NBT_COMPOUND:
				resultArgument = new NBTCompoundArgument(nodeName);
				break;
			case OBJECTIVE:
				resultArgument = new ObjectiveArgument(nodeName);
				break;
			case OBJECTIVE_CRITERIA:
				resultArgument = new ObjectiveCriteriaArgument(nodeName);
				break;
			case PARTICLE:
				resultArgument = new ParticleArgument(nodeName);
				break;
			case PLAYER:
				resultArgument = new PlayerArgument(nodeName);
				break;
			case POTION_EFFECT:
				resultArgument = new PotionEffectArgument(nodeName);
				break;
			case RECIPE:
				resultArgument = new RecipeArgument(nodeName);
				break;
			case ROTATION:
				resultArgument = new RotationArgument(nodeName);
				break;
			case SCOREBOARD_SLOT:
				resultArgument = new ScoreboardSlotArgument(nodeName);
				break;
			case SCORE_HOLDER:
				break;
			case SOUND:
				resultArgument = new SoundArgument(nodeName);
				break;
			case TEAM:
				resultArgument = new TeamArgument(nodeName);
				break;
			case TIME:
				resultArgument = new TimeArgument(nodeName);
				break;
			case UUID:
				resultArgument = new UUIDArgument(nodeName);
				break;
			case PRIMITIVE_BOOLEAN:
				resultArgument = new BooleanArgument(nodeName);
				break;
			case PRIMITIVE_DOUBLE:
				break;
			case PRIMITIVE_FLOAT:
				break;
			case PRIMITIVE_GREEDY_STRING:
				resultArgument = new GreedyStringArgument(nodeName);
				break;
			case PRIMITIVE_INTEGER:
				break;
			case PRIMITIVE_LONG:
				break;
			case PRIMITIVE_STRING:
				resultArgument = new StringArgument(nodeName);
				break;
			case PRIMITIVE_TEXT:
				resultArgument = new TextArgument(nodeName);
				break;
			default:
				break;
			}
			
			return resultArgument;
			
		}
		return null;
	}
	
}
