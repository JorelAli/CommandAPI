package io.github.jorelali.commandapi.api;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

import io.github.jorelali.commandapi.api.arguments.Argument;
import io.github.jorelali.commandapi.api.arguments.GreedyArgument;
import io.github.jorelali.commandapi.api.exceptions.GreedyArgumentException;
import io.github.jorelali.commandapi.api.exceptions.InvalidCommandNameException;
import io.github.jorelali.commandapi.api.exceptions.WrapperCommandSyntaxException;
import io.github.jorelali.commandapi.api.executors.CommandExecutor;
import io.github.jorelali.commandapi.api.executors.ResultingCommandExecutor;

/**
 * Class to register commands with the 1.13 command UI
 *
 */
public class CommandAPI {
	
	//Static instance of CommandAPI
	private static CommandAPI instance;
	
	protected static boolean canRegister = true;
	private static CommandAPIHandler handler;
	
	/**
	 * Forces a command to return a success value of 0
	 * @param message Description of the error message
	 * @throws WrapperCommandSyntaxException
	 */
	public static void fail(String message) throws WrapperCommandSyntaxException {
		throw new WrapperCommandSyntaxException(new SimpleCommandExceptionType(new LiteralMessage(message)).create());
	}
	
	/**
	 * An instance of the CommandAPI, used to register and unregister commands
	 * @return An instance of the CommandAPI
	 */
	public static CommandAPI getInstance() {
		return CommandAPI.instance;
	}	
	
	//Fixes all broken permissions
	protected static void fixPermissions() {
		handler.fixPermissions();
	}

