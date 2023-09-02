package dev.jorel.commandapi.arguments;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;

/**
 * A special type of {@link LiteralArgumentBuilder} for {@link MultiLiteral}. Compared to the {@link LiteralArgumentBuilder},
 * this class also has a {code nodeName} and builds a {@link MultiLiteralCommandNode}.
 *
 * @param <Source> The Brigadier Source object for running commands.
 */
public class MultiLiteralArgumentBuilder<Source> extends LiteralArgumentBuilder<Source> {
	private final String nodeName;

	/**
	 * Creates a new MultiLiteralArgumentBuilder with the given nodeName and literal.
	 *
	 * @param nodeName the string that identifies the parsed command node in the CommandContext.
	 * @param literal  the literal that identifies the built command node in the CommandDispatcher
	 */
	protected MultiLiteralArgumentBuilder(String nodeName, String literal) {
		super(literal);
		this.nodeName = nodeName;
	}

	/**
	 * A factory method to create a new builder for a {@link MultiLiteralCommandNode}.
	 *
	 * @param nodeName the string that identifies the parsed command node in the CommandContext.
	 * @param literal  the literal that identifies the built command node in the CommandDispatcher
	 * @param <Source> The Brigadier Source object for running commands.
	 * @return the created MultiLiteralArgumentBuilder.
	 */
	public static <Source> MultiLiteralArgumentBuilder<Source> multiLiteral(String nodeName, String literal) {
		return new MultiLiteralArgumentBuilder<>(nodeName, literal);
	}

	/**
	 * @return the node name that the built command node will be identified by.
	 */
	public String getNodeName() {
		return nodeName;
	}

	@Override
	public MultiLiteralCommandNode<Source> build() {
		MultiLiteralCommandNode<Source> result = new MultiLiteralCommandNode<>(getNodeName(), getLiteral(), getCommand(), getRequirement(), getRedirect(), getRedirectModifier(), isFork());

		for (CommandNode<Source> argument : getArguments()) {
			result.addChild(argument);
		}

		return result;
	}
}
