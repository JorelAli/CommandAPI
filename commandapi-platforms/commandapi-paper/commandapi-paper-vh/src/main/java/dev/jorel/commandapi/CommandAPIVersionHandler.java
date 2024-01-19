package dev.jorel.commandapi;

import dev.jorel.commandapi.exceptions.UnsupportedVersionException;
import dev.jorel.commandapi.nms.PaperNMS_1_16_R3;
import dev.jorel.commandapi.nms.PaperNMS_1_17;
import dev.jorel.commandapi.nms.PaperNMS_1_17_R1;
import dev.jorel.commandapi.nms.PaperNMS_1_18_R1;
import dev.jorel.commandapi.nms.PaperNMS_1_18_R2;
import dev.jorel.commandapi.nms.PaperNMS_1_19_1_R1;
import dev.jorel.commandapi.nms.PaperNMS_1_19_3_R2;
import dev.jorel.commandapi.nms.PaperNMS_1_19_4_R3;
import dev.jorel.commandapi.nms.PaperNMS_1_19_R1;
import dev.jorel.commandapi.nms.PaperNMS_1_20_R1;
import dev.jorel.commandapi.nms.PaperNMS_1_20_R2;
import dev.jorel.commandapi.nms.PaperNMS_1_20_R3;
import dev.jorel.commandapi.nms.PaperNMS_1_20_R4;
import dev.jorel.commandapi.nms.PaperNMS_1_21_R1;
import org.bukkit.Bukkit;

public interface CommandAPIVersionHandler {

	static CommandAPIPlatform<?, ?, ?> getPlatform() {
		if (CommandAPI.getConfiguration().shouldUseLatestNMSVersion()) {
			return new PaperNMS_1_21_R1();
		}
		String version = Bukkit.getBukkitVersion().split("-")[0];
		return switch (version) {
			case "1.16.5" -> new PaperNMS_1_16_R3();
			case "1.17" -> new PaperNMS_1_17();
			case "1.17.1" -> new PaperNMS_1_17_R1();
			case "1.18", "1.18.1" -> new PaperNMS_1_18_R1();
			case "1.18.2" -> new PaperNMS_1_18_R2();
			case "1.19" -> new PaperNMS_1_19_R1();
			case "1.19.1", "1.19.2" -> new PaperNMS_1_19_1_R1();
			case "1.19.3" -> new PaperNMS_1_19_3_R2();
			case "1.19.4" -> new PaperNMS_1_19_4_R3();
			case "1.20", "1.20.1" -> new PaperNMS_1_20_R1();
			case "1.20.2" -> new PaperNMS_1_20_R2();
			case "1.20.3", "1.20.4" -> new PaperNMS_1_20_R3();
			case "1.20.5", "1.20.6" -> new PaperNMS_1_20_R4();
			case "1.21" -> new PaperNMS_1_21_R1();
			default -> throw new UnsupportedVersionException(version);
		};
	}

}
