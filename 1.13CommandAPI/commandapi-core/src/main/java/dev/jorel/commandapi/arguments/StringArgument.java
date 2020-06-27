package dev.jorel.commandapi.arguments;

import java.util.Map;
import java.util.function.BiFunction;

import org.bukkit.command.CommandSender;

import com.mojang.brigadier.arguments.StringArgumentType;

/**
 * An argument that represents a simple String
 */
public class StringArgument extends Argument {

	/**
	 * A string argument for one word
	 */
	public StringArgument() {
		super(StringArgumentType.word());
	}

	@Override
	public Class<?> getPrimitiveType() {
		return String.class;
	}

	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.SIMPLE_TYPE;
	}
	
	public BiFunction<CommandSender, Map<String, Object>, String[]> getSpecialOverriddenSuggestions() {
		return (sender, argMap) -> {
			System.out.println(argMap);
			System.out.println(argMap.get("number"));
			return new String[] {};
		};		
	}
}
