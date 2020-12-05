package dev.jorel.commandapi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import org.bukkit.command.CommandSender;

import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.IGreedyArgument;
import dev.jorel.commandapi.arguments.MultiLiteralArgument;
import dev.jorel.commandapi.arguments.SafeOverrideableArgument;
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
	private CustomCommandExecutor executor = new CustomCommandExecutor();
	private boolean isConverted;
	
	/**
	 * Creates a new command builder
	 * @param commandName The name of the command to create
	 */
	public CommandAPICommand(String commandName) {
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
	 * Appends the argument(s) to the current command builder
	 * @param args Arguments that this command can accept
	 * @return this command builder
	 */
	public <S> CommandAPICommand withOptionalArgument(SafeOverrideableArgument<S> argument, S defaultValue) {
		argument.setOptional(true);
		this.args.add(argument);
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
	public CustomCommandExecutor getExecutor() {
		return executor;
	}

	/**
	 * Sets the executors for this command
	 * @param executor the executors for this command
	 */
	public void setExecutor(CustomCommandExecutor executor) {
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
				if(argument.getArgumentPermission() == CommandPermission.NONE) {
					argument.withPermission(permission);
				}
			}
			
			if(!executor.isEmpty()) {
				CommandAPIHandler.getInstance().register(commandName, permission, aliases, requirements, copyOfArgs, executor, isConverted);
			}
			
			if(this.subcommands.size() > 0) {
				for(CommandAPICommand subcommand : this.subcommands) {
					flatten(this, new ArrayList<>(), subcommand);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
