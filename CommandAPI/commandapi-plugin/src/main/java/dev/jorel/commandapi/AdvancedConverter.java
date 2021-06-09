/*******************************************************************************
 * Copyright 2018, 2021 Jorel Ali (Skepter) - MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *******************************************************************************/
package dev.jorel.commandapi;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.plugin.java.JavaPlugin;

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

	private static final Pattern ARGUMENT_PATTERN = Pattern.compile("<(\\w+)>\\[([a-z:_]+|(?:[0-9\\.]+)?\\.\\.(?:[0-9\\.]+)?)\\]");
	private static final Pattern LITERAL_PATTERN = Pattern.compile("\\((\\w+(?:\\|\\w+)*)\\)");
	
	private final JavaPlugin plugin;
	private final String command;
	private int argumentIndex = 1;
	
	public AdvancedConverter(JavaPlugin plugin, String command) {
		this.plugin = plugin;
		this.command = command;
	}
	
	public AdvancedConverter(String command) {
		this.plugin = null;
		this.command = command;
	}
	
	public void convert() {
		String commandName = command.split(" ")[0];
		List<Argument> arguments;
		try {
			arguments = parseArguments(command);
		} catch (UnknownArgumentException | InvalidNumberException e) {
			CommandAPI.logError(e.getMessage());
			return;
		}
		if(arguments.size() == 0) {
			Converter.convert(plugin, commandName);
		} else {
			Converter.convert(plugin, commandName, arguments);
		}
		
	}
	
	public void convertCommand() {
		String commandName = command.split(" ")[0];
		List<Argument> arguments;
		try {
			arguments = parseArguments(command);
		} catch (UnknownArgumentException | InvalidNumberException e) {
			CommandAPI.logError(e.getMessage());
			return;
		}
		if(arguments.size() == 0) {
			Converter.convert(commandName);
		} else {
			Converter.convert(commandName, arguments);
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
	
	private Argument parseDefinedArgumentType(String argumentType, String nodeName) throws UnknownArgumentException {
		return switch(CommandAPIArgumentType.fromInternal(argumentType)) {
		case ADVANCEMENT             -> new AdvancementArgument(nodeName);
		case ANGLE                   -> new AngleArgument(nodeName);
		case AXIS                    -> new AxisArgument(nodeName);
		case BIOME                   -> new BiomeArgument(nodeName);
		case BLOCKSTATE              -> new BlockStateArgument(nodeName);
		case BLOCK_PREDICATE         -> new BlockPredicateArgument(nodeName);
		case CHAT                    -> new ChatArgument(nodeName);
		case CHATCOLOR               -> new ChatColorArgument(nodeName);
		case CHAT_COMPONENT          -> new ChatComponentArgument(nodeName);
		case ENCHANTMENT             -> new EnchantmentArgument(nodeName);
		case ENTITY_SELECTOR         -> new EntitySelectorArgument(nodeName, EntitySelector.ONE_ENTITY);
		case ENTITY_TYPE             -> new EntityTypeArgument(nodeName);
		case ENVIRONMENT             -> new EnvironmentArgument(nodeName);
		case FLOAT_RANGE             -> new FloatRangeArgument(nodeName);
		case FUNCTION                -> new FunctionArgument(nodeName);
		case INT_RANGE               -> new IntegerRangeArgument(nodeName);
		case ITEMSTACK               -> new ItemStackArgument(nodeName);
		case ITEMSTACK_PREDICATE     -> new ItemStackPredicateArgument(nodeName);
		case LOCATION                -> new LocationArgument(nodeName, LocationType.BLOCK_POSITION);
		case LOCATION_2D             -> new Location2DArgument(nodeName, LocationType.BLOCK_POSITION);
		case LOOT_TABLE              -> new LootTableArgument(nodeName);
		case MATH_OPERATION          -> new MathOperationArgument(nodeName);
		case NBT_COMPOUND            -> new NBTCompoundArgument(nodeName);
		case OBJECTIVE               -> new ObjectiveArgument(nodeName);
		case OBJECTIVE_CRITERIA      -> new ObjectiveCriteriaArgument(nodeName);
		case PARTICLE                -> new ParticleArgument(nodeName);
		case PLAYER                  -> new PlayerArgument(nodeName);
		case POTION_EFFECT           -> new PotionEffectArgument(nodeName);
		case RECIPE                  -> new RecipeArgument(nodeName);
		case ROTATION                -> new RotationArgument(nodeName);
		case SCOREBOARD_SLOT         -> new ScoreboardSlotArgument(nodeName);
		case SCORE_HOLDER            -> new ScoreHolderArgument(nodeName, ScoreHolderType.SINGLE);
		case SOUND                   -> new SoundArgument(nodeName);
		case TEAM                    -> new TeamArgument(nodeName);
		case TIME                    -> new TimeArgument(nodeName);
		case UUID                    -> new UUIDArgument(nodeName);
		case PRIMITIVE_BOOLEAN       -> new BooleanArgument(nodeName);
		case PRIMITIVE_DOUBLE        -> new DoubleArgument(nodeName);
		case PRIMITIVE_FLOAT         -> new FloatArgument(nodeName);
		case PRIMITIVE_GREEDY_STRING -> new GreedyStringArgument(nodeName);
		case PRIMITIVE_INTEGER       -> new IntegerArgument(nodeName);
		case PRIMITIVE_LONG          -> new LongArgument(nodeName);
		case PRIMITIVE_STRING        -> new StringArgument(nodeName);
		case PRIMITIVE_TEXT          -> new TextArgument(nodeName);
		case LITERAL, 
			 MULTI_LITERAL,
			 CUSTOM                  -> throw new UnknownArgumentException(argumentType);
		default                      -> throw new UnknownArgumentException(argumentType);
		};
	}
	
	private Argument parseArgument(String argument) throws UnknownArgumentException, InvalidNumberException {
		Matcher literalMatcher = LITERAL_PATTERN.matcher(argument);
		Matcher argumentMatcher = ARGUMENT_PATTERN.matcher(argument);
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
				// We have a few edge cases to handle
				return switch(argumentType) {
				case "api:entity"     -> new EntitySelectorArgument(nodeName, EntitySelector.ONE_ENTITY);
				case "api:entities"   -> new EntitySelectorArgument(nodeName, EntitySelector.MANY_ENTITIES);
				case "api:player"     -> new EntitySelectorArgument(nodeName, EntitySelector.ONE_PLAYER);
				case "api:players"    -> new EntitySelectorArgument(nodeName, EntitySelector.MANY_PLAYERS);
				case "minecraft:vec3" -> new LocationArgument(nodeName, LocationType.PRECISE_POSITION);
				case "minecraft:vec2" -> new Location2DArgument(nodeName, LocationType.PRECISE_POSITION);
				default               -> parseDefinedArgumentType(argumentType, nodeName);
				};
			}
		} else {
			throw new UnknownArgumentException(argument);
		}
	}
	
}
