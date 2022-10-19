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

import com.mojang.brigadier.exceptions.CommandSyntaxException;

import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.IGreedyArgument;
import dev.jorel.commandapi.arguments.MultiLiteralArgument;
import dev.jorel.commandapi.exceptions.GreedyArgumentException;

/**
 * A builder used to create commands to be registered by the CommandAPI.
 */
public class AbstractCommandAPICommand<Impl extends AbstractCommandAPICommand<Impl>> extends ExecutableCommand<AbstractCommandAPICommand<Impl>> {

	private List<Argument<?>> args = new ArrayList<>();
	private List<AbstractCommandAPICommand<Impl>> subcommands = new ArrayList<>();
	private boolean isConverted;

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
	protected AbstractCommandAPICommand(CommandMetaData metaData) {
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
	public Impl withArguments(List<Argument<?>> args) {
		this.args.addAll(args);
		return (Impl) this;
	}

	/**
	 * Appends the argument(s) to the current command builder
	 * 
	 * @param args Arguments that this command can accept
	 * @return this command builder
	 */
	public Impl withArguments(Argument<?>... args) {
		this.args.addAll(Arrays.asList(args));
		return (Impl) this;
	}

	/**
	 * Adds a subcommand to this command builder
	 * 
	 * @param subcommand the subcommand to add as a child of this command
	 * @return this command builder
	 */
	public Impl withSubcommand(AbstractCommandAPICommand<Impl> subcommand) {
		this.subcommands.add(subcommand);
		return (Impl) this;
	}

	/**
	 * Adds subcommands to this command builder
	 * 
	 * @param subcommands the subcommands to add as children of this command
	 * @return this command builder
	 */
	public Impl withSubcommands(AbstractCommandAPICommand<Impl>... subcommands) {
		this.subcommands.addAll(Arrays.asList(subcommands));
		return (Impl) this;
	}

	/**
	 * Returns the list of arguments that this command has
	 * 
	 * @return the list of arguments that this command has
	 */
	public List<Argument<?>> getArguments() {
		return args;
	}

	/**
	 * Sets the arguments that this command has
	 * 
	 * @param args the arguments that this command has
	 */
	public void setArguments(List<Argument<?>> args) {
		this.args = args;
	}

	/**
	 * Returns the list of subcommands that this command has
	 * 
	 * @return the list of subcommands that this command has
	 */
	public List<AbstractCommandAPICommand<Impl>> getSubcommands() {
		return subcommands;
	}

	/**
	 * Sets the list of subcommands that this command has
	 * 
	 * @param subcommands the list of subcommands that this command has
	 */
	public void setSubcommands(List<AbstractCommandAPICommand<Impl>> subcommands) {
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
	AbstractCommandAPICommand setConverted(boolean isConverted) {
		this.isConverted = isConverted;
		return this;
	}

	// Expands subcommands into arguments. This method should be static (it
	// shouldn't
	// be accessing/depending on any of the contents of the current class instance)
	private static <Impl extends AbstractCommandAPICommand<Impl>> void flatten(AbstractCommandAPICommand rootCommand, List<Argument<?>> prevArguments, AbstractCommandAPICommand subcommand) {
		// Get the list of literals represented by the current subcommand. This
		// includes the subcommand's name and any aliases for this subcommand
		String[] literals = new String[subcommand.meta.aliases.length + 1];
		literals[0] = subcommand.meta.commandName;
		System.arraycopy(subcommand.meta.aliases, 0, literals, 1, subcommand.meta.aliases.length);

		// Create a MultiLiteralArgument using the subcommand information
		MultiLiteralArgument literal = (MultiLiteralArgument) new MultiLiteralArgument(literals)
			.withPermission(subcommand.meta.permission)
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

		// TODO: Sneaky cast. Might need checking
		for (Object subsubcommand : new ArrayList<>(subcommand.getSubcommands())) {
			flatten(rootCommand, new ArrayList<>(prevArguments), (AbstractCommandAPICommand) subsubcommand);
		}
	}

	@Override
	public void register() {
		if (!CommandAPI.canRegister()) {
			CommandAPI.logWarning("Command /" + meta.commandName + " is being registered after the server had loaded. Undefined behavior ahead!");
		}
		try {
			Argument<?>[] argumentsArr = args == null ? new Argument<?>[0] : args.toArray(new Argument<?>[0]);

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
			for (Argument<?> argument : argumentsArr) {
				if (argument.getArgumentPermission() == null) {
					argument.withPermission(meta.permission);
				}
			}

			if (executor.hasAnyExecutors()) {
				BaseHandler.getInstance().register(meta, argumentsArr, executor, isConverted);
			}

			// Convert subcommands into multiliteral arguments
			for (AbstractCommandAPICommand subcommand : new ArrayList<>(this.subcommands)) {
				flatten(new AbstractCommandAPICommand(this), new ArrayList<>(), subcommand);
			}
		} catch (CommandSyntaxException | IOException e) {
			e.printStackTrace();
		}
	}

	public AbstractCommandAPICommand(AbstractCommandAPICommand original) {
		this(new CommandMetaData(original.meta));
		this.args = new ArrayList<>(original.args);
		this.subcommands = new ArrayList<>(original.subcommands);
		this.isConverted = original.isConverted;
	}

}
