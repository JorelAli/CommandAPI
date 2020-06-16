package dev.jorel.commandapi.arguments;

import java.util.function.Function;

import org.bukkit.command.CommandSender;

import com.mojang.brigadier.arguments.ArgumentType;

import dev.jorel.commandapi.CommandPermission;

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
	
	private Function<CommandSender, String[]> suggestions = null;
	
	@Override
	public final Argument overrideSuggestions(String... suggestions) {
		this.suggestions = (c) -> suggestions;
		return this;
	}
	
	@Override
	public final Argument overrideSuggestions(Function<CommandSender, String[]> suggestions) {
		this.suggestions = suggestions;
		return this;
	}
	
	@Override
	public final Function<CommandSender, String[]> getOverriddenSuggestions() {
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