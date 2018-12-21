package io.github.jorelali.commandapi.api.arguments;

import org.bukkit.entity.Player;

import io.github.jorelali.commandapi.api.SemiReflector;

@SuppressWarnings("unchecked")
public class PlayerArgument implements Argument, OverrideableSuggestions {

	com.mojang.brigadier.arguments.ArgumentType<?> rawType;
	
	/**
	 * A Player argument. Produces a single player, regardless of whether @a, @p, @r or @e is used.
	 */
	public PlayerArgument() {
		rawType = SemiReflector.getNMSArgumentInstance("ArgumentProfile");
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
	
	private String[] suggestions;
	
	@Override
	public PlayerArgument overrideSuggestions(String... suggestions) {
		this.suggestions = suggestions;
		return this;
	}
	
	@Override
	public String[] getOverriddenSuggestions() {
		return suggestions;
	}
}
