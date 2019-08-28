package io.github.jorelali.commandapi.api.wrappers;

import org.bukkit.Location;
import org.bukkit.World;

public class Location2D extends Location {

	class Location2DException extends RuntimeException {
		
		private static final long serialVersionUID = 493399606975183058L;

		@Override
	    public String getMessage() {
			return "Cannot retrieve Y coordinate of a Location2D object";
	    }
		
	}
	
	public Location2D(World world, double x, double y, double z, float yaw, float pitch) {
		super(world, x, 0, z, yaw, pitch);
	}
	
	public Location2D(World world, double x, double y, double z) {
		super(world, x, 0, z);
	}
	
	public Location2D(World world, double x, double z) {
		super(world, x, 0, z);
	}
	
	@Override
	public double getY() {
		throw new Location2DException();
	}
	
	@Override
	public int getBlockY() {
		throw new Location2DException();
	}
	
	@Override
	public void setY(double y) {
		throw new Location2DException();
	}
	
}
