package dev.jorel.commandapi.arguments;

import java.util.EnumSet;

import dev.jorel.commandapi.CommandAPIHandler;

/**
 * An argument that represents x, y and z axes as an EnumSet of Axis
 */
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
