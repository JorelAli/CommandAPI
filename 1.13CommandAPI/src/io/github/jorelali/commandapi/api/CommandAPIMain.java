package io.github.jorelali.commandapi.api;

import java.util.LinkedHashMap;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.jorelali.commandapi.api.arguments.Argument;
import io.github.jorelali.commandapi.api.arguments.SuggestedStringArgument;

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
		//Prevent command registration after server has loaded
		Bukkit.getScheduler().scheduleSyncDelayedTask(JavaPlugin.getPlugin(CommandAPIMain.class), () -> {
			CommandAPI.canRegister = false;
			
			//Sort out permissions after the server has finished registering them all
			CommandAPI.fixPermissions();
		}, 0L);
		
		//Test repo, I can do what I want

		LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();

		//User requires the "custompermission" permission to run this command
		CommandAPI.getInstance().register("permissiontest", CommandPermission.fromString("custompermission"), arguments, (sender, args) -> {
			sender.sendMessage("test1 success!");
		});

		arguments.put("values", new SuggestedStringArgument("hello", "world"));
		//User MUST have "custompermission2" in order to /SEE/ this command, regardless if they have "custompermission"
		CommandAPI.getInstance().register("permissiontest", CommandPermission.fromString("custompermission2"), arguments, (sender, args) -> {
			sender.sendMessage("test2 success!");
		});
		
		arguments.put("moreVals", new SuggestedStringArgument("foo", "bar", "baz"));
		//User MUST have "custompermission2" in order to /SEE/ this command, regardless if they have "custompermission"
		CommandAPI.getInstance().register("permissiontest", CommandPermission.fromString("custompermission3"), arguments, (sender, args) -> {
			sender.sendMessage("test3 success!");
		});

	}
	
}
