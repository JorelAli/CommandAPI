package dev.jorel.commandapi.arguments;

import dev.jorel.commandapi.CommandAPIHandler;

/**
 * An argument that represents a duration of time in ticks
 */
public class TimeArgument extends Argument {
	
	/**
	 * A Time argument. Represents the number of in game ticks 
	 */
	public TimeArgument() {
		super(CommandAPIHandler.getNMS()._ArgumentTime());
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
