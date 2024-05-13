package dev.jorel.commandapi.arguments;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import dev.jorel.commandapi.CommandAPIHandler;
import dev.jorel.commandapi.CommandPermission;
import dev.jorel.commandapi.RegisteredCommand;
import dev.jorel.commandapi.arguments.AbstractArgument.NodeInformation;
import dev.jorel.commandapi.commandnodes.FlagsArgumentEndingNode;
import dev.jorel.commandapi.commandnodes.FlagsArgumentRootNode;
import dev.jorel.commandapi.executors.CommandArguments;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;

public interface FlagsArgumentCommon<Impl
/// @cond DOX
extends FlagsArgumentCommon<Impl, Argument, CommandSender>
/// @endcond
, Argument
/// @cond DOX
extends AbstractArgument<?, ?, Argument, CommandSender>
/// @endcond
, CommandSender> {
	// Setup information
	Impl loopingBranch(Argument... arguments);

	List<List<Argument>> getLoopingBranches();

	Impl terminalBranch(Argument... arguments);

	List<List<Argument>> getTerminalBranches();

	///////////////////////////////////////////////////////////////////////////////////////////////////////
	// LINKED METHODS                                                                                    //
	//  These automatically link to methods in AbstractArgument (make sure they have the same signature) //
	///////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Links to {@link AbstractArgument#getNodeName()}.
	 */
	String getNodeName();

	/**
	 * Links to {@link AbstractArgument#getArgumentPermission()}.
	 */
	CommandPermission getArgumentPermission();

	/**
	 * Links to {@link AbstractArgument#getRequirements()}.
	 */
	Predicate<CommandSender> getRequirements();

	/**
	 * Links to {@link AbstractArgument#getCombinedArguments()}.
	 */
	List<Argument> getCombinedArguments();

	/**
	 * Links to {@link AbstractArgument#checkPreconditions(NodeInformation, List, List)}.
	 */
	<Source> void checkPreconditions(
		NodeInformation<CommandSender, Source> previousNodes, List<Argument> previousArguments, List<String> previousArgumentNames
	);

	/**
	 * Links to {@link AbstractArgument#finishBuildingNode(ArgumentBuilder, List, Function)}.
	 */
	<Source> CommandNode<Source> finishBuildingNode(ArgumentBuilder<Source, ?> rootBuilder, List<Argument> previousArguments, Function<List<Argument>, Command<Source>> terminalExecutorCreator);

	/////////////////////////////////////////////////////////////////////////////////////////////////////
	// OVERRIDING METHODS                                                                              //
	//  A FlagsArgument has special logic that should override the implementations in AbstractArgument //
	/////////////////////////////////////////////////////////////////////////////////////////////////////

	record ParseInformation<Argument
	/// @cond DOX
	extends AbstractArgument<?, ?, Argument, CommandSender>
	/// @endcond
	, CommandSender, Source>
		(CommandContext<Source> context, List<Argument> arguments) {
	}

	/**
	 * Overrides {@link AbstractArgument#parseArgument(CommandContext, String, CommandArguments)}
	 */
	default <Source> List<CommandArguments> parseArgument(CommandContext<Source> cmdCtx, String key, CommandArguments previousArgs) throws CommandSyntaxException {
		CommandAPIHandler<Argument, CommandSender, Source> handler = CommandAPIHandler.getInstance();

		List<ParseInformation<Argument, CommandSender, Source>> parseInformations = cmdCtx.getArgument(key, List.class);
		List<CommandArguments> results = new ArrayList<>(parseInformations.size());

		for (ParseInformation<Argument, CommandSender, Source> parseInformation : parseInformations) {
			results.add(handler.argsToCommandArgs(parseInformation.context(), parseInformation.arguments()));
		}

		return results;
	}

	/**
	 * Overrides {@link AbstractArgument#addArgumentNodes(NodeInformation, List, List, Function)}.
	 * <p>
	 * A FlagsArgument works completely differently from a typical argument, so we need to completely
	 * override the usual logic.
	 */
	default <Source> NodeInformation<CommandSender, Source> addArgumentNodes(
		NodeInformation<CommandSender, Source> previousNodeInformation,
		List<Argument> previousArguments, List<String> previousArgumentNames,
		Function<List<Argument>, Command<Source>> terminalExecutorCreator
	) {
		// Typical preconditions still apply
		checkPreconditions(previousNodeInformation, previousArguments, previousArgumentNames);

		// Add this argument to the lists
		String nodeName = getNodeName();
		previousArguments.add((Argument) this);
		previousArgumentNames.add(nodeName);

		// Create root node, add to previous
		LiteralArgumentBuilder<Source> rootBuilder = LiteralArgumentBuilder.literal(nodeName);
		finishBuildingNode(rootBuilder, previousArguments, null);
		FlagsArgumentRootNode<Argument, CommandSender, Source> rootNode = new FlagsArgumentRootNode<>(rootBuilder.build());

		for(CommandNode<Source> previousNode : previousNodeInformation.lastCommandNodes()) {
			previousNode.addChild(rootNode);
		}

		// Setup looping branches
		boolean loopingBranchesExecutable = getTerminalBranches().isEmpty();
		Function<List<Argument>, Command<Source>> loopingBranchExecutor = loopingBranchesExecutable ? terminalExecutorCreator : null;

		for(List<Argument> loopingBranch : getLoopingBranches()) {
			setupBranch(loopingBranch, rootNode, previousArguments, previousArgumentNames, loopingBranchExecutor, rootNode::makeChildrenLoopBack);
		}

		// Setup terminal branches
		boolean terminalBranchesExecutable = getCombinedArguments().isEmpty();
		Function<List<Argument>, Command<Source>> terminalBranchExecutor = terminalBranchesExecutable ? terminalExecutorCreator : null;

		// The last nodes here will be our final nodes
		List<CommandNode<Source>> newNodes = new ArrayList<>();
		for(List<Argument> terminalBranch : getTerminalBranches()) {
			newNodes.addAll(
				setupBranch(terminalBranch, rootNode, previousArguments, previousArgumentNames, terminalBranchExecutor, this::wrapTerminalBranchNodes)
			);
		}

		// Create information for this node
		NodeInformation<CommandSender, Source> nodeInformation = new NodeInformation<>(
			newNodes, 
			// Create registered node information once children are created
			children -> previousNodeInformation.childrenConsumer().createNodeWithChildren(List.of(
				new RegisteredCommand.Node<>(
					nodeName, getClass().getSimpleName(), "<" + nodeName + ">", 
					loopingBranchesExecutable || terminalBranchesExecutable, 
					getArgumentPermission(), getRequirements(),
					children
				)
			))
		);

		// Stack on combined arguments and return last nodes
		return AbstractArgument.stackArguments(getCombinedArguments(), nodeInformation, previousArguments, previousArgumentNames, terminalExecutorCreator);
	}

	private static <Argument extends AbstractArgument<?, ?, Argument, CommandSender>, CommandSender, Source> List<CommandNode<Source>> setupBranch(
		List<Argument> branchArguments, CommandNode<Source> rootNode,
		List<Argument> previousArguments, List<String> previousArgumentNames,
		Function<List<Argument>, Command<Source>> terminalExecutorCreator,
		BiConsumer<Collection<CommandNode<Source>>, List<Argument>> secondLastNodeProcessor
	) {
		// Make clones of our lists to treat this branch independently
		List<Argument> branchPreviousArguments = new ArrayList<>(previousArguments);
		List<String> branchPreviousArgumentNames = new ArrayList<>(previousArgumentNames);

		RootCommandNode<Source> branchRoot = new RootCommandNode<>();
		NodeInformation<CommandSender, Source> branchNodeInformation = new NodeInformation<>(List.of(branchRoot), null);

		// Stack branch nodes
		branchNodeInformation = AbstractArgument.stackArguments(branchArguments, branchNodeInformation, branchPreviousArguments, branchPreviousArgumentNames, terminalExecutorCreator);

		// Find second-to-last nodes so their children can be modified
		//  Unfortunately, we can't get this while stacking since arguments may (and may not) unpack to multiple layers
		Collection<CommandNode<Source>> currentNodes = branchRoot.getChildren();
		Collection<CommandNode<Source>> lastNodes = List.of(branchRoot);
		Collection<CommandNode<Source>> secondLastNodes = null;
		while (!currentNodes.isEmpty()) {
			secondLastNodes = lastNodes;
			lastNodes = currentNodes;
			currentNodes = new HashSet<>();

			for (CommandNode<Source> node : lastNodes) {
				currentNodes.addAll(node.getChildren());
			}
		}

		// Modify the children of the secondLastNodes
		secondLastNodeProcessor.accept(secondLastNodes, branchPreviousArguments);

		// Copy branch nodes directly to the root node (branchRoot's maps may have been intentionally de-synced)
		CommandAPIHandler.getCommandNodeChildren(rootNode).putAll(CommandAPIHandler.getCommandNodeChildren(branchRoot));
		CommandAPIHandler.getCommandNodeLiterals(rootNode).putAll(CommandAPIHandler.getCommandNodeLiterals(branchRoot));
		CommandAPIHandler.getCommandNodeArguments(rootNode).putAll(CommandAPIHandler.getCommandNodeArguments(branchRoot));

		// Return the last nodes in the tree
		return branchNodeInformation.lastCommandNodes();
	}

	private <Source> void wrapTerminalBranchNodes(
		Collection<CommandNode<Source>> secondLastNodes, List<Argument> branchPreviousArguments
	) {
		String nodeName = getNodeName();

		// Wrap terminating nodes to extract flag values
		for(CommandNode<Source> node : secondLastNodes) {
			// Nodes in the `children` and `arguments`/`literals` maps need to be wrapped and substituted
			Map<String, CommandNode<Source>> children = CommandAPIHandler.getCommandNodeChildren(node);
			Map<String, LiteralCommandNode<Source>> literals = CommandAPIHandler.getCommandNodeLiterals(node);
			Map<String, ArgumentCommandNode<Source, ?>> arguments = CommandAPIHandler.getCommandNodeArguments(node);

			for (CommandNode<Source> child : node.getChildren()) {
				CommandNode<Source> finalWrappedNode;
				if (child instanceof LiteralCommandNode<Source> literalNode) {
					LiteralCommandNode<Source> wrappedNode =
						FlagsArgumentEndingNode.wrapNode(literalNode, nodeName, branchPreviousArguments);

					literals.put(literalNode.getName(), wrappedNode);
					finalWrappedNode = wrappedNode;
				} else if(child instanceof ArgumentCommandNode<Source,?> argumentNode) {
					ArgumentCommandNode<Source, ?> wrappedNode =
						FlagsArgumentEndingNode.wrapNode(argumentNode, nodeName, branchPreviousArguments);

					arguments.put(argumentNode.getName(), wrappedNode);
					finalWrappedNode = wrappedNode;
				} else {
					throw new IllegalArgumentException("Node must be an argument or literal. Given " + child + " with type " + child.getClass().getName());
				}
				children.put(child.getName(), finalWrappedNode);

				// These wrapped nodes should always have the same children as the node they are wrapping, so let's share map instances
				CommandAPIHandler.setCommandNodeChildren(finalWrappedNode, CommandAPIHandler.getCommandNodeChildren(child));
				CommandAPIHandler.setCommandNodeLiterals(finalWrappedNode, CommandAPIHandler.getCommandNodeLiterals(child));
				CommandAPIHandler.setCommandNodeArguments(finalWrappedNode, CommandAPIHandler.getCommandNodeArguments(child));
			}
		}
	}
}
