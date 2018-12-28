package io.github.jorelali.commandapi.api;

import java.util.LinkedHashMap;

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

import io.github.jorelali.commandapi.api.arguments.Argument;
import io.github.jorelali.commandapi.api.arguments.GreedyStringArgument;
import io.github.jorelali.commandapi.api.exceptions.GreedyStringException;
import io.github.jorelali.commandapi.api.exceptions.InvalidCommandNameException;

/**
 * Class to register commands with the 1.13 command UI
 *
 */
public class CommandAPI {
	
	//Static instance of CommandAPI
	private static CommandAPI instance;
	
	protected static boolean canRegister = true;
	private static SemiReflector reflector;
	
	/**
	 * Forces a command to return a success value of 0
	 * @param message Description of the error message
	 * @throws CommandSyntaxException
	 */
	public static void fail(String message) throws CommandSyntaxException {
		throw new SimpleCommandExceptionType(new LiteralMessage(message)).create();
	}
	
	/**
	 * An instance of the CommandAPI, used to register and unregister commands
	 * @return An instance of the CommandAPI
	 */
	public static CommandAPI getInstance() {
		if(instance == null) {
			new CommandAPI();
		}
		return instance;
	}	
	
	//Fixes all broken permissions
	protected static void fixPermissions() {
		reflector.fixPermissions();
	}
	

	protected CommandAPI() {
		if(instance == null) {
			instance = this;
		} else {
			throw new RuntimeException("CommandAPI cannot be instantiated twice"); //Don't need to re-instantiate CommandAPI
		}
		
		//Only ever called once
		try {
			CommandAPI.reflector = new SemiReflector();			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Unregisters a command
	 * @param command The name of the command to unregister
	 */
	public void unregister(String command) {
		reflector.unregister(command, false);
	}
	
	/**
	 * Unregisters a command, by force (removes all instances of that command)
	 * @param command The name of the command to unregister
	 */
	public void unregister(String command, boolean force) {
		if(!canRegister) {
			CommandAPIMain.getLog().warning("Unexpected unregistering of /" + command + ", as server is loaded! Unregistering anyway, but this can lead to unstable results!");
		}
		reflector.unregister(command, force);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Registers a command
	 * @param commandName The name of the command
	 * @param args The mapping of arguments for the command
	 * @param executor The command executor
	 */
	public void register(String commandName, final LinkedHashMap<String, Argument> args, CommandExecutor executor) {
		register(commandName, CommandPermission.NONE, args, executor);
	}	

	/**
	 * Registers a command with aliases
	 * @param commandName The name of the command
	 * @param aliases The array of aliases which also run this command
	 * @param args The mapping of arguments for the command
	 * @param executor The command executor
	 */
	public void register(String commandName, String[] aliases, final LinkedHashMap<String, Argument> args, CommandExecutor executor) {
		register(commandName, CommandPermission.NONE, aliases, args, executor);
	}
	
	/**
	 * Registers a command with permissions
	 * @param commandName The name of the command
	 * @param permissions The permissions required to run this command
	 * @param args The mapping of arguments for the command
	 * @param executor The command executor
	 */
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
	 */
	public void register(String commandName, CommandPermission permissions, String[] aliases, LinkedHashMap<String, Argument> args, CommandExecutor executor) {
		register(commandName, permissions, aliases, args, new CustomCommandExecutor(executor, null));
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Registers a command
	 * @param commandName The name of the command
	 * @param args The mapping of arguments for the command
	 * @param executor The command executor
	 */
	public void register(String commandName, final LinkedHashMap<String, Argument> args, ResultingCommandExecutor executor) {
		register(commandName, CommandPermission.NONE, args, executor);
	}	

	/**
	 * Registers a command with aliases
	 * @param commandName The name of the command
	 * @param aliases The array of aliases which also run this command
	 * @param args The mapping of arguments for the command
	 * @param executor The command executor
	 */
	public void register(String commandName, String[] aliases, final LinkedHashMap<String, Argument> args, ResultingCommandExecutor executor) {
		register(commandName, CommandPermission.NONE, aliases, args, executor);
	}
	
	/**
	 * Registers a command with permissions
	 * @param commandName The name of the command
	 * @param permissions The permissions required to run this command
	 * @param args The mapping of arguments for the command
	 * @param executor The command executor
	 */
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
	 */
	public void register(String commandName, CommandPermission permissions, String[] aliases, LinkedHashMap<String, Argument> args, ResultingCommandExecutor executor) {
		register(commandName, permissions, aliases, args, new CustomCommandExecutor(null, executor));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private void register(String commandName, CommandPermission permissions, String[] aliases, LinkedHashMap<String, Argument> args, CustomCommandExecutor executor) {
		if(!canRegister) {
			CommandAPIMain.getLog().severe("Cannot register command /" + commandName + ", because server has finished loading!");
			return;
		}
		try {
			
			//Sanitize commandNames
			if(commandName.length() == 0 || commandName == null) {
				throw new InvalidCommandNameException(commandName);
			}
			
			if(args == null) {
				args = new LinkedHashMap<>();
			}
			
			//Make a local copy of args to deal with
			@SuppressWarnings("unchecked")
			LinkedHashMap<String, Argument> copyOfArgs = (LinkedHashMap<String, Argument>) args.clone();
			
			//if args contains a GreedyString && args.getLast != GreedyString
			long numGreedyArgs = copyOfArgs.values().stream().filter(arg -> arg instanceof GreedyStringArgument).count();
			if(numGreedyArgs >= 1) {
				//A GreedyString has been found
				if(!(copyOfArgs.values().toArray(new Argument[copyOfArgs.size()])[copyOfArgs.size() - 1] instanceof GreedyStringArgument)) {
					throw new GreedyStringException();
				}
				
				if(numGreedyArgs > 1) {
					throw new GreedyStringException();
				}
			}
			reflector.register(commandName, permissions, aliases, copyOfArgs, executor);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
