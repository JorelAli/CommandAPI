package dev.jorel.commandapi.arguments;

import org.bukkit.Location;

import dev.jorel.commandapi.CommandAPIHandler;

public class LocationArgument extends Argument {
	
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
		super(type == LocationType.BLOCK_POSITION ? CommandAPIHandler.getNMS()._ArgumentPosition()
				: CommandAPIHandler.getNMS()._ArgumentVec3());
		locationType = type;
	}
	
	private final LocationType locationType;

	public LocationType getLocationType() {
		return locationType;
	}
	
	@Override
	public Class<?> getPrimitiveType() {
		return Location.class;
	}
	
	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.LOCATION;
	}
}
