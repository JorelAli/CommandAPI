package io.github.jorelali.commandapi.api.arguments;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;

import io.github.jorelali.commandapi.api.CommandPermission;

@SuppressWarnings("unchecked")
public abstract class ZArg implements Argument, OverrideableSuggestions {

	ArgumentType<?> rawType;
	
	@Override
	public <T> ArgumentType<T> getRawType() {
		return (ArgumentType<T>) rawType;
	}
	
	/////////////////
	// Suggestions //
	/////////////////
	
	private DynamicSuggestions suggestions;
	
	@Override
	public ZArg overrideSuggestions(String... suggestions) {
		this.suggestions = mkSuggestions(suggestions);
		return this;
	}
	
	@Override
	public ZArg overrideSuggestions(DynamicSuggestions suggestions) {
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
	public ZArg withPermission(CommandPermission permission) {
		this.permission = permission;
		return this;
	}

	@Override
	public CommandPermission getArgumentPermission() {
		return permission;
	}
}
