package io.github.jorelali.commandapi.api.arguments;

import java.util.function.IntBinaryOperator;

import io.github.jorelali.commandapi.api.CommandAPIHandler;

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
