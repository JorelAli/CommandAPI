package io.github.jorelali.commandapi.api.arguments;

import org.bukkit.advancement.Advancement;

import io.github.jorelali.commandapi.api.SemiReflector;

@SuppressWarnings("unchecked")
public class AdvancementArgument implements Argument {

	com.mojang.brigadier.arguments.ArgumentType<?> rawType;
	
	/**
	 * An Advancement Argument. Represents a Bukkit Argument
	 */
	public AdvancementArgument() {
		rawType = SemiReflector.getNMSArgumentInstance("ArgumentMinecraftKeyRegistered");
	}
	
	@Override
	public <T> com.mojang.brigadier.arguments.ArgumentType<T> getRawType() {
		return (com.mojang.brigadier.arguments.ArgumentType<T>) rawType;
	}

	@Override
	public <V> Class<V> getPrimitiveType() {
		return (Class<V>) Advancement.class;
	}

	@Override
	public boolean isSimple() {
		return false;
	}
}
