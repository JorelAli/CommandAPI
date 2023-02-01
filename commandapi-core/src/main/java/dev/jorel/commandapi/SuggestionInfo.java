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
package dev.jorel.commandapi;

import java.util.Arrays;
import java.util.Objects;

/**
 * A class that represents information which you can use to generate
 * suggestions.
 * 
 * @param sender       - the CommandSender typing this command
 * @param previousArgs - the list of previously declared (and parsed) arguments
 * @param currentInput - a string representing the full current input (including
 *                     /)
 * @param currentArg   - the current partially typed argument. For example
 *                     "/mycmd tes" will return "tes"
 */
public record SuggestionInfo<CommandSender>(
	/** @param sender - the CommandSender typing this command */
	CommandSender sender,

	/**
	 * @param previousArgs - the list of previously declared (and parsed) arguments
	 */
	Object[] previousArgs,

	/**
	 * @param currentInput - a string representing the full current input (including
	 *                     /)
	 */
	String currentInput,

	/**
	 * @param currentArg - the current partially typed argument. For example "/mycmd
	 *                   tes" will return "tes"
	 */
	String currentArg) {

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.deepHashCode(previousArgs);
		result = prime * result + Objects.hash(currentArg, currentInput, sender);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof SuggestionInfo)) {
			return false;
		}
		@SuppressWarnings("rawtypes")
		SuggestionInfo other = (SuggestionInfo) obj;
		return Objects.equals(currentArg, other.currentArg) && Objects.equals(currentInput, other.currentInput) && Arrays.deepEquals(previousArgs, other.previousArgs)
			&& Objects.equals(sender, other.sender);
	}

	@Override
	public String toString() {
		return "SuggestionInfo [sender=" + sender + ", previousArgs=" + Arrays.toString(previousArgs) + ", currentInput=" + currentInput + ", currentArg=" + currentArg + "]";
	}

}
