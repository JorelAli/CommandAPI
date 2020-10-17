package dev.jorel.commandapi;

import dev.jorel.commandapi.exceptions.UnsupportedVersionException;
import dev.jorel.commandapi.nms.NMS;
import dev.jorel.commandapi.nms.NMS_1_13;
import dev.jorel.commandapi.nms.NMS_1_13_1;
import dev.jorel.commandapi.nms.NMS_1_13_2;
import dev.jorel.commandapi.nms.NMS_1_14;
import dev.jorel.commandapi.nms.NMS_1_14_3;
import dev.jorel.commandapi.nms.NMS_1_14_4;
import dev.jorel.commandapi.nms.NMS_1_15;
import dev.jorel.commandapi.nms.NMS_1_16_R1;
import dev.jorel.commandapi.nms.NMS_1_16_R2;

public interface CommandAPIVersionHandler {

	public static NMS<?> getNMS(String version) {
		switch (version) {
		case "1.13":
			return new NMS_1_13();
		case "1.13.1":
			return new NMS_1_13_1();
		case "1.13.2":
			return new NMS_1_13_2();
		case "1.14":
		case "1.14.1":
		case "1.14.2":
			return new NMS_1_14();
		case "1.14.3":
			return new NMS_1_14_3();
		case "1.14.4":
			return new NMS_1_14_4();
		case "1.15":
		case "1.15.1":
		case "1.15.2":
			return new NMS_1_15();
		case "1.16.1":
			return new NMS_1_16_R1();
		case "1.16.2":
		case "1.16.3":
			return new NMS_1_16_R2();
		default:
			throw new UnsupportedVersionException("This version of Minecraft is unsupported: " + version);
		}
	}

}
