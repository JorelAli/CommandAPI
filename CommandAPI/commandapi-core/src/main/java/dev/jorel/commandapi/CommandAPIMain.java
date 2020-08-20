package dev.jorel.commandapi;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.plugin.java.JavaPlugin;

import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.LiteralArgument;

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
	
//	@EventHandler
//	public void onPlayerExp(PlayerExpChangeEvent e) {
//		CommandAPIHandler.getNMS().resendPackets(e.getPlayer());
//	}
	
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

		
//		
//		@SafeVarargs
//		public static void withEvents(Class<? extends PlayerEvent>... events) {
//			RegisteredListener registeredListener = new RegisteredListener(new Listener() {}, new EventExecutor() {
//			    @Override
//			    public void execute(Listener listener, Event event) throws EventException {
//			    	System.out.println(event.getEventName());
//					if(event instanceof PlayerEvent) {
//						for(Class<? extends PlayerEvent> pEvent : new Class[] {PlayerExpChangeEvent.class}) {
//							if(pEvent.isInstance(event)) {
//								System.out.println("Sending packets...");
//								
//								CommandAPIHandler.getNMS().resendPackets(((PlayerEvent) event).getPlayer());
//							}
//						}
//					}
//			    }
//			}, EventPriority.NORMAL, this, false);
//			
//			for(HandlerList handler : HandlerList.getHandlerLists()) {
//			    handler.register(registeredListener);
//			}
//		    HandlerList.bakeAll();

//		}
		
//		CommandAPI.withEvents(PlayerExpChangeEvent.class);
		
		//TODO: Remove before release
		{
			LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
			arguments.put("test", new LiteralArgument("prize").withRequirement(s -> ((Player) s).getLevel() >= 20));
			
			new CommandAPICommand("hello")
				.withArguments(arguments)
				.executesPlayer((player, args) -> {
					player.getInventory().addItem(new ItemStack(Material.DIAMOND));
				})
				.register();
		} {
			LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
			arguments.put("test", new LiteralArgument("otherprize").withRequirement(s -> ((Player) s).getLevel() >= 10));
			
			new CommandAPICommand("hello")
				.withArguments(arguments)
				.executesPlayer((player, args) -> {
					player.getInventory().addItem(new ItemStack(Material.GOLD_INGOT));
				})
				.register();
		} {
			LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
			arguments.put("test1", new LiteralArgument("aprize").withRequirement(s -> ((Player) s).getLevel() >= 10));
			arguments.put("test2", new LiteralArgument("anotherprize").withRequirement(s -> ((Player) s).getLevel() >= 20));
			
			new CommandAPICommand("bye")
				.withArguments(arguments)
				.executesPlayer((player, args) -> {
					player.getInventory().addItem(new ItemStack(Material.IRON_INGOT));
				})
				.register();
		} {
			LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
			arguments.put("test", new LiteralArgument("aa").withRequirement(s -> {
				if(s instanceof BlockCommandSender) {
					BlockCommandSender ss = (BlockCommandSender) s;
					Block b = ss.getBlock().getRelative(BlockFace.UP);
					if(b.getType() == Material.DIAMOND_BLOCK) {
						return true;
					}
				}
				return false;
			}));
			
			new CommandAPICommand("zzz")
			.withArguments(arguments)
			.executesCommandBlock((block, args) -> {
				Bukkit.broadcastMessage("HI");
			})
			.register();
		}
	
	}
	
	@SafeVarargs
	public final void registerEvents(Class<? extends PlayerEvent>... events) {
		for(HandlerList handler : HandlerList.getHandlerLists()) {
			handler.register(new RegisteredListener(new Listener() {}, new EventExecutor() {

				@Override
				public void execute(Listener var1, Event event) throws EventException {
					if(event instanceof PlayerEvent) {
						for(Class<? extends PlayerEvent> pEvent : events) {
							System.out.println("Event: " + event.getEventName());
							if(pEvent.isInstance(event)) {
								System.out.println("Sending packets... " + event.getEventName());
								CommandAPIHandler.getNMS().resendPackets(((PlayerEvent) event).getPlayer());
							}
						}
					}
				}
				
			}, EventPriority.HIGHEST, this, false));
		}
	}
	
	@Override
	public void onEnable() {
		//Prevent command registration after server has loaded
		Bukkit.getScheduler().scheduleSyncDelayedTask(this, () -> {
			CommandAPI.cleanup();
		}, 0L);
        
        getServer().getPluginManager().registerEvents(this, this);
        registerEvents(PlayerExpChangeEvent.class);
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
