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

import java.util.Objects;

/**
 * A class to represent the yaw and pitch rotation in degrees
 */
public class Rotation {

	private final float yaw;
	private final float pitch;

	/**
	 * Constructs a Rotation with a given yaw and pitch
	 * 
	 * @param yaw   the yaw of this rotation in degrees
	 * @param pitch the pitch of this rotation in degrees
	 */
	public Rotation(float yaw, float pitch) {
		this.yaw = yaw;
		this.pitch = pitch;
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
	 * Gets the pitch of this rotation, measured in degrees.
	 * 
	 * @return this rotation's pitch
	 */
	public float getPitch() {
		return this.pitch;
	}

	/**
	 * Normalizes the given yaw angle to a value between +/-180 degrees.
	 * 
	 * @return the normalized yaw in degrees
	 */
	public float getNormalizedYaw() {
		float normalizedYaw = yaw % 360.0F;
		if (normalizedYaw >= 180.0F) {
			normalizedYaw -= 360.0F;
		} else if (normalizedYaw < -180.0F) {
			normalizedYaw += 360.0F;
		}

		return normalizedYaw;
	}

	/**
	 * Normalizes the given pitch angle to a value between +/-90 degrees.
	 * 
	 * @return the normalized pitch in degrees
	 */
	public float getNormalizedPitch() {
		return pitch > 90.0F ? 90.0F : Math.max(pitch, -90.0F);
	}
	
	/**
	 * @return the Minecraft string value of this {@link Rotation}, in the form "{@code <yaw> <pitch>}"
	 */
	@Override
	public String toString() {
		return yaw + " " + pitch;
	}

	@Override
	public int hashCode() {
		return Objects.hash(pitch, yaw);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Rotation other)) {
			return false;
		}
		return Float.floatToIntBits(pitch) == Float.floatToIntBits(other.pitch) &&
			Float.floatToIntBits(yaw) == Float.floatToIntBits(other.yaw);
	}

}
