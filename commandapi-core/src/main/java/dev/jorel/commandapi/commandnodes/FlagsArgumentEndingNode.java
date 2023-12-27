package dev.jorel.commandapi.commandnodes;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.CommandContextBuilder;
import com.mojang.brigadier.context.ParsedArgument;
import com.mojang.brigadier.context.ParsedCommandNode;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.jorel.commandapi.arguments.AbstractArgument;
import dev.jorel.commandapi.arguments.FlagsArgumentCommon;

import java.util.ArrayList;
import java.util.List;

public interface FlagsArgumentEndingNode<Argument
/// @cond DOX
extends AbstractArgument<?, ?, Argument, CommandSender>
/// @endcond
, CommandSender, Source> {
	static <Argument extends AbstractArgument<?, ?, Argument, CommandSender>, CommandSender, Source>
	LiteralCommandNode<Source> wrapNode(
		LiteralCommandNode<Source> literalNode, String flagsArgumentName, List<Argument> previousArguments
	) {
		return new LiteralNode<>(literalNode, flagsArgumentName, previousArguments);
	}

	static <Argument extends AbstractArgument<?, ?, Argument, CommandSender>, CommandSender, Source>
	ArgumentCommandNode<Source, ?> wrapNode(
		ArgumentCommandNode<Source, ?> argumentNode, String flagsArgumentName, List<Argument> previousArguments
	) {
		return new ArgumentNode<>(argumentNode, flagsArgumentName, previousArguments);
	}

	String getFlagsArgumentName();

	List<Argument> getPreviousArguments();

	default void parse(StringReader reader, CommandContextBuilder<Source> contextBuilder) throws CommandSyntaxException {
		// Correctly set parsed node value
		List<ParsedCommandNode<Source>> nodes = contextBuilder.getNodes();
		int lastNodeIndex = nodes.size() - 1;
		ParsedCommandNode<Source> currentNode = nodes.get(lastNodeIndex);
		nodes.set(lastNodeIndex, new ParsedCommandNode<>((CommandNode<Source>) this, currentNode.getRange()));

		// Extract previous flags
		String name = getFlagsArgumentName();
		ParsedArgument<Source, List<FlagsArgumentCommon.ParseInformation<Argument, CommandSender, Source>>> currentValue =
			(ParsedArgument<Source, List<FlagsArgumentCommon.ParseInformation<Argument, CommandSender, Source>>>)
				contextBuilder.getArguments().get(name);
		List<FlagsArgumentCommon.ParseInformation<Argument, CommandSender, Source>> currentInformation = currentValue.getResult();

		// Add new flags
		//  We do need to copy the information list and the contextBuilder
		//  Otherwise, parsing the argument result references itself, and you get a stack overflow
		List<FlagsArgumentCommon.ParseInformation<Argument, CommandSender, Source>> newInformation = new ArrayList<>(currentInformation);
		newInformation.add(new FlagsArgumentCommon.ParseInformation<>(
			contextBuilder.copy().build(reader.getRead()), getPreviousArguments()
		));

		// Add new flags back
		ParsedArgument<Source, List<FlagsArgumentCommon.ParseInformation<Argument, CommandSender, Source>>> newValue =
			new ParsedArgument<>(currentValue.getRange().getStart(), reader.getCursor(), newInformation);
		contextBuilder.withArgument(name, newValue);
	}

	class LiteralNode<Argument
		/// @cond DOX
		extends AbstractArgument<?, ?, Argument, CommandSender>
		/// @endcond
		, CommandSender, Source>
		extends LiteralCommandNode<Source> implements FlagsArgumentEndingNode<Argument, CommandSender, Source> {
		private final LiteralCommandNode<Source> literalNode;
		private final String flagsArgumentName;
		private final List<Argument> previousArguments;

		public LiteralNode(
			LiteralCommandNode<Source> literalNode, String flagsArgumentName, List<Argument> previousArguments
		) {
			super(
				literalNode.getName(), literalNode.getCommand(), literalNode.getRequirement(),
				literalNode.getRedirect(), literalNode.getRedirectModifier(), literalNode.isFork()
			);

			this.literalNode = literalNode;
			this.flagsArgumentName = flagsArgumentName;
			this.previousArguments = previousArguments;
		}

		@Override
		public String getFlagsArgumentName() {
			return flagsArgumentName;
		}

		@Override
		public List<Argument> getPreviousArguments() {
			return previousArguments;
		}

		@Override
		public void parse(StringReader reader, CommandContextBuilder<Source> contextBuilder) throws CommandSyntaxException {
			literalNode.parse(reader, contextBuilder);
			FlagsArgumentEndingNode.super.parse(reader, contextBuilder);
		}

		@Override
		public String toString() {
			return "<flagargend " + literalNode.toString() + ">";
		}
	}

	class ArgumentNode<Argument
		/// @cond DOX
		extends AbstractArgument<?, ?, Argument, CommandSender>
		/// @endcond
		, CommandSender, Source, T>
		extends ArgumentCommandNode<Source, T> implements FlagsArgumentEndingNode<Argument, CommandSender, Source> {
		private final ArgumentCommandNode<Source, T> argumentNode;
		private final String flagsArgumentName;
		private final List<Argument> previousArguments;

		public ArgumentNode(
			ArgumentCommandNode<Source, T> argumentNode, String flagsArgumentName, List<Argument> previousArguments
		) {
			super(
				argumentNode.getName(), argumentNode.getType(),
				argumentNode.getCommand(), argumentNode.getRequirement(),
				argumentNode.getRedirect(), argumentNode.getRedirectModifier(), argumentNode.isFork(),
				argumentNode.getCustomSuggestions()
			);

			this.argumentNode = argumentNode;
			this.flagsArgumentName = flagsArgumentName;
			this.previousArguments = previousArguments;
		}

		@Override
		public String getFlagsArgumentName() {
			return flagsArgumentName;
		}

		@Override
		public List<Argument> getPreviousArguments() {
			return previousArguments;
		}

		@Override
		public void parse(StringReader reader, CommandContextBuilder<Source> contextBuilder) throws CommandSyntaxException {
			argumentNode.parse(reader, contextBuilder);
			FlagsArgumentEndingNode.super.parse(reader, contextBuilder);
		}

		@Override
		public String toString() {
			return "<flagargend " + argumentNode.toString() + ">";
		}
	}
}
