package dev.jorel.commandapi;

import be.seeseemelk.mockbukkit.MockBukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.function.Consumer;

public class MockCommandAPIPlugin extends JavaPlugin {
	// Allow loading with settings,
	//  Default to none if `MockBukkit.load(MockCommandAPIPlugin.class)` is used directly
	private static Consumer<CommandAPIBukkitConfig> configureSettings = null;

	public static MockCommandAPIPlugin load() {
		return load(null);
	}

	public static MockCommandAPIPlugin load(Consumer<CommandAPIBukkitConfig> configureSettings) {
		MockCommandAPIPlugin.configureSettings = configureSettings;
		return MockBukkit.load(MockCommandAPIPlugin.class);
	}

	@Override
	public void onLoad() {
		CommandAPIBukkitConfig config = new CommandAPIBukkitConfig(this);

		if (configureSettings != null) configureSettings.accept(config);
		configureSettings = null; // Reset to avoid configs persisting between loads

		CommandAPI.onLoad(config);
	}

	@Override
	public void onEnable() {
		CommandAPI.onEnable();
	}

	@Override
	public void onDisable() {
		CommandAPI.onDisable();
	}
}
