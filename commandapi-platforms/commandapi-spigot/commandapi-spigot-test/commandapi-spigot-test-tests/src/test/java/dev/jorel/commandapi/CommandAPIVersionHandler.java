package dev.jorel.commandapi;

import dev.jorel.commandapi.test.MockPlatform;

/**
 * This file handles loading the correct platform implementation. The CommandAPIVersionHandler
 * file within the commandapi-core module is NOT used at run time. Instead, the platform modules
 * replace this class with their own version that handles loads the correct class for their version
 */
public abstract class CommandAPIVersionHandler {

	/**
	 * The test profile, without the {@code _Mojang} suffix. See commandapi-spigot-test-tests/build.gradle.kts
	 */
	public static final String PROFILE_ID = System.getProperty("profileId", "Minecraft_1_20").replace("_Mojang", "");

	/**
	 * Used by the shared tests in commandapi-bukkit-test-tests for behavior that differs between platforms
	 */
	public static final boolean IS_PAPER = false;

	public static final boolean IS_MOJANG_MAPPED = System.getProperty("profileId", "").endsWith("_Mojang");

	/**
	 * @return the Minecraft version being tested, used by the shared tests in commandapi-bukkit-test-tests
	 */
	public static MCVersion getVersion() {
		return switch (PROFILE_ID) {
			case "Minecraft_1_20_5" -> MCVersion.V1_20_5;
			case "Minecraft_1_20_3" -> MCVersion.V1_20_3;
			case "Minecraft_1_20_2" -> MCVersion.V1_20_2;
			case "Minecraft_1_20" -> MCVersion.V1_20;
			default -> throw new IllegalArgumentException("Unexpected value: " + PROFILE_ID);
		};
	}

	static LoadContext getPlatform(CommandAPIConfig<?> config) {
		if (!(config instanceof CommandAPISpigotConfig spigotConfig)) {
			throw new IllegalArgumentException("CommandAPISpigot was loaded with non-Spigot config!");
		}

		// Mirrors commandapi-spigot-vh, with the SpigotNMS implementation swapped for the mocked one. Only the
		//  selected version's test implementation is on the classpath, so this has to be loaded by name.
		String className = "dev.jorel.commandapi.test.TestSpigotNMS_" + switch (PROFILE_ID) {
			case "Minecraft_1_20_5" -> "1_20_R4";
			case "Minecraft_1_20_3" -> "1_20_R3";
			case "Minecraft_1_20_2" -> "1_20_R2";
			case "Minecraft_1_20" -> "1_20_R1";
			default -> throw new IllegalArgumentException("Unexpected value: " + PROFILE_ID);
		};

		try {
			CommandAPIPlatform<?, ?, ?> platform = (CommandAPIPlatform<?, ?, ?>) Class.forName(className)
				.getConstructor(InternalSpigotConfig.class)
				.newInstance(new InternalSpigotConfig(spigotConfig));

			// MockBukkit's paper-api has Paper's Brigadier API, so CommandAPIBukkit thinks it's running on Paper
			MockPlatform.setField(CommandAPIBukkit.class, "isPaperBrigAPI", platform, false);
			return new LoadContext(platform);
		} catch (ReflectiveOperationException e) {
			throw new IllegalStateException("Failed to load " + className, e);
		}
	}
}
