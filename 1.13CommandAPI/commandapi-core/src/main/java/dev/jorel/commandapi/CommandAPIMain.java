package dev.jorel.commandapi;

import java.io.File;
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
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;

import dev.jorel.commandapi.CommandAPIHandler.Brigadier;
import dev.jorel.commandapi.arguments.Argument;
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
		
		//TODO: Remove before release
		{
			LiteralCommandNode randomChance = Brigadier.registerNewLiteralArgument("randomchance");
			
			//Declare arguments as normal
			LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
			arguments.put("firstVal", new IntegerArgument(0));
			arguments.put("secondVal", new IntegerArgument(1));
			
			//firstVal and secondVal should be able to be constructed using the CommandAPI
			RequiredArgumentBuilder firstVal = Brigadier.argBuildOf(arguments, "firstVal");
			ArgumentBuilder secondVal = Brigadier.argBuildOf(arguments, "secondVal")
				.fork(Brigadier.getRootNode().getChild("execute"), Brigadier.fromPredicate((sender, args) -> {
					//Parse arguments like normal
					int first = (int) args[0];
					int second = (int) args[1];
					
					//Return boolean with a first/second chance
					return Math.ceil(Math.random() * (double) second) <= (double) first;
				}, arguments))
				.executes(Brigadier.fromCommand(new CommandAPICommand("") /* And so on... */));
			
			//Optionally, you can include another 'execute' here, so you could do '/execute if <firstVal> <secondVal>' and that returns a value
			
			//Add <firstVal> -> <secondVal> as a child of randomchance
			randomChance.addChild(firstVal.then(secondVal).build());
			
			//Add (randomchance -> <firstVal> -> <secondVal>) as a child of (execute -> if)
			//This results in execute -> if -> randomchance -> <firstVal> -> <secondVal>
			Brigadier.getRootNode().getChild("execute").getChild("if").addChild(randomChance);
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
arguments.put("items", new ItemStackPredicateArgument());

new CommandAPICommand("rem")
.withArguments(arguments)
.executesPlayer((player, args) -> {
	
	@SuppressWarnings("unchecked")
	Predicate<ItemStack> predicate = (Predicate<ItemStack>) args[0];
	
	for(ItemStack item : player.getInventory()) {
		if(predicate.test(item)) {
			player.getInventory().remove(item);
		}
	}
	player.getInventory().forEach(i -> {
		if(predicate.test(i)) {
			player.getInventory().remove(i);
		}
	});
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
