package dev.jorel.commandapi.arguments;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.RedirectModifier;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.CommandContextBuilder;
import com.mojang.brigadier.context.ParsedArgument;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;

import java.util.function.Predicate;

/**
 * A special type of {@link LiteralCommandNode} for {@link MultiLiteral}. Compared to the {@link LiteralCommandNode},
 * this class also has a {@code nodeName}. When this node is parsed, it will add its literal value as an argument in
 * the CommandContext, allowing a MultiLiteralArgument to know which literal was selected.
 *
 * @param <Source> The Brigadier Source object for running commands.
 */
public class MultiLiteralCommandNode<Source> extends LiteralCommandNode<Source> {
	private final String nodeName;

	public MultiLiteralCommandNode(String nodeName, String literal, Command<Source> command, Predicate<Source> requirement, CommandNode<Source> redirect, RedirectModifier<Source> modifier, boolean forks) {
		super(literal, command, requirement, redirect, modifier, forks);
		this.nodeName = nodeName;
	}

	/**
	 * @return the node name this command represents.
	 */
	public String getNodeName() {
		return nodeName;
	}

	// A MultiLiteralCommandNode is mostly identical to a LiteralCommandNode
	//  The only difference is that when a MultiLiteral is parsed, it adds its literal as an argument based on the nodeName
	@Override
	public void parse(StringReader reader, CommandContextBuilder<Source> contextBuilder) throws CommandSyntaxException {
		int start = reader.getCursor();
		super.parse(reader, contextBuilder);

		contextBuilder.withArgument(getNodeName(), new ParsedArgument<>(start, reader.getCursor(), getLiteral()));
	}

	// Typical LiteralCommandNode methods, adding in the nodeName parameter
	//  Mostly copied and inspired by the implementations for these methods in LiteralCommandNode

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!(obj instanceof MultiLiteralCommandNode<?> other)) return false;


		if (!nodeName.equals(other.nodeName)) return false;
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		int result = nodeName.hashCode();
		result = 31 * result + super.hashCode();
		return result;
	}

	@Override
	public MultiLiteralArgumentBuilder<Source> createBuilder() {
		MultiLiteralArgumentBuilder<Source> builder = MultiLiteralArgumentBuilder.multiLiteral(this.nodeName, getLiteral());

		builder.requires(getRequirement());
		builder.forward(getRedirect(), getRedirectModifier(), isFork());
		if (getCommand() != null) {
			builder.executes(getCommand());
		}
		return builder;
	}

	@Override
	public String toString() {
		return "<multiliteral " + nodeName + ":" + getLiteral() + ">";
	}
}
