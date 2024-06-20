package dev.jorel.commandapi.commandnodes;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.RedirectModifier;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.context.CommandContextBuilder;
import com.mojang.brigadier.context.ParsedArgument;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.jorel.commandapi.CommandAPIHandler;
import dev.jorel.commandapi.arguments.DynamicMultiLiteralArgumentCommon.LiteralsCreator;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

public class DynamicMultiLiteralCommandNode<CommandSender, Source> extends DifferentClientNode<Source, String> {
	private final boolean isListed;
	private final LiteralsCreator<CommandSender> literalsCreator;

	public DynamicMultiLiteralCommandNode(
		String name, boolean isListed, LiteralsCreator<CommandSender> literalsCreator,
		Command<Source> command, Predicate<Source> requirement,
		CommandNode<Source> redirect, RedirectModifier<Source> modifier, boolean forks
	) {
		// This mostly acts like a StringArgument
		super(name, StringArgumentType.word(), command, requirement, redirect, modifier, forks);

		this.isListed = isListed;
		this.literalsCreator = literalsCreator;
	}

	// Getters
	public boolean isListed() {
		return isListed;
	}

	public LiteralsCreator<CommandSender> getLiteralsCreator() {
		return literalsCreator;
	}

	// On the client, this node looks like a bunch of literal nodes
	@Override
	public List<CommandNode<Source>> rewriteNodeForClient(Source client) {
		CommandAPIHandler<?, CommandSender, Source> commandAPIHandler = CommandAPIHandler.getInstance();
		CommandSender sender = commandAPIHandler.getPlatform().getCommandSenderFromCommandSource(client);
		List<String> literals = literalsCreator.createLiterals(sender);

		if (literals.isEmpty()) return List.of();

		List<CommandNode<Source>> clientNodes = new ArrayList<>(literals.size());
		for (String literal : literals) {
			LiteralCommandNode<Source> clientNode = new LiteralCommandNode<>(
				literal,
				getCommand(), getRequirement(),
				getRedirect(), getRedirectModifier(), isFork()
			);
			for (CommandNode<Source> child : getChildren()) {
				clientNode.addChild(child);
			}
			clientNodes.add(clientNode);
		}

		return clientNodes;
	}

	// Implement CommandNode methods
	@Override
	public void parse(StringReader reader, CommandContextBuilder<Source> contextBuilder) throws CommandSyntaxException {
		// Read the input
		int start = reader.getCursor();
		String literal = reader.readUnquotedString();

		// Validate input
		CommandAPIHandler<?, CommandSender, Source> commandAPIHandler = CommandAPIHandler.getInstance();
		CommandSender client = commandAPIHandler.getPlatform().getCommandSenderFromCommandSource(contextBuilder.getSource());
		List<String> literals = literalsCreator.createLiterals(client);
		if (!literals.contains(literal)) {
			reader.setCursor(start);
			throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.literalIncorrect().createWithContext(reader, literals);
		}

		// Add node to list
		ParsedArgument<Source, String> parsed = new ParsedArgument<>(start, reader.getCursor(), literal);
		if (isListed) contextBuilder.withArgument(this.getName(), parsed);
		contextBuilder.withNode(this, parsed.getRange());
	}

	@Override
	public CompletableFuture<Suggestions> listSuggestions(CommandContext<Source> context, SuggestionsBuilder builder) throws CommandSyntaxException {
		CommandAPIHandler<?, CommandSender, Source> commandAPIHandler = CommandAPIHandler.getInstance();
		CommandSender client = commandAPIHandler.getPlatform().getCommandSenderFromCommandSource(context.getSource());
		List<String> literals = literalsCreator.createLiterals(client);

		String remaining = builder.getRemaining().toLowerCase();
		for (String literal : literals) {
			if (literal.startsWith(remaining)) {
				builder.suggest(literal);
			}
		}
		return builder.buildFuture();
	}

	@Override
	public boolean isValidInput(String input) {
		// For ambiguity checking purposes, this node could accept any input
		return true;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!(obj instanceof DynamicMultiLiteralCommandNode<?, ?> other)) return false;

		if (this.isListed != other.isListed) return false;
		if (!Objects.equals(this.literalsCreator, other.literalsCreator)) return false;
		return super.equals(other);
	}

	@Override
	public int hashCode() {
		int result = Objects.hash(this.isListed, this.literalsCreator);
		result = 31 * result + super.hashCode();
		return result;
	}

	@Override
	public String toString() {
		return "<dynamicMultiLiteral " + getName() + ">";
	}
}
