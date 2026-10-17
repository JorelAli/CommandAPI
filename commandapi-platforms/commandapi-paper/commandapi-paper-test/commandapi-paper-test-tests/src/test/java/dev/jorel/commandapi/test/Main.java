package dev.jorel.commandapi.test;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPILogger;
import dev.jorel.commandapi.CommandAPIPaperConfig;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import java.io.File;
import java.util.function.Function;

/**
 * The Paper version of the test plugin used by the shared tests in commandapi-bukkit-test-tests
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class Main extends JavaPlugin {

	public static Class nbtContainerClass = null;
	public static Function nbtContainerConstructor = null;

	@Override
	public void onLoad() {
		getDataFolder().mkdirs();
		CommandAPI.setLogger(CommandAPILogger.fromJavaLogger(getLogger()));

		CommandAPIPaperConfig config = new CommandAPIPaperConfig(this)
			.silentLogs(true)
			.enableNetworking(true)
			.dispatcherFile(new File(getDataFolder(), "command_registration.json"));

		if (nbtContainerClass != null && nbtContainerConstructor != null) {
			config = config.initializeNBTAPI(nbtContainerClass, nbtContainerConstructor);
		}

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

	// Additional constructors required for MockBukkit
	public Main() {
		super();
	}

	public Main(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
		super(loader, description, dataFolder, file);
	}

}
