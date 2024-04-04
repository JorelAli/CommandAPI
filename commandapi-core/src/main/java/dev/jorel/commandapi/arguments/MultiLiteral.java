package dev.jorel.commandapi.arguments;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import dev.jorel.commandapi.commandnodes.NamedLiteralArgumentBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

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
	 * Links to {@link AbstractArgument#isListed()}.
	 */
	boolean isListed();

	/**
	 * Links to {@link AbstractArgument#getCombinedArguments()}.
	 */
	List<Argument> getCombinedArguments();

	/**
	 * Links to {@link AbstractArgument#finishBuildingNode(ArgumentBuilder, List, Function)}.
	 */
	<Source> CommandNode<Source> finishBuildingNode(ArgumentBuilder<Source, ?> rootBuilder, List<Argument> previousArguments, Function<List<Argument>, Command<Source>> terminalExecutorCreator);

	////////////////////////////////////////////////////////////////////////////////////////////////////
	// OVERRIDING METHODS                                                                             //
	//  A MultiLiteral has special logic that should override the implementations in AbstractArgument //
	////////////////////////////////////////////////////////////////////////////////////////////////////

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
	 */
	default <Source> ArgumentBuilder<Source, ?> createArgumentBuilder(List<Argument> previousArguments, List<String> previousArgumentNames) {
		String nodeName = getNodeName();
		String literal = getLiterals()[0];

		previousArguments.add((Argument) this);
		if(isListed()) previousArgumentNames.add(nodeName);

		return isListed() ?
			NamedLiteralArgumentBuilder.namedLiteral(nodeName, literal) :
			LiteralArgumentBuilder.literal(literal);
	}

	/**
	 * Overrides {@link AbstractArgument#linkNode(List, CommandNode, List, List, Function)}.
	 * <p>
	 * Normally, Arguments are only represented by a single node, and so only need to link one node. However, a MultiLiteral
	 * represents multiple literal node paths, which also need to be generated and inserted into the node structure.
	 */
	default <Source> List<CommandNode<Source>> linkNode(
		List<CommandNode<Source>> previousNodes, CommandNode<Source> rootNode,
		List<Argument> previousArguments, List<String> previousArgumentNames,
		Function<List<Argument>, Command<Source>> terminalExecutorCreator
	) {
		List<CommandNode<Source>> newNodes = new ArrayList<>();
		// Add root node to previous
		for(CommandNode<Source> previousNode : previousNodes) {
			previousNode.addChild(rootNode);
		}
		newNodes.add(rootNode);

		// Generate nodes for other literals
		String nodeName = getNodeName();
		boolean isListed = isListed();
		Iterator<String> literals = Arrays.asList(getLiterals()).iterator();
		literals.next(); // Skip first literal; that was handled by `#createArgumentBuilder`
		while (literals.hasNext()) {
			// Create node
			LiteralArgumentBuilder<Source> literalBuilder = isListed ?
				NamedLiteralArgumentBuilder.namedLiteral(nodeName, literals.next()) :
				LiteralArgumentBuilder.literal(literals.next());

			// Finish building node
			CommandNode<Source> literalNode = finishBuildingNode(literalBuilder, previousArguments, terminalExecutorCreator);

			// Add node to previous
			for(CommandNode<Source> previousNode : previousNodes) {
				previousNode.addChild(literalNode);
			}
			newNodes.add(literalNode);
		}

		// Stack on combined arguments
		previousNodes = AbstractArgument.stackArguments(getCombinedArguments(), newNodes, previousArguments, previousArgumentNames, terminalExecutorCreator);

		// Return last nodes
		return previousNodes;
	}
}
