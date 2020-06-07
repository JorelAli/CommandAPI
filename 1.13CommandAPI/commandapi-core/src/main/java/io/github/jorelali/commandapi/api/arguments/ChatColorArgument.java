package io.github.jorelali.commandapi.api.arguments;

import org.bukkit.ChatColor;

import com.mojang.brigadier.arguments.ArgumentType;

import io.github.jorelali.commandapi.api.CommandAPIHandler;
import io.github.jorelali.commandapi.api.CommandPermission;

@SuppressWarnings("unchecked")
public class ChatColorArgument implements Argument, OverrideableSuggestions {

	ArgumentType<?> rawType;
	
	/**
	 * A ChatColor argument. Represents a color or formatting for chat
	 */
	public ChatColorArgument() {
		rawType = CommandAPIHandler.getNMS()._ArgumentChatFormat();
	}
	
	@Override
	public <T> ArgumentType<T> getRawType() {
		return (ArgumentType<T>) rawType;
	}

	@Override
	public <V> Class<V> getPrimitiveType() {
		return (Class<V>) ChatColor.class;
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
	
	private CommandPermission permission = null;
	
	@Override
	public ChatColorArgument withPermission(CommandPermission permission) {
		this.permission = permission;
		return this;
	}

	@Override
	public CommandPermission getArgumentPermission() {
		return permission;
	}
	
	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.CHATCOLOR;
	}
}
