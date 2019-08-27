package io.github.jorelali.commandapi.api.arguments;

import com.mojang.brigadier.arguments.ArgumentType;

import io.github.jorelali.commandapi.api.CommandAPIHandler;
import io.github.jorelali.commandapi.api.CommandPermission;
import io.github.jorelali.commandapi.api.Location2D;

@SuppressWarnings("unchecked")
public class Location2DArgument implements Argument, OverrideableSuggestions {
	
	ArgumentType<?> rawType;
	
	/**
	 * A Location argument. Represents Minecraft locations
	 */
	public Location2DArgument() {
		this(LocationType.PRECISE_POSITION);
	}
	
	/**
	 * A Location argument. Represents Minecraft locations
	 */
	public Location2DArgument(LocationType type) {
		locationType = type;
		switch(type) {
			case BLOCK_POSITION:
				rawType = CommandAPIHandler.getNMS()._ArgumentPosition2D();
				break;
			case PRECISE_POSITION:
				rawType = CommandAPIHandler.getNMS()._ArgumentVec2();
				break;
		}
	}
	
	private final LocationType locationType;
	
	@Override
	public <T> ArgumentType<T> getRawType() {
		return (ArgumentType<T>) rawType;
	}

	@Override
	public <V> Class<V> getPrimitiveType() {
		return (Class<V>) Location2D.class;
	}

	@Override
	public boolean isSimple() {
		return false;
	}
	
	public LocationType getLocationType() {
		return locationType;
	}
	
	private String[] suggestions;
	
	@Override
	public Location2DArgument overrideSuggestions(String... suggestions) {
		this.suggestions = suggestions;
		return this;
	}
	
	@Override
	public String[] getOverriddenSuggestions() {
		return suggestions;
	}
	
	private CommandPermission permission = null;
	
	@Override
	public Location2DArgument withPermission(CommandPermission permission) {
		this.permission = permission;
		return this;
	}

	@Override
	public CommandPermission getArgumentPermission() {
		return permission;
	}
	
	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.LOCATION_2D;
	}
}
