package dev.jorel.commandapi.arguments;

import java.util.function.BiFunction;
import java.util.function.Function;

import org.bukkit.command.CommandSender;
import org.bukkit.scoreboard.Team;

import dev.jorel.commandapi.CommandAPIHandler;

/**
 * An argument that represents the name of a scoreboard Team
 */
public class TeamArgument extends Argument implements ISafeOverrideableSuggestions<Team> {

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

	@Override
	public Argument safeOverrideSuggestions(Team... suggestions) {
		return super.overrideSuggestions(sMap0(Team::getName, suggestions));
	}

	@Override
	public Argument safeOverrideSuggestions(Function<CommandSender, Team[]> suggestions) {
		return super.overrideSuggestions(sMap1(Team::getName, suggestions));
	}

	@Override
	public Argument safeOverrideSuggestions(BiFunction<CommandSender, Object[], Team[]> suggestions) {
		return super.overrideSuggestions(sMap2(Team::getName, suggestions));
	}
}
