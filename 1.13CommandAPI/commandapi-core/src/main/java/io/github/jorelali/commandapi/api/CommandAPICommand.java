package io.github.jorelali.commandapi.api;

import java.util.LinkedHashMap;

import io.github.jorelali.commandapi.api.arguments.Argument;
import io.github.jorelali.commandapi.api.executors.CommandBlockCommandExecutor;
import io.github.jorelali.commandapi.api.executors.CommandBlockResultingCommandExecutor;
import io.github.jorelali.commandapi.api.executors.CommandExecutor;
import io.github.jorelali.commandapi.api.executors.EntityCommandExecutor;
import io.github.jorelali.commandapi.api.executors.EntityResultingCommandExecutor;
import io.github.jorelali.commandapi.api.executors.PlayerCommandExecutor;
import io.github.jorelali.commandapi.api.executors.PlayerResultingCommandExecutor;
import io.github.jorelali.commandapi.api.executors.ResultingCommandExecutor;

public class CommandAPICommand {

	private final String commandName;
	private CommandPermission permission = CommandPermission.NONE;
	private String[] aliases = new String[0];
	private LinkedHashMap<String, Argument> args = new LinkedHashMap<>();
	private CustomCommandExecutor executor = null;
	
	/**
	 * Creates a new command builder
	 * @param commandName The name of the command to create
	 */
	public CommandAPICommand(String commandName) {
		this.commandName = commandName;
	}
	
	/**
	 * Applies a permission to the current command builder
	 * @param permission The permission node required to execute this command
	 * @return This command builder
	 */
	public CommandAPICommand withPermission(CommandPermission permission) {
		this.permission = permission;
		return this;
	}
	
	/**
	 * Adds an array of aliases to the current command builder
	 * @param aliases An array of aliases which can be used to execute this command
	 * @return This command builder
	 */
	public CommandAPICommand withAliases(String... aliases) {
		this.aliases = aliases;
		return this;
	}
	
	/**
	 * Adds the mapping of arguments to the current command builder
	 * @param args A <code>LinkedHashMap</code> that represents the arguments that this command can accept
	 * @return This command builder
	 */
	public CommandAPICommand withArguments(LinkedHashMap<String, Argument> args) {
		this.args = args;
		return this;
	}
	
	// Regular command executor 
	
	/**
	 * Adds an executor to the current command builder
	 * @param executor A lambda of type <code>(CommandSender, Object[]) -> ()</code> that will be executed when the command is run
	 */
	public void executes(CommandExecutor executor) {
		this.executor = new CustomCommandExecutor(executor);
		register();
	}
	
	/**
	 * Adds an executor to the current command builder
	 * @param executor A lambda of type <code>(CommandSender, Object[]) -> int</code> that will be executed when the command is run
	 */
	public void executes(ResultingCommandExecutor executor) {
		this.executor = new CustomCommandExecutor(executor);
		register();
	}
	
	// Player command executor
	
	/**
	 * Adds an executor to the current command builder
	 * @param executor A lambda of type <code>(Player, Object[]) -> ()</code> that will be executed when the command is run
	 */
	public void executesPlayer(PlayerCommandExecutor executor) {
		this.executor = new CustomCommandExecutor(executor);
		register();
	}
	
	/**
	 * Adds an executor to the current command builder
	 * @param executor A lambda of type <code>(Player, Object[]) -> int</code> that will be executed when the command is run
	 */
	public void executesPlayer(PlayerResultingCommandExecutor executor) {
		this.executor = new CustomCommandExecutor(executor);
		register();
	}
	
	// Entity command executor
	
	/**
	 * Adds an executor to the current command builder
	 * @param executor A lambda of type <code>(Entity, Object[]) -> ()</code> that will be executed when the command is run
	 */
	public void executesEntity(EntityCommandExecutor executor) {
		this.executor = new CustomCommandExecutor(executor);
		register();
	}
	
	/**
	 * Adds an executor to the current command builder
	 * @param executor A lambda of type <code>(Entity, Object[]) -> int</code> that will be executed when the command is run
	 */
	public void executesEntity(EntityResultingCommandExecutor executor) {
		this.executor = new CustomCommandExecutor(executor);
		register();
	}
	
	// Command block command sender
	
	/**
	 * Adds an executor to the current command builder
	 * @param executor A lambda of type <code>(BlockCommandSender, Object[]) -> ()</code> that will be executed when the command is run
	 */
	public void executesCommandBlock(CommandBlockCommandExecutor executor) {
		this.executor = new CustomCommandExecutor(executor);
		register();
	}
	
	/**
	 * Adds an executor to the current command builder
	 * @param executor A lambda of type <code>(BlockCommandSender, Object[]) -> int</code> that will be executed when the command is run
	 */
	public void executesCommandBlock(CommandBlockResultingCommandExecutor executor) {
		this.executor = new CustomCommandExecutor(executor);
		register();
	}
	
	/**
	 * Registers the command
	 */
	private void register() {
		CommandAPI.getInstance().register(commandName, permission, aliases, args, executor);
	}
	
}
