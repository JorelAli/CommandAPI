package io.github.jorelali.commandapi.api.arguments;

import org.bukkit.potion.PotionEffectType;

import io.github.jorelali.commandapi.api.SemiReflector;

@SuppressWarnings("unchecked")
public class PotionEffectArgument implements Argument, OverrideableSuggestions {

	com.mojang.brigadier.arguments.ArgumentType<?> rawType;
	
	/**
	 * A PotionEffect argument. Represents status/potion effects 
	 */
	public PotionEffectArgument() {
		rawType = SemiReflector.getNMSArgumentInstance("ArgumentMobEffect");
	}
	
	@Override
	public <T> com.mojang.brigadier.arguments.ArgumentType<T> getRawType() {
		return (com.mojang.brigadier.arguments.ArgumentType<T>) rawType;
	}

	@Override
	public <V> Class<V> getPrimitiveType() {
		return (Class<V>) PotionEffectType.class;
	}

	@Override
	public boolean isSimple() {
		return false;
	}
	
	private String[] suggestions;
	
	@Override
	public PotionEffectArgument overrideSuggestions(String... suggestions) {
		this.suggestions = suggestions;
		return this;
	}
	
	@Override
	public String[] getOverriddenSuggestions() {
		return suggestions;
	}
}
