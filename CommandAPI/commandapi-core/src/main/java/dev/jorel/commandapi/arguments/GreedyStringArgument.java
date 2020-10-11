package dev.jorel.commandapi.arguments;

import com.mojang.brigadier.arguments.StringArgumentType;

/**
 * An argument that represents arbitrary strings
 */
public class GreedyStringArgument extends SafeOverrideableArgument<String> implements IGreedyArgument {
	
	/**
	 * A string argument for a string of any length
	 * @param nodeName the name of the node for this argument
	 */
	public GreedyStringArgument(String nodeName) {
		super(nodeName, StringArgumentType.greedyString(), s -> s);
	}

	@Override
	public Class<?> getPrimitiveType() {
		return String.class;
	}

	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.PRIMITIVE_GREEDY_STRING;
	}
}
