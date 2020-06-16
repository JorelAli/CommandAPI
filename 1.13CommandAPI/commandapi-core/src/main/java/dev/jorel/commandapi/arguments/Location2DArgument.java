package dev.jorel.commandapi.arguments;

import dev.jorel.commandapi.CommandAPIHandler;
import dev.jorel.commandapi.wrappers.Location2D;

public class Location2DArgument extends Argument {

	/**
	 * A Location argument. Represents Minecraft locations in 2D space
	 */
	public Location2DArgument() {
		this(LocationType.PRECISE_POSITION);
	}

	/**
	 * A Location argument. Represents Minecraft locations in 2D space
	 */
	public Location2DArgument(LocationType type) {
		super(type == LocationType.BLOCK_POSITION ? CommandAPIHandler.getNMS()._ArgumentPosition2D()
				: CommandAPIHandler.getNMS()._ArgumentVec2());
		locationType = type;
	}

	private final LocationType locationType;

	public LocationType getLocationType() {
		return locationType;
	}
	
	@Override
	public Class<?> getPrimitiveType() {
		return Location2D.class;
	}

	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.LOCATION_2D;
	}
}
