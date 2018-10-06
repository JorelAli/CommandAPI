package io.github.jorelali.commandapi.api.arguments;

import org.bukkit.entity.Player;

import com.mojang.brigadier.arguments.ArgumentType;

@SuppressWarnings("unchecked")
public class PlayerArgument implements Argument {

	com.mojang.brigadier.arguments.ArgumentType<?> rawType;
	
	/**
	 * A Player argument. Produces a single player, regardless of whether @a, @p, @r or @e is used.
	 */
	public PlayerArgument() {
		try {
			rawType = (ArgumentType<?>) ArgumentUtil.getNMS("ArgumentProfile").newInstance();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | IllegalArgumentException | SecurityException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public <T> com.mojang.brigadier.arguments.ArgumentType<T> getRawType() {
		return (com.mojang.brigadier.arguments.ArgumentType<T>) rawType;
	}

	@Override
	public <V> Class<V> getPrimitiveType() {
		return (Class<V>) Player.class;
	}

	@Override
	public boolean isSimple() {
		return false;
	}
}
