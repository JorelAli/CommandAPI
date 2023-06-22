package dev.jorel.commandapi;

import java.util.concurrent.atomic.AtomicReference;

import dev.jorel.commandapi.nms.NMS_1_19_4_R3;
import dev.jorel.commandapi.nms.NMS_1_20_R1;
import dev.jorel.commandapi.test.MockNMS;

/**
 * This file handles loading the correct platform implementation. The CommandAPIVersionHandler
 * file within the commandapi-core module is NOT used at run time. Instead, the platform modules
 * replace this class with their own version that handles loads the correct class for their version
 */
public interface CommandAPIVersionHandler {
	
	static AtomicReference<Version> version = new AtomicReference<>();
	
	public static void setVersion(Version version) {
		CommandAPIVersionHandler.version.set(version);
	}
	
	public static enum Version {
		MINECRAFT_1_20,
		MINECRAFT_1_19_4;
	}
	
	// The only important thing we care about.
	static CommandAPIPlatform<?, ?, ?> getPlatform() {
		if(CommandAPIVersionHandler.version.get() == null) {
			System.out.println("Using default version 1.19.4");
			return new MockNMS(new NMS_1_19_4_R3());
		} else {
			return new MockNMS(switch(CommandAPIVersionHandler.version.get()) {
				case MINECRAFT_1_20 -> new NMS_1_20_R1();
				case MINECRAFT_1_19_4 -> new NMS_1_19_4_R3();
//				case "Minecraft_1_19_2" -> new NMS_1_19_1_R1();
//				case "Minecraft_1_18" -> new NMS_1_18_R1();
//				case "Minecraft_1_17" -> new NMS_1_17();
//				case "Minecraft_1_16_5" -> new NMS_1_16_R3();
				default -> throw new IllegalArgumentException("Unsupported version value: " + CommandAPIVersionHandler.version.get());
			});
		}
	}
}
