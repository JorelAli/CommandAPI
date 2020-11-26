package dev.jorel.commandapi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import org.bukkit.command.CommandSender;

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

	//region With Commands (Adds/Subtracts traits)
	//================================================================================
	
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

	//================================================================================
	//endregion
	//region Executes
	//================================================================================

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

	//================================================================================
	//endregion
	//region Set (Sets the underlying array/etc. DIRECTLY) Commands
	//Note: We should probably improve the documentation in this area too, to better reflect what it does.
	// (Many people may just think its the plural version of the `with` commands
	//================================================================================


	/**
	 * Sets the permissions required to run this command
	 * @param permission the permission required to run this command
	 * @return this command builder
	 * @deprecated setPermission and withPermission function exactly the same
	 */
	public CommandAPICommand setPermission(CommandPermission permission) {
		this.permission = permission;
		return this;
	}

	/**
	 * Sets the requirements that must be satisfied to run this command
	 * @param requirements the requirements that must be satisfied to run this command (previous requirements will be replaced)
	 * @return this command builder
	 */
	public CommandAPICommand setRequirements(Predicate<CommandSender> requirements) {
		this.requirements = requirements;
		return this;
	}

	/**
	 * Sets the aliases for this command
	 * @param aliases aliases that this command's aliases will be set to (previous aliases will be replaced)
	 * @return this command builder
	 */
	public CommandAPICommand setAliases(String[] aliases) {
		this.aliases = aliases;
		return this;
	}

	/**
	 * Sets the arguments that this command has
	 * @param args list of arguments this command's arguments will be set to (previous arguments will be replaced)
	 * @return this command builder
	 */
	public CommandAPICommand setArguments(List<Argument> args) {
		this.args = args;
		return this;
	}

	/**
	 * Sets the list of subcommands that this command has
	 * @param subcommands subcommands to set to (previous subcommands will be replaced)
	 * @return this command builder
	 */
	public CommandAPICommand setSubcommands(List<CommandAPICommand> subcommands) {
		this.subcommands = subcommands;
		return this;
	}

	/**
	 * Sets the executors for this command
	 * @param executor the executor for this command (previous executors will be replaced)
	 * @return this command builder
	 */
	public CommandAPICommand setExecutor(CustomCommandExecutor executor) {
		this.executor = executor;
		return this;
	}

	//================================================================================
	//endregion
	//region Getter Commands
	//================================================================================

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
	 * Returns an array of aliases that can be used to run this command
	 * @return an array of aliases that can be used to run this command
	 */
	public String[] getAliases() {
		return aliases;
	}

	/**
	 * Returns the requirements that must be satisfied to run this command
	 * @return the requirements that must be satisfied to run this command
	 */
	public Predicate<CommandSender> getRequirements() { return requirements; }

	/**
	 * Returns the list of arguments that this command has
	 * @return the list of arguments that this command has
	 */
	public List<Argument> getArguments() {
		return args;
	}

	/**
	 * Returns the list of subcommands that this command has
	 * @return the list of subcommands that this command has
	 */
	public List<CommandAPICommand> getSubcommands() {
		return subcommands;
	}

	/**
	 * Returns the executors that this command has
	 * @return the executors that this command has
	 */
	public CustomCommandExecutor getExecutor() {
		return executor;
	}

	//================================================================================
	//endregion
	//region misc.
	//================================================================================

	/**
	 * Returns whether this command is an automatically converted command
	 * @return whether this command is an automatically converted command
	 */

	public boolean isConverted() {
		return isConverted;
	}

	/**
	 * @param isConverted
	 * @todo Do we mark this as depricated? There are no javadocs for this so I'd assume its
	 * for internal usage, but then shouldn't you mark as protected? I'm gonna leave this alone for now
	 */
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
				if(argument.getArgumentPermission() == null) {
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
	//================================================================================
	//endregion
}
