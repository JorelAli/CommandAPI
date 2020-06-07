package io.github.jorelali.commandapi.api.arguments;

import com.mojang.brigadier.arguments.ArgumentType;

import io.github.jorelali.commandapi.api.CommandAPIHandler;
import io.github.jorelali.commandapi.api.exceptions.SpigotNotFoundException;
import net.md_5.bungee.api.chat.BaseComponent;


public class ChatComponentArgument extends Argument {

	ArgumentType<?> rawType;
	
	/**
	 * A ChatComponent argument. Represents raw JSON text, used in Book MetaData, Chat and other various areas of Minecraft
	 * @see <a href="https://minecraft.gamepedia.com/Commands#Raw_JSON_text">Raw JSON text</a>
	 */
	public ChatComponentArgument() {
		super(CommandAPIHandler.getNMS()._ArgumentChatComponent());
		
		try {
			Class.forName("org.spigotmc.SpigotConfig");
		} catch(ClassNotFoundException e) {
			throw new SpigotNotFoundException(this.getClass());
		}
	}
	
	@Override
	public Class<?> getPrimitiveType() {
		return BaseComponent[].class;
	}
	
	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.CHAT_COMPONENT;
	}
}
