package dev.jorel.commandapi.arguments;

import dev.jorel.commandapi.CommandAPIHandler;
import dev.jorel.commandapi.exceptions.PaperAdventureNotFoundException;
import dev.jorel.commandapi.exceptions.SpigotNotFoundException;
import net.md_5.bungee.api.chat.BaseComponent;

/**
 * An argument that represents raw JSON text
 */
public class ChatComponentArgument extends Argument {

	/**
	 * Constructs a ChatComponent argument with a given node name. Represents raw JSON text, used in Book MetaData, Chat and other various areas of Minecraft
	 * @see <a href="https://minecraft.gamepedia.com/Commands#Raw_JSON_text">Raw JSON text</a> 
	 * @param nodeName the name of the node for argument
	 */
	public ChatComponentArgument(String nodeName) {
		super(nodeName, CommandAPIHandler.getInstance().getNMS()._ArgumentChatComponent());
		
		if(CommandAPIHandler.getInstance().usePaperAdventure()) {
			try {
				Class.forName("net.kyori.adventure.text.Component");
			} catch(ClassNotFoundException e) {
				throw new PaperAdventureNotFoundException(this.getClass());
			}
		} else {
			try {
				Class.forName("org.spigotmc.SpigotConfig");
			} catch(ClassNotFoundException e) {
				throw new SpigotNotFoundException(this.getClass());
			}
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
