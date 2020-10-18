package dev.jorel.commandapi.arguments;

import dev.jorel.commandapi.CommandAPIHandler;
import dev.jorel.commandapi.wrappers.Time;

/**
 * An argument that represents a duration of time in ticks
 */
public class TimeArgument extends SafeOverrideableArgument<Time> {
	
	/**
	 * A Time argument. Represents the number of in game ticks
	 * @param nodeName the name of the node for this argument 
	 */
	public TimeArgument(String nodeName) {
		super(nodeName, CommandAPIHandler.getInstance().getNMS()._ArgumentTime(), Time::toString);
	}

	@Override
	public Class<?> getPrimitiveType() {
		return int.class;
	}
	
	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.TIME;
	}
}
