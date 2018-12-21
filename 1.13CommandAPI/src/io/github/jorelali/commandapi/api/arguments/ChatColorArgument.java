package io.github.jorelali.commandapi.api.arguments;

import org.bukkit.ChatColor;

import io.github.jorelali.commandapi.api.SemiReflector;

@SuppressWarnings("unchecked")
public class ChatColorArgument implements Argument, OverrideableSuggestions {

	com.mojang.brigadier.arguments.ArgumentType<?> rawType;
	
	/**
	 * A ChatColor argument. Represents a color or formatting for chat
	 */
	public ChatColorArgument() {
		rawType = SemiReflector.getNMSArgumentInstance("ArgumentChatFormat");
	}
	
	@Override
	public <T> com.mojang.brigadier.arguments.ArgumentType<T> getRawType() {
		return (com.mojang.brigadier.arguments.ArgumentType<T>) rawType;
	}

	@Override
	public <V> Class<V> getPrimitiveType() {
		return (Class<V>) ChatColor.class;
	}

	@Override
	public boolean isSimple() {
		return false;
	}
	
	private String[] suggestions;
	
	@Override
	public ChatColorArgument overrideSuggestions(String... suggestions) {
		this.suggestions = suggestions;
		return this;
	}
	
	@Override
	public String[] getOverriddenSuggestions() {
		return suggestions;
	}
}
