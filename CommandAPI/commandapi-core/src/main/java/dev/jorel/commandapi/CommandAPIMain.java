package dev.jorel.commandapi;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.arguments.StringArgument;

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
	
	static List<String> players = new ArrayList<>();
	
	@Override
	public void onEnable() {
		//Prevent command registration after server has loaded
		Bukkit.getScheduler().scheduleSyncDelayedTask(this, () -> {
			CommandAPI.cleanup();
		}, 0L);
        
        getServer().getPluginManager().registerEvents(this, this);
        
        //TODO: Remove on release
        {
        	Map<String, List<UUID>> partyMembers = new HashMap<>();
        	
        	LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
        	arguments.put("createParty", new LiteralArgument("create")
    			.withRequirement(sender -> {
    				
    				/* Check if any of the List<UUID> in partyMembers
    				 * contains the player's UUID. If so, then we know
    				 * that the player already has a party */
    				boolean playerHasParty = partyMembers.values()
    					.stream().flatMap(List::stream)
    					.anyMatch(((Player) sender).getUniqueId()::equals);
    				
    				return !playerHasParty;
    			}));
        	arguments.put("partyName", new StringArgument());
        	
        	new CommandAPICommand("party")
        		.withArguments(arguments)
        		.executesPlayer((player, args) -> {
        			
        			//Get the name of the party to create
        			String partyName = (String) args[0];
        			
        			//Add the player to the party they created
        			List<UUID> members = new ArrayList<>();
        			members.add(player.getUniqueId());
        			
        			partyMembers.put(partyName, members);
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
