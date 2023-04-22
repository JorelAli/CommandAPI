package dev.jorel.commandapi.arguments;

import dev.jorel.commandapi.ChainableBuilder;

public interface MultiLiteral<Impl extends AbstractArgument<String, ?, ?, ?>> extends ChainableBuilder<Impl> {
	// MultiLiteral specific information

	/**
	 * Returns the literals that are present in this argument
	 *
	 * @return the literals that are present in this argument
	 */
	String[] getLiterals();
}
