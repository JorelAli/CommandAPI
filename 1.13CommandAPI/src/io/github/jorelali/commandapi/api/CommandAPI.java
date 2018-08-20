package io.github.jorelali.commandapi.api;

import java.util.LinkedHashMap;

import io.github.jorelali.commandapi.api.arguments.Argument;

/**
 * Class to register commands with the 1.13 command UI
 *
 */
public class CommandAPI {

	private static CommandAPI instance;
	
	public static CommandAPI getInstance() {
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
	 * Register a new command
	 * 
	 * @param commandName
	 *            The name of the command to register (e.g. "god"). A forward
	 *            slash is not needed
	 * @param args
	 *            The mapping of argument descriptions to argument types, in the
	 *            order of execution.
	 * @param executor
	 *            The code to run when this command is performed
	 */
	public void register(String commandName, final LinkedHashMap<String, Argument> args, CommandExecutor executor) {

		try {
			reflector.register(commandName, args, executor);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
