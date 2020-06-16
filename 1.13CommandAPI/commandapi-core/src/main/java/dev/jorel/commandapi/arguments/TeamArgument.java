package dev.jorel.commandapi.arguments;

import org.bukkit.scoreboard.Team;

import dev.jorel.commandapi.CommandAPIHandler;

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
