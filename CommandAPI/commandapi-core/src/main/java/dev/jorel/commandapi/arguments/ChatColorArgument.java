package dev.jorel.commandapi.arguments;

import java.util.function.BiFunction;
import java.util.function.Function;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import dev.jorel.commandapi.CommandAPIHandler;

/**
 * An argument that represents the Bukkit ChatColor object
 */
public class ChatColorArgument extends Argument implements ISafeOverrideableSuggestions<ChatColor> {

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
	
	public Argument safeOverrideSuggestions(ChatColor... suggestions) {
		return super.overrideSuggestions(sMap0(((Function<ChatColor, String>) ChatColor::name).andThen(String::toLowerCase), suggestions));
	}

	public Argument safeOverrideSuggestions(Function<CommandSender, ChatColor[]> suggestions) {
		return super.overrideSuggestions(sMap1(((Function<ChatColor, String>) ChatColor::name).andThen(String::toLowerCase), suggestions));
	}

	public Argument safeOverrideSuggestions(BiFunction<CommandSender, Object[], ChatColor[]> suggestions) {
		return super.overrideSuggestions(sMap2(((Function<ChatColor, String>) ChatColor::name).andThen(String::toLowerCase), suggestions));
	}
}
