package dev.jorel.commandapi;

import com.mojang.brigadier.Message;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import dev.jorel.commandapi.abstractions.AbstractPlatform;
import dev.jorel.commandapi.abstractions.AbstractTooltip;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;

import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class to register commands with the 1.13 command UI
 *
 */
public class CommandAPI {
	// Cannot be instantiated
	private CommandAPI() {
	}

	static {
		onDisable();
	}

	private static boolean canRegister;
	private static boolean loaded;
	private static InternalConfig config;
	private static CommandAPILogger logger;

	// Accessing static variables

	/**
	 * Returns whether the CommandAPI is currently loaded. This should be true when
	 * {@link CommandAPI#onLoad(CommandAPIConfig, CommandAPILogger)} is called. If the CommandAPI is
	 * loaded, commands are available to register.
	 *
	 * @return whether the CommandAPI has been loaded properly
	 */
	public static boolean isLoaded() {
		return loaded;
	}

	/**
	 * Flag that commands should no longer be registered. After running this,
	 * {@link CommandAPI#canRegister()} will return false.
	 */
	public static void stopCommandRegistration() {
		CommandAPI.canRegister = false;
	}

	/**
	 * Determines whether command registration is permitted via the CommandAPI
	 *
	 * @return true if commands can still be registered
	 */
	public static boolean canRegister() {
		return canRegister;
	}

	/**
	 * Returns the internal configuration used to manage the CommandAPI
	 *
	 * @return the internal configuration used to manage the CommandAPI
	 */
	public static InternalConfig getConfiguration() {
		if (config == null) {
			CommandAPI.config = new InternalConfig(new CommandAPIConfig());
			logWarning("Could not find any configuration for the CommandAPI. Loading basic built-in configuration. Did you forget to call CommandAPI.onLoad(config, logger)?");
		}
		return config;
	}

	/**
	 * Returns the CommandAPI's logger
	 *
	 * @return the CommandAPI's logger
	 */
	public static CommandAPILogger getLogger() {
		if (logger == null) {
			logger = new DefaultLogger();
			logWarning("Could not find a logger for the CommandAPI. Using built-in logger. Did you forget to call CommandAPI.onLoad(config, logger)?");
		}
		return logger;
	}

	private static class DefaultLogger extends Logger implements CommandAPILogger {
		protected DefaultLogger() {
			super("CommandAPI", null);
			// TODO: Do we need to set the parent?
//			setParent(Bukkit.getServer().getLogger());
			setLevel(Level.ALL);
		}

	}

	// Loading, enabling, and disabling

	/**
	 * Initializes the CommandAPI for loading. This should be placed at the start of
	 * your <code>onLoad()</code> method.
	 *
	 * @param config the configuration to use for the CommandAPI
	 * @deprecated Please use {@link CommandAPI#onLoad(CommandAPIConfig, CommandAPILogger)} instead
	 */
	public static void onLoad(CommandAPIConfig config) {
		onLoad(config, null);
	}

	/**
	 * Initializes the CommandAPI for loading. This should be placed at the start of
	 * your <code>onLoad()</code> method.
	 *
	 * @param config the configuration to use for the CommandAPI
	 * @param logger the object to use to send messages
	 */
	public static void onLoad(CommandAPIConfig config, CommandAPILogger logger) {
		if (!loaded) {
			// Setup variables
			CommandAPI.config = new InternalConfig(config);
			CommandAPI.logger = logger;

			// Initialize handlers
			AbstractPlatform<?> platform = CommandAPIVersionHandler.getPlatform();
			new BaseHandler<>(platform);
			BaseHandler.getInstance().onLoad();


			loaded = true;
		} else {
			getLogger().severe("You've tried to call the CommandAPI's onLoad() method more than once!");
		}
	}

	/**
	 * Enables the CommandAPI. This should be placed at the start of your
	 * <code>onEnable()</code> method.
	 *
	 * @param plugin the plugin that this onEnable method is called from
	 */
	// TODO: Is an Object the best way to reference a generic plugin?
	public static void onEnable(Object plugin) {
		BaseHandler.getInstance().onEnable(plugin);
	}

