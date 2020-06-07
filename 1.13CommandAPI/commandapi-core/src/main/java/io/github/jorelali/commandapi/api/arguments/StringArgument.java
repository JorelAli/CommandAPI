package io.github.jorelali.commandapi.api.arguments;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;

import io.github.jorelali.commandapi.api.CommandPermission;

@SuppressWarnings("unchecked")
public class StringArgument implements Argument, OverrideableSuggestions {

	ArgumentType<?> rawType;
	
	/**
	 * A string argument for one word
	 */
	public StringArgument() {
		rawType = StringArgumentType.word();
	}
	
	@Override
	public <T> ArgumentType<T> getRawType() {
		return (ArgumentType<T>) rawType;
	}

	@Override
	public <V> Class<V> getPrimitiveType() {
		return (Class<V>) String.class;
	}
	
	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.SIMPLE_TYPE;
	}
	
	/////////////////
	// Suggestions //
	/////////////////
	
	private DynamicSuggestions suggestions;
	
	@Override
	public StringArgument overrideSuggestions(String... suggestions) {
		this.suggestions = mkSuggestions(suggestions);
		return this;
	}
	
	@Override
	public StringArgument overrideSuggestions(DynamicSuggestions suggestions) {
		this.suggestions = suggestions;
		return this;
	}
	
	@Override
	public DynamicSuggestions getOverriddenSuggestions() {
		return suggestions;
	}
	
	/////////////////
	// Permissions //
	/////////////////
	
	private CommandPermission permission = null;
	
	@Override
	public StringArgument withPermission(CommandPermission permission) {
		this.permission = permission;
		return this;
	}

	@Override
	public CommandPermission getArgumentPermission() {
		return permission;
	}
}
