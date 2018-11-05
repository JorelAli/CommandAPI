package io.github.jorelali.commandapi.api.arguments;

import io.github.jorelali.commandapi.api.SemiReflector;
import io.github.jorelali.commandapi.api.exceptions.SpigotNotFoundException;
import net.md_5.bungee.api.chat.BaseComponent;


@SuppressWarnings("unchecked")
public class ChatComponentArgument implements Argument {

	com.mojang.brigadier.arguments.ArgumentType<?> rawType;
	
	/**
	 * A ChatComponent argument. Represents raw JSON text, used in Book MetaData, Chat and other various areas of Minecraft
	 * @see <a href="https://minecraft.gamepedia.com/Commands#Raw_JSON_text">Raw JSON text</a>
	 */
	public ChatComponentArgument() {
		
		try {
			Class.forName("org.spigotmc.SpigotConfig");
		} catch(ClassNotFoundException e) {
			throw new SpigotNotFoundException(this.getClass());
		}
		
		rawType = SemiReflector.getNMSArgumentInstance("ArgumentChatComponent");
	}
	
	@Override
	public <T> com.mojang.brigadier.arguments.ArgumentType<T> getRawType() {
		return (com.mojang.brigadier.arguments.ArgumentType<T>) rawType;
	}

	@Override
	public <V> Class<V> getPrimitiveType() {
		return (Class<V>) BaseComponent[].class;
	}

	@Override
	public boolean isSimple() {
		return false;
	}
}
