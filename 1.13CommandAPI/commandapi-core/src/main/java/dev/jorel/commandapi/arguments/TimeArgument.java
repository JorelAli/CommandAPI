package dev.jorel.commandapi.arguments;

import dev.jorel.commandapi.CommandAPIHandler;

public class TimeArgument extends Argument {
	
	/**
	 * A Time argument. Represents the number of ingame ticks 
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
