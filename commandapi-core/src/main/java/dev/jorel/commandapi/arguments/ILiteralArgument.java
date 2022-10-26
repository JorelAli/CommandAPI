package dev.jorel.commandapi.arguments;

import dev.jorel.commandapi.IChainableBuilder;

public interface ILiteralArgument<Impl extends AbstractArgument<String, ?, ?, ?>> extends IChainableBuilder<Impl> {
	// Literal specific information

	/**
	 * Returns the literal string represented by this argument
	 *
	 * @return the literal string represented by this argument
	 */
	String getLiteral();
}
