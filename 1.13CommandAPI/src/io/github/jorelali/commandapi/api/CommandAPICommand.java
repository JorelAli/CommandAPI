package io.github.jorelali.commandapi.api;

import java.util.LinkedHashMap;

import io.github.jorelali.commandapi.api.arguments.Argument;
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
	
	public CommandAPICommand(String commandName) {
		this.commandName = commandName;
	}
	
	public CommandAPICommand withPermission(CommandPermission permission) {
		this.permission = permission;
		return this;
	}
	
	public CommandAPICommand withAliases(String[] aliases) {
		this.aliases = aliases;
		return this;
	}
	
	public CommandAPICommand withArguments(LinkedHashMap<String, Argument> args) {
		this.args = args;
		return this;
	}
	
	// Executors 
	
	public CommandAPICommand executes(CommandExecutor executor) {
		this.executor = new CustomCommandExecutor(executor);
		return this;
	}
	
	public CommandAPICommand executes(ResultingCommandExecutor executor) {
		this.executor = new CustomCommandExecutor(executor);
		return this;
	}
	
	public CommandAPICommand executesPlayer(PlayerCommandExecutor executor) {
		this.executor = new CustomCommandExecutor(executor);
		return this;
	}
	
	public CommandAPICommand executesPlayer(PlayerResultingCommandExecutor executor) {
		this.executor = new CustomCommandExecutor(executor);
		return this;
	}
	
	public CommandAPICommand executesEntity(EntityCommandExecutor executor) {
		this.executor = new CustomCommandExecutor(executor);
		return this;
	}
	
	public CommandAPICommand executesEntity(EntityResultingCommandExecutor executor) {
		this.executor = new CustomCommandExecutor(executor);
		return this;
	}
	
	// Registration
	
	public void register() {
		if(executor == null) {
			CommandAPIMain.getLog().severe("Failed to register command /" + commandName + " - invalid executor");
			return;
		} else {
			CommandAPI.getInstance().register(commandName, permission, aliases, args, executor);
		}
	}
	
}
