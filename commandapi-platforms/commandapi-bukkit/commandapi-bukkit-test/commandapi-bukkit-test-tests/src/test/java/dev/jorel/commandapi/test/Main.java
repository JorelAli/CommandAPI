package dev.jorel.commandapi.test;
import java.io.File;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import de.tr7zw.changeme.nbtapi.NBTContainer;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import dev.jorel.commandapi.CommandAPILogger;

public class Main extends JavaPlugin {
	
	@Override
	public void onLoad() {
		getDataFolder().mkdirs();
		CommandAPI.setLogger(CommandAPILogger.fromJavaLogger(getLogger()));
		CommandAPI.onLoad(new CommandAPIBukkitConfig(this)
			.useLatestNMSVersion(true) // Doesn't matter because we implement CommandAPIVersionHandler here
			.silentLogs(true)
			.dispatcherFile(new File(getDataFolder(), "command_registration.json"))
			.initializeNBTAPI(NBTContainer.class, NBTContainer::new)
		);
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
