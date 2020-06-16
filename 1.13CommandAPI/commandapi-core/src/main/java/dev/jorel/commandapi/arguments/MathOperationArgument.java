package dev.jorel.commandapi.arguments;

import java.util.function.IntBinaryOperator;

import dev.jorel.commandapi.CommandAPIHandler;

public class MathOperationArgument extends Argument {

	/**
	 * An EntityType argument. Represents the type of an Entity
	 */
	public MathOperationArgument() {
		super(CommandAPIHandler.getNMS()._ArgumentMathOperation());
	}
	
	@Override
	public Class<?> getPrimitiveType() {
		return IntBinaryOperator.class;
	}
	
	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.MATH_OPERATION;
	}
}
