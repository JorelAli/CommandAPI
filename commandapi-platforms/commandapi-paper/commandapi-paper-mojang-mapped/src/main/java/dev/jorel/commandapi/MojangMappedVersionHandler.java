package dev.jorel.commandapi;

import dev.jorel.commandapi.exceptions.UnsupportedVersionException;
import org.bukkit.Bukkit;

/**
 * This file handles loading the correct mappings information. The MojangMappedVersionHandler
 * file within the commandapi-core module is not used on Paper, since Paper 1.20.5+ uses mojang mappings.
 */
public interface MojangMappedVersionHandler {
    static boolean isMojangMapped() {
		// TODO: Is it possible to consider `shouldUseLatestNMSVersion` here?
		//  Currently, the configuration isn't yet loaded when this method is called for the first time by
		//  SafeVarHandle, since CommandAPIHandler's static initializer uses SafeVarHandle for reflection:
		//    at dev.jorel.commandapi.CommandAPIHandler.<clinit>(CommandAPIHandler.java:87)
		//    at dev.jorel.commandapi.CommandAPI.onDisable(CommandAPI.java:168)
		//    at dev.jorel.commandapi.CommandAPI.<clinit>(CommandAPI.java:23)
		//    at dev.jorel.commandapi.CommandAPIMain.onLoad(CommandAPIMain.java:66)
		//  I *think* it's okay to assume mojang-mappings for an unknown version. When CommandAPIVersionHandler is used,
		//  it can properly use `shouldUseLatestNMSVersion` and throw UnsupportedVersionException if needed.
//		if (CommandAPI.getConfiguration().shouldUseLatestNMSVersion()) {
//			// 1.21
//			return true;
//		}
		String version = Bukkit.getBukkitVersion().split("-")[0];

		// TODO: Inspired by CommandAPIVersionHandler, but probably a better way to do this...
		return switch (version) {
			case "1.16.5", "1.17", "1.17.1", "1.18", "1.18.1", "1.18.2", "1.19", "1.19.1", "1.19.2", "1.19.3",
				 "1.19.4", "1.20", "1.20.1", "1.20.2", "1.20.3", "1.20.4" -> false;
			case "1.20.5", "1.20.6", "1.21" -> true;
//			default -> throw new UnsupportedVersionException(version);
			default -> true;
		};
    }
}
