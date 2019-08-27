package io.github.jorelali.commandapi.api;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World.Environment;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.jorelali.commandapi.api.arguments.Argument;
import io.github.jorelali.commandapi.api.arguments.EnvironmentArgument;
import io.github.jorelali.commandapi.api.arguments.FloatRangeArgument;
import io.github.jorelali.commandapi.api.arguments.IntegerRangeArgument;
import io.github.jorelali.commandapi.api.arguments.Location2DArgument;
import io.github.jorelali.commandapi.api.arguments.LocationType;
import io.github.jorelali.commandapi.api.arguments.TimeArgument;
import io.github.jorelali.commandapi.api.wrappers.FloatRange;
import io.github.jorelali.commandapi.api.wrappers.IntegerRange;

public class CommandAPIMain extends JavaPlugin implements Listener {
	
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
		
		//Instantiate CommandAPI
		CommandAPI.getInstance();
	}
	
	@Override
	public void onEnable() {
		//Prevent command registration after server has loaded
		Bukkit.getScheduler().scheduleSyncDelayedTask(this, () -> {
			CommandAPI.canRegister = false;
			
			//Sort out permissions after the server has finished registering them all
			CommandAPI.fixPermissions();
		}, 0L);
        
        getServer().getPluginManager().registerEvents(this, this);
        
        //Testing all happens below here
        
        LinkedHashMap<String, Argument> args = new LinkedHashMap<>();
        args.put("time", new TimeArgument());
        
        CommandAPI.getInstance().register("tim", args, (s, a) -> {
        	System.out.println(a[0]);
        });
        
        args.clear();
        args.put("2d", new Location2DArgument(LocationType.BLOCK_POSITION));
        
        CommandAPI.getInstance().register("2dblock", args, (s, a) -> {
        	System.out.println(a[0]);
        });
        
        args.clear();
        args.put("2d", new Location2DArgument(LocationType.PRECISE_POSITION));
        
        CommandAPI.getInstance().register("2dprecise", args, (s, a) -> {
        	System.out.println(a[0]);
        });
        
        args.clear();
        args.put("range", new IntegerRangeArgument());
        
        CommandAPI.getInstance().register("range", args, (s, a) -> {
        	IntegerRange r = (IntegerRange) a[0];
        	System.out.println(r.getLowerBound());
        	System.out.println(r.getUpperBound());
        });
        
        args.clear();
        args.put("frange", new FloatRangeArgument());
        
        CommandAPI.getInstance().register("frange", args, (s, a) -> {
        	FloatRange r = (FloatRange) a[0];
        	System.out.println(r.getLowerBound());
        	System.out.println(r.getUpperBound());
        });
        
        args.clear();
        args.put("dim", new EnvironmentArgument());
        
        CommandAPI.getInstance().register("dim", args, (s, a) -> {
        	Environment r = (Environment) a[0];
        	System.out.println(r);
        });
        
	}
	
	void a(Location a) {
		
	}
	
}
