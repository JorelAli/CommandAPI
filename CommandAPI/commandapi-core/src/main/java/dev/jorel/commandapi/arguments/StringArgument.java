package dev.jorel.commandapi.arguments;

import com.mojang.brigadier.arguments.StringArgumentType;

/**
 * An argument that represents a simple String
 */
public class StringArgument extends SafeOverrideableArgument<String> {

	/**
	 * A string argument for one word
	 * @param nodeName the name of the node for this argument
	 */
	public StringArgument(String nodeName) {
		super(nodeName, StringArgumentType.word(), s -> s);
	}

	@Override
	public Class<?> getPrimitiveType() {
		return String.class;
	}

	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.PRIMITIVE_STRING;
	}
}
