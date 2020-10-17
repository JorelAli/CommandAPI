package dev.jorel.commandapi.arguments;

import dev.jorel.commandapi.CommandAPIHandler;
import dev.jorel.commandapi.wrappers.Location2D;

/**
 * An argument that represents the Bukkit Location object in x and z directions
 */
public class Location2DArgument extends SafeOverrideableArgument<Location2D> {

	/**
	 * A Location argument. Represents Minecraft locations in 2D space. Defaults to LocationType.PRECISE_POSITION
	 * @param nodeName the name of the node for this argument
	 */
	public Location2DArgument(String nodeName) {
		this(nodeName, LocationType.PRECISE_POSITION);
	}

	/**
	 * A Location argument. Represents Minecraft locations in 2D space
	 * @param nodeName the name of the node for this argument
	 * @param type the location type of this location, either LocationType.BLOCK_POSITION or LocationType.PRECISE_POSITION
	 */
	public Location2DArgument(String nodeName, LocationType type) {
		super(nodeName, type == LocationType.BLOCK_POSITION ? CommandAPIHandler.getInstance().getNMS()._ArgumentPosition2D()
				: CommandAPIHandler.getInstance().getNMS()._ArgumentVec2(),
				type == LocationType.BLOCK_POSITION ? (Location2D l) -> l.getBlockX() + " " + l.getBlockZ()
						: (Location2D l) -> l.getX() + " " + l.getZ());
		locationType = type;
	}

	private final LocationType locationType;

	/**
	 * Returns whether this argument is LocationType.BLOCK_POSITION or LocationType.PRECISE_POSITION 
	 * @return the location type of this argument
	 */
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
