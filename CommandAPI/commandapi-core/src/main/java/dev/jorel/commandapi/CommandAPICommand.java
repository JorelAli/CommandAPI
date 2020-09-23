package dev.jorel.commandapi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import org.bukkit.command.CommandSender;

import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.IGreedyArgument;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.exceptions.EmptyExecutorException;
import dev.jorel.commandapi.exceptions.GreedyArgumentException;
import dev.jorel.commandapi.exceptions.InvalidCommandNameException;
import dev.jorel.commandapi.executors.CommandBlockCommandExecutor;
import dev.jorel.commandapi.executors.CommandBlockResultingCommandExecutor;
import dev.jorel.commandapi.executors.CommandExecutor;
import dev.jorel.commandapi.executors.ConsoleCommandExecutor;
import dev.jorel.commandapi.executors.ConsoleResultingCommandExecutor;
import dev.jorel.commandapi.executors.EntityCommandExecutor;
import dev.jorel.commandapi.executors.EntityResultingCommandExecutor;
import dev.jorel.commandapi.executors.NativeCommandExecutor;
import dev.jorel.commandapi.executors.NativeResultingCommandExecutor;
import dev.jorel.commandapi.executors.PlayerCommandExecutor;
import dev.jorel.commandapi.executors.PlayerResultingCommandExecutor;
import dev.jorel.commandapi.executors.ProxyCommandExecutor;
import dev.jorel.commandapi.executors.ProxyResultingCommandExecutor;
import dev.jorel.commandapi.executors.ResultingCommandExecutor;

/**
 * A builder used to create commands to be registered by the CommandAPI.
 */
public class CommandAPICommand {

	final String commandName;
	CommandPermission permission = CommandPermission.NONE;
	String[] aliases = new String[0];
	Predicate<CommandSender> requirements = s -> true;
	List<Argument> args = new ArrayList<>();
	List<CommandAPICommand> subcommands = new ArrayList<>();
	CustomCommandExecutor executor = new CustomCommandExecutor();
	boolean isConverted;
	
	/**
	 * Creates a new command builder
	 * @param commandName The name of the command to create
	 */
	public CommandAPICommand(String commandName) {
		this.commandName = commandName;
		this.isConverted = false;
	}
	
	static CommandAPICommand convertedOf(String commandName) {
		CommandAPICommand result = new CommandAPICommand(commandName);
		result.isConverted = true;
		return result;
	}
	
	/**
	 * Applies a permission to the current command builder
	 * @param permission The permission node required to execute this command
	 * @return this command builder
	 */
	public CommandAPICommand withPermission(CommandPermission permission) {
		this.permission = permission;
		return this;
	}
	
	/**
	 * Adds a requirement that has to be satisfied to use this command. This method
	 * can be used multiple times and each use of this method will AND its
	 * requirement with the previously declared ones
	 * 
	 * @param requirement the predicate that must be satisfied to use this command
	 * @return this command builder
	 */
	public CommandAPICommand withRequirement(Predicate<CommandSender> requirement) {
		this.requirements = this.requirements.and(requirement);
		return this;
	}
	
	/**
	 * Adds an array of aliases to the current command builder
	 * @param aliases An array of aliases which can be used to execute this command
	 * @return this command builder
	 */
	public CommandAPICommand withAliases(String... aliases) {
		this.aliases = aliases;
		return this;
	}
	
	/**
	 * Adds the mapping of arguments to the current command builder
	 * @param args A <code>List</code> that represents the arguments that this command can accept
	 * @return this command builder
	 */
	public CommandAPICommand withArguments(List<Argument> args) {
		this.args.addAll(args);
		return this;
	}
	
	public CommandAPICommand withArguments(Argument... args) {
		this.args = Arrays.asList(args);
		return this;
	}
	
	public CommandAPICommand withArgument(Argument argument) {
		this.args.add(argument);
		return this;
	}
	
	public CommandAPICommand withSubcommand(CommandAPICommand subcommand) {
		this.subcommands.add(subcommand);
		return this;
	}
	
	// Regular command executor 
	
	/**
	 * Adds an executor to the current command builder
	 * @param executor A lambda of type <code>(CommandSender, Object[]) -> ()</code> that will be executed when the command is run
	 * @return this command builder
	 */
	public CommandAPICommand executes(CommandExecutor executor) {
		this.executor.addNormalExecutor(executor);
		return this;
	}
	
	/**
	 * Adds an executor to the current command builder
	 * @param executor A lambda of type <code>(CommandSender, Object[]) -> int</code> that will be executed when the command is run
	 * @return this command builder
	 */
	public CommandAPICommand executes(ResultingCommandExecutor executor) {
		this.executor.addResultingExecutor(executor);
		return this;
	}
	
	// Player command executor
	
	/**
	 * Adds an executor to the current command builder
	 * @param executor A lambda of type <code>(Player, Object[]) -> ()</code> that will be executed when the command is run
	 * @return this command builder
	 */
	public CommandAPICommand executesPlayer(PlayerCommandExecutor executor) {
		this.executor.addNormalExecutor(executor);
		return this;
	}
	
	/**
	 * Adds an executor to the current command builder
	 * @param executor A lambda of type <code>(Player, Object[]) -> int</code> that will be executed when the command is run
	 * @return this command builder
	 */
	public CommandAPICommand executesPlayer(PlayerResultingCommandExecutor executor) {
		this.executor.addResultingExecutor(executor);
		return this;
	}
	
	// Entity command executor
	
