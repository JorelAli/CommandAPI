package dev.jorel.commandapi.arguments;

import org.bukkit.ChatColor;

import dev.jorel.commandapi.CommandAPIHandler;

/**
 * An argument that represents the Bukkit ChatColor object
 */
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
