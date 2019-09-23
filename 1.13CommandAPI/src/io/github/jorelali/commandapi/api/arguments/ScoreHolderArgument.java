package io.github.jorelali.commandapi.api.arguments;

import java.util.Collection;

import com.mojang.brigadier.arguments.ArgumentType;

import io.github.jorelali.commandapi.api.CommandAPIHandler;
import io.github.jorelali.commandapi.api.CommandPermission;

@SuppressWarnings("unchecked")
public class ScoreHolderArgument implements Argument, OverrideableSuggestions {
	
	ArgumentType<?> rawType;
	private final boolean single;
	
	/**
	 * A Score Holder argument. Represents a collection of score holders
	 */
	public ScoreHolderArgument(ScoreHolderType type) {
		if(type == ScoreHolderType.SINGLE) {
			single = true;
		} else {
			single = false;
		}
		rawType = CommandAPIHandler.getNMS()._ArgumentScoreholder(single);
	}
	
	public boolean isSingle() {
		return this.single;
	}
	
	@Override
	public <T> ArgumentType<T> getRawType() {
		return (ArgumentType<T>) rawType;
	}

	@Override
	public <V> Class<V> getPrimitiveType() {
		return single ? (Class<V>) String.class : (Class<V>) Collection.class;
	}

	@Override
	public boolean isSimple() {
		return false;
	}
	
	private String[] suggestions;
	
	@Override
	public ScoreHolderArgument overrideSuggestions(String... suggestions) {
		this.suggestions = suggestions;
		return this;
	}
	
	@Override
	public String[] getOverriddenSuggestions() {
		return suggestions;
	}
	
	private CommandPermission permission = null;
	
	@Override
	public ScoreHolderArgument withPermission(CommandPermission permission) {
		this.permission = permission;
		return this;
	}

	@Override
	public CommandPermission getArgumentPermission() {
		return permission;
	}
	
	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.SCORE_HOLDER;
	}

	public static enum ScoreHolderType {
		SINGLE, MULTIPLE;
	}
}
