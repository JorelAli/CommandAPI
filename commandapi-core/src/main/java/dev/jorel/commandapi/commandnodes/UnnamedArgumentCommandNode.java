package dev.jorel.commandapi.commandnodes;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.RedirectModifier;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContextBuilder;
import com.mojang.brigadier.context.StringRange;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.CommandNode;

import java.util.function.Predicate;

/**
 * A special type of {@link ArgumentCommandNode} for unlisted Arguments. Compared to the {@link ArgumentCommandNode},
 * when this node is parsed, it will not add its  value as an argument in the CommandContext, like a literal argument.
 *
 * @param <Source> The Brigadier Source object for running commands.
 * @param <T> The type returned when this argument is parsed.
 */
public class UnnamedArgumentCommandNode<Source, T> extends ArgumentCommandNode<Source, T> {
	public UnnamedArgumentCommandNode(String name, ArgumentType<T> type, Command<Source> command, Predicate<Source> requirement, CommandNode<Source> redirect, RedirectModifier<Source> modifier, boolean forks, SuggestionProvider<Source> customSuggestions) {
		super(name, type, command, requirement, redirect, modifier, forks, customSuggestions);
	}

	// A UnnamedArgumentCommandNode is mostly identical to a ArgumentCommandNode
	//  The only difference is that when a UnnamedArgument is parsed, it does not add its result to the CommandContext
	@Override
	public void parse(StringReader reader, CommandContextBuilder<Source> contextBuilder) throws CommandSyntaxException {
		final int start = reader.getCursor();
		getType().parse(reader);
		contextBuilder.withNode(this, new StringRange(start, reader.getCursor()));
	}

	// Typical ArgumentCommandNode methods, but make it our classes
	//  Mostly copied and inspired by the implementations for these methods in ArgumentCommandNode

	// TODO: Um, this currently doesn't work since UnnamedRequiredArgumentBuilder does not extend RequiredArgumentBuilder
	//  See UnnamedRequiredArgumentBuilder for why
	//  I hope no one tries to use this method!
//	@Override
//	public UnnamedRequiredArgumentBuilder<Source, T> createBuilder() {
//		UnnamedRequiredArgumentBuilder<Source, T> builder = UnnamedRequiredArgumentBuilder.unnamedArgument(getName(), getType());
//
//		builder.requires(getRequirement());
//		builder.forward(getRedirect(), getRedirectModifier(), isFork());
//		builder.suggests(getCustomSuggestions());
//		if (getCommand() != null) {
//			builder.executes(getCommand());
//		}
//		return builder;
//	}

	@Override
	public String toString() {
		return "<unnamedargument " + getName() + ":" + getType() + ">";
	}
}
