package dev.jorel.commandapi.converter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dev.jorel.commandapi.arguments.Argument;
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
			
			
		}
		return null;
	}
	
}
