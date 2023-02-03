package dev.jorel.commandapi;

import dev.jorel.commandapi.nms.NMS_1_16_R3;
import dev.jorel.commandapi.nms.NMS_1_17;
import dev.jorel.commandapi.nms.NMS_1_18_R1;
import dev.jorel.commandapi.nms.NMS_1_19_1_R1;
import dev.jorel.commandapi.test.MockNMS;

/**
 * This file handles loading the correct platform implementation. The CommandAPIVersionHandler
 * file within the commandapi-core module is NOT used at run time. Instead, the platform modules
 * replace this class with their own version that handles loads the correct class for their version
 */
public interface CommandAPIVersionHandler {
	
	static final String profileId = System.getProperty("profileId");
	
	static CommandAPIPlatform<?, ?, ?> getPlatform() {
		if(profileId == null) {
			System.out.println("Using default version 1.19.2");
			return new MockNMS(new NMS_1_19_1_R1());
		} else {
			return new MockNMS(switch(profileId) {
				case "Minecraft_1_19_2" -> new NMS_1_19_1_R1();
				case "Minecraft_1_18" -> new NMS_1_18_R1();
				case "Minecraft_1_17" -> new NMS_1_17();
				case "Minecraft_1_16_5" -> new NMS_1_16_R3();
				default -> throw new IllegalArgumentException("Unexpected value: " + System.getProperty("profileId"));
			});
		}
	}
	
	public static MCVersion getVersion() {
		if(profileId == null) {
			System.out.println("Using default version 1.19.2");
			return MCVersion.V1_19_2;
		} else {
			return switch(profileId) {
				case "Minecraft_1_19_2" -> MCVersion.V1_19_2;
				case "Minecraft_1_18" -> MCVersion.V1_18;
				case "Minecraft_1_17" -> MCVersion.V1_17;
				case "Minecraft_1_16_5" -> MCVersion.V1_16_5;
				default -> throw new IllegalArgumentException("Unexpected value: " + System.getProperty("profileId"));
			};
		}
	}
}
