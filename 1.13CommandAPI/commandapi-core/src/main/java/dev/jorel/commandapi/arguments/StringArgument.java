package dev.jorel.commandapi.arguments;

import com.mojang.brigadier.arguments.StringArgumentType;

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
}
