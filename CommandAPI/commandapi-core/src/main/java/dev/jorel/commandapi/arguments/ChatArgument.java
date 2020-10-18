package dev.jorel.commandapi.arguments;

import dev.jorel.commandapi.CommandAPIHandler;
import dev.jorel.commandapi.exceptions.SpigotNotFoundException;
import net.md_5.bungee.api.chat.BaseComponent;

/**
 * An argument that represents chat with entity selectors
 */
public class ChatArgument extends Argument implements IGreedyArgument {

	/**
	 * Constructs a Chat argument with a given node name. Represents fancy greedy
	 * strings that can parse entity selectors
	 * 
	 * @param nodeName the name of the node for argument
	 */
	public ChatArgument(String nodeName) {
		super(nodeName, CommandAPIHandler.getInstance().getNMS()._ArgumentChat());
		
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
