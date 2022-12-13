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

/**
 * An exception caused when a greedy argument is not declared at the end of a
 * List
 */
@SuppressWarnings("serial")
public class GreedyArgumentException extends RuntimeException {
	/**
	 * Creates a GreedyArgumentException
	 * 
	 * @param arguments the list of arguments that have been used for this command
	 *                  (including the greedy string argument)
	 */
	public GreedyArgumentException(AbstractArgument<?, ?, ?, ?>[] arguments) {
		super("Only one GreedyStringArgument or ChatArgument can be declared, at the end of a List. Found arguments: "
				+ buildArgsStr(arguments));
	}

	private static String buildArgsStr(AbstractArgument<?, ?, ?, ?>[] arguments) {
		StringBuilder builder = new StringBuilder();
		for (AbstractArgument<?, ?, ?, ?> arg : arguments) {
			builder.append(arg.getNodeName()).append("<").append(arg.getClass().getSimpleName()).append("> ");
		}
		return builder.toString();
	}
}
