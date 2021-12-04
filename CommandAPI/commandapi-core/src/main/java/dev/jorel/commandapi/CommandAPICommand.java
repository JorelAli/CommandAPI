/*******************************************************************************
 * Copyright 2018, 2020 Jorel Ali (Skepter) - MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *******************************************************************************/
package dev.jorel.commandapi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import org.bukkit.command.CommandSender;

import com.mojang.brigadier.exceptions.CommandSyntaxException;

import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.IGreedyArgument;
import dev.jorel.commandapi.arguments.MultiLiteralArgument;
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

	private final String commandName;
	
	private CommandPermission permission = CommandPermission.NONE;
	private String[] aliases = new String[0];
	private Predicate<CommandSender> requirements = s -> true;
	private List<Argument> args = new ArrayList<>();
	private List<CommandAPICommand> subcommands = new ArrayList<>();
	private CustomCommandExecutor<?> executor = new CustomCommandExecutor<>();
	private boolean isConverted;
	private Optional<String> shortDescription = Optional.empty();
	private Optional<String> fullDescription = Optional.empty();
	
	/**
	 * Creates a new command builder
	 * @param commandName The name of the command to create
	 */
	public CommandAPICommand(String commandName) {
		if(commandName == null || commandName.length() == 0) {
			throw new InvalidCommandNameException(commandName);
		}
		
		this.commandName = commandName;
		this.isConverted = false;
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
	 * Applies a permission to the current command builder
	 * @param permission The permission node required to execute this command
	 * @return this command builder
	 */
	public CommandAPICommand withPermission(String permission) {
		this.permission = CommandPermission.fromString(permission);
		return this;
	}
	
	/**
	 * Applies a permission to the current command builder
	 * @param permission The permission node required to execute this command
	 * @return this command builder
	 */
	public CommandAPICommand withoutPermission(CommandPermission permission) {
		this.permission = permission.negate();
		return this;
	}
	
	/**
	 * Applies a permission to the current command builder
	 * @param permission The permission node required to execute this command
	 * @return this command builder
	 */
	public CommandAPICommand withoutPermission(String permission) {
		this.permission = CommandPermission.fromString(permission).negate();
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
	 * Appends the arguments to the current command builder
	 * @param args A <code>List</code> that represents the arguments that this command can accept
	 * @return this command builder
	 */
	public CommandAPICommand withArguments(List<Argument> args) {
		this.args.addAll(args);
		return this;
	}
	
	/**
	 * Appends the argument(s) to the current command builder
	 * @param args Arguments that this command can accept
	 * @return this command builder
	 */
	public CommandAPICommand withArguments(Argument... args) {
		this.args.addAll(Arrays.asList(args));
		return this;
	}
	
	/**
	 * Adds a subcommand to this command builder
	 * @param subcommand the subcommand to add as a child of this command 
	 * @return this command builder
	 */
	public CommandAPICommand withSubcommand(CommandAPICommand subcommand) {
		this.subcommands.add(subcommand);
		return this;
	}
	
	/**
	 * Sets the short description for this command. This is the help which is
	 * shown in the main /help menu.
	 * @param description the short description for this command
	 * @return this command builder
	 */
	public CommandAPICommand withShortDescription(String description) {
		this.shortDescription = Optional.ofNullable(description);
		return this;
	}
	
	/**
	 * Sets the full description for this command. This is the help which is
	 * shown in the specific /help page for this command (e.g. /help mycommand).
	 * @param description the full description for this command
	 * @return this command builder
	 */
	public CommandAPICommand withFullDescription(String description) {
		this.fullDescription = Optional.ofNullable(description);
		return this;
	}
	
	/**
	 * Sets the short and full description for this command. This is a short-hand
	 * for the {@link CommandAPICommand#withShortDescription} and 
	 * {@link CommandAPICommand#withFullDescription} methods.
	 * @param shortDescription the short description for this command
	 * @param fullDescription the full description for this command
	 * @return this command builder
	 */
	public CommandAPICommand withHelp(String shortDescription, String fullDescription) {
		this.shortDescription = Optional.ofNullable(shortDescription);
		this.fullDescription = Optional.ofNullable(fullDescription);
		return this;
	}
	
	// Regular command executor 
	
	/**
	 * Adds an executor to the current command builder
	 * @param executor A lambda of type <code>(CommandSender, Object[]) -&gt; ()</code> that will be executed when the command is run
	 * @return this command builder
	 */
	public CommandAPICommand executes(CommandExecutor executor) {
		this.executor.addNormalExecutor(executor);
		return this;
	}
	
	/**
	 * Adds an executor to the current command builder
	 * @param executor A lambda of type <code>(CommandSender, Object[]) -&gt; int</code> that will be executed when the command is run
	 * @return this command builder
	 */
	public CommandAPICommand executes(ResultingCommandExecutor executor) {
		this.executor.addResultingExecutor(executor);
		return this;
	}
	
	// Player command executor
	
	/**
	 * Adds an executor to the current command builder
	 * @param executor A lambda of type <code>(Player, Object[]) -&gt; ()</code> that will be executed when the command is run
	 * @return this command builder
	 */
	public CommandAPICommand executesPlayer(PlayerCommandExecutor executor) {
		this.executor.addNormalExecutor(executor);
		return this;
	}
	
	/**
	 * Adds an executor to the current command builder
	 * @param executor A lambda of type <code>(Player, Object[]) -&gt; int</code> that will be executed when the command is run
	 * @return this command builder
	 */
	public CommandAPICommand executesPlayer(PlayerResultingCommandExecutor executor) {
		this.executor.addResultingExecutor(executor);
		return this;
	}
	
	// Entity command executor
	
	/**
	 * Adds an executor to the current command builder
	 * @param executor A lambda of type <code>(Entity, Object[]) -&gt; ()</code> that will be executed when the command is run
	 * @return this command builder
	 */
	public CommandAPICommand executesEntity(EntityCommandExecutor executor) {
		this.executor.addNormalExecutor(executor);
		return this;
	}
	
	/**
	 * Adds an executor to the current command builder
	 * @param executor A lambda of type <code>(Entity, Object[]) -&gt; int</code> that will be executed when the command is run
	 * @return this command builder
	 */
	public CommandAPICommand executesEntity(EntityResultingCommandExecutor executor) {
		this.executor.addResultingExecutor(executor);
		return this;
	}
	
	// Proxy command executor
	
	/**
	 * Adds an executor to the current command builder
	 * @param executor A lambda of type <code>(Entity, Object[]) -&gt; ()</code> that will be executed when the command is run
	 * @return this command builder
	 */
	public CommandAPICommand executesProxy(ProxyCommandExecutor executor) {
		this.executor.addNormalExecutor(executor);
		return this;
	}
	
	/**
	 * Adds an executor to the current command builder
	 * @param executor A lambda of type <code>(Entity, Object[]) -&gt; int</code> that will be executed when the command is run
	 * @return this command builder
	 */
	public CommandAPICommand executesProxy(ProxyResultingCommandExecutor executor) {
		this.executor.addResultingExecutor(executor);
		return this;
	}
	
	// Command block command sender
	
	/**
	 * Adds an executor to the current command builder
	 * @param executor A lambda of type <code>(BlockCommandSender, Object[]) -&gt; ()</code> that will be executed when the command is run
	 * @return this command builder
	 */
	public CommandAPICommand executesCommandBlock(CommandBlockCommandExecutor executor) {
		this.executor.addNormalExecutor(executor);
		return this;
	}
	
	/**
	 * Adds an executor to the current command builder
	 * @param executor A lambda of type <code>(BlockCommandSender, Object[]) -&gt; int</code> that will be executed when the command is run
	 * @return this command builder
	 */
	public CommandAPICommand executesCommandBlock(CommandBlockResultingCommandExecutor executor) {
		this.executor.addResultingExecutor(executor);
		return this;
	}
	
	// Console command sender
	
	/**
	 * Adds an executor to the current command builder
	 * @param executor A lambda of type <code>(BlockCommandSender, Object[]) -&gt; ()</code> that will be executed when the command is run
	 * @return this command builder
	 */
	public CommandAPICommand executesConsole(ConsoleCommandExecutor executor) {
		this.executor.addNormalExecutor(executor);
		return this;
	}
	
	/**
	 * Adds an executor to the current command builder
	 * @param executor A lambda of type <code>(BlockCommandSender, Object[]) -&gt; int</code> that will be executed when the command is run
	 * @return this command builder
	 */
	public CommandAPICommand executesConsole(ConsoleResultingCommandExecutor executor) {
		this.executor.addResultingExecutor(executor);
		return this;
	}
	
	/**
	 * Adds an executor to the current command builder
	 * @param executor A lambda of type <code>(NativeCommandExecutor, Object[]) -&gt; ()</code> that will be executed when the command is run
	 * @return this command builder
	 */
	public CommandAPICommand executesNative(NativeCommandExecutor executor) {
		this.executor.addNormalExecutor(executor);
		return this;
	}
	
	/**
	 * Adds an executor to the current command builder
	 * @param executor A lambda of type <code>(NativeCommandExecutor, Object[]) -&gt; int</code> that will be executed when the command is run
	 * @return this command builder
	 */
	public CommandAPICommand executesNative(NativeResultingCommandExecutor executor) {
		this.executor.addResultingExecutor(executor);
		return this;
	}
	
	/**
	 * Returns the name of this command
	 * @return the name of this command
	 */
	public String getName() {
		return commandName;
	}

	/**
	 * Returns the permission associated with this command
	 * @return the permission associated with this command
	 */
	public CommandPermission getPermission() {
		return permission;
	}

	/**
	 * Sets the permission required to run this command
	 * @param permission the permission required to run this command
	 */
	public void setPermission(CommandPermission permission) {
		this.permission = permission;
	}

	/**
	 * Returns an array of aliases that can be used to run this command
	 * @return an array of aliases that can be used to run this command
	 */
	public String[] getAliases() {
		return aliases;
	}

	/**
	 * Sets the aliases for this command
	 * @param aliases the aliases for this command
	 */
	public void setAliases(String[] aliases) {
		this.aliases = aliases;
	}

	/**
	 * Returns the requirements that must be satisfied to run this command
	 * @return the requirements that must be satisfied to run this command
	 */
	public Predicate<CommandSender> getRequirements() {
		return requirements;
	}

	/**
	 * Sets the requirements that must be satisfied to run this command
	 * @param requirements the requirements that must be satisfied to run this command
	 */
	public void setRequirements(Predicate<CommandSender> requirements) {
		this.requirements = requirements;
	}

	/**
	 * Returns the list of arguments that this command has
	 * @return the list of arguments that this command has
	 */
	public List<Argument> getArguments() {
		return args;
	}

	/**
	 * Sets the arguments that this command has
	 * @param args the arguments that this command has
	 */
	public void setArguments(List<Argument> args) {
		this.args = args;
	}

	/**
	 * Returns the list of subcommands that this command has
	 * @return the list of subcommands that this command has
	 */
	public List<CommandAPICommand> getSubcommands() {
		return subcommands;
	}

	/**
	 * Sets the list of subcommands that this command has
	 * @param subcommands the list of subcommands that this command has
	 */
	public void setSubcommands(List<CommandAPICommand> subcommands) {
		this.subcommands = subcommands;
	}

	/**
	 * Returns the executors that this command has
	 * @return the executors that this command has
	 */
	public CustomCommandExecutor<? extends CommandSender> getExecutor() {
		return executor;
	}

	/**
	 * Sets the executors for this command
	 * @param executor the executors for this command
	 */
	public void setExecutor(CustomCommandExecutor<? extends CommandSender> executor) {
		this.executor = executor;
	}

	/**
	 * Returns whether this command is an automatically converted command
	 * @return whether this command is an automatically converted command
	 */
	public boolean isConverted() {
		return isConverted;
	}

	void setConverted(boolean isConverted) {
		this.isConverted = isConverted;
	}
	
	//Expand subcommands into arguments
	private void flatten(CommandAPICommand rootCommand, List<Argument> prevArguments, CommandAPICommand subcommand) {
		
		String[] literals = new String[subcommand.aliases.length + 1];
		literals[0] = subcommand.commandName;
		System.arraycopy(subcommand.aliases, 0, literals, 1, subcommand.aliases.length);
		MultiLiteralArgument literal = (MultiLiteralArgument) new MultiLiteralArgument(literals)
			.withPermission(subcommand.permission)
			.withRequirement(subcommand.requirements)
			.setListed(false);
		
		prevArguments.add(literal);
		
		if(!subcommand.executor.isEmpty()) {	
			rootCommand.args = prevArguments;
			rootCommand.withArguments(subcommand.args);
			rootCommand.executor = subcommand.executor;
			
			rootCommand.subcommands = new ArrayList<>();
			rootCommand.register();
		}
		
		for(CommandAPICommand subsubcommand : new ArrayList<>(subcommand.subcommands)) {
			flatten(rootCommand, new ArrayList<>(prevArguments), subsubcommand);
		}
	}
	
	/**
	 * Registers the command
	 */
	public void register() {
		if(!CommandAPI.canRegister()) {
			CommandAPI.logWarning("Command /" + commandName + " is being registered after the server had loaded. Undefined behavior ahead!");
		}
		try {
			Argument[] argumentsArr = args == null ? new Argument[0] : args.toArray(new Argument[0]);
			
			// Check IGreedyArgument constraints 
			for(int i = 0, numGreedyArgs = 0; i < argumentsArr.length; i++) {
				if(argumentsArr[i] instanceof IGreedyArgument) {
					if(++numGreedyArgs > 1 || i != argumentsArr.length - 1) {
						throw new GreedyArgumentException();
					}
				}
			}
			
			//Assign the command's permissions to arguments if the arguments don't already have one
			for(Argument argument : argumentsArr) {
				if(argument.getArgumentPermission() == null) {
					argument.withPermission(permission);
				}
			}
			
			if(!executor.isEmpty()) {
				CommandAPIHandler.getInstance().register(commandName, shortDescription, fullDescription, permission, aliases, requirements, argumentsArr, executor, isConverted);
			}
			
			for(CommandAPICommand subcommand : this.subcommands) {
				flatten(this, new ArrayList<>(), subcommand);
			}
		} catch (InvalidCommandNameException | GreedyArgumentException | CommandSyntaxException | IOException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * Overrides a command. Effectively the same as unregistering the command using
	 * CommandAPI.unregister() and then registering the command using .register()
	 */
	public void override() {
		CommandAPI.unregister(this.commandName, true);
		register();
	}
	
}
