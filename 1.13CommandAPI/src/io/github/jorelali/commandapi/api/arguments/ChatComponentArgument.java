package io.github.jorelali.commandapi.api.arguments;

import java.lang.reflect.InvocationTargetException;

import org.bukkit.Bukkit;

import com.mojang.brigadier.arguments.ArgumentType;

import net.md_5.bungee.api.chat.BaseComponent;

@SuppressWarnings("unchecked")
public class ChatComponentArgument implements Argument {

	com.mojang.brigadier.arguments.ArgumentType<?> rawType;
	
	/**
	 * A ChatComponent argument. Represents some sort of chat related thing which only exists in Spigot
	 * 
	 */
	public ChatComponentArgument() {
		
		try {
			Class.forName("org.spigotmc.SpigotConfig");
		} catch(ClassNotFoundException e) {
			Bukkit.getLogger().warning("Spigot is not supportedby this server. ChatComponentArgument cannot be used!");
			e.printStackTrace();
		}
		
		try {
			
			rawType = (ArgumentType<?>) ArgumentUtil.getNMS("ArgumentChatComponent").getDeclaredMethod("a").invoke(null);
		} catch (IllegalAccessException | ClassNotFoundException | IllegalArgumentException | SecurityException | InvocationTargetException | NoSuchMethodException e) {
			e.printStackTrace();
		}
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
