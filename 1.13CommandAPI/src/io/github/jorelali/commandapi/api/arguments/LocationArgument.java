package io.github.jorelali.commandapi.api.arguments;

import org.bukkit.Location;

import com.mojang.brigadier.arguments.ArgumentType;

import io.github.jorelali.commandapi.api.CommandPermission;
import io.github.jorelali.commandapi.api.CommandAPIHandler;

@SuppressWarnings("unchecked")
public class LocationArgument implements Argument, OverrideableSuggestions {

	public enum LocationType {
		/**
		 * Represents the integer coordinates of a block, for example: (10, 70, -19)
		 */
		BLOCK_POSITION, 
		
		/**
		 * Represents the exact coordinates of a position, for example: (10.24, 70.00, -18.79)
		 */
		PRECISE_POSITION;
	}
	
	ArgumentType<?> rawType;
	
	/**
	 * A Location argument. Represents Minecraft locations
	 */
	public LocationArgument() {
		this(LocationType.PRECISE_POSITION);
	}
	
	/**
	 * A Location argument. Represents Minecraft locations
	 */
	public LocationArgument(LocationType type) {
		locationType = type;
		switch(type) {
			case BLOCK_POSITION:
				rawType = CommandAPIHandler.getNMS()._ArgumentPosition();
				break;
			case PRECISE_POSITION:
				rawType = CommandAPIHandler.getNMS()._ArgumentVec3();
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
		return (Class<V>) Location.class;
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
	public LocationArgument overrideSuggestions(String... suggestions) {
		this.suggestions = suggestions;
		return this;
	}
	
	@Override
	public String[] getOverriddenSuggestions() {
		return suggestions;
	}
	
	private CommandPermission permission = CommandPermission.NONE;
	
	@Override
	public LocationArgument withPermission(CommandPermission permission) {
		this.permission = permission;
		return this;
	}

	@Override
	public CommandPermission getArgumentPermission() {
		return permission;
	}
	
	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.LOCATION;
	}
}
