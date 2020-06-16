package dev.jorel.commandapi.arguments;

import dev.jorel.commandapi.CommandAPIHandler;
import dev.jorel.commandapi.wrappers.FloatRange;

public class FloatRangeArgument extends Argument {

	/**
	 * A FloatRange argument that represents
	 */
	public FloatRangeArgument() {
		super(CommandAPIHandler.getNMS()._ArgumentFloatRange());
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
