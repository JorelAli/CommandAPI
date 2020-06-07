package io.github.jorelali.commandapi.api.arguments;

import io.github.jorelali.commandapi.api.CommandAPIHandler;
import io.github.jorelali.commandapi.api.wrappers.IntegerRange;

public class IntegerRangeArgument extends Argument {

	/**
	 * A Time argument. Represents the number of ingame ticks 
	 */
	public IntegerRangeArgument() {
		super(CommandAPIHandler.getNMS()._ArgumentIntRange());
	}
	
	@Override
	public Class<?> getPrimitiveType() {
		return IntegerRange.class;
	}
	
	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.INT_RANGE;
	}
}
