package dev.jorel.commandapi.arguments;

import dev.jorel.commandapi.CommandAPIHandler;
import dev.jorel.commandapi.wrappers.IntegerRange;

/**
 * An argument that represents a range of integer values
 */
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
