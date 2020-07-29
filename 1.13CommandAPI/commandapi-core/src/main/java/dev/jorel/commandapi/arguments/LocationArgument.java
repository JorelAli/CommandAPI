package dev.jorel.commandapi.arguments;

import java.util.function.BiFunction;
import java.util.function.Function;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;

import dev.jorel.commandapi.CommandAPIHandler;

/**
 * An argument that represents the Bukkit Location object
 */
public class LocationArgument extends Argument implements ISafeOverrideableSuggestions<Location> {
	
	/**
	 * A Location argument. Represents Minecraft locations
	 */
	public LocationArgument() {
		this(LocationType.PRECISE_POSITION);
	}
	
	/**
	 * A Location argument. Represents Minecraft locations
	 * @param type the location type of this location, either LocationType.BLOCK_POSITION or LocationType.PRECISE_POSITION
	 */
	public LocationArgument(LocationType type) {
		super(type == LocationType.BLOCK_POSITION ? CommandAPIHandler.getNMS()._ArgumentPosition()
				: CommandAPIHandler.getNMS()._ArgumentVec3());
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
		return Location.class;
	}
	
	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.LOCATION;
	}

	@Override
	public Argument safeOverrideSuggestions(Location... suggestions) {
		if(locationType == LocationType.BLOCK_POSITION) {
			return super.overrideSuggestions(sMap0((Location l) -> l.getBlockX() + " " + l.getBlockY() + " " + l.getBlockZ(), suggestions));
		} else {
			return super.overrideSuggestions(sMap0((Location l) -> l.getX() + " " + l.getY() + " " + l.getZ(), suggestions));
		}
	}

	@Override
	public Argument safeOverrideSuggestions(Function<CommandSender, Location[]> suggestions) {
		if(locationType == LocationType.BLOCK_POSITION) {
			return super.overrideSuggestions(sMap1((Location l) -> l.getBlockX() + " " + l.getBlockY() + " " + l.getBlockZ(), suggestions));
		} else {
			return super.overrideSuggestions(sMap1((Location l) -> l.getX() + " " + l.getY() + " " + l.getZ(), suggestions));
		}
	}

	@Override
	public Argument safeOverrideSuggestions(BiFunction<CommandSender, Object[], Location[]> suggestions) {
		if(locationType == LocationType.BLOCK_POSITION) {
			return super.overrideSuggestions(sMap2((Location l) -> l.getBlockX() + " " + l.getBlockY() + " " + l.getBlockZ(), suggestions));
		} else {
			return super.overrideSuggestions(sMap2((Location l) -> l.getX() + " " + l.getY() + " " + l.getZ(), suggestions));
		}
	}
}
