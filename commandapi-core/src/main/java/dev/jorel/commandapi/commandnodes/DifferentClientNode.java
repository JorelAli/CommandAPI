package dev.jorel.commandapi.commandnodes;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.RedirectModifier;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.context.CommandContextBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.CommandNode;
import dev.jorel.commandapi.CommandAPIHandler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

public abstract class DifferentClientNode<Source, T> extends ArgumentCommandNode<Source, T> {
	// Rewrite node trees for the client
	public static <Source> void rewriteAllChildren(Source client, CommandNode<Source> parent) {
		// Copy the children, as we do expect them to be modified
		List<CommandNode<Source>> children = new ArrayList<>(parent.getChildren());
		for (CommandNode<Source> child : children) {
			rewriteChild(client, parent, child);
		}
	}

	public static <Source> void rewriteChild(Source client, CommandNode<Source> parent, CommandNode<Source> child) {
		// Get the node creator
		DifferentClientNode<Source, ?> clientNodeCreator = null;
		if (child instanceof DifferentClientNode<?, ?>) {
			// child is directly a DifferentClientNode
			clientNodeCreator = (DifferentClientNode<Source, ?>) child;
		} else if (child instanceof ArgumentCommandNode<Source, ?> argument && argument.getType() instanceof Type<?, ?>) {
			// child was copied from a DifferentClientNode using `createBuilder` (real node hidden in ArgumentType)
			Type<Source, ?> type = (Type<Source, ?>) argument.getType();
			clientNodeCreator = type.node;
		}

		if (clientNodeCreator != null) {
			// Get the new client nodes
			List<CommandNode<Source>> clientNodes = clientNodeCreator.rewriteNodeForClient(client);

			// Inject client node
			Map<String, CommandNode<Source>> children = CommandAPIHandler.getCommandNodeChildren(parent);
			children.remove(child.getName());
			for (CommandNode<Source> clientNode : clientNodes) {
				children.put(clientNode.getName(), clientNode);
			}
		}

		// Modify all children
		rewriteAllChildren(client, child);
	}

	// Node information
	private final ArgumentType<T> type;

	public DifferentClientNode(
		String name, ArgumentType<T> type,
		Command<Source> command, Predicate<Source> requirement,
		CommandNode<Source> redirect, RedirectModifier<Source> modifier, boolean forks
	) {
		// Platforms often copy this node as a normal ArgumentCommandNode,
		//  but we can hide a reference to ourselves in the ArgumentType used
		//  when ArgumentCommandNode#createBuilder is called
		super(name, new Type<Source, T>(), command, requirement, redirect, modifier, forks, null);

		((Type<Source, T>) super.getType()).node = this;

		// This type actually represents this argument when serializing nodes to json
		//  and also helps this argument blend in with ambiguity checks
		this.type = type;
	}

	// Handle type nonsense
	private static class Type<Source, T> implements ArgumentType<T> {
		private DifferentClientNode<Source, T> node;

		@Override
		public T parse(StringReader stringReader) {
			throw new IllegalStateException("Not supposed to be called");
		}
	}

	@Override
	public ArgumentType<T> getType() {
		return type;
	}

	@Override
	public boolean isValidInput(String input) {
		try {
			StringReader reader = new StringReader(input);
			this.type.parse(reader);
			return !reader.canRead() || reader.peek() == ' ';
		} catch (CommandSyntaxException var3) {
			return false;
		}
	}

	@Override
	public Collection<String> getExamples() {
		return this.type.getExamples();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!(obj instanceof DifferentClientNode<?, ?> other)) return false;

		return this.type.equals(other.type) && super.equals(other);
	}

	@Override
	public int hashCode() {
		int result = this.type.hashCode();
		result = 31 * result + super.hashCode();
		return result;
	}

	// Special client-side nodes

	/**
	 * Transforms this node into one the client should see.
	 *
	 * @param client The client who the new node is for. If this is null,
	 *               the generated node should just be for any general client.
	 * @return The version of this {@link CommandNode} the client should see. This is a list, so one server
	 * node may become many on the client. If the list is empty, the client will not see any nodes.
	 */
	public abstract List<CommandNode<Source>> rewriteNodeForClient(Source client);

	// Require inheritors to redefine these methods from ArgumentCommandNode
	@Override
	public abstract void parse(StringReader reader, CommandContextBuilder<Source> contextBuilder) throws CommandSyntaxException;

	@Override
	public abstract CompletableFuture<Suggestions> listSuggestions(CommandContext<Source> context, SuggestionsBuilder builder) throws CommandSyntaxException;

	@Override
	public abstract String toString();
}
