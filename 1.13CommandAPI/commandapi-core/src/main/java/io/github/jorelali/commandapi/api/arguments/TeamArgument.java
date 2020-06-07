package io.github.jorelali.commandapi.api.arguments;

import org.bukkit.scoreboard.Team;

import io.github.jorelali.commandapi.api.CommandAPIHandler;

public class TeamArgument extends Argument {

	/**
	 * A Team argument. Represents a scoreboard Team
	 */
	public TeamArgument() {
		super(CommandAPIHandler.getNMS()._ArgumentScoreboardTeam());
	}

	@Override
	public Class<?> getPrimitiveType() {
		return Team.class;
	}
	
	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.TEAM;
	}
}
