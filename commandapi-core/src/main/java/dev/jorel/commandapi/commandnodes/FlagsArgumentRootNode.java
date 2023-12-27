package dev.jorel.commandapi.commandnodes;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.CommandContextBuilder;
import com.mojang.brigadier.context.ParsedArgument;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.jorel.commandapi.CommandAPIHandler;
import dev.jorel.commandapi.arguments.AbstractArgument;

import java.util.*;

public class FlagsArgumentRootNode<Argument
/// @cond DOX
extends AbstractArgument<?, ?, Argument, CommandSender>
/// @endcond
, CommandSender, Source> extends LiteralCommandNode<Source> {
	public FlagsArgumentRootNode(LiteralCommandNode<Source> literalNode) {
		super(
			literalNode.getName(), literalNode.getCommand(), literalNode.getRequirement(),
			literalNode.getRedirect(), literalNode.getRedirectModifier(), literalNode.isFork()
		);
	}

	// Handle setting up the loop back
	public void makeChildrenLoopBack(Collection<CommandNode<Source>> nodes, List<Argument> previousArguments) {
		for (CommandNode<Source> node : nodes) {
			// Nodes in the `children` map should redirect to this node, so the client doesn't see a child cycle
			// Nodes in the `arguments`/`literals` maps should have all children of this node as children
			//  and store all the flag values given
			Map<String, CommandNode<Source>> children = CommandAPIHandler.getCommandNodeChildren(node);
			for (CommandNode<Source> child : node.getChildren()) {
				// Clone the node, redirect it here, then put the clone into the children map
				children.put(child.getName(), child.createBuilder().redirect(this).build());

				// Wrap nodes in the `arguments`/`literals` map to extract the flag values on loop
				CommandNode<Source> finalWrappedNode;
				if (child instanceof LiteralCommandNode<Source> literalNode) {
					LiteralCommandNode<Source> wrappedNode =
						FlagsArgumentEndingNode.wrapNode(literalNode, getName(), previousArguments);

					CommandAPIHandler.getCommandNodeLiterals(node).put(literalNode.getName(), wrappedNode);
					finalWrappedNode = wrappedNode;
				} else if(child instanceof ArgumentCommandNode<Source,?> argumentNode) {
					ArgumentCommandNode<Source, ?> wrappedNode =
						FlagsArgumentEndingNode.wrapNode(argumentNode, getName(), previousArguments);

					CommandAPIHandler.getCommandNodeArguments(node).put(argumentNode.getName(), wrappedNode);
					finalWrappedNode = wrappedNode;
				} else {
					throw new IllegalArgumentException("Node must be an argument or literal. Given " + child + " with type " + child.getClass().getName());
				}

				// These wrapped nodes should always have our children as their children, so let's share map instances
				CommandAPIHandler.setCommandNodeChildren(finalWrappedNode, CommandAPIHandler.getCommandNodeChildren(this));
				CommandAPIHandler.setCommandNodeLiterals(finalWrappedNode, CommandAPIHandler.getCommandNodeLiterals(this));
				CommandAPIHandler.setCommandNodeArguments(finalWrappedNode, CommandAPIHandler.getCommandNodeArguments(this));
			}
		}
	}

	// Set up the list of information output by this argument
	@Override
	public void parse(StringReader reader, CommandContextBuilder<Source> contextBuilder) throws CommandSyntaxException {
		int start = reader.getCursor();
		super.parse(reader, contextBuilder);

		contextBuilder.withArgument(getName(), new ParsedArgument<>(start, reader.getCursor(), new ArrayList<>()));
	}

	@Override
	public String toString() {
		return "<flagargstart " + getLiteral() + ">";
	}
}
