package dev.jorel.commandapi.arguments;

import dev.jorel.commandapi.CommandAPIHandler;

/**
 * An argument that represents a yaw angle, measured in degrees with float
 * number. -180.0 for due north, -90.0 for due east, 0.0 for due south, 90.0 for
 * due west, to 179.9 for just west of due north, before wrapping back around to
 * -180.0. Tilde notation can be used to specify a rotation relative to the
 * executor's yaw angle.
 */
public class AngleArgument extends SafeOverrideableArgument<Float> {

	/**
	 * Constructs an AngleArgument with a given node name
	 * @param nodeName the name of the node for argument
	 */
	public AngleArgument(String nodeName) {
		super(nodeName, CommandAPIHandler.getInstance().getNMS()._ArgumentAngle(), String::valueOf);
	}

	@Override
	public Class<?> getPrimitiveType() {
		return float.class;
	}
	
	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.ANGLE;
	}
}