	/**
	 * Unloads the CommandAPI.
	 */
	public static void onDisable() {
		CommandAPI.canRegister = true;
		CommandAPI.config = null;
		CommandAPI.logger = null;
		CommandAPI.loaded = false;

		BaseHandler.getInstance().onDisable();
	}

	// Logging

	/**
	 * Logs a message to the console using Logger.info() if the configuration has
	 * verbose logging enabled
	 *
	 * @param message the message to log to the console
	 */
	public static void logInfo(String message) {
		if (config.hasVerboseOutput() && !config.hasSilentLogs()) {
			getLogger().info(message);
		}
	}

	/**
	 * Logs a message from the CommandAPI. If silent logs are enabled, this message
	 * is not logged.
	 *
	 * @param message the message to log
	 */
	public static void logNormal(String message) {
		if (!config.hasSilentLogs()) {
			getLogger().info(message);
		}
	}

	/**
	 * Logs a warning from the CommandAPI. If silent logs are enabled, this warning
	 * is not logged.
	 *
	 * @param message the message to log as a warning
	 */
	public static void logWarning(String message) {
		if (!config.hasSilentLogs()) {
			getLogger().warning(message);
		}
	}

	/**
	 * Logs an error from the CommandAPI. This always gets logged, even if silent
	 * logs are enabled.
	 *
	 * @param message the message to log as an error
	 */
	public static void logError(String message) {
		getLogger().severe(message);
	}

	// Produce WrapperCommandSyntaxException

	/**
	 * Forces a command to return a success value of 0
	 *
	 * @param message Description of the error message
	 * @return a {@link WrapperCommandSyntaxException} that wraps Brigadier's
	 * {@link CommandSyntaxException}
	 * @deprecated Please use {@link CommandAPI#failWithString(String)} instead
	 */
	@Deprecated
	public static WrapperCommandSyntaxException fail(String message) {
		return failWithString(message);
	}

	/**
	 * Forces a command to return a success value of 0
	 *
	 * @param message Description of the error message
	 * @return a {@link WrapperCommandSyntaxException} that wraps Brigadier's
	 * {@link CommandSyntaxException}
	 */
	public static WrapperCommandSyntaxException failWithString(String message) {
		return failWithMessage(AbstractTooltip.messageFromString(message));
	}

	/**
	 * Forces a command to return a success value of 0
	 *
	 * @param message Description of the error message, formatted as a brigadier message
	 * @return a {@link WrapperCommandSyntaxException} that wraps Brigadier's
	 * {@link CommandSyntaxException}
	 */
	public static WrapperCommandSyntaxException failWithMessage(Message message) {
		return new WrapperCommandSyntaxException(new SimpleCommandExceptionType(message).create());
	}

	// Command registration and unregistration

	/**
	 * Unregisters a command
	 *
	 * @param command the name of the command to unregister
	 */
	public static void unregister(String command) {
		// TODO: Uhhh, the platform unregister doesn't have force?
		BaseHandler.getInstance().getPlatform().unregister(command, false);
	}

	/**
	 * Unregisters a command, by force (removes all instances of that command)
	 *
	 * @param command the name of the command to unregister
	 * @param force   if true, attempt to unregister all instances of the command
	 *                across all plugins as well as minecraft, bukkit and spigot
	 */
	public static void unregister(String command, boolean force) {
		if (!canRegister) {
			getLogger().warning("Unexpected unregistering of /" + command
				+ ", as server is loaded! Unregistering anyway, but this can lead to unstable results!");
		}
		BaseHandler.getInstance().getPlatform().unregister(command, force);
	}

	/**
	 * Registers a command. Used with the CommandAPI's Annotation API.
	 *
	 * @param commandClass the class to register
	 */
	public static void registerCommand(Class<?> commandClass) {
		try {
			Class.forName(commandClass.getName() + "$Command").getDeclaredMethod("register").invoke(null);
		} catch (ReflectiveOperationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return A list of all {@link RegisteredCommand}{@code s} that have been
	 * registered by the CommandAPI so far. The returned list is immutable.
	 */
	public static List<RegisteredCommand> getRegisteredCommands() {
		return Collections.unmodifiableList(BaseHandler.getInstance().registeredCommands);
	}
}
