package dev.jorel.commandapi.arguments;

import dev.jorel.commandapi.ChainableBuilder;

/**
 * An interface representing literal-based arguments
 */
public interface Literal<Impl
/// @cond DOX
extends AbstractArgument<String, ?, ?, ?>
/// @endcond
> extends ChainableBuilder<Impl> {
	// Literal specific information

	/**
	 * Returns the literal string represented by this argument
	 *
	 * @return the literal string represented by this argument
	 */
	String getLiteral();
}
