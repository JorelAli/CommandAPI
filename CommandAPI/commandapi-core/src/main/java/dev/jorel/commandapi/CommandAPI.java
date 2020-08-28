package dev.jorel.commandapi;

import java.io.File;
import java.util.Map.Entry;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;

/**
 * Class to register commands with the 1.13 command UI
 *
 */
public abstract class CommandAPI {
	
	private static boolean canRegister = true;
	private static Config config;
	private static File dispatcherFile;

	static Config getConfiguration() {
		return config;
	}
	
	static File getDispatcherFile() {
		return dispatcherFile;
	}
	
	public static Logger getLog() {
		return Logger.getLogger("CommandAPI");
	}
	
	static final void onLoad(Plugin plugin) {
		if(plugin.getName().equals("CommandAPI")) {
			//Config loading
			plugin.saveDefaultConfig();
			CommandAPI.config = new Config(plugin.getConfig());
			CommandAPI.dispatcherFile = new File(plugin.getDataFolder(), "command_registration.json");
			
			//Check dependencies for CommandAPI
			CommandAPIHandler.checkDependencies();
			
			//Convert all plugins to be converted
			for(Entry<Plugin, String[]> pluginToConvert : config.getPluginsToConvert()) {
				if(pluginToConvert.getValue().length == 0) {
					Converter.convert(pluginToConvert.getKey());
				} else {
					for(String command : pluginToConvert.getValue()) {
						Converter.convert(pluginToConvert.getKey(), command);
					}
				}
			}
		} else {
			onLoad();
		}
	}
	
	/**
	 * Initializes the CommandAPI for loading. This should be placed at the
	 * start of your <code>onLoad()</code> method.
	 */
	public static void onLoad() {
		CommandAPI.config = new Config();
	}
	
	/**
	 * Enables the CommandAPI. This should be placed at the
	 * start of your <code>onEnable()</code> method.
	 */
	public static void onEnable(Plugin plugin) {
		//Prevent command registration after server has loaded
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
			canRegister = false;
			
			//Sort out permissions after the server has finished registering them all
			CommandAPIHandler.fixPermissions();
			
			try {
				CommandAPIHandler.getNMS().reloadDataPacks();
			} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}, 0L);
		
		final Listener listener = new Listener() {
			@EventHandler(priority = EventPriority.MONITOR)
			public void onPlayerJoin(PlayerJoinEvent e) {
				CommandAPIHandler.getNMS().resendPackets(e.getPlayer());
			}
		};
        
        Bukkit.getServer().getPluginManager().registerEvents(listener, plugin);
	}
	
	/**
	 * Updates the requirements required for a given player to execute a command.
	 * @param player the player whos requirements to update
	 */
	public static void updateRequirements(Player player) {
		CommandAPIHandler.getNMS().resendPackets(player);
	}
	
	/**
	 * Forces a command to return a success value of 0
	 * @param message Description of the error message
	 * @throws WrapperCommandSyntaxException
	 */
	public static void fail(String message) throws WrapperCommandSyntaxException {
		throw new WrapperCommandSyntaxException(new SimpleCommandExceptionType(new LiteralMessage(message)).create());
	}

	/**
	 * Determines whether command registration is permitted via the CommandAPI
	 * @return true if commands can still be registered
	 */
	public static boolean canRegister() {
		return canRegister;
	}
	
	/**
	 * Unregisters a command
	 * @param command the name of the command to unregister
	 */
	public static void unregister(String command) {
		CommandAPIHandler.unregister(command, false);
	}
	
	/**
	 * Unregisters a command, by force (removes all instances of that command)
	 * 
	 * @param command the name of the command to unregister
	 * @param force   if true, attempt to unregister all instances of the command
	 *                across all plugins as well as minecraft, bukkit and spigot
	 */
	public static void unregister(String command, boolean force) {
		if(!canRegister()) {
			getLog().warning("Unexpected unregistering of /" + command + ", as server is loaded! Unregistering anyway, but this can lead to unstable results!");
		}
		CommandAPIHandler.unregister(command, force);
	}
}
