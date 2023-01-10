package dev.jorel.commandapi;

import java.util.function.Supplier;

import dev.jorel.commandapi.nms.NMS;
import dev.jorel.commandapi.nms.NMS_1_19_1_R1;
import dev.jorel.commandapi.test.MockNMS;

/**
 * This file handles loading the correct platform implementation. The CommandAPIVersionHandler
 * file within the commandapi-core module is NOT used at run time. Instead, the platform modules
 * replace this class with their own version that handles loads the correct class for thier version
 */
public interface CommandAPIVersionHandler {
	
	static final Supplier<NMS<?>> DEFAULT_NMS_IMPLEMENTATION = () -> new NMS_1_19_1_R1();
	
	static Supplier[] nmsImplementation = { DEFAULT_NMS_IMPLEMENTATION };
	
	static void setPlatform(Supplier<NMS<?>> nmsImplementation) {
		if(nmsImplementation == null) {
			nmsImplementation = DEFAULT_NMS_IMPLEMENTATION;
		}
		CommandAPIVersionHandler.nmsImplementation[0] = nmsImplementation;
	}
	
	static void resetPlatform() {
		CommandAPIVersionHandler.nmsImplementation[0] = DEFAULT_NMS_IMPLEMENTATION;
	}
	
	static CommandAPIPlatform<?, ?, ?> getPlatform() {
		return new MockNMS((NMS<?>) nmsImplementation[0].get());
	}
}
