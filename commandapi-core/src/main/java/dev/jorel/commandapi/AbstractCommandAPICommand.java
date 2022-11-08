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

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.jorel.commandapi.arguments.AbstractArgument;
import dev.jorel.commandapi.arguments.IGreedyArgument;
import dev.jorel.commandapi.exceptions.GreedyArgumentException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A builder used to create commands to be registered by the CommandAPI.
 * @param <Impl> The class extending this class, used as the return type for chain calls
 * @param <Argument> The implementation of AbstractArgument used by the class extending this class
 * @param <CommandSender> The CommandSender class used by the class extending this class
 */
public abstract class AbstractCommandAPICommand<Impl extends AbstractCommandAPICommand<Impl, Argument, CommandSender>,
	Argument extends AbstractArgument<?, ?, Argument, CommandSender>, CommandSender> extends ExecutableCommand<Impl, CommandSender> {

	protected List<Argument> args = new ArrayList<>();
	protected List<Impl> subcommands = new ArrayList<>();
	protected boolean isConverted;

	/**
	 * Creates a new command builder
	 *
	 * @param commandName The name of the command to create
	 */
	public AbstractCommandAPICommand(String commandName) {
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
		this.args.addAll(args);
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
		this.args.addAll(Arrays.asList(args));
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
	public Impl withSubcommands(Impl... subcommands) {
		this.subcommands.addAll(Arrays.asList(subcommands));
		return instance();
	}

	/**
	 * Returns the list of arguments that this command has
	 *
	 * @return the list of arguments that this command has
	 */
	public List<Argument> getArguments() {
		return args;
	}

	/**
	 * Sets the arguments that this command has
	 *
	 * @param args the arguments that this command has
	 */
	public void setArguments(List<Argument> args) {
		this.args = args;
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
	// shouldn't
	// be accessing/depending on any of the contents of the current class instance)
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
			.withRequirement(subcommand.meta.requirements)
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
			rootCommand.args = prevArguments;
			rootCommand.withArguments(subcommand.args);
			rootCommand.executor = subcommand.executor;
			rootCommand.subcommands = new ArrayList<>();
			rootCommand.register();
		}

		for (Impl subsubcommand : subcommand.getSubcommands()) {
			flatten(rootCommand, new ArrayList<>(prevArguments), subsubcommand);
		}
	}

	@Override
	public void register() {
		if (!CommandAPI.canRegister()) {
			CommandAPI.logWarning("Command /" + meta.commandName + " is being registered after the server had loaded. Undefined behavior ahead!");
		}
		try {
			Argument[] argumentsArr = (Argument[]) (args == null ? new AbstractArgument[0] : args.toArray(AbstractArgument[]::new));

			// Check IGreedyArgument constraints
			for (int i = 0, numGreedyArgs = 0; i < argumentsArr.length; i++) {
				if (argumentsArr[i] instanceof IGreedyArgument) {
					if (++numGreedyArgs > 1 || i != argumentsArr.length - 1) {
						throw new GreedyArgumentException(argumentsArr);
					}
				}
			}

			// Assign the command's permissions to arguments if the arguments don't already
			// have one
			for (Argument argument : argumentsArr) {
				if (argument.getArgumentPermission() == null) {
					argument.withPermission(meta.permission);
				}
			}

			if (executor.hasAnyExecutors()) {
				// Need to cast handler to the right CommandSender type so that argumentsArr and executor are accepted
				CommandAPIHandler<Argument, CommandSender, ?> handler = (CommandAPIHandler<Argument, CommandSender, ?>) CommandAPIHandler.getInstance();
				handler.register(meta, argumentsArr, executor, isConverted);
			}

			// Convert subcommands into multiliteral arguments
			for (Impl subcommand : this.subcommands) {
				flatten(this.copy(), new ArrayList<>(), subcommand);
			}
		} catch (CommandSyntaxException | IOException e) {
			e.printStackTrace();
		}
	}

	public Impl copy() {
		Impl command = newConcreteCommandAPICommand(new CommandMetaData<>(this.meta));
		command.args = new ArrayList<>(this.args);
		command.subcommands = new ArrayList<>(this.subcommands);
		command.isConverted = this.isConverted;
		return command;
	}

	protected abstract Impl newConcreteCommandAPICommand(CommandMetaData<CommandSender> metaData);
}
