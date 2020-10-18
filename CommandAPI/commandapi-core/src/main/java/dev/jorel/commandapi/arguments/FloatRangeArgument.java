package dev.jorel.commandapi.arguments;

import dev.jorel.commandapi.CommandAPIHandler;
import dev.jorel.commandapi.wrappers.FloatRange;

/**
 * An argument that represents a range of float values
 */
public class FloatRangeArgument extends SafeOverrideableArgument<FloatRange> {

	/**
	 * A FloatRange argument that represents a range of floating-point values
	 * @param nodeName the name of the node for this argument
	 */
	public FloatRangeArgument(String nodeName) {
		super(nodeName, CommandAPIHandler.getInstance().getNMS()._ArgumentFloatRange(), FloatRange::toString);
	}

	@Override
	public Class<?> getPrimitiveType() {
		return FloatRange.class;
	}

	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.FLOAT_RANGE;
	}
}
