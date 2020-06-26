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
	 * @param pitch the pitch of this rotation in degrees
	 * @param yaw the yaw of this rotation in degrees
	 */
	public Rotation(float pitch, float yaw) {
		this.pitch = pitch;
		this.yaw = yaw;
	}
	
	/**
	 * Gets the pitch of this rotation, measured in degrees.
	 * @return this rotation's pitch
	 */
	public float getPitch() {
		return this.pitch;
	}
	
	/**
	 * Gets the yaw of this location, measured in degrees.
	 * @return this rotation's yaw
	 */
	public float getYaw() {
		return this.yaw;
	}
	
	/**
	 * TODO: Rotation documentation for normalized pitch
	 * @return
	 */
	public float getNormalizedPitch() {
		return Location.normalizePitch(this.pitch);
	}
	
	/**
	 * TODO: Rotation documentation for normalized yaw
	 * @return
	 */
	public float getNormalizedYaw() {
		return Location.normalizeYaw(this.yaw);
	}
	
}
