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
import dev.jorel.commandapi.nms.NMS;
import dev.jorel.commandapi.nms.NMS_1_17_R1;
import dev.jorel.commandapi.nms.NMS_1_18_R1;
import dev.jorel.commandapi.nms.NMS_1_18_R2;

/**
 * This file handles the NMS version to be loaded. The CommandAPIVersionHandler
 * file within the commandapi-core module is NOT used at compile time. Instead,
 * the commandapi-vh module is loaded instead, which doesn't use reflection to
 * load NMS instances.
 */
public interface CommandAPIVersionHandler {

	/**
	 * Returns an instance of the version's implementation of NMS.
	 * @param version the string of the Minecraft version (e.g. 1.16.5 or 1.17)
	 * @return an instance of NMS which can run on the specified Minecraft version
	 */
	public static NMS<?> getNMS(String version) {
		if(CommandAPI.getConfiguration().shouldUseLatestNMSVersion()) {
			return new NMS_1_18_R2();
		} else {
			switch (version) {
			case "1.17":
			case "1.17.1":
				return new NMS_1_17_R1();
			case "1.18":
			case "1.18.1":
				return new NMS_1_18_R1();
			case "1.18.2":
				return new NMS_1_18_R2();
			default:
				throw new UnsupportedVersionException("This version of Minecraft is unsupported: " + version);
			}
		}
	}

}