	static {
		if(CommandAPI.instance == null) {
			CommandAPI.instance = new CommandAPI();
		}

		try {
			CommandAPI.handler = new CommandAPIHandler();			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Determines whether command registration is permitted via the CommandAPI
	 * @return A boolean representing whether commands can be registered or not.
	 */
	public boolean canRegister() {
		return CommandAPI.canRegister;
	}
	
	/**
	 * Unregisters a command
	 * @param command The name of the command to unregister
	 */
	public void unregister(String command) {
		handler.unregister(command, false);
	}
	
	/**
	 * Unregisters a command, by force (removes all instances of that command)
	 * @param command The name of the command to unregister
	 */
	public void unregister(String command, boolean force) {
		if(!canRegister) {
			CommandAPIMain.getLog().warning("Unexpected unregistering of /" + command + ", as server is loaded! Unregistering anyway, but this can lead to unstable results!");
		}
		handler.unregister(command, force);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Registers a command
	 * @param commandName The name of the command
	 * @param args The mapping of arguments for the command
	 * @param executor The command executor
	 * @deprecated Use new CommandAPICommand class instead to register commands
	 */
	@Deprecated
	public void register(String commandName, final LinkedHashMap<String, Argument> args, CommandExecutor executor) {
		register(commandName, CommandPermission.NONE, args, executor);
	}	

	/**
	 * Registers a command with aliases
	 * @param commandName The name of the command
	 * @param aliases The array of aliases which also run this command
	 * @param args The mapping of arguments for the command
	 * @param executor The command executor
	 * @deprecated Use new CommandAPICommand class instead to register commands
	 */
	@Deprecated
	public void register(String commandName, String[] aliases, final LinkedHashMap<String, Argument> args, CommandExecutor executor) {
		register(commandName, CommandPermission.NONE, aliases, args, executor);
	}
	
	/**
	 * Registers a command with permissions
	 * @param commandName The name of the command
	 * @param permissions The permissions required to run this command
	 * @param args The mapping of arguments for the command
	 * @param executor The command executor
	 * @deprecated Use new CommandAPICommand class instead to register commands
	 */
	@Deprecated
	public void register(String commandName, CommandPermission permissions, final LinkedHashMap<String, Argument> args, CommandExecutor executor) {
		register(commandName, permissions, new String[0], args, executor);
	}

	/**
	 * Registers a command with permissions and aliases
	 * @param commandName The name of the command
	 * @param permissions The permissions required to run this command
	 * @param aliases The array of aliases which also run this command
	 * @param args The mapping of arguments for the command
	 * @param executor The command executor
	 * @deprecated Use new CommandAPICommand class instead to register commands
	 */
	@Deprecated
	public void register(String commandName, CommandPermission permissions, String[] aliases, LinkedHashMap<String, Argument> args, CommandExecutor executor) {
		registerD(commandName, permissions, aliases, args, new CustomCommandExecutor(executor));
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Registers a command
	 * @param commandName The name of the command
	 * @param args The mapping of arguments for the command
	 * @param executor The command executor
	 * @deprecated Use new CommandAPICommand class instead to register commands
	 */
	@Deprecated
	public void register(String commandName, final LinkedHashMap<String, Argument> args, ResultingCommandExecutor executor) {
		register(commandName, CommandPermission.NONE, args, executor);
	}	

	/**
	 * Registers a command with aliases
	 * @param commandName The name of the command
	 * @param aliases The array of aliases which also run this command
	 * @param args The mapping of arguments for the command
	 * @param executor The command executor
	 * @deprecated Use new CommandAPICommand class instead to register commands
	 */
	@Deprecated
	public void register(String commandName, String[] aliases, final LinkedHashMap<String, Argument> args, ResultingCommandExecutor executor) {
		register(commandName, CommandPermission.NONE, aliases, args, executor);
	}
	
	/**
	 * Registers a command with permissions
	 * @param commandName The name of the command
	 * @param permissions The permissions required to run this command
	 * @param args The mapping of arguments for the command
	 * @param executor The command executor
	 * @deprecated Use new CommandAPICommand class instead to register commands
	 */
	@Deprecated
	public void register(String commandName, CommandPermission permissions, final LinkedHashMap<String, Argument> args, ResultingCommandExecutor executor) {
		register(commandName, permissions, new String[0], args, executor);
	}

	/**
	 * Registers a command with permissions and aliases
	 * @param commandName The name of the command
	 * @param permissions The permissions required to run this command
	 * @param aliases The array of aliases which also run this command
	 * @param args The mapping of arguments for the command
	 * @param executor The command executor
	 * @deprecated Use new CommandAPICommand class instead to register commands
	 */
	@Deprecated
	public void register(String commandName, CommandPermission permissions, String[] aliases, LinkedHashMap<String, Argument> args, ResultingCommandExecutor executor) {
		registerD(commandName, permissions, aliases, args, new CustomCommandExecutor(executor));
	}
	
	private void registerD(String commandName, CommandPermission permissions, String[] aliases, LinkedHashMap<String, Argument> args, CustomCommandExecutor executor) {
		CommandAPIMain.getLog().warning("Command /" + commandName + " is being registered with the old register() method. This is deprecated as of 3.0. Consider using the new CommandAPICommand registration system.");
		register(commandName, permissions, aliases, args, executor);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	void register(String commandName, CommandPermission permissions, String[] aliases, LinkedHashMap<String, Argument> args, CustomCommandExecutor executor) {
		if(!canRegister) {
			CommandAPIMain.getLog().severe("Cannot register command /" + commandName + ", because the server has finished loading!");
			return;
		}
		try {

			//Sanitize commandNames
			if(commandName == null || commandName.length() == 0) {
				throw new InvalidCommandNameException(commandName);
			}
			
			//Make a local copy of args to deal with
			@SuppressWarnings("unchecked")
			LinkedHashMap<String, Argument> copyOfArgs = args == null ? new LinkedHashMap<>() : (LinkedHashMap<String, Argument>) args.clone();
			
			//if args contains a GreedyString && args.getLast != GreedyString
			long numGreedyArgs = copyOfArgs.values().stream().filter(arg -> arg instanceof GreedyArgument).count();
			if(numGreedyArgs >= 1) {
				//A GreedyString has been found
				if(!(copyOfArgs.values().toArray(new Argument[copyOfArgs.size()])[copyOfArgs.size() - 1] instanceof GreedyArgument)) {
					throw new GreedyArgumentException();
				}
				
				if(numGreedyArgs > 1) {
					throw new GreedyArgumentException();
				}
			}
			
			//Reassign permissions to arguments if not declared
			for(Entry<String, Argument> entry : copyOfArgs.entrySet()) {
				if(entry.getValue().getArgumentPermission() == null) {
					entry.setValue(entry.getValue().withPermission(permissions));
				}
			}
			
			handler.register(commandName, permissions, aliases, copyOfArgs, executor);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
