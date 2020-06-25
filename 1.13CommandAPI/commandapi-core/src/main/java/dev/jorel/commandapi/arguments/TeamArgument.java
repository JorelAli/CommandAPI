package dev.jorel.commandapi.arguments;

import dev.jorel.commandapi.CommandAPIHandler;

/**
 * An argument that represents the name of a scoreboard Team
 */
public class TeamArgument extends Argument {

	/**
	 * A Team argument. Represents a scoreboard Team
	 */
	public TeamArgument() {
		super(CommandAPIHandler.getNMS()._ArgumentScoreboardTeam());
	}

	@Override
	public Class<?> getPrimitiveType() {
		return String.class;
	}
	
	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.TEAM;
	}
}
