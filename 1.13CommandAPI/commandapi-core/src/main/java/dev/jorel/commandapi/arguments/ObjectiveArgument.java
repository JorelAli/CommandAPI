package dev.jorel.commandapi.arguments;

import org.bukkit.scoreboard.Objective;

import dev.jorel.commandapi.CommandAPIHandler;

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
