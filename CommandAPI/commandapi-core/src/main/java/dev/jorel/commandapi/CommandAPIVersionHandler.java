package dev.jorel.commandapi;

import dev.jorel.commandapi.nms.NMS;

/**
 * This file handles the NMS version to be loaded. The CommandAPIVersionHandler
 * file within the commandapi-core module is NOT used at compile time. Instead,
 * the commandapi-vh module is loaded instead, which doesn't use reflection to
 * load NMS instances.
 */
public interface CommandAPIVersionHandler {
	
	public static <CommandListenerWrapper> NMS<CommandListenerWrapper> getNMS(String version) {
		throw new RuntimeException("You have the wrong copy of the CommandAPI! Make sure to use the one from https://github.com/JorelAli/CommandAPI/releases");
	}
	
}
