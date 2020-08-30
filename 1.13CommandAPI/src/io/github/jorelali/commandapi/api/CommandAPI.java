package io.github.jorelali.commandapi.api;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

import io.github.jorelali.commandapi.api.arguments.Argument;
import io.github.jorelali.commandapi.api.arguments.GreedyArgument;
import io.github.jorelali.commandapi.api.arguments.LiteralArgument;
import io.github.jorelali.commandapi.api.arguments.SuperLiteralArgument;
import io.github.jorelali.commandapi.api.exceptions.GreedyArgumentException;
import io.github.jorelali.commandapi.api.exceptions.InvalidCommandNameException;
import io.github.jorelali.commandapi.api.exceptions.WrapperCommandSyntaxException;

/**
 * Class to register commands with the 1.13 command UI
 *
 */
public final class CommandAPI {
	
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
			
			if(copyOfArgs.values().stream().filter(arg -> arg instanceof SuperLiteralArgument).count() > 0) {
				Set<LinkedHashMap<String, Argument>> argsSet = new HashSet<>();
				
				for(Entry<String, Argument> entry : copyOfArgs.entrySet()) {
					
					List<LinkedHashMap<String, Argument>> newArgs = new ArrayList<>();
					
					int size = copyOfArgs.values().stream().filter(arg -> arg instanceof SuperLiteralArgument).reduce(0, (acc, arg) -> {
						return ((SuperLiteralArgument) arg).getLiterals().length;
					}, (a, b) -> a + b);
					
					for(int i = 0; i < size; i++) {
						newArgs.add(new LinkedHashMap<>());
					}
					
					int index = 0;
					
					if(entry.getValue() instanceof SuperLiteralArgument) {
						
						SuperLiteralArgument superArg = (SuperLiteralArgument) entry.getValue();
						for(int i = 0; i < superArg.getLiterals().length; i++) {
							LiteralArgument litArg = new LiteralArgument(superArg.getLiterals()[i]);
							litArg.isSuper = true;
														
							newArgs.get(index++).put(entry.getKey(), litArg);
						}
						index = 0;
						
					} else {
						newArgs.get(index).put(entry.getKey(), entry.getValue());
					}
					
					argsSet.addAll(newArgs);
				}
				
				for(LinkedHashMap<String, Argument> newArg : argsSet) {
					handler.register(commandName, permissions, aliases, newArg, executor);
				}
				
			} else {
				handler.register(commandName, permissions, aliases, copyOfArgs, executor);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
