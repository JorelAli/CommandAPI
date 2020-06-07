package io.github.jorelali.commandapi.api.arguments;

import io.github.jorelali.commandapi.api.CommandAPIHandler;
import io.github.jorelali.commandapi.api.wrappers.FloatRange;

public class FloatRangeArgument extends Argument {

	/**
	 * A Time argument. Represents the number of ingame ticks 
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
