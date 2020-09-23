package dev.jorel.commandapi.arguments;

import org.bukkit.scoreboard.Team;

import dev.jorel.commandapi.CommandAPIHandler;

/**
 * An argument that represents the name of a scoreboard Team
 */
public class TeamArgument extends SafeOverrideableArgument<Team> {

	/**
	 * A Team argument. Represents a scoreboard Team
	 */
	public TeamArgument(String nodeName) {
		super(nodeName, CommandAPIHandler.getNMS()._ArgumentScoreboardTeam(), Team::getName);
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
