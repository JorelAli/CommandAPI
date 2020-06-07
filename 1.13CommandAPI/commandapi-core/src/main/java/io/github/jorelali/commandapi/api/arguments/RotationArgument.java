package io.github.jorelali.commandapi.api.arguments;

import io.github.jorelali.commandapi.api.CommandAPIHandler;
import io.github.jorelali.commandapi.api.wrappers.Rotation;

public class RotationArgument extends Argument {

	/**
	 * A Rotation argument. Represents pitch and yaw
	 */
	public RotationArgument() {
		super(CommandAPIHandler.getNMS()._ArgumentRotation());
	}
	
	@Override
	public Class<?> getPrimitiveType() {
		return Rotation.class;
	}
	
	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.ROTATION;
	}
}
