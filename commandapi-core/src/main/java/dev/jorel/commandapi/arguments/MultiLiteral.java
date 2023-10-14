package dev.jorel.commandapi.arguments;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import dev.jorel.commandapi.CommandAPIExecutor;
import dev.jorel.commandapi.commandsenders.AbstractCommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * An interface representing arguments with multiple literal string definitions
 */
public interface MultiLiteral<Argument
/// @cond DOX
extends AbstractArgument<?, ?, Argument, CommandSender>
/// @endcond
, CommandSender> {
	// MultiLiteral specific information

	/**
	 * Returns the literals that are present in this argument.
	 *
	 * @return the literals that are present in this argument.
	 * @apiNote A MultiLiteral argument must have at least one literal. E.g. the array returned by this method should
	 * never be empty.
	 */
	String[] getLiterals();

	///////////////////////////////////////////////////////////////////////////////////////////////////////
	// LINKED METHODS                                                                                    //
	//  These automatically link to methods in AbstractArgument (make sure they have the same signature) //
	///////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Links to {@link AbstractArgument#getNodeName()}.
	 */
	String getNodeName();

	/**
	 * Links to {@link AbstractArgument#getCombinedArguments()}.
	 */
	List<Argument> getCombinedArguments();

	/**
	 * Links to {@link AbstractArgument#finishBuildingNode(ArgumentBuilder, List, CommandAPIExecutor)}.
	 */
	<Source> CommandNode<Source> finishBuildingNode(ArgumentBuilder<Source, ?> rootBuilder, List<Argument> previousArguments, CommandAPIExecutor<CommandSender, AbstractCommandSender<? extends CommandSender>> terminalExecutor);

	////////////////////////////////////////////////////////////////////////////////////////////////////
	// OVERRIDING METHODS                                                                             //
	//  A MultiLiteral has special logic that should override the implementations in AbstractArgument //
	////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Overrides {@link AbstractArgument#checkPreconditions(List, List)}.
	 * <p>
	 * Normally, Arguments cannot be built if their node name is found in {@code previousNonLiteralArgumentNames} list.
	 * However, LiteralArguments do not have this problem, so we want to skip that check.
	 */
	default void checkPreconditions(List<Argument> previousArguments, List<String> previousNonLiteralArgumentNames) {

	}

	/**
	 * Overrides {@link AbstractArgument#appendToCommandPaths(List)}.
	 * <p>
	 * Normally, Arguments only represent a single branching path. However, a MultiLiteral represents multiple literal
	 * node paths, so we need to branch out the current paths for each literal.
	 */
	default void appendToCommandPaths(List<List<String>> argumentStrings) {
		// Create paths for this argument
		Iterator<String> literals = Arrays.asList(getLiterals()).iterator();
		String firstLiteralArgumentString = literals.next() + ":LiteralArgument";

		// Copy each path for the other literals
		List<List<String>> newPaths = new ArrayList<>();
		while (literals.hasNext()) {
			String literalArgumentString = literals.next() + ":LiteralArgument";
			for (List<String> path : argumentStrings) {
				List<String> newPath = new ArrayList<>(path);
				newPath.add(literalArgumentString);
				newPaths.add(newPath);
			}
		}

		// Add first literal to the original paths
		for (List<String> path : argumentStrings) {
			path.add(firstLiteralArgumentString);
		}
		argumentStrings.addAll(newPaths);

		// Add combined arguments
		for (Argument argument : getCombinedArguments()) {
			argument.appendToCommandPaths(argumentStrings);
		}
	}

	/**
	 * Overrides {@link AbstractArgument#createArgumentBuilder(List, List)}.
	 * <p>
	 * Normally, Arguments will use Brigadier's RequiredArgumentBuilder. However, MultiLiterals use LiteralArgumentBuilders.
	 * Arguments also usually add their name to the {@code previousNonLiteralArgumentNames} list, but literal node names
	 * do not conflict with required argument node names.
	 */
	default <Source> ArgumentBuilder<Source, ?> createArgumentBuilder(List<Argument> previousArguments, List<String> previousNonLiteralArgumentNames) {
		previousArguments.add((Argument) this);

		return MultiLiteralArgumentBuilder.multiLiteral(getNodeName(), getLiterals()[0]);
	}

	/**
	 * Overrides {@link AbstractArgument#linkNode(CommandNode, CommandNode, List, List, CommandAPIExecutor)}.
	 * <p>
	 * Normally, Arguments are only represented by a single node, and so only need to link one node. However, a MultiLiteral
	 * represents multiple literal node paths, which also need to be generated and inserted into the node structure.
	 */
	default <Source> CommandNode<Source> linkNode(CommandNode<Source> previousNode, CommandNode<Source> rootNode, List<Argument> previousArguments, List<String> previousNonLiteralArgumentNames,
												  CommandAPIExecutor<CommandSender, AbstractCommandSender<? extends CommandSender>> terminalExecutor) {
		// Generate nodes for other literals
		Iterator<String> literals = Arrays.asList(getLiterals()).iterator();
		literals.next(); // Skip first literal; that was handled by `#createArgumentBuilder`
		while (literals.hasNext()) {
			// Create node
			MultiLiteralArgumentBuilder<Source> literalBuilder = MultiLiteralArgumentBuilder.multiLiteral(getNodeName(), literals.next());

			// Redirect to root node so all its arguments come after this
			literalBuilder.redirect(rootNode);

			// Finish building node
			CommandNode<Source> literalNode = finishBuildingNode(literalBuilder, previousArguments, terminalExecutor);

			// Link node to previous
			previousNode.addChild(literalNode);
		}

		// Add root node to previous
		previousNode.addChild(rootNode);

		// Add combined arguments
		previousNode = rootNode;
		List<Argument> combinedArguments = getCombinedArguments();
		for (int i = 0; i < combinedArguments.size(); i++) {
			Argument subArgument = combinedArguments.get(i);
			previousNode = subArgument.addArgumentNodes(previousNode, previousArguments, previousNonLiteralArgumentNames,
				// Only apply the `terminalExecutor` to the last argument
				i == combinedArguments.size() - 1 ? terminalExecutor : null);
		}

		// Return last node
		return previousNode;
	}
}
