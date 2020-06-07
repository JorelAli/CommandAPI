package io.github.jorelali.commandapi.api.arguments;

import org.bukkit.scoreboard.Objective;

import io.github.jorelali.commandapi.api.CommandAPIHandler;

public class ObjectiveArgument extends Argument {

	/**
	 * An Objective argument. Represents a scoreboard objective
	 */
	public ObjectiveArgument() {
		super(CommandAPIHandler.getNMS()._ArgumentScoreboardObjective());
	}

	@Override
	public Class<?> getPrimitiveType() {
		return Objective.class;
	}
	
	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.OBJECTIVE;
	}
}
