package dev.jorel.commandapi.arguments;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import java.util.List;

/**
 * An interface representing literal-based arguments
 */
public interface Literal<Argument
/// @cond DOX
extends AbstractArgument<?, ?, Argument, CommandSender>
/// @endcond
, CommandSender> {
	// Literal specific information

	/**
	 * Returns the literal string represented by this argument
	 *
	 * @return the literal string represented by this argument
	 */
	String getLiteral();

	///////////////////////////////////////////////////////////////////////////////////////////////
	// OVERRIDING METHODS                                                                        //
	//  A Literal has special logic that should override the implementations in AbstractArgument //
	///////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Overrides {@link AbstractArgument#checkPreconditions(List, List)}.
	 * <p>
	 * Normally, Arguments cannot be built if their node name is found in {@code previousNonLiteralArgumentNames} list.
	 * However, Literals do not have this problem, so we want to skip that check.
	 */
	default void checkPreconditions(List<Argument> previousArguments, List<String> previousNonLiteralArgumentNames) {

	}

	/**
	 * Overrides {@link AbstractArgument#createArgumentBuilder(List, List)}.
	 * <p>
	 * Normally, Arguments will use Brigadier's RequiredArgumentBuilder. However, Literals use LiteralArgumentBuilders.
	 * Arguments also usually add their name to the {@code previousNonLiteralArgumentNames} list, but literal node names
	 * do not conflict with required argument node names.
	 */
	default <Source> ArgumentBuilder<Source, ?> createArgumentBuilder(List<Argument> previousArguments, List<String> previousNonLiteralArgumentNames) {
		previousArguments.add((Argument) this);

		return LiteralArgumentBuilder.literal(getLiteral());
	}
}
