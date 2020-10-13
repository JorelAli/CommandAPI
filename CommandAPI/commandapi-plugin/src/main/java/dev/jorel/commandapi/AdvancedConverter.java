package dev.jorel.commandapi;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.plugin.Plugin;

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
import dev.jorel.commandapi.arguments.FloatArgument;
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
import dev.jorel.commandapi.exceptions.InvalidNumberException;
import dev.jorel.commandapi.exceptions.UnknownArgumentException;

/**
 * A command parsing system that converts string arguments into something way more useful
 */
public class AdvancedConverter {

	private static Pattern argumentPattern = Pattern.compile("<(\\w+)>\\[([a-z:_]+|(?:[0-9\\.]+)?\\.\\.(?:[0-9\\.]+)?)\\]");
	private static Pattern literalPattern = Pattern.compile("\\((\\w+(?:\\|\\w+)*)\\)");
	
	private final Plugin plugin;
	private final String command;
	private int argumentIndex = 1;
	
	public AdvancedConverter(Plugin plugin, String command) {
		this.plugin = plugin;
		this.command = command;
	}
	
	public void convert() {
		String commandName = command.split(" ")[0];
		List<Argument> arguments;
		try {
			arguments = parseArguments(command);
		} catch (UnknownArgumentException | InvalidNumberException e) {
			CommandAPI.getLog().severe(e.getMessage());
			return;
		}
		if(arguments.size() == 0) {
			Converter.convert(plugin, commandName);
		} else {
			Converter.convert(plugin, commandName, arguments);
		}
	}
	
	/*
	 * plugins-to-convert: 
	 *   - Essentials: 
	 *     - speed <speed>[0..10]
	 *     - speed <target>[minecraft:game_profile]
	 *     - speed (walk|fly) <speed>[0..10]
	 *     - speed (walk|fly) <speed>[0..10] <target>[minecraft:game_profile]
	 */
	private List<Argument> parseArguments(String command) throws UnknownArgumentException, InvalidNumberException {
		List<Argument> arguments = new ArrayList<>();
		String[] parts = command.split(" ");
		for (argumentIndex = 1; argumentIndex < parts.length; argumentIndex++) {
			Argument argument = parseArgument(parts[argumentIndex]);
			if(argument != null) {
				arguments.add(argument);
			}
		}
		return arguments;
	}
	
	private boolean isRangeAnInteger(double value) {
		return value == (long) value;
	}
	
	private double parseValue(String bound) throws InvalidNumberException {
		try {
			return Double.parseDouble(bound);
		} catch(NumberFormatException e) {
			throw new InvalidNumberException(bound, command, argumentIndex);
		}
	}
	
	private Argument parseRange(String nodeName, String[] bounds) throws InvalidNumberException {		
		if(bounds.length == 1) {
			//x..
			double value = parseValue(bounds[0]);
			if(isRangeAnInteger(value)) {
				return new LongArgument(nodeName, (long) value);
			} else {
				return new DoubleArgument(nodeName, value);
			}
		} else if(bounds[0].length() == 0) {
			//..x
			double value = parseValue(bounds[1]);
			if(isRangeAnInteger(value)) {
				return new LongArgument(nodeName, Long.MIN_VALUE, (long) value);
			} else {
				return new DoubleArgument(nodeName, -Double.MAX_VALUE, value);
			}
		} else {
			//x..x
			double value0 = parseValue(bounds[0]);
			double value1 = parseValue(bounds[1]);
			if(!isRangeAnInteger(value0) || !isRangeAnInteger(value1)) {
				return new DoubleArgument(nodeName, value0, value1);
			} else {
				return new LongArgument(nodeName, (long) value0, (long) value1);
			}
		}
	}
	
	private Argument parseArgument(String argument) throws UnknownArgumentException, InvalidNumberException {
		Matcher literalMatcher = literalPattern.matcher(argument);
		Matcher argumentMatcher = argumentPattern.matcher(argument);
		if(literalMatcher.matches()) {
			//Parse literals
			String literals = literalMatcher.group(1);
			return new MultiLiteralArgument(literals.split("\\|"));
		} else if(argumentMatcher.matches()) {
			//Parse arguments
			String nodeName = argumentMatcher.group(1);
			String argumentType = argumentMatcher.group(2);
			
			if(argumentType.contains("..")) {
				//Parse ranges
				return parseRange(nodeName, argumentType.split("\\.\\."));
			} else {
				//Parse everything else
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
				case LOCATION:
					return new LocationArgument(nodeName, LocationType.BLOCK_POSITION);
				case LOCATION_2D:
					return new Location2DArgument(nodeName, LocationType.BLOCK_POSITION);
				case LOOT_TABLE:
					return new LootTableArgument(nodeName);
				case MATH_OPERATION:
					return new MathOperationArgument(nodeName);
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
					return new DoubleArgument(nodeName);
				case PRIMITIVE_FLOAT:
					return new FloatArgument(nodeName);
				case PRIMITIVE_GREEDY_STRING:
					return new GreedyStringArgument(nodeName);
				case PRIMITIVE_INTEGER:
					return new IntegerArgument(nodeName);
				case PRIMITIVE_LONG:
					return new LongArgument(nodeName);
				case PRIMITIVE_STRING:
					return new StringArgument(nodeName);
				case PRIMITIVE_TEXT:
					return new TextArgument(nodeName);
				case LITERAL:
				case MULTI_LITERAL:
				case CUSTOM:
				default:
					throw new UnknownArgumentException(argumentType);
				}
			}
		} else {
			throw new UnknownArgumentException(argument);
		}
	}
	
}
