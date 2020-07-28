package dev.jorel.commandapi.arguments;

import java.util.function.BiFunction;
import java.util.function.Function;

import org.bukkit.command.CommandSender;

import dev.jorel.commandapi.CommandAPIHandler;
import dev.jorel.commandapi.wrappers.Location2D;

/**
 * An argument that represents the Bukkit Location object in x and z directions
 */
public class Location2DArgument extends Argument implements ISafeOverrideableSuggestions<Location2D> {

	/**
	 * A Location argument. Represents Minecraft locations in 2D space. Defaults to LocationType.PRECISE_POSITION
	 */
	public Location2DArgument() {
		this(LocationType.PRECISE_POSITION);
	}

	/**
	 * A Location argument. Represents Minecraft locations in 2D space
	 * @param type the location type of this location, either LocationType.BLOCK_POSITION or LocationType.PRECISE_POSITION
	 */
	public Location2DArgument(LocationType type) {
		super(type == LocationType.BLOCK_POSITION ? CommandAPIHandler.getNMS()._ArgumentPosition2D()
				: CommandAPIHandler.getNMS()._ArgumentVec2());
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
	
	@Override
	public Argument safeOverrideSuggestions(Location2D... suggestions) {
		if(locationType == LocationType.BLOCK_POSITION) {
			super.suggestions = sMap0((Location2D l) -> l.getBlockX() + " " + l.getBlockZ(), suggestions);
		} else {
			super.suggestions = sMap0((Location2D l) -> l.getX() + " " + l.getZ(), suggestions);
		}
		return this;
	}

	@Override
	public Argument safeOverrideSuggestions(Function<CommandSender, Location2D[]> suggestions) {
		if(locationType == LocationType.BLOCK_POSITION) {
			super.suggestions = sMap1((Location2D l) -> l.getBlockX() + " " + l.getBlockZ(), suggestions);
		} else {
			super.suggestions = sMap1((Location2D l) -> l.getX() + " " + l.getZ(), suggestions);
		}
		return this;
	}

	@Override
	public Argument safeOverrideSuggestions(BiFunction<CommandSender, Object[], Location2D[]> suggestions) {
		if(locationType == LocationType.BLOCK_POSITION) {
			super.suggestions = sMap2((Location2D l) -> l.getBlockX() + " " + l.getBlockZ(), suggestions);
		} else {
			super.suggestions = sMap2((Location2D l) -> l.getX() + " " + l.getZ(), suggestions);
		}
		return this;
	}
}
