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
import dev.jorel.commandapi.nms.PaperNMS_1_21_R2;
import org.bukkit.Bukkit;

public interface CommandAPIVersionHandler {

	static LoadContext getPlatform() {
		String latestMajorVersion = "21"; // Change this for Minecraft's major update
		if (CommandAPI.getConfiguration().shouldUseLatestNMSVersion()) {
			return new LoadContext(new PaperNMS_1_21_R2(), () -> {
				CommandAPI.logWarning("Loading the CommandAPI with the latest and potentially incompatible NMS implementation.");
				CommandAPI.logWarning("While you may find success with this, further updates might be necessary to fully support the version you are using.");
			});
		} else {
			String version = Bukkit.getBukkitVersion().split("-")[0];
			CommandAPIPlatform<?, ?, ?> platform = switch (version) {
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
				case "1.21", "1.21.1" -> new PaperNMS_1_21_R1();
				case "1.21.2", "1.21.3" -> new PaperNMS_1_21_R2();
				default -> null;
			};
			if (platform != null) {
				return new LoadContext(platform);
			}
			if (CommandAPI.getConfiguration().shouldBeLenientForMinorVersions()) {
				String currentMajorVersion = version.split("\\.")[1];
				if (latestMajorVersion.equals(currentMajorVersion)) {
					return new LoadContext(new PaperNMS_1_21_R2(), () -> {
						CommandAPI.logWarning("Loading the CommandAPI with a potentially incompatible NMS implementation.");
						CommandAPI.logWarning("While you may find success with this, further updates might be necessary to fully support the version you are using.");
					});
				}
			}
			throw new UnsupportedVersionException(version);
		}
	}

}
