/*******************************************************************************
 * Copyright 2018, 2021 Jorel Ali (Skepter) - MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *******************************************************************************/
package dev.jorel.commandapi;

import dev.jorel.commandapi.exceptions.UnsupportedVersionException;
import dev.jorel.commandapi.nms.*;
import org.bukkit.Bukkit;

/**
 * This file handles the NMS version to be loaded. The CommandAPIVersionHandler
 * file within the commandapi-core module is NOT used at compile time. Instead,
 * the commandapi-vh module is loaded instead, which doesn't use reflection to
 * load NMS instances.
 * 
 * NMS classes implement {@code NMS<CommandListenerWrapper>}. The type
 * CommandListenerWrapper isn't visible as in this Maven module (it's not
 * included and in some cases, cannot be included because Maven will only select
 * one version of a specific project (in this case, it'll only import one copy
 * of Spigot, almost always the latest stable version (from 1.16.5)). In
 * Eclipse, this can produce an error saying that a class cannot be resolved
 * because it is indirectly referenced from required .class files So sidestep
 * this, we introduce a second intermediary class NMSWrapper_VERSION which
 * depends on CommandListenerWrapper and then our main NMS_VERSION class extends
 * that.
 */
public interface CommandAPIVersionHandler {

	/**
	 * Returns an instance of the current running version's implementation of the Bukkit NMS.
	 *
	 * @return an instance of NMS which can run on the specified Minecraft version
	 */
	static CommandAPIPlatform<?, ?, ?> getPlatform() {
		CommandAPIPlatform<?, ?, ?> latestNMS = new NMS_1_21_R1();
		if (CommandAPI.getConfiguration().shouldUseLatestNMSVersion()) {
			return latestNMS;
		} else {
			String version = Bukkit.getBukkitVersion().split("-")[0];
			Version minecraftVersion = new Version(version);
			CommandAPIPlatform<?, ?, ?> platform = switch (version) {
				case "1.16.5" -> new NMS_1_16_R3();
				case "1.17" -> new NMS_1_17();
				case "1.17.1" -> new NMS_1_17_R1();
				case "1.18", "1.18.1" -> new NMS_1_18_R1();
				case "1.18.2" -> new NMS_1_18_R2();
				case "1.19" -> new NMS_1_19_R1();
				case "1.19.1", "1.19.2" -> new NMS_1_19_1_R1();
				case "1.19.3" -> new NMS_1_19_3_R2();
				case "1.19.4" -> new NMS_1_19_4_R3();
				case "1.20", "1.20.1" -> new NMS_1_20_R1();
				case "1.20.2" -> new NMS_1_20_R2();
				case "1.20.3", "1.20.4" -> new NMS_1_20_R3();
				case "1.20.5", "1.20.6" -> new NMS_1_20_R4();
				case "1.21", "1.21.1" -> new NMS_1_21_R1();
				default -> null;
			};
			if (platform != null) {
				return platform;
			} else {
				if (CommandAPI.getConfiguration().shouldBeMoreLenientForMinorVersions()) {
					return minecraftVersion.matchesPatch((NMS<?>) latestNMS);
				}
				throw new UnsupportedVersionException(version);
			}
		}
	}

	class Version {

		private final String version;

		// The minor version is something like 1.x
		private final int minor;

		private Version(String version) {
			this.version = version;
			String[] versionParts = version.split("\\.");
			minor = Integer.parseInt(versionParts[1]);
		}

		private CommandAPIPlatform<?, ?, ?> matchesPatch(NMS<?> latestNMS) {
			if (minor <= 16) {
				// We absolutely do not support versions older than 1.16.5
				// As we match 1.16.5 in the getPlatform() method, this if branch means that
				// the server is on 1.16.4 or older
				throw new UnsupportedVersionException(version);
			} else {
				// Any other Minecraft that is older or exactly the last version we match in getPlatform()
				// does not appear here. Furthermore, we do not want to be lenient with 1.x versions so we
				// can simply return the latest NMS version here if its supported Minecraft version's minor version
				// is the same as the current Minecraft version
				String[] supportedNMSVersions = latestNMS.compatibleVersions();
				int minorVersion = Integer.parseInt(supportedNMSVersions[0].split("\\.")[1]); // Index 1 returns the minor version by SemVer's rules
				if (this.minor == minorVersion) {
					return (CommandAPIPlatform<?, ?, ?>) latestNMS;
				}
			}
			// If we end up here, that means the server's current minor version in SemVer terms is newer than the
			// latest minor version of Minecraft in SemVer terms that this method allows. We do not support that.
			throw new UnsupportedVersionException(version);
		}

	}

}
