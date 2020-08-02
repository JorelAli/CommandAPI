package dev.jorel.commandapi;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.BlockPredicateArgument;
import dev.jorel.commandapi.arguments.BlockStateArgument;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.ItemStackPredicateArgument;
import dev.jorel.commandapi.arguments.UUIDArgument;

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
		//Config loading
		saveDefaultConfig();
		CommandAPIMain.config = new Config(getConfig());
		CommandAPIMain.dispatcherFile = new File(getDataFolder(), "command_registration.json");
		logger = getLogger();
		
		//Check dependencies for CommandAPI
		CommandAPIHandler.checkDependencies();

		//Convert all plugins to be converted
		for(Entry<Plugin, String[]> pluginToConvert : config.pluginsToConvert.entrySet()) {
			if(pluginToConvert.getValue().length == 0) {
				Converter.convert(pluginToConvert.getKey());
			} else {
				for(String command : pluginToConvert.getValue()) {
					Converter.convert(pluginToConvert.getKey(), command);
				}
			}
		}
		
	}
	
	@Override
	public void onEnable() {
		
		//Prevent command registration after server has loaded
		Bukkit.getScheduler().scheduleSyncDelayedTask(this, () -> {
			CommandAPI.cleanup();
		}, 0L);
        
        getServer().getPluginManager().registerEvents(this, this);
        
        //TODO: Remove before release
        {
        	LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
        	arguments.put("val", new IntegerArgument());
        	
        	new CommandAPICommand("hello")
        	.withArguments(arguments)
        	.withAliases("bye", "cya", "hi")
        	.executes((s, a) -> {
        		System.out.println(Arrays.deepToString(a));
        	})
        	.register();
        	
        	new CommandAPICommand("hello2")
        	.withAliases("bye2", "cya2", "hi2")
        	.executes((s, a) -> {
        		System.out.println(Arrays.deepToString(a));
        	})
        	.register();
        } {
        	LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
        	arguments.put("uuidarg", new UUIDArgument());
        	
        	new CommandAPICommand("u1")
        	.withArguments(arguments)
        	.executes((s, a) -> {
        		System.out.println(Arrays.deepToString(a));
        		System.out.println(Bukkit.getEntity((UUID) a[0]).getName());
        	})
        	.register();
        } {
        	LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
        	arguments.put("uuidarg", new UUIDArgument().safeOverrideSuggestions(UUID.randomUUID(), UUID.randomUUID()));
        	
        	new CommandAPICommand("u2")
        	.withArguments(arguments)
        	.executes((s, a) -> {
        		System.out.println(Arrays.deepToString(a));
        		System.out.println(Bukkit.getEntity((UUID) a[0]).getName());
        	})
        	.register();
        } {
        	
LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
arguments.put("radius", new IntegerArgument());
arguments.put("fromBlock", new BlockPredicateArgument());
arguments.put("toBlock", new BlockStateArgument());

new CommandAPICommand("replace")
.withArguments(arguments)
.executesPlayer((player, args) -> {
	
	// Parse the arguments
	int radius = (int) args[0];
	@SuppressWarnings("unchecked")
	Predicate<Block> predicate = (Predicate<Block>) args[1];
	BlockData blockData = (BlockData) args[2];
	
	// Find a (solid) sphere of blocks around the player with a given radius
	ArrayList<Block> sphere = new ArrayList<Block>();
	Location center = player.getLocation();
	for (int Y = -radius; Y < radius; Y++) {
		for (int X = -radius; X < radius; X++) {
			for (int Z = -radius; Z < radius; Z++) {
				if (Math.sqrt((X * X) + (Y * Y) + (Z * Z)) <= radius) {
					Block block = center.getWorld().getBlockAt(X + center.getBlockX(), Y + center.getBlockY(), Z + center.getBlockZ());
					sphere.add(block);
				}
			}
		}
	}
	
	// Iterate through the blocks in the radius
	for(Block block : sphere) {
		
		// If that block matches a block from the predicate, set it
		if(predicate.test(block)) {
			block.setType(blockData.getMaterial());
			block.setBlockData(blockData);
		}
	}
})
.register();

        } {
        	LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
        	arguments.put("items", new ItemStackPredicateArgument());
        	
        	new CommandAPICommand("rem")
        	.withArguments(arguments)
        	.executesPlayer((s, a) -> {
        		
        		@SuppressWarnings("unchecked")
				Predicate<ItemStack> predicate = (Predicate<ItemStack>) a[0];
        		s.getInventory().forEach(i -> {
        			if(predicate.test(i)) {
        				s.getInventory().remove(i);
        			}
        		});
        		System.out.println(Arrays.deepToString(a));
        	})
        	.register();
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
		private final Map<Plugin, String[]> pluginsToConvert; 
				
		public Config(FileConfiguration fileConfig) {
			verboseOutput = fileConfig.getBoolean("verbose-outputs");
			createDispatcherFile = fileConfig.getBoolean("create-dispatcher-json");
			pluginsToConvert = new HashMap<>();
			
			for(Map<?, ?> map : fileConfig.getMapList("plugins-to-convert")) {
				String pluginName = (String) map.keySet().stream().findFirst().get();
				Plugin plugin = Bukkit.getPluginManager().getPlugin(pluginName);
				if(plugin == null) {
					getLog().severe("Plugin '" + pluginName + "' was not found. Did you add loadbefore: [CommandAPI] to " + pluginName + "'s plugin.yml file?");
					continue;
				}
				
				String[] pluginCommands;
				if(map.values() == null) {
					pluginCommands = new String[0];
				} else {
					@SuppressWarnings("unchecked")
					List<String> commands = (List<String>) map.values().stream().findFirst().get();
					pluginCommands = commands.toArray(new String[0]);
				}
				pluginsToConvert.put(plugin, pluginCommands);
			}
		}
		
		public boolean hasVerboseOutput() {
			return verboseOutput;
		}
		
		public boolean willCreateDispatcherFile() {
			return createDispatcherFile;
		}
		
	}
	
}
