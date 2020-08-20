package dev.jorel.commandapi;

import java.lang.reflect.InvocationTargetException;

import dev.jorel.commandapi.exceptions.UnsupportedVersionException;
import dev.jorel.commandapi.nms.NMS;

/**
 * This file handles the NMS version to be loaded. The CommandAPIVersionHandler
 * file within the commandapi-core module is NOT used at compile time. Instead,
 * the commandapi-extra module is loaded instead, which doesn't use reflection to
 * load NMS instances.
 */
public interface CommandAPIVersionHandler {
	
	public static NMS getNMS(String version) {

		NMS nms = null;
		
		// Handle versioning
		try {
			switch (version) {
			case "1.13":
				nms = (NMS) Class.forName("dev.jorel.commandapi.nms.NMS_1_13").getDeclaredConstructor()
						.newInstance();
				break;
			case "1.13.1":
				nms = (NMS) Class.forName("dev.jorel.commandapi.nms.NMS_1_13_1").getDeclaredConstructor()
						.newInstance();
				break;
			case "1.13.2":
				nms = (NMS) Class.forName("dev.jorel.commandapi.nms.NMS_1_13_2").getDeclaredConstructor()
						.newInstance();
				break;
			case "1.14":
			case "1.14.1":
			case "1.14.2":
				nms = (NMS) Class.forName("dev.jorel.commandapi.nms.NMS_1_14").getDeclaredConstructor()
						.newInstance();
				break;
			case "1.14.3":
				nms = (NMS) Class.forName("dev.jorel.commandapi.nms.NMS_1_14_3").getDeclaredConstructor()
						.newInstance();
				break;
			case "1.14.4":
				nms = (NMS) Class.forName("dev.jorel.commandapi.nms.NMS_1_14_4").getDeclaredConstructor()
						.newInstance();
				break;
			case "1.15":
			case "1.15.1":
			case "1.15.2":
				nms = (NMS) Class.forName("dev.jorel.commandapi.nms.NMS_1_15").getDeclaredConstructor()
						.newInstance();
				break;
			default:
				throw new UnsupportedVersionException("This version of Minecraft is unsupported: " + version);
			}
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return nms;
	}
	
}
