package dev.jorel.commandapi;

import java.io.File;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
<<<<<<< converter-improvements
import org.bukkit.plugin.Plugin;
=======
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
>>>>>>> release-3.4
import org.bukkit.plugin.java.JavaPlugin;

public class CommandAPIMain extends JavaPlugin implements Listener {
	
	private static Logger logger;
	
	public static Logger getLog() {
		return logger;
	}
	
	private static Config config;
	private static File dispatcherFile;

	//Gets the instance of Config
	protected static Config getConfiguration() {
		return config;
	}
	
	protected static File getDispatcherFile() {
		return dispatcherFile;
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent e) {
		CommandAPIHandler.getNMS().resendPackets(e.getPlayer());
	}
	
	@Override
	public void onLoad() {
		saveDefaultConfig();
		CommandAPIMain.config = new Config(getConfig());
		CommandAPIMain.dispatcherFile = new File(getDataFolder(), "command_registration.json");
		logger = getLogger();
		new CommandAPI();
		
		{
			//TODO remove before next release
			new CommandAPICommand("killall")
			.executes((s, a) -> {
				System.out.println("hi");
			}).register();;
		}
	}
	
	@Override
	public void onEnable() {
		
		//Convert all plugins to be converted
		config.pluginsToConvert.forEach(Converter::convert);
		
		//Prevent command registration after server has loaded
		Bukkit.getScheduler().scheduleSyncDelayedTask(this, () -> {
			CommandAPI.cleanup();
		}, 0L);
        
        getServer().getPluginManager().registerEvents(this, this);
        
        {
        	//TODO remove before next release
        	NamespacedKey customItem = new NamespacedKey(this, "TestItem");
            ShapedRecipe customRecipe = new ShapedRecipe(customItem, new ItemStack(Material.COMPASS));
            customRecipe.shape("AAA", "AAA", "AAA");
            customRecipe.setIngredient('A', Material.CRAFTING_TABLE);
            getServer().addRecipe(customRecipe);
        }
	}
	
	/** 
	 * Configuration wrapper class.
	 * The config.yml file used by the CommandAPI is only ever read from,
	 * nothing is ever written to it. That's why there's only getter methods.
	 */
	class Config {
		
		//Output registering and unregistering of commands
		private final boolean verboseOutput;
		
		//Create a command_registration.json file
		private final boolean createDispatcherFile;
		
		//List of plugins to convert
		private final List<Plugin> pluginsToConvert; 
				
		public Config(FileConfiguration fileConfig) {
			verboseOutput = fileConfig.getBoolean("verbose-outputs");
			createDispatcherFile = fileConfig.getBoolean("create-dispatcher-json");
			pluginsToConvert = fileConfig.getStringList("plugins-to-convert").stream().map(Bukkit.getPluginManager()::getPlugin).collect(Collectors.toList());
		}
		
		public boolean hasVerboseOutput() {
			return verboseOutput;
		}
		
		public boolean willCreateDispatcherFile() {
			return createDispatcherFile;
		}
		
	}
	
}
