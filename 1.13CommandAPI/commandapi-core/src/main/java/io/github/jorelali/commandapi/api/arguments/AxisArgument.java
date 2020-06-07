package io.github.jorelali.commandapi.api.arguments;

import java.util.EnumSet;

import io.github.jorelali.commandapi.api.CommandAPIHandler;

public class AxisArgument extends Argument {
	
	/**
	 * An Axis argument. Represents the axes x, y and z
	 */
	public AxisArgument() {
		super(CommandAPIHandler.getNMS()._ArgumentAxis());
	}

	@Override
	public Class<?> getPrimitiveType() {
		return EnumSet.class;
	}
	
	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.AXIS;
	}
}
