package io.github.jorelali.commandapi.api.arguments;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;

import io.github.jorelali.commandapi.api.CommandPermission;

@SuppressWarnings("unchecked")
public class BooleanArgument implements Argument, OverrideableSuggestions {

	ArgumentType<?> rawType;
	
	/**
	 * An Boolean argument
	 */
	public BooleanArgument() {
		rawType = BoolArgumentType.bool();
	}
	@Override
	public <T> ArgumentType<T> getRawType() {
		return (ArgumentType<T>) rawType;
	}

	@Override
	public <V> Class<V> getPrimitiveType() {
		return (Class<V>) boolean.class;
	}
	
	private String[] suggestions;
	
	@Override
	public BooleanArgument overrideSuggestions(String... suggestions) {
		this.suggestions = suggestions;
		return this;
	}
	
	@Override
	public String[] getOverriddenSuggestions() {
		return suggestions;
	}
	
	private CommandPermission permission = null;
	
	@Override
	public BooleanArgument withPermission(CommandPermission permission) {
		this.permission = permission;
		return this;
	}

	@Override
	public CommandPermission getArgumentPermission() {
		return permission;
	}
	
	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.SIMPLE_TYPE;
	}
	
}
