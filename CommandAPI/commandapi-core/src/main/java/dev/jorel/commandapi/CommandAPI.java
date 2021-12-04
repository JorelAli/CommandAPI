/*******************************************************************************
 * Copyright 2018, 2021 Jorel Ali (Skepter) - MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *******************************************************************************/
package dev.jorel.commandapi;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.LogRecord;
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
public final class CommandAPI {
	
	// Cannot be instantiated
	private CommandAPI() {}
	
	private static boolean canRegister = true;
	static Config config;
	static File dispatcherFile;
	static Logger logger;
	private static boolean loaded = false;

	static Config getConfiguration() {
		return config;
	}
	
	static File getDispatcherFile() {
		return dispatcherFile;
	}
	
	/**
	 * Returns the CommandAPI's logger
	 * @return the CommandAPI's logger
	 */
	public static Logger getLog() {
		if(logger == null) {
			logger = new Logger("CommandAPI", null) {
				{
					this.setParent(Bukkit.getServer().getLogger());
					this.setLevel(Level.ALL);
				}
				
				public void log(LogRecord logRecord) {
					logRecord.setMessage("[CommandAPI] " + logRecord.getMessage());
					super.log(logRecord);
				}
			};
		}
		return logger;
	}
	
	/**
	 * Logs a message to the console using Logger.info() if the configuration has
	 * verbose logging enabled
	 * 
	 * @param message the message to log to the console
	 */
	public static void logInfo(String message) {
		if(config.hasVerboseOutput() && !config.hasSilentLogs()) {
			getLog().info(message);
		}
	}
	
	/**
	 * Logs a message from the CommandAPI. If silent logs are enabled, this message
	 * is not logged.
	 * 
	 * @param message the message to log
	 */
	public static void logNormal(String message) {
		if(!config.hasSilentLogs()) {
			getLog().info(message);			
		}
	}
	
	/**
	 * Logs a warning from the CommandAPI. If silent logs are enabled, this warning
	 * is not logged.
	 * 
	 * @param message the message to log as a warning
	 */
	public static void logWarning(String message) {
		if(!config.hasSilentLogs()) {
			getLog().warning(message);
		}
	}
	
	/**
	 * Logs an error from the CommandAPI. This always gets logged, even if silent
	 * logs are enabled.
	 * 
	 * @param message the message to log as an error
	 */
	public static void logError(String message) {
		getLog().severe(message);
	}
	
	/**
	 * Initializes the CommandAPI for loading. This should be placed at the
	 * start of your <code>onLoad()</code> method.
	 * @param verbose if true, enables verbose output for the CommandAPI
	 * @deprecated Use {@link CommandAPI#onLoad(CommandAPIConfig)} instead
	 */
	@Deprecated(forRemoval = true)
	public static void onLoad(boolean verbose) {
		if(!loaded) {
			CommandAPI.config = new Config(verbose);
			CommandAPIHandler.getInstance().checkDependencies();
			loaded = true;
		} else {
			getLog().severe("You've tried to call the CommandAPI's onLoad() method more than once!");
		}
	}
	
	/**
	 * Initializes the CommandAPI for loading. This should be placed at the
	 * start of your <code>onLoad()</code> method.
	 * @param config the configuration to use for the CommandAPI
	 */
	public static void onLoad(CommandAPIConfig config) {
		if(!loaded) {
			CommandAPI.config = new Config(config);
			CommandAPIHandler.getInstance().checkDependencies();
			loaded = true;
		} else {
			getLog().severe("You've tried to call the CommandAPI's onLoad() method more than once!");
		}
	}
	
	/**
	 * Enables the CommandAPI. This should be placed at the
	 * start of your <code>onEnable()</code> method.
	 * @param plugin the plugin that this onEnable method is called from
	 */
	public static void onEnable(Plugin plugin) {
		//Prevent command registration after server has loaded
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
			canRegister = false;
			
			//Sort out permissions after the server has finished registering them all
			CommandAPIHandler.getInstance().fixPermissions();
			
			try {
				CommandAPIHandler.getInstance().getNMS().reloadDataPacks();
			} catch (ReflectiveOperationException e) {
				e.printStackTrace();
			}
			
			CommandAPIHandler.getInstance().updateHelpForCommands();
		}, 0L);
		
		final Listener playerJoinListener = new Listener() {
			@EventHandler(priority = EventPriority.MONITOR)
			public void onPlayerJoin(PlayerJoinEvent e) {
				CommandAPIHandler.getInstance().getNMS().resendPackets(e.getPlayer());
			}
		};
        
		Bukkit.getServer().getPluginManager().registerEvents(playerJoinListener, plugin);  
	}
	
	/**
	 * Updates the requirements required for a given player to execute a command.
	 * @param player the player whos requirements to update
	 */
	public static void updateRequirements(Player player) {
		CommandAPIHandler.getInstance().getNMS().resendPackets(player);
	}
	
	/**
	 * Reloads all of the datapacks that are on the server. This should be used if
	 * you change a datapack and want to reload a server. Execute this method after
	 * running /minecraft:reload, NOT before.
	 */
	public static void reloadDatapacks() {
		try {
			CommandAPIHandler.getInstance().getNMS().reloadDataPacks();
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Forces a command to return a success value of 0
	 * @param message Description of the error message
	 * @throws WrapperCommandSyntaxException which indicates that there was a command failure
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
		CommandAPIHandler.getInstance().unregister(command, false);
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
		CommandAPIHandler.getInstance().unregister(command, force);
	}

	/**
	 * Registers a command. Used with the CommandAPI's Annotation API.
	 * @param commandClass the class to register
	 */
	public static void registerCommand(Class<?> commandClass) {
		try {
			Class.forName(commandClass.getName() + "$Command").getDeclaredMethod("register").invoke(null);
		} catch (ReflectiveOperationException e) {
			e.printStackTrace();
		}
	}
}
