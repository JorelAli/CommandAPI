package dev.jorel.commandapi.arguments;

import dev.jorel.commandapi.CommandAPIHandler;
import dev.jorel.commandapi.exceptions.PaperAdventureNotFoundException;
import net.kyori.adventure.text.Component;

/**
 * An argument that represents chat with entity selectors
 */
public class AdventureChatArgument extends Argument implements IGreedyArgument {

	/**
	 * Constructs a Chat argument with a given node name. Represents fancy greedy
	 * strings that can parse entity selectors
	 * 
	 * @param nodeName the name of the node for argument
	 */
	public AdventureChatArgument(String nodeName) {
		super(nodeName, CommandAPIHandler.getInstance().getNMS()._ArgumentChat());
		
		try {
			Class.forName("net.kyori.adventure.text.Component");
		} catch(ClassNotFoundException e) {
			throw new PaperAdventureNotFoundException(this.getClass());
		}
	}

	@Override
	public Class<?> getPrimitiveType() {
		return Component.class;
	}
	
	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.ADVENTURE_CHAT;
	}
}
