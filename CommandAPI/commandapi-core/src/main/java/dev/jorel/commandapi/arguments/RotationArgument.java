package dev.jorel.commandapi.arguments;

import dev.jorel.commandapi.CommandAPIHandler;
import dev.jorel.commandapi.wrappers.Rotation;

/**
 * An argument that represents rotation as pitch and yaw
 */
public class RotationArgument extends SafeOverrideableArgument<Rotation> {

	/**
	 * A Rotation argument. Represents pitch and yaw
	 */
	public RotationArgument() {
		super(CommandAPIHandler.getNMS()._ArgumentRotation(), Rotation::toString);
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
