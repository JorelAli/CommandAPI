package io.github.jorelali.commandapi.api.arguments;

import org.bukkit.Location;

import io.github.jorelali.commandapi.api.SemiReflector;

@SuppressWarnings("unchecked")
public class LocationArgument implements Argument, OverrideableSuggestions {

	com.mojang.brigadier.arguments.ArgumentType<?> rawType;
	
	/**
	 * A Location argument. Represents Minecraft locations
	 */
	public LocationArgument() {
		rawType = SemiReflector.getNMSArgumentInstance("ArgumentVec3");
	}
	
	@Override
	public <T> com.mojang.brigadier.arguments.ArgumentType<T> getRawType() {
		return (com.mojang.brigadier.arguments.ArgumentType<T>) rawType;
	}

	@Override
	public <V> Class<V> getPrimitiveType() {
		return (Class<V>) Location.class;
	}

	@Override
	public boolean isSimple() {
		return false;
	}
	
	private String[] suggestions;
	
	@Override
	public LocationArgument overrideSuggestions(String... suggestions) {
		this.suggestions = suggestions;
		return this;
	}
	
	@Override
	public String[] getOverriddenSuggestions() {
		return suggestions;
	}
}
