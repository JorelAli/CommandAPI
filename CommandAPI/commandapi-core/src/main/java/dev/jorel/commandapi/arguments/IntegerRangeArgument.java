package dev.jorel.commandapi.arguments;

import dev.jorel.commandapi.CommandAPIHandler;
import dev.jorel.commandapi.wrappers.IntegerRange;

/**
 * An argument that represents a range of integer values
 */
public class IntegerRangeArgument extends SafeOverrideableArgument<IntegerRange> {

	/**
	 * An IntegerRange argument. Represents a range of whole numbers
	 * @param nodeName the name of the node for this argument 
	 */
	public IntegerRangeArgument(String nodeName) {
		super(nodeName, CommandAPIHandler.getInstance().getNMS()._ArgumentIntRange(), IntegerRange::toString);
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
