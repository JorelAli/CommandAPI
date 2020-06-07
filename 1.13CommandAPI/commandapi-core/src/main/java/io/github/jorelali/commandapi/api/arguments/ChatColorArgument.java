package io.github.jorelali.commandapi.api.arguments;

import org.bukkit.ChatColor;

import io.github.jorelali.commandapi.api.CommandAPIHandler;

public class ChatColorArgument extends Argument {

	/**
	 * A ChatColor argument. Represents a color or formatting for chat
	 */
	public ChatColorArgument() {
		super(CommandAPIHandler.getNMS()._ArgumentChatFormat());
	}
	
	@Override
	public Class<?> getPrimitiveType() {
		return ChatColor.class;
	}
	
	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.CHATCOLOR;
	}
}
