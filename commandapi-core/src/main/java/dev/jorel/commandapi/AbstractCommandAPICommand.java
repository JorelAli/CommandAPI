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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import dev.jorel.commandapi.arguments.AbstractArgument;
import dev.jorel.commandapi.arguments.GreedyArgument;
import dev.jorel.commandapi.exceptions.GreedyArgumentException;
import dev.jorel.commandapi.exceptions.MissingCommandExecutorException;
import dev.jorel.commandapi.exceptions.OptionalArgumentException;

/**
 * A builder used to create commands to be registered by the CommandAPI.
 * @param <Impl> The class extending this class, used as the return type for chain calls
 * @param <Argument> The implementation of AbstractArgument used by the class extending this class
 * @param <CommandSender> The CommandSender class used by the class extending this class
 */
public abstract class AbstractCommandAPICommand<Impl extends AbstractCommandAPICommand<Impl, Argument, CommandSender>,
	Argument extends AbstractArgument<?, ?, Argument, CommandSender>, CommandSender> extends ExecutableCommand<Impl, CommandSender> {

	protected List<Argument> arguments = new ArrayList<>();
	protected List<Impl> subcommands = new ArrayList<>();
	protected boolean isConverted;

	/**
	 * Creates a new command builder
	 *
	 * @param commandName The name of the command to create
	 */
	protected AbstractCommandAPICommand(String commandName) {
		super(commandName);
		this.isConverted = false;
	}

	/**
	 * Creates a new Command builder
	 *
	 * @param metaData The metadata of the command to create
	 */
	protected AbstractCommandAPICommand(CommandMetaData<CommandSender> metaData) {
		super(metaData);
		this.isConverted = false;
	}

	/**
	 * Appends the arguments to the current command builder
	 *
	 * @param args A <code>List</code> that represents the arguments that this
	 *             command can accept
	 * @return this command builder
	 */
	public Impl withArguments(List<Argument> args) {
		this.arguments.addAll(args);
		return instance();
	}

	/**
	 * Appends the argument(s) to the current command builder
	 *
	 * @param args Arguments that this command can accept
	 * @return this command builder
	 */
	@SafeVarargs
	public final Impl withArguments(Argument... args) {
		this.arguments.addAll(Arrays.asList(args));
		return instance();
	}

	/**
	 * Appends the optional arguments to the current command builder.
	 *
	 * This also calls {@link AbstractArgument#setOptional(boolean)} on each argument to make sure they are optional
	 *
	 * @param args A <code>List</code> that represents the arguments that this
	 *             command can accept
	 * @return this command builder
	 */
	public Impl withOptionalArguments(List<Argument> args) {
		for (Argument argument : args) {
			argument.setOptional(true);
			this.arguments.add(argument);
		}
		return instance();
	}

	/**
	 * Appends the optional arguments to the current command builder.
	 *
	 * This also calls {@link AbstractArgument#setOptional(boolean)} on each argument to make sure they are optional
	 *
	 * @param args Arguments that this command can accept
	 * @return this command builder
	 */
	@SafeVarargs
	public final Impl withOptionalArguments(Argument... args) {
		for (Argument argument : args) {
			argument.setOptional(true);
			this.arguments.add(argument);
		}
		return instance();
	}

	/**
	 * Adds a subcommand to this command builder
	 *
	 * @param subcommand the subcommand to add as a child of this command
	 * @return this command builder
	 */
	public Impl withSubcommand(Impl subcommand) {
		this.subcommands.add(subcommand);
		return instance();
	}

	/**
	 * Adds subcommands to this command builder
	 *
	 * @param subcommands the subcommands to add as children of this command
	 * @return this command builder
	 */
	public Impl withSubcommands(@SuppressWarnings("unchecked") Impl... subcommands) {
		this.subcommands.addAll(Arrays.asList(subcommands));
		return instance();
	}

	/**
	 * Returns the list of arguments that this command has
	 *
	 * @return the list of arguments that this command has
	 */
	public List<Argument> getArguments() {
		return arguments;
	}

	/**
	 * Sets the arguments that this command has
	 *
	 * @param args the arguments that this command has
	 */
	public void setArguments(List<Argument> args) {
		this.arguments = args;
	}

	/**
	 * Returns the list of subcommands that this command has
	 *
	 * @return the list of subcommands that this command has
	 */
	public List<Impl> getSubcommands() {
		return subcommands;
	}

	/**
	 * Sets the list of subcommands that this command has
	 *
	 * @param subcommands the list of subcommands that this command has
	 */
	public void setSubcommands(List<Impl> subcommands) {
		this.subcommands = subcommands;
	}

	/**
	 * Returns whether this command is an automatically converted command
	 *
	 * @return whether this command is an automatically converted command
	 */
	public boolean isConverted() {
		return isConverted;
	}

	/**
	 * Sets a command as "converted". This tells the CommandAPI that this command
	 * was converted by the CommandAPI's Converter. This should not be used outside
	 * of the CommandAPI's internal API
	 *
	 * @param isConverted whether this command is converted or not
	 * @return this command builder
	 */
	Impl setConverted(boolean isConverted) {
		this.isConverted = isConverted;
		return instance();
	}

	// Expands subcommands into arguments. This method should be static (it
	// shouldn't be accessing/depending on any of the contents of the current class instance)
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static <Impl extends AbstractCommandAPICommand<Impl, Argument, CommandSender>, Argument extends AbstractArgument<?, ?, Argument, CommandSender>, CommandSender>
	void flatten(Impl rootCommand, List<Argument> prevArguments, Impl subcommand) {
		// Get the list of literals represented by the current subcommand. This
		// includes the subcommand's name and any aliases for this subcommand
		String[] literals = new String[subcommand.meta.aliases.length + 1];
		literals[0] = subcommand.meta.commandName;
		System.arraycopy(subcommand.meta.aliases, 0, literals, 1, subcommand.meta.aliases.length);

		// Create a MultiLiteralArgument using the subcommand information
		Argument literal = (Argument) CommandAPIHandler.getInstance().getPlatform().newConcreteMultiLiteralArgument(literals);

		literal.withPermission(subcommand.meta.permission)
			.withRequirement((Predicate) subcommand.meta.requirements)
			.setListed(false);

		prevArguments.add(literal);

		if (subcommand.executor.hasAnyExecutors()) {
			// Create the new command. The new command:
			// - starts at the root command node
			// - has all of the previously declared arguments (i.e. not itself)
			// - uses the subcommand's executor
			// - has no subcommands(?)
			// Honestly, if you're asking how or why any of this works, I don't
			// know because I just trialled random code until it started working
			rootCommand.arguments = prevArguments;
			rootCommand.withArguments(subcommand.arguments);
			rootCommand.executor = subcommand.executor;
			rootCommand.subcommands = new ArrayList<>();
			rootCommand.register();
		}

		for (Impl subsubcommand : subcommand.getSubcommands()) {
			flatten(rootCommand, new ArrayList<>(prevArguments), subsubcommand);
		}
	}
	
	boolean hasAnyExecutors() {
		if (this.executor.hasAnyExecutors()) {
			return true;
		} else {
			for(Impl subcommand : this.subcommands) {
				if (subcommand.hasAnyExecutors()) {
					return true;
				}
			}
		}
		return false;
	}
	
	private void checkHasExecutors() {
		if(!hasAnyExecutors()) {
			throw new MissingCommandExecutorException(this.meta.commandName);
		}
	}

	@Override
	public void register() {
		if (!CommandAPI.canRegister()) {
			CommandAPI.logWarning("Command /" + meta.commandName + " is being registered after the server had loaded. Undefined behavior ahead!");
		}

		@SuppressWarnings("unchecked")
		Argument[] argumentsArray = (Argument[]) (arguments == null ? new AbstractArgument[0] : arguments.toArray(AbstractArgument[]::new));

		// Check GreedyArgument constraints
		checkGreedyArgumentConstraints(argumentsArray);
		checkHasExecutors();
		
		// Assign the command's permissions to arguments if the arguments don't already
		// have one
		for (Argument argument : argumentsArray) {
			if (argument.getArgumentPermission() == null) {
				argument.withPermission(meta.permission);
			}
		}

		if (executor.hasAnyExecutors()) {
			// Need to cast handler to the right CommandSender type so that argumentsArray and executor are accepted
			@SuppressWarnings("unchecked")
			CommandAPIHandler<Argument, CommandSender, ?> handler = (CommandAPIHandler<Argument, CommandSender, ?>) CommandAPIHandler.getInstance();
			
			// Create a List<Argument[]> that is used to register optional arguments
			for (Argument[] args : getArgumentsToRegister(argumentsArray)) {
				handler.register(meta, args, executor, isConverted);
			}
		}

		// Convert subcommands into multiliteral arguments
		for (Impl subcommand : this.subcommands) {
			flatten(this.copy(), new ArrayList<>(), subcommand);
		}
	}
	
	// Checks that greedy arguments don't have any other arguments at the end,
	// and only zero or one greedy argument is present in an array of arguments
	private void checkGreedyArgumentConstraints(Argument[] argumentsArray) {
		for (int i = 0; i < argumentsArray.length; i++) {
			// If we've seen a greedy argument that isn't at the end, then that
			// also covers the case of seeing more than one greedy argument, as
			// if there are more than one greedy arguments, one of them must not
			// be at the end!
			if (argumentsArray[i] instanceof GreedyArgument && i != argumentsArray.length - 1) {
				throw new GreedyArgumentException(argumentsArray);
			}
		}
	}

	public Impl copy() {
		Impl command = newConcreteCommandAPICommand(new CommandMetaData<>(this.meta));
		command.arguments = new ArrayList<>(this.arguments);
		command.subcommands = new ArrayList<>(this.subcommands);
		command.isConverted = this.isConverted;
		return command;
	}

	protected abstract Impl newConcreteCommandAPICommand(CommandMetaData<CommandSender> metaData);

	private List<Argument[]> getArgumentsToRegister(Argument[] argumentsArray) {
		List<Argument[]> argumentsToRegister = new ArrayList<>();

		// Check optional argument constraints
		// They can only be at the end, no required argument can follow an optional argument
		int firstOptionalArgumentIndex = -1;
		boolean seenOptionalArgument = false;
		for (int i = 0; i < argumentsArray.length; i++) {
			if (argumentsArray[i].isOptional()) {
				if (firstOptionalArgumentIndex == -1) {
					firstOptionalArgumentIndex = i;
				}
				seenOptionalArgument = true;
			} else if (seenOptionalArgument) {
				// Argument is not optional
				throw new OptionalArgumentException(meta.commandName);
			}
		}

		// Create a List of arrays that hold arguments to register optional arguments
		// if optional arguments have been found
		if (firstOptionalArgumentIndex != -1) {
			for (int i = 0; i <= argumentsArray.length; i++) {
				if (i >= firstOptionalArgumentIndex) {
					@SuppressWarnings("unchecked")
					Argument[] optionalArguments = (Argument[]) new AbstractArgument[i];
					System.arraycopy(argumentsArray, 0, optionalArguments, 0, i);
					argumentsToRegister.add(optionalArguments);
				}
			}
		}
		
		// If there were no optional arguments, let's just return the array of
		// arguments we were going to register normally.
		if(argumentsToRegister.isEmpty()) {
			argumentsToRegister.add(argumentsArray);
		}

		return argumentsToRegister;
	}
}