	/**
	 * Adds an executor to the current command builder
	 * @param executor A lambda of type <code>(Entity, Object[]) -> ()</code> that will be executed when the command is run
	 * @return this command builder
	 */
	public CommandAPICommand executesEntity(EntityCommandExecutor executor) {
		this.executor.addNormalExecutor(executor);
		return this;
	}
	
	/**
	 * Adds an executor to the current command builder
	 * @param executor A lambda of type <code>(Entity, Object[]) -> int</code> that will be executed when the command is run
	 * @return this command builder
	 */
	public CommandAPICommand executesEntity(EntityResultingCommandExecutor executor) {
		this.executor.addResultingExecutor(executor);
		return this;
	}
	
	// Proxy command executor
	
	/**
	 * Adds an executor to the current command builder
	 * @param executor A lambda of type <code>(Entity, Object[]) -> ()</code> that will be executed when the command is run
	 * @return this command builder
	 */
	public CommandAPICommand executesProxy(ProxyCommandExecutor executor) {
		this.executor.addNormalExecutor(executor);
		return this;
	}
	
	/**
	 * Adds an executor to the current command builder
	 * @param executor A lambda of type <code>(Entity, Object[]) -> int</code> that will be executed when the command is run
	 * @return this command builder
	 */
	public CommandAPICommand executesProxy(ProxyResultingCommandExecutor executor) {
		this.executor.addResultingExecutor(executor);
		return this;
	}
	
	// Command block command sender
	
	/**
	 * Adds an executor to the current command builder
	 * @param executor A lambda of type <code>(BlockCommandSender, Object[]) -> ()</code> that will be executed when the command is run
	 * @return this command builder
	 */
	public CommandAPICommand executesCommandBlock(CommandBlockCommandExecutor executor) {
		this.executor.addNormalExecutor(executor);
		return this;
	}
	
	/**
	 * Adds an executor to the current command builder
	 * @param executor A lambda of type <code>(BlockCommandSender, Object[]) -> int</code> that will be executed when the command is run
	 * @return this command builder
	 */
	public CommandAPICommand executesCommandBlock(CommandBlockResultingCommandExecutor executor) {
		this.executor.addResultingExecutor(executor);
		return this;
	}
	
	// Console command sender
	
	/**
	 * Adds an executor to the current command builder
	 * @param executor A lambda of type <code>(BlockCommandSender, Object[]) -> ()</code> that will be executed when the command is run
	 * @return this command builder
	 */
	public CommandAPICommand executesConsole(ConsoleCommandExecutor executor) {
		this.executor.addNormalExecutor(executor);
		return this;
	}
	
	/**
	 * Adds an executor to the current command builder
	 * @param executor A lambda of type <code>(BlockCommandSender, Object[]) -> int</code> that will be executed when the command is run
	 * @return this command builder
	 */
	public CommandAPICommand executesConsole(ConsoleResultingCommandExecutor executor) {
		this.executor.addResultingExecutor(executor);
		return this;
	}
	
	/**
	 * Adds an executor to the current command builder
	 * @param executor A lambda of type <code>(NativeCommandExecutor, Object[]) -> ()</code> that will be executed when the command is run
	 * @return this command builder
	 */
	public CommandAPICommand executesNative(NativeCommandExecutor executor) {
		this.executor.addNormalExecutor(executor);
		return this;
	}
	
	/**
	 * Adds an executor to the current command builder
	 * @param executor A lambda of type <code>(NativeCommandExecutor, Object[]) -> int</code> that will be executed when the command is run
	 * @return this command builder
	 */
	public CommandAPICommand executesNative(NativeResultingCommandExecutor executor) {
		this.executor.addResultingExecutor(executor);
		return this;
	}
	
	/**
	 * Registers the command
	 */
	public void register() {
		if(!CommandAPI.canRegister()) {
			CommandAPI.getLog().severe("Cannot register command /" + commandName + ", because the server has finished loading!");
			return;
		}
		try {
			//Sanitize commandNames
			if(commandName == null || commandName.length() == 0) {
				throw new InvalidCommandNameException(commandName);
			}
			
			//Make a local copy of args to deal with
			List<Argument> copyOfArgs = args == null ? new ArrayList<>() : new ArrayList<>(args);
			
			//if args contains a GreedyString && args.getLast != GreedyString
			long numGreedyArgs = copyOfArgs.stream().filter(arg -> arg instanceof IGreedyArgument).count();
			if(numGreedyArgs >= 1) {
				//A GreedyString has been found
				if(!(copyOfArgs.toArray()[copyOfArgs.size() - 1] instanceof IGreedyArgument)) {
					throw new GreedyArgumentException();
				}
				
				if(numGreedyArgs > 1) {
					throw new GreedyArgumentException();
				}
			}
			
			//Assign the command's permissions to arguments if the arguments don't already have one
			for(Argument argument : copyOfArgs) {
				if(argument.getArgumentPermission() == null) {
					argument.withPermission(permission);
				}
			}
			
			if(!executor.isEmpty()) {
				CommandAPIHandler.register(commandName, permission, aliases, requirements, copyOfArgs, executor, isConverted);
			}
			
			for(CommandAPICommand subcommand : subcommands) {
				CommandAPICommand cmdToRegister = new CommandAPICommand(commandName)
					.withArgument(new LiteralArgument(subcommand.commandName))
					.withArguments(subcommand.args)
					.withPermission(subcommand.permission)
					.withRequirement(subcommand.requirements)
					.withAliases(subcommand.aliases);
				cmdToRegister.executor = subcommand.executor;
				//isConverted?
				cmdToRegister.register();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
