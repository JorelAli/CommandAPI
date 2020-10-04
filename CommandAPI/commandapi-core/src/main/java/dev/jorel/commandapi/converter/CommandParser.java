package dev.jorel.commandapi.converter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.CommandAPIArgumentType;
import dev.jorel.commandapi.arguments.MultiLiteralArgument;

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
			
			switch(CommandAPIArgumentType.fromInternal(argumentType)) {
			case ADVANCEMENT:
				break;
			case ANGLE:
				break;
			case AXIS:
				break;
			case BIOME:
				break;
			case BLOCKSTATE:
				break;
			case BLOCK_PREDICATE:
				break;
			case CHAT:
				break;
			case CHATCOLOR:
				break;
			case CHAT_COMPONENT:
				break;
			case CUSTOM:
				break;
			case ENCHANTMENT:
				break;
			case ENTITY_SELECTOR:
				break;
			case ENTITY_TYPE:
				break;
			case ENVIRONMENT:
				break;
			case FLOAT_RANGE:
				break;
			case FUNCTION:
				break;
			case INT_RANGE:
				break;
			case ITEMSTACK:
				break;
			case ITEMSTACK_PREDICATE:
				break;
			case LITERAL:
				break;
			case LOCATION:
				break;
			case LOCATION_2D:
				break;
			case LOOT_TABLE:
				break;
			case MATH_OPERATION:
				break;
			case MULTI_LITERAL:
				break;
			case NBT_COMPOUND:
				break;
			case OBJECTIVE:
				break;
			case OBJECTIVE_CRITERIA:
				break;
			case PARTICLE:
				break;
			case PLAYER:
				break;
			case POTION_EFFECT:
				break;
			case RECIPE:
				break;
			case ROTATION:
				break;
			case SCOREBOARD_SLOT:
				break;
			case SCORE_HOLDER:
				break;
			case SIMPLE_TYPE:
				break;
			case SOUND:
				break;
			case TEAM:
				break;
			case TIME:
				break;
			case UUID:
				break;
			default:
				break;
			}
			
		}
		return null;
	}
	
}
