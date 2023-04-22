package dev.jorel.commandapi.arguments;

import dev.jorel.commandapi.ChainableBuilder;

public interface Literal<Impl extends AbstractArgument<String, ?, ?, ?>> extends ChainableBuilder<Impl> {
	// Literal specific information

	/**
	 * Returns the literal string represented by this argument
	 *
	 * @return the literal string represented by this argument
	 */
	String getLiteral();
}
