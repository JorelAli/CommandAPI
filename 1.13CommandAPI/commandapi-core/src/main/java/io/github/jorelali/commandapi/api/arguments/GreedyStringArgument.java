package io.github.jorelali.commandapi.api.arguments;

import com.mojang.brigadier.arguments.StringArgumentType;

public class GreedyStringArgument extends Argument implements GreedyArgument {
	
	/**
	 * A string argument for a string of any length
	 */
	public GreedyStringArgument() {
		super(StringArgumentType.greedyString());
	}

	@Override
	public Class<?> getPrimitiveType() {
		return String.class;
	}

	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.SIMPLE_TYPE;
	}
}
