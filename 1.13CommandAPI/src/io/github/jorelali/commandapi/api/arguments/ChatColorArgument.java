package io.github.jorelali.commandapi.api.arguments;

import java.lang.reflect.InvocationTargetException;

import org.bukkit.ChatColor;

import com.mojang.brigadier.arguments.ArgumentType;

@SuppressWarnings("unchecked")
public class ChatColorArgument implements Argument {

	com.mojang.brigadier.arguments.ArgumentType<?> rawType;
	
	/**
	 * A ChatColor argument. Represents a color or formatting for chat
	 */
	public ChatColorArgument() {
		try {
			
			rawType = (ArgumentType<?>) ArgumentUtil.getNMS("ArgumentChatFormat").getDeclaredMethod("a").invoke(null);
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
		return (Class<V>) ChatColor.class;
	}

	@Override
	public boolean isSimple() {
		return false;
	}
}
