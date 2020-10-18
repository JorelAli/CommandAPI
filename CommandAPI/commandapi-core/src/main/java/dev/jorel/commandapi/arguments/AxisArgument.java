package dev.jorel.commandapi.arguments;

import java.util.EnumSet;

import org.bukkit.Axis;

import dev.jorel.commandapi.CommandAPIHandler;

/**
 * An argument that represents x, y and z axes as an EnumSet of Axis
 */
public class AxisArgument extends SafeOverrideableArgument<EnumSet<Axis>> {

	/**
	 * Constructs an AxisArgument with a given node name. Represents the axes x, y and z
	 * @param nodeName the name of the node for argument
	 */
	public AxisArgument(String nodeName) {
		super(nodeName, CommandAPIHandler.getInstance().getNMS()._ArgumentAxis(), e -> e.stream().map(Axis::name).map(String::toLowerCase).reduce(String::concat).get());
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
