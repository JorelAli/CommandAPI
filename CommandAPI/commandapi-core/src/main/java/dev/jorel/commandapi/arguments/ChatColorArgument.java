package dev.jorel.commandapi.arguments;

import java.util.function.Function;

import org.bukkit.ChatColor;

import dev.jorel.commandapi.CommandAPIHandler;

/**
 * An argument that represents the Bukkit ChatColor object
 */
public class ChatColorArgument extends SafeOverrideableArgument<ChatColor> {

	/**
	 * A ChatColor argument. Represents a color or formatting for chat
	 */
	public ChatColorArgument(String nodeName) {
		super(nodeName, CommandAPIHandler.getNMS()._ArgumentChatFormat(), ((Function<ChatColor, String>) ChatColor::name).andThen(String::toLowerCase));
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
