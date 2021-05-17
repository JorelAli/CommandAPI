package dev.jorel.commandapi;

import dev.jorel.commandapi.exceptions.UnsupportedVersionException;
import dev.jorel.commandapi.nms.NMS;
import dev.jorel.commandapi.nms.NMS_1_16_R3;

public interface CommandAPIVersionHandler {

	public static NMS<?> getNMS(String version) {
		switch (version) {
		case "1.16.5":
			return new NMS_1_16_R3();
		default:
			throw new UnsupportedVersionException("This version of Minecraft is unsupported: " + version);
		}
	}

}
