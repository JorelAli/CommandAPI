package io.github.jorelali.commandapi.api.arguments;

import io.github.jorelali.commandapi.api.CommandAPIHandler;
import io.github.jorelali.commandapi.api.exceptions.SpigotNotFoundException;
import net.md_5.bungee.api.chat.BaseComponent;

public class ChatArgument extends Argument implements GreedyArgument {
	
	/**
	 * A Chat argument. Represents fancy greedy strings that can parse entity selectors
	 */
	public ChatArgument() {
		super(CommandAPIHandler.getNMS()._ArgumentChat());
		
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
		return CommandAPIArgumentType.CHAT;
	}
}
