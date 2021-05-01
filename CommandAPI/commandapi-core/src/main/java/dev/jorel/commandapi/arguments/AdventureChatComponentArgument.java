package dev.jorel.commandapi.arguments;

import dev.jorel.commandapi.CommandAPIHandler;
import dev.jorel.commandapi.exceptions.PaperAdventureNotFoundException;
import net.kyori.adventure.text.Component;

/**
 * An argument that represents raw JSON text
 */
public class AdventureChatComponentArgument extends Argument {

	/**
	 * Constructs a ChatComponnent argument with a given node name. Represents raw JSON text, used in Book MetaData, Chat and other various areas of Minecraft
	 * @see <a href="https://minecraft.gamepedia.com/Commands#Raw_JSON_text">Raw JSON text</a> 
	 * @param nodeName the name of the node for argument
	 */
	public AdventureChatComponentArgument(String nodeName) {
		super(nodeName, CommandAPIHandler.getInstance().getNMS()._ArgumentChatComponent());
		
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
		return CommandAPIArgumentType.ADVENTURE_CHAT_COMPONENT;
	}
}
