package dev.jorel.commandapi.arguments;

import dev.jorel.commandapi.CommandAPIHandler;
import dev.jorel.commandapi.wrappers.MathOperation;

/**
 * An argument that represents Minecraft scoreboard math operations
 */
public class MathOperationArgument extends SafeOverrideableArgument<MathOperation> {

	/**
	 * A MathOperation argument. Represents a math operation (e.g. addition, subtraction etc.)
	 * @param nodeName the name of the node for this argument
	 */
	public MathOperationArgument(String nodeName) {
		super(nodeName, CommandAPIHandler.getInstance().getNMS()._ArgumentMathOperation(), MathOperation::toString);
	}
	
	@Override
	public Class<?> getPrimitiveType() {
		return MathOperation.class;
	}
	
	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.MATH_OPERATION;
	}
}
