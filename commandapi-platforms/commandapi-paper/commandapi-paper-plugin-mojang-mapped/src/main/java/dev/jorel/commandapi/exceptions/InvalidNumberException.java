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

/**
 * An exception caused when a number is not a number
 */
@SuppressWarnings("serial")
public class InvalidNumberException extends Exception {
	
	/**
	 * An exception caused when a number is not a number
	 * @param input the full command input
	 * @param command the name of the command
	 * @param index the index where the number was invalid
	 */
	public InvalidNumberException(String input, String command, int index) {
		super(format(input, command, index));
	}
	
	private static String format(String input, String command, int index) {
		String[] parts = command.split(" ");
		parts[index] = parts[index] + "<--[HERE]";
		return "Invalid number found in command '" + String.join(" ", parts) + 
			"': '" + input + "' is not a valid number!";
	}
	
}
