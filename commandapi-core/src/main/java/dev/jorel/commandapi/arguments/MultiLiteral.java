package dev.jorel.commandapi.arguments;

import dev.jorel.commandapi.ChainableBuilder;

/**
 * An interface representing arguments with multiple literal string definitions
 */
public interface MultiLiteral<Impl
/// @cond DOX
extends AbstractArgument<String, ?, ?, ?>
/// @endcond
> extends ChainableBuilder<Impl> {
	// MultiLiteral specific information

	/**
	 * Returns the literals that are present in this argument
	 *
	 * @return the literals that are present in this argument
	 */
	String[] getLiterals();
}
