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

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.jorel.commandapi.arguments.AbstractArgument;
import dev.jorel.commandapi.arguments.GreedyArgument;
import dev.jorel.commandapi.exceptions.GreedyArgumentException;
import dev.jorel.commandapi.exceptions.MissingCommandExecutorException;
import dev.jorel.commandapi.exceptions.OptionalArgumentException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A builder used to create commands to be registered by the CommandAPI.
 *
 * @param <Impl>          The class extending this class, used as the return type for chain calls
 * @param <Argument>      The implementation of AbstractArgument used by the class extending this class
 * @param <CommandSender> The CommandSender class used by the class extending this class
 */
public abstract class AbstractCommandAPICommand<Impl
/// @cond DOX
extends AbstractCommandAPICommand<Impl, Argument, CommandSender>
/// @endcond
, Argument
/// @cond DOX
extends AbstractArgument<?, ?, Argument, CommandSender>
/// @endcond
, CommandSender> extends ExecutableCommand<Impl, CommandSender> {

	protected List<Argument> arguments = new ArrayList<>();
	protected List<Impl> subcommands = new ArrayList<>();

	/**
	 * Creates a new command builder
	 *
	 * @param commandName The name of the command to create
	 */
	protected AbstractCommandAPICommand(String commandName) {
		super(commandName);
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
	 * <p>
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
	 * <p>
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
	@SafeVarargs
	public final Impl withSubcommands(Impl... subcommands) {
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

	@Override
	public List<List<String>> getArgumentsAsStrings() {
		// Return an empty list if we have no arguments
		if (arguments.isEmpty() && subcommands.isEmpty()) {
			// Note: the inner list needs to be mutable in the case that this is a subcommand/sub-subcommand...
			//  In that case, the parent subcommands will be built backwards inside this list
			return List.of(new ArrayList<>());
		}

		List<List<String>> argumentStrings = new ArrayList<>();

		if (!arguments.isEmpty()) {
			// Build main path
			List<List<String>> currentPaths = new ArrayList<>();
			currentPaths.add(new ArrayList<>());
			boolean foundOptional = arguments.get(0).isOptional();
			for (int i = 0; i < arguments.size(); i++) {
				Argument argument = arguments.get(i);
				argument.appendToCommandPaths(currentPaths);

				// Non-optional argument after an optional argument
				//  This state is invalid, so we cannot continue
				boolean nextIsOptional = i == arguments.size() - 1 || arguments.get(i + 1).isOptional();
				if (foundOptional && !nextIsOptional)
					throw new OptionalArgumentException(name, arguments.subList(0, i), argument);
				foundOptional = nextIsOptional;

				// If this is the last argument, or the next argument is optional, then the current path should be included by itself
				if (nextIsOptional) argumentStrings.addAll(currentPaths);
			}
		}

		// Add subcommands
		for (Impl subCommand : subcommands) {
			String subCommandArgument = subCommand.name + ":LiteralArgument";
			for (List<String> subArgs : subCommand.getArgumentsAsStrings()) {
				subArgs.add(0, subCommandArgument);
				argumentStrings.add(subArgs);
			}
		}

		return argumentStrings;
	}

	@Override
	<Source> Nodes<Source> createCommandNodes() {
		CommandAPIHandler<Argument, CommandSender, Source> handler = CommandAPIHandler.getInstance();

		// Check preconditions
		if (!executor.hasAnyExecutors() && (subcommands.isEmpty() || !arguments.isEmpty())) {
			// If we don't have any executors then:
			//  No subcommands is bad because this path can't be run at all
			//  Having arguments is bad because developer intended this path to be executable with arguments
			throw new MissingCommandExecutorException(name);
		}

		// Create node
		LiteralArgumentBuilder<Source> rootBuilder = LiteralArgumentBuilder.literal(name);

		// Add permission and requirements
		rootBuilder.requires(handler.generateBrigadierRequirements(permission, requirements));

		// Add our executor if this is the last node, or the next argument is optional
		if ((arguments.isEmpty() || arguments.get(0).isOptional()) && executor.hasAnyExecutors()) {
			rootBuilder.executes(handler.generateBrigadierCommand(List.of(), executor));
		}

		// Register main node
		LiteralCommandNode<Source> rootNode = rootBuilder.build();

		// Create arguments
		if (!arguments.isEmpty()) {
			CommandNode<Source> previousNode = rootNode;
			List<Argument> previousArguments = new ArrayList<>();
			List<String> previousArgumentNames = new ArrayList<>();

			// The previous arguments include an unlisted MultiLiteral representing the command name and aliases
			//  This doesn't affect how the command acts, but it helps represent the command path in exceptions
			String[] literals = new String[aliases.length + 1];
			literals[0] = name;
			System.arraycopy(aliases, 0, literals, 1, aliases.length);
			Argument commandNames = handler.getPlatform().newConcreteMultiLiteralArgument(name, literals);
			commandNames.setListed(false);

			previousArguments.add(commandNames);

			boolean foundOptional = arguments.get(0).isOptional();
			for (int i = 0; i < arguments.size(); i++) {
				Argument argument = arguments.get(i);

				boolean nextIsOptional = i == arguments.size() - 1 || arguments.get(i + 1).isOptional();
				// Non-optional argument after an optional argument
				//  This state is invalid, so we cannot continue
				if (foundOptional && !nextIsOptional) throw new OptionalArgumentException(previousArguments, argument);
				foundOptional = nextIsOptional;

				previousNode = argument.addArgumentNodes(previousNode, previousArguments, previousArgumentNames,
					// If this is the last argument, or the next argument is optional, add the executor
					nextIsOptional ? executor : null);
			}

			// Check greedy argument constraint
			//  We need to check it down here so that all the combined arguments are properly considered after unpacking
			for (int i = 0; i < previousArguments.size() - 1 /* Minus one since we don't need to check last argument */; i++) {
				Argument argument = previousArguments.get(i);
				if (argument instanceof GreedyArgument) {
					throw new GreedyArgumentException(
						previousArguments.subList(0, i), // Arguments before this
						argument,
						List.of(previousArguments.subList(i + 1, previousArguments.size())) // Arguments after this
					);
				}
			}
		}

		// Add subcommands
		for (Impl subCommand : subcommands) {
			Nodes<Source> nodes = subCommand.createCommandNodes();
			rootNode.addChild(nodes.rootNode());
			for (LiteralCommandNode<Source> aliasNode : nodes.aliasNodes()) {
				rootNode.addChild(aliasNode);
			}
		}

		// Generate alias nodes
		List<LiteralCommandNode<Source>> aliasNodes = new ArrayList<>();
		for (String alias : aliases) {
			// Create node
			LiteralArgumentBuilder<Source> aliasBuilder = LiteralArgumentBuilder.literal(alias);

			// Add permission and requirements
			aliasBuilder.requires(handler.generateBrigadierRequirements(permission, requirements));

			// Add our executor
			if ((arguments.isEmpty() || arguments.get(0).isOptional()) && executor.hasAnyExecutors()) {
				aliasBuilder.executes(handler.generateBrigadierCommand(List.of(), executor));
			}

			// Redirect to rootNode so all its arguments come after this node
			aliasBuilder.redirect(rootNode);

			// Register alias node
			aliasNodes.add(aliasBuilder.build());
		}

		return new Nodes<>(rootNode, aliasNodes);
	}
}
