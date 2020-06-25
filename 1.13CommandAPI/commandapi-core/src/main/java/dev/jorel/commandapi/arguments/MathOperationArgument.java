package dev.jorel.commandapi.arguments;

import dev.jorel.commandapi.CommandAPIHandler;
import dev.jorel.commandapi.wrappers.MathOperation;

public class MathOperationArgument extends Argument {

	/**
	 * A MathOperation argument. Represents a math operation (e.g. addition, subtraction etc.)
	 */
	public MathOperationArgument() {
		super(CommandAPIHandler.getNMS()._ArgumentMathOperation());
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
