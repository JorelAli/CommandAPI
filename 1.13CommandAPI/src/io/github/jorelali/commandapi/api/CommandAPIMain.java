package io.github.jorelali.commandapi.api;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandAPIMain extends JavaPlugin {
	
	private static Logger logger;
	
	public static Logger getLog() {
		return logger;
	}
	
	/** 
	 * Configuration wrapper class.
	 * The config.yml file used by the CommandAPI is only ever read from,
	 * nothing is ever written to it. That's why there's only getter methods.
	 */
	public class Config {
		
		//Output registering and unregistering of commands
		private final boolean verboseOutput;
		
		//Create a command_registration.json file
		private final boolean createDispatcherFile;
				
		public Config(FileConfiguration fileConfig) {
			verboseOutput = fileConfig.getBoolean("verbose-outputs");
			createDispatcherFile = fileConfig.getBoolean("create-dispatcher-json");
		}
		
		public boolean hasVerboseOutput() {
			return verboseOutput;
		}
		
		public boolean willCreateDispatcherFile() {
			return createDispatcherFile;
		}
		
	}

	private static Config config;

	//Gets the instance of Config
	public static Config getConfiguration() {
		return config;
	}
	
	
	@Override
	public void onLoad() {
		saveDefaultConfig();
		CommandAPIMain.config = new Config(getConfig());
		logger = getLogger();
		//Instantiate CommandAPI
		CommandAPI.getInstance();
	}
	
	@Override
	public void onEnable() {
		
		//Test code, remove after testing
		CommandAPI.getInstance().z();
		//End of test code
		
		//Prevent command registration after server has loaded
		Bukkit.getScheduler().scheduleSyncDelayedTask(JavaPlugin.getPlugin(CommandAPIMain.class), () -> {
			CommandAPI.canRegister = false;
			
			//Sort out permissions after the server has finished registering them all
			CommandAPI.fixPermissions();
		}, 0L);
	}
	
}
