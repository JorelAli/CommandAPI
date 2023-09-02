/*******************************************************************************
 * Copyright 2018, 2020 Jorel Ali (Skepter) - MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *******************************************************************************/
package dev.jorel.commandapi.exceptions;

import dev.jorel.commandapi.arguments.AbstractArgument;

import java.util.List;

/**
 * An exception caused when a greedy argument is not declared at the end of a List.
 */
public class GreedyArgumentException extends CommandRegistrationException {
	/**
	 * Creates a GreedyArgumentException
	 *
	 * @param previousArguments The arguments that came before the greedy argument
	 * @param argument          The greedy argument that is in an invalid spot
	 * @param followingBranches The branches following the greedy argument that weren't supposed to be there
	 * @param <Argument>        The Argument class being used
	 */
	public <Argument extends AbstractArgument<?, ?, ?, ?>> GreedyArgumentException(
		List<Argument> previousArguments, Argument argument, List<List<Argument>> followingBranches) {
		super(buildMessage(previousArguments, argument, followingBranches));
	}

	private static <Argument extends AbstractArgument<?, ?, ?, ?>> String buildMessage(
		List<Argument> previousArguments, Argument argument, List<List<Argument>> followingBranches) {
		StringBuilder builder = new StringBuilder();

		builder.append("A greedy argument must be declared at the end of a command. Going down the ");
		addArgumentList(builder, previousArguments);
		builder.append(" branch, found ");
		addArgument(builder, argument);
		builder.append(" followed by ");

		for (List<Argument> branch : followingBranches) {
			addArgumentList(builder, branch);
			builder.append(" and ");
		}

		builder.setLength(builder.length() - 5);

		return builder.toString();
	}
}
