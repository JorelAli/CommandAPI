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
import dev.jorel.commandapi.exceptions.InvalidCommandNameException;

/**
 * A builder used to create commands to be registered by the CommandAPI.
 */
public class CommandAPICommand extends ExecutableCommand<CommandAPICommand> {

	private List<Argument<?>> args = new ArrayList<>();
	private List<CommandAPICommand> subcommands = new ArrayList<>();
	private boolean isConverted;
	
	/**
	 * Creates a new command builder
	 * @param commandName The name of the command to create
	 */
	public CommandAPICommand(String commandName) {
		super(commandName);
		this.isConverted = false;
	}

	protected CommandAPICommand(CommandMetaData metaData) {
		super(metaData);
		this.isConverted = false;
	}
	
	/**
	 * Appends the arguments to the current command builder
	 * @param args A <code>List</code> that represents the arguments that this command can accept
	 * @return this command builder
	 */
	public CommandAPICommand withArguments(List<Argument<?>> args) {
		this.args.addAll(args);
		return this;
	}
	
	/**
	 * Appends the argument(s) to the current command builder
	 * @param args Arguments that this command can accept
	 * @return this command builder
	 */
	public CommandAPICommand withArguments(Argument<?>... args) {
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
	 * Adds subcommands to this command builder
	 * @param subcommands the subcommands to add as children of this command 
	 * @return this command builder
	 */
	public CommandAPICommand withSubcommands(CommandAPICommand... subcommands) {
		this.subcommands.addAll(Arrays.asList(subcommands));
		return this;
	}

	/**
	 * Returns the list of arguments that this command has
	 * @return the list of arguments that this command has
	 */
	public List<Argument<?>> getArguments() {
		return args;
	}

	/**
	 * Sets the arguments that this command has
	 * @param args the arguments that this command has
	 */
	public void setArguments(List<Argument<?>> args) {
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
	 * Returns whether this command is an automatically converted command
	 * @return whether this command is an automatically converted command
	 */
	public boolean isConverted() {
		return isConverted;
	}

	/**
	 * Sets a command as "converted". This tells the CommandAPI that this command
	 * was converted by the CommandAPI's Converter. This should not be used outside
	 * of the CommandAPI's internal API
	 * @param isConverted whether this command is converted or not
	 * @return this command builder
	 */
	CommandAPICommand setConverted(boolean isConverted) {
		this.isConverted = isConverted;
		return this;
	}
	
	//Expand subcommands into arguments
	private void flatten(CommandAPICommand rootCommand, List<Argument<?>> prevArguments, CommandAPICommand subcommand) {
		
		String[] literals = new String[subcommand.meta.aliases.length + 1];
		literals[0] = subcommand.meta.commandName;
		System.arraycopy(subcommand.meta.aliases, 0, literals, 1, subcommand.meta.aliases.length);
		MultiLiteralArgument literal = (MultiLiteralArgument) new MultiLiteralArgument(literals)
			.withPermission(subcommand.meta.permission)
			.withRequirement(subcommand.meta.requirements)
			.setListed(false);
		
		prevArguments.add(literal);
		
		if(subcommand.executor.hasAnyExecutors()) {	
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
			CommandAPI.logWarning("Command /" + meta.commandName + " is being registered after the server had loaded. Undefined behavior ahead!");
		}
		try {
			Argument<?>[] argumentsArr = args == null ? new Argument[0] : args.toArray(new Argument[0]);
			
			// Check IGreedyArgument constraints 
			for(int i = 0, numGreedyArgs = 0; i < argumentsArr.length; i++) {
				if(argumentsArr[i] instanceof IGreedyArgument) {
					if(++numGreedyArgs > 1 || i != argumentsArr.length - 1) {
						throw new GreedyArgumentException(argumentsArr);
					}
				}
			}
			
			//Assign the command's permissions to arguments if the arguments don't already have one
			for(Argument<?> argument : argumentsArr) {
				if(argument.getArgumentPermission() == null) {
					argument.withPermission(meta.permission);
				}
			}
			
			if(executor.hasAnyExecutors()) {
				CommandAPIHandler.getInstance().register(meta, argumentsArr, executor, isConverted);
			}
			
			for(CommandAPICommand subcommand : this.subcommands) {
				flatten(this, new ArrayList<>(), subcommand);
			}
		} catch (InvalidCommandNameException | GreedyArgumentException | CommandSyntaxException | IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
