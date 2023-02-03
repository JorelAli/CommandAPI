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
		if (CommandAPI.getConfiguration().shouldUseLatestNMSVersion()) {
			return new NMS_1_19_3_R2();
		} else {
			String bukkit = Bukkit.getServer().toString();
			String version = bukkit.substring(bukkit.indexOf("minecraftVersion") + 17, bukkit.length() - 1);
			return switch (version) {
				case "1.15", "1.15.1", "1.15.2" -> new NMS_1_15();
				case "1.16.1" -> new NMS_1_16_R1();
				case "1.16.2", "1.16.3" -> new NMS_1_16_R2();
				case "1.16.4" -> new NMS_1_16_4_R3();
				case "1.16.5" -> new NMS_1_16_R3();
				case "1.17" -> new NMS_1_17();
				case "1.17.1" -> new NMS_1_17_R1();
				case "1.18", "1.18.1" -> new NMS_1_18_R1();
				case "1.18.2" -> new NMS_1_18_R2();
				case "1.19" -> new NMS_1_19_R1();
				case "1.19.1", "1.19.2" -> new NMS_1_19_1_R1();
				case "1.19.3" -> new NMS_1_19_3_R2();
				default -> throw new UnsupportedVersionException(version);
			};
		}
	}

}
