/*******************************************************************************
 * Copyright 2018, 2020 Jorel Ali (Skepter) - MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *******************************************************************************/
package dev.jorel.commandapi.wrappers;

import org.bukkit.Location;
import org.bukkit.World;

/**
 * A class that represents a Location in the x and z directions. This class
 * extends Location, so can be used anywhere a regular Location could be used.
 */
public class Location2D extends Location {

	/**
	 * Constructs a Location2D location
	 * @param world the world in which this location resides
	 * @param x the x-coordinate of this location
	 * @param y the y-coordinate of this location
	 * @param z the z-coordinate of this location
	 * @param yaw the absolute rotation on the x-plane, in degrees
	 * @param pitch the absolute rotation on the y-plane, in degrees
	 */
	public Location2D(World world, double x, double y, double z, float yaw, float pitch) {
		super(world, x, 0, z, yaw, pitch);
	}

	/**
	 * Constructs a Location2D location
	 * @param world the world in which this location resides
	 * @param x the x-coordinate of this location
	 * @param y the y-coordinate of this location
	 * @param z the z-coordinate of this location
	 */
	public Location2D(World world, double x, double y, double z) {
		super(world, x, 0, z);
	}

	/**
	 * Constructs a Location2D location
	 * @param world the world in which this location resides
	 * @param x the x-coordinate of this location
	 * @param z the z-coordinate of this location
	 */
	public Location2D(World world, double x, double z) {
		super(world, x, 0, z);
	}

	/**
	 * @return A Location2DException
	 */
	@Override
	public double getY() {
		throw new Location2DException();
	}

	/**
	 * @return A Location2DException
	 */
	@Override
	public int getBlockY() {
		throw new Location2DException();
	}

	/**
	 * Throws a Location2DException
	 * @param y y-coordinate
	 */
	@Override
	public void setY(double y) {
		throw new Location2DException();
	}

	/**
	 * An exception caused when accessing or setting the y-coordinate of a Location2D
	 * @author jorel
	 *
	 */
	@SuppressWarnings("serial")
	class Location2DException extends RuntimeException {

		public Location2DException() {
			super("Cannot retrieve Y coordinate of a Location2D object");
		}

	}

}
