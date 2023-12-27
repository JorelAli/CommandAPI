package dev.jorel.commandapi.arguments;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.jorel.commandapi.commandnodes.NamedLiteralArgumentBuilder;

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

	///////////////////////////////////////////////////////////////////////////////////////////////////////
	// LINKED METHODS                                                                                    //
	//  These automatically link to methods in AbstractArgument (make sure they have the same signature) //
	///////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Links to {@link AbstractArgument#getNodeName()}.
	 */
	String getNodeName();

	/**
	 * Links to {@link AbstractArgument#isListed()}.
	 */
	boolean isListed();

	///////////////////////////////////////////////////////////////////////////////////////////////
	// OVERRIDING METHODS                                                                        //
	//  A Literal has special logic that should override the implementations in AbstractArgument //
	///////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Overrides {@link AbstractArgument#createArgumentBuilder(List, List)}.
	 * <p>
	 * Normally, Arguments will use Brigadier's RequiredArgumentBuilder. However, Literals use LiteralArgumentBuilders.
	 * Arguments also usually add their name to the {@code previousArgumentNames} list here, but Literals only do
	 * this if they are listed.
	 */
	default <Source> ArgumentBuilder<Source, ?> createArgumentBuilder(List<Argument> previousArguments, List<String> previousArgumentNames) {
		String nodeName = getNodeName();
		String literal = getLiteral();

		previousArguments.add((Argument) this);
		if(isListed()) previousArgumentNames.add(nodeName);

		// If we are listed, use a NamedLiteralArgumentBuilder to put our literal into the CommandContext
		return isListed() ?
			NamedLiteralArgumentBuilder.namedLiteral(nodeName, literal) :
			LiteralArgumentBuilder.literal(literal);
	}
}
