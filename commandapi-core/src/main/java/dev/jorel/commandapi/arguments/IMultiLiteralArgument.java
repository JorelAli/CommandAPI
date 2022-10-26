package dev.jorel.commandapi.arguments;

import dev.jorel.commandapi.IChainableBuilder;

public interface IMultiLiteralArgument<Impl extends AbstractArgument<String, ?, ?, ?>> extends IChainableBuilder<Impl> {
	// MultiLiteral specific information

	/**
	 * Returns the literals that are present in this argument
	 *
	 * @return the literals that are present in this argument
	 */
	String[] getLiterals();
}
