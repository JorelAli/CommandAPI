package dev.jorel.commandapi.test;

import java.io.File;
import java.util.function.Function;

import dev.jorel.commandapi.CommandAPIPaperConfig;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import de.tr7zw.changeme.nbtapi.NBTContainer;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import dev.jorel.commandapi.CommandAPILogger;

public class Main extends JavaPlugin {
	
	public static Class nbtContainerClass = null;
	public static Function nbtContainerConstructor = null;
	
	@Override
	public void onLoad() {
		getDataFolder().mkdirs();
		CommandAPI.setLogger(CommandAPILogger.fromJavaLogger(getLogger()));
		
		CommandAPIPaperConfig config = new CommandAPIPaperConfig(this)
		.useLatestNMSVersion(true) // Doesn't matter because we implement CommandAPIVersionHandler here
		.silentLogs(true)
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
