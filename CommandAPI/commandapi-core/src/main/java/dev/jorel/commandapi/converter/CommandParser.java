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
import dev.jorel.commandapi.arguments.DoubleArgument;
import dev.jorel.commandapi.arguments.EnchantmentArgument;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.EntitySelectorArgument.EntitySelector;
import dev.jorel.commandapi.arguments.EntityTypeArgument;
import dev.jorel.commandapi.arguments.EnvironmentArgument;
import dev.jorel.commandapi.arguments.FloatRangeArgument;
import dev.jorel.commandapi.arguments.FunctionArgument;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.IntegerRangeArgument;
import dev.jorel.commandapi.arguments.ItemStackArgument;
import dev.jorel.commandapi.arguments.ItemStackPredicateArgument;
import dev.jorel.commandapi.arguments.Location2DArgument;
import dev.jorel.commandapi.arguments.LocationArgument;
import dev.jorel.commandapi.arguments.LocationType;
import dev.jorel.commandapi.arguments.LongArgument;
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
import dev.jorel.commandapi.arguments.ScoreHolderArgument;
import dev.jorel.commandapi.arguments.ScoreHolderArgument.ScoreHolderType;
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
	public CommandAPICommand parse(String command) {
		String[] parts = command.split(" ");
		String commandName = parts[0];
		
		CommandAPICommand cmdCommand = new CommandAPICommand(commandName);
		if (parts.length != 1) {
			for (int i = 1; i < parts.length; i++) {
				cmdCommand.withArguments(parseArgument(parts[i]));
			}
		}
		return cmdCommand;
	}
	
	private CommandAPIArgumentType a(String s) {
		double value;
		try {
			value = Double.parseDouble(s);
		} catch(Exception e) {
			throw new RuntimeException();
		}
		if(value > Integer.MAX_VALUE) {
			if(value == (long) value) {
				return CommandAPIArgumentType.PRIMITIVE_LONG;
			} else {
				return CommandAPIArgumentType.PRIMITIVE_DOUBLE;
			}
		} else {
			if(value == (int) value) {
				return CommandAPIArgumentType.PRIMITIVE_INTEGER;
			} else {
				return CommandAPIArgumentType.PRIMITIVE_DOUBLE;
			}
		}
	}
	
	//TODO: Whatever this function is
	public Argument parseRange(String nodeName, String[] bounds) {
		double value;
		try {
			value = Double.parseDouble(bounds[0]);
		} catch(Exception e) {
			throw new RuntimeException();
		}
		
		if(bounds.length == 1) {
			//x..
			switch(a(bounds[0])) {
				case PRIMITIVE_LONG:
					return new LongArgument(nodeName, (long) value);
				case PRIMITIVE_DOUBLE:
					return new DoubleArgument(nodeName, value);
				case PRIMITIVE_INTEGER:
					return new IntegerArgument(nodeName, (int) value);
			}
		} else {
			if(bounds[0].length() == 0) {
				//..x
				switch(a(bounds[0])) {
					case PRIMITIVE_LONG:
						return new LongArgument(nodeName, Long.MIN_VALUE, (long) value);
					case PRIMITIVE_DOUBLE:
						return new DoubleArgument(nodeName, -Double.MAX_VALUE, value);
					case PRIMITIVE_INTEGER:
						return new IntegerArgument(nodeName, Integer.MIN_VALUE, (int) value);
				}
			} else {
				//x..x
				if(a(bounds[0]) == CommandAPIArgumentType.PRIMITIVE_LONG || a(bounds[1]) == CommandAPIArgumentType.PRIMITIVE_LONG) {
					new LongArgument(nodeName, Long.MIN_VALUE, (long) value);
				}
			}
		}
		return null;
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
			
			if(argumentType.contains("..")) {
				return parseRange(nodeName, argumentType.split("\\.\\."));
			}
			
			switch(CommandAPIArgumentType.fromInternal(argumentType)) {
			case ADVANCEMENT:
				return new AdvancementArgument(nodeName);
			case ANGLE:
				return new AngleArgument(nodeName);
			case AXIS:
				return new AxisArgument(nodeName);
			case BIOME:
				return new BiomeArgument(nodeName);
			case BLOCKSTATE:
				return new BlockStateArgument(nodeName);
			case BLOCK_PREDICATE:
				return new BlockPredicateArgument(nodeName);
			case CHAT:
				return new ChatArgument(nodeName);
			case CHATCOLOR:
				return new ChatColorArgument(nodeName);
			case CHAT_COMPONENT:
				return new ChatComponentArgument(nodeName);
			case CUSTOM:
				break;
			case ENCHANTMENT:
				return new EnchantmentArgument(nodeName);
			case ENTITY_SELECTOR:
				return new EntitySelectorArgument(nodeName, EntitySelector.ONE_ENTITY);
			case ENTITY_TYPE:
				return new EntityTypeArgument(nodeName);
			case ENVIRONMENT:
				return new EnvironmentArgument(nodeName);
			case FLOAT_RANGE:
				return new FloatRangeArgument(nodeName);
			case FUNCTION:
				return new FunctionArgument(nodeName);
			case INT_RANGE:
				return new IntegerRangeArgument(nodeName);
			case ITEMSTACK:
				return new ItemStackArgument(nodeName);
			case ITEMSTACK_PREDICATE:
				return new ItemStackPredicateArgument(nodeName);
			case LITERAL:
				break;
			case LOCATION:
				return new LocationArgument(nodeName, LocationType.BLOCK_POSITION);
			case LOCATION_2D:
				return new Location2DArgument(nodeName, LocationType.BLOCK_POSITION);
			case LOOT_TABLE:
				return new LootTableArgument(nodeName);
			case MATH_OPERATION:
				return new MathOperationArgument(nodeName);
			case MULTI_LITERAL:
				break;
			case NBT_COMPOUND:
				return new NBTCompoundArgument(nodeName);
			case OBJECTIVE:
				return new ObjectiveArgument(nodeName);
			case OBJECTIVE_CRITERIA:
				return new ObjectiveCriteriaArgument(nodeName);
			case PARTICLE:
				return new ParticleArgument(nodeName);
			case PLAYER:
				return new PlayerArgument(nodeName);
			case POTION_EFFECT:
				return new PotionEffectArgument(nodeName);
			case RECIPE:
				return new RecipeArgument(nodeName);
			case ROTATION:
				return new RotationArgument(nodeName);
			case SCOREBOARD_SLOT:
				return new ScoreboardSlotArgument(nodeName);
			case SCORE_HOLDER:
				return new ScoreHolderArgument(nodeName, ScoreHolderType.SINGLE);
			case SOUND:
				return new SoundArgument(nodeName);
			case TEAM:
				return new TeamArgument(nodeName);
			case TIME:
				return new TimeArgument(nodeName);
			case UUID:
				return new UUIDArgument(nodeName);
			case PRIMITIVE_BOOLEAN:
				return new BooleanArgument(nodeName);
			case PRIMITIVE_DOUBLE:
				break;
			case PRIMITIVE_FLOAT:
				break;
			case PRIMITIVE_GREEDY_STRING:
				return new GreedyStringArgument(nodeName);
			case PRIMITIVE_INTEGER:
				break;
			case PRIMITIVE_LONG:
				break;
			case PRIMITIVE_STRING:
				return new StringArgument(nodeName);
			case PRIMITIVE_TEXT:
				return new TextArgument(nodeName);
			default:
				break;
			}
			
			return null;
			
		}
		return null;
	}
	
}
