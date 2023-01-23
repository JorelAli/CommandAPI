package dev.jorel.commandapi;

import dev.jorel.commandapi.nms.NMS_1_19_1_R1;
import dev.jorel.commandapi.test.MockNMS;

/**
 * This file handles loading the correct platform implementation. The CommandAPIVersionHandler
 * file within the commandapi-core module is NOT used at run time. Instead, the platform modules
 * replace this class with their own version that handles loads the correct class for their version
 */
interface CommandAPIVersionHandler {

	companion object {

		@JvmStatic
		fun getPlatform() : CommandAPIPlatform<*, *, *> {
			return MockNMS(NMS_1_19_1_R1());
		}

	}
}
