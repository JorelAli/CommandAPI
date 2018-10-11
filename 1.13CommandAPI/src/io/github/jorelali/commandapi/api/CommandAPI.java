package io.github.jorelali.commandapi.api;

import java.util.LinkedHashMap;

import io.github.jorelali.commandapi.api.CommandPermission.PermissionNode;
import io.github.jorelali.commandapi.api.arguments.Argument;
import io.github.jorelali.commandapi.api.arguments.GreedyStringArgument;
import io.github.jorelali.commandapi.api.exceptions.GreedyStringException;

/**
 * Class to register commands with the 1.13 command UI
 *
 */
public class CommandAPI {

	//Static instance of CommandAPI
	private static CommandAPI instance;
	
	public static CommandAPI getInstance() {
		if(instance == null) {
			new CommandAPI();
		}
		return instance;
	}	
	
	private SemiReflector reflector;

	/**
	 * Deprecated as of version 1.1 - use CommandAPI.getInstance() instead
	 */
	@Deprecated
	public CommandAPI() {
		if(instance == null) {
			instance = this;
		} else {
			throw new RuntimeException("CommandAPI cannot be instantiated twice"); //Don't need to re-instantiate CommandAPI
		}
		
		try {
			this.reflector = new SemiReflector();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Registers a command
	 * @param commandName The name of the command
	 * @param args The mapping of arguments for the command
	 * @param executor The command executor
	 */
	public void register(String commandName, final LinkedHashMap<String, Argument> args, CommandExecutor executor) {
		register(commandName, new CommandPermission(PermissionNode.NONE), args, executor);
	}	

	/**
	 * Registers a command with aliases
	 * @param commandName The name of the command
	 * @param aliases The array of aliases which also run this command
	 * @param args The mapping of arguments for the command
	 * @param executor The command executor
	 */
	public void register(String commandName, String[] aliases, final LinkedHashMap<String, Argument> args, CommandExecutor executor) {
		register(commandName, new CommandPermission(PermissionNode.NONE), aliases, args, executor);
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
		try {
			if(args == null) {
				args = new LinkedHashMap<>();
			}
			//if args contains a GreedyString && args.getLast != GreedyString
			long numGreedyArgs = args.values().stream().filter(arg -> arg instanceof GreedyStringArgument).count();
			if(numGreedyArgs >= 1) {
				//A GreedyString has been found
				if(!(args.values().toArray(new Argument[args.size()])[args.size() - 1] instanceof GreedyStringArgument)) {
					throw new GreedyStringException();
				}
				
				if(numGreedyArgs > 1) {
					throw new GreedyStringException();
				}
			}
			reflector.register(commandName, permissions, aliases, args, executor);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
