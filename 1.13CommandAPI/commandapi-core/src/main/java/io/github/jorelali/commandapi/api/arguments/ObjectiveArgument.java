package io.github.jorelali.commandapi.api.arguments;

import org.bukkit.scoreboard.Objective;

import com.mojang.brigadier.arguments.ArgumentType;

import io.github.jorelali.commandapi.api.CommandAPIHandler;
import io.github.jorelali.commandapi.api.CommandPermission;

@SuppressWarnings("unchecked")
public class ObjectiveArgument extends Argument {

	ArgumentType<?> rawType;
	
	/**
	 * An Objective argument. Represents a scoreboard objective
	 */
	public ObjectiveArgument() {
		rawType = CommandAPIHandler.getNMS()._ArgumentScoreboardObjective();
	}
	
	@Override
	public <T> ArgumentType<T> getRawType() {
		return (ArgumentType<T>) rawType;
	}

	@Override
	public Class<?> getPrimitiveType() {
		return Objective.class;
	}

	@Override
	public boolean isSimple() {
		return false;
	}
	
	private String[] suggestions;
	
	@Override
	public ObjectiveArgument overrideSuggestions(String... suggestions) {
		this.suggestions = suggestions;
		return this;
	}
	
	@Override
	public String[] getOverriddenSuggestions() {
		return suggestions;
	}
	
	private CommandPermission permission = null;
	
	@Override
	public ObjectiveArgument withPermission(CommandPermission permission) {
		this.permission = permission;
		return this;
	}

	@Override
	public CommandPermission getArgumentPermission() {
		return permission;
	}
	
	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.OBJECTIVE;
	}
}
