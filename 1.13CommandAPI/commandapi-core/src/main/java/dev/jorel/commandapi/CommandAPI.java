package dev.jorel.commandapi;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.GreedyArgument;
import dev.jorel.commandapi.exceptions.GreedyArgumentException;
import dev.jorel.commandapi.exceptions.InvalidCommandNameException;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;

/**
 * Class to register commands with the 1.13 command UI
 *
 */
public class CommandAPI {
	
	static boolean canRegister = true;
	private static CommandAPIHandler handler;

	static {
		try {
			CommandAPI.handler = new CommandAPIHandler();			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Prevents command registration when the server has finished loading and fixes
	 * the registration of permissions.
	 */
	static void cleanup() {
		canRegister = false;
		
		//Sort out permissions after the server has finished registering them all
		handler.fixPermissions();
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
	 * @return A boolean representing whether commands can be registered or not.
	 */
	public static boolean canRegister() {
		return CommandAPI.canRegister;
	}
	
	/**
	 * Unregisters a command
	 * @param command The name of the command to unregister
	 */
	public static void unregister(String command) {
		handler.unregister(command, false);
	}
	
	/**
	 * Unregisters a command, by force (removes all instances of that command)
	 * @param command The name of the command to unregister
	 */
	public static void unregister(String command, boolean force) {
		if(!canRegister) {
			CommandAPIMain.getLog().warning("Unexpected unregistering of /" + command + ", as server is loaded! Unregistering anyway, but this can lead to unstable results!");
		}
		handler.unregister(command, force);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	static void register(String commandName, CommandPermission permissions, String[] aliases, LinkedHashMap<String, Argument> args, CustomCommandExecutor executor) {
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
				if(!(copyOfArgs.values().toArray()[copyOfArgs.size() - 1] instanceof GreedyArgument)) {
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
