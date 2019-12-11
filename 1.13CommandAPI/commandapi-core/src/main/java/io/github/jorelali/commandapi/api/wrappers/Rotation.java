package io.github.jorelali.commandapi.api.wrappers;

import org.bukkit.Location;

public class Rotation {

	private final float pitch;
	private final float yaw;
	
	public Rotation(float pitch, float yaw) {
		this.pitch = pitch;
		this.yaw = yaw;
	}
	
	public float getPitch() {
		return this.pitch;
	}
	
	public float getYaw() {
		return this.yaw;
	}
	
	public float getNormalizedPitch() {
		return Location.normalizePitch(this.pitch);
	}
	
	public float getNormalizedYaw() {
		return Location.normalizeYaw(this.yaw);
	}
	
}
