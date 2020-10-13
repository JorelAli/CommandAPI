package dev.jorel.commandapi.arguments;

import com.mojang.brigadier.arguments.BoolArgumentType;

/**
 * An argument that represents primitive Java booleans
 */
public class BooleanArgument extends SafeOverrideableArgument<Boolean> {
	
	/**
	 * Constructs a Boolean argument with a given node name
	 * @param nodeName the name of the node for argument
	 */
	public BooleanArgument(String nodeName) {
		super(nodeName, BoolArgumentType.bool(), String::valueOf);
	}

	@Override
	public Class<?> getPrimitiveType() {
		return boolean.class;
	}
	
	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.PRIMITIVE_BOOLEAN;
	}
	
}
