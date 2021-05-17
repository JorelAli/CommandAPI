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

/**
 * A class to represent the pitch and yaw rotation in degrees
 */
public class Rotation {

	private final float pitch;
	private final float yaw;

	/**
	 * Constructs a Rotation with a given pitch and yaw
	 * 
	 * @param pitch the pitch of this rotation in degrees
	 * @param yaw   the yaw of this rotation in degrees
	 */
	public Rotation(float pitch, float yaw) {
		this.pitch = pitch;
		this.yaw = yaw;
	}

	/**
	 * Gets the pitch of this rotation, measured in degrees.
	 * 
	 * @return this rotation's pitch
	 */
	public float getPitch() {
		return this.pitch;
	}

	/**
	 * Gets the yaw of this location, measured in degrees.
	 * 
	 * @return this rotation's yaw
	 */
	public float getYaw() {
		return this.yaw;
	}

	/**
	 * Normalizes the given pitch angle to a value between +/-90 degrees.
	 * 
	 * @return the normalized pitch in degrees
	 */
	public float getNormalizedPitch() {
		return Location.normalizePitch(this.pitch);
	}

	/**
	 * Normalizes the given yaw angle to a value between +/-180 degrees.
	 * 
	 * @return the normalized yaw in degrees
	 */
	public float getNormalizedYaw() {
		return Location.normalizeYaw(this.yaw);
	}
	
	/**
	 * Returns the Minecraft string value of this Rotation
	 * @return the Minecraft string value of this Rotation
	 */
	@Override
	public String toString() {
		return yaw + " " + pitch;
	}

}
