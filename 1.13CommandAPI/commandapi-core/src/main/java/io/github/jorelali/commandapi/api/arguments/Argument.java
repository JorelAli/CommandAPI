package io.github.jorelali.commandapi.api.arguments;

import com.mojang.brigadier.arguments.ArgumentType;

import io.github.jorelali.commandapi.api.CommandPermission;

public abstract class Argument implements OverrideableSuggestions<Argument> {
		
	
	public abstract Class<?> getPrimitiveType();
	public abstract CommandAPIArgumentType getArgumentType();
	
	////////////////////////
	// Raw Argument Types //
	////////////////////////
	
	private final ArgumentType<?> rawType;
	
	protected Argument(ArgumentType<?> rawType) {
		this.rawType = rawType;
	}
	
	@SuppressWarnings("unchecked")
	public final <T> ArgumentType<T> getRawType() {
		return (ArgumentType<T>) rawType;
	}
	
	/////////////////
	// Suggestions //
	/////////////////
	
	private DynamicSuggestions suggestions;
	
	public final Argument overrideSuggestions(String... suggestions) {
		this.suggestions = mkSuggestions(suggestions);
		return this;
	}
	
	public final Argument overrideSuggestions(DynamicSuggestions suggestions) {
		this.suggestions = suggestions;
		return this;
	}
	
	public final DynamicSuggestions getOverriddenSuggestions() {
		return suggestions;
	}
	
	/////////////////
	// Permissions //
	/////////////////
	
	private CommandPermission permission = null;
	
	public final Argument withPermission(CommandPermission permission) {
		this.permission = permission;
		return this;
	}

	public final CommandPermission getArgumentPermission() {
		return permission;
	}
		
}