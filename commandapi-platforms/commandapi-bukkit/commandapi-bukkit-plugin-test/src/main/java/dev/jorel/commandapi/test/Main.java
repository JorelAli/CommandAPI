package dev.jorel.commandapi.test;
import java.io.File;

import dev.jorel.commandapi.CommandAPIJavaLogger;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import de.tr7zw.changeme.nbtapi.NBTContainer;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIConfig;

public class Main extends JavaPlugin {
	
	@Override
	public void onLoad() {
		CommandAPI.setLogger(new CommandAPIJavaLogger(getLogger()));
		CommandAPI.onLoad(new CommandAPIConfig()
			.useLatestNMSVersion(true)
			.silentLogs(true)
			.dispatcherFile(new File(getDataFolder(), "command_registration.json"))
			.initializeNBTAPI(NBTContainer.class, NBTContainer::new)
		);
	}

	@Override
	public void onEnable() {
		CommandAPI.onEnable(this);
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
