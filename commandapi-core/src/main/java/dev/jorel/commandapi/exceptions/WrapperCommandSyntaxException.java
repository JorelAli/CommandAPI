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

import java.io.PrintStream;
import java.io.PrintWriter;

import com.mojang.brigadier.Message;
import com.mojang.brigadier.exceptions.CommandExceptionType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

/**
 * A wrapper for the CommandSyntaxException so other developers don't have to
 * import Mojang's brigadier
 */
@SuppressWarnings("serial")
public class WrapperCommandSyntaxException extends Exception {

	/**
	 * The Brigadier CommandSyntaxException that this class wraps
	 */
	private final CommandSyntaxException exception;

	/**
	 * Creates a WrapperCommandSyntaxException
	 * 
	 * @param exception the exception to wrap
	 */
	public WrapperCommandSyntaxException(CommandSyntaxException exception) {
		this.exception = exception;
	}

	/**
	 * Returns the wrapped CommandSyntaxException
	 * 
	 * @return the wrapped CommandSyntaxException
	 */
	public CommandSyntaxException getException() {
		return this.exception;
	}

	// Overrides. Because this exception is effectively a CommandSyntaxException, we
	// pass all Exception-related methods to this.exception

	/**
	 * Returns the message together with the position it occurred on and some
	 * context.
	 *
	 * @return the message together with the position it occurred on and some
	 *         context
	 */
	@Override
	public String getMessage() {
		return this.exception.getMessage();
	}

	@Override
	public synchronized Throwable getCause() {
		return this.exception.getCause();
	}

	@Override
	public StackTraceElement[] getStackTrace() {
		return this.exception.getStackTrace();
	}

	@Override
	public synchronized Throwable initCause(Throwable cause) {
		return this.exception.initCause(cause);
	}

	@Override
	public String getLocalizedMessage() {
		return this.exception.getLocalizedMessage();
	}

	@Override
	public void printStackTrace() {
		this.exception.printStackTrace();
	}

	@Override
	public void printStackTrace(PrintStream s) {
		this.exception.printStackTrace(s);
	}

	@Override
	public void printStackTrace(PrintWriter s) {
		this.exception.printStackTrace(s);
	}

	@Override
	public void setStackTrace(StackTraceElement[] stackTrace) {
		this.exception.setStackTrace(stackTrace);
	}

	// CommandSyntaxException implemented methods

	/**
	 * Returns the raw message, not including positional information
	 *
	 * @return the raw message without any formatting or positional information
	 */
	public Message getRawMessage() {
		return this.exception.getRawMessage();
	}

	/**
	 * Returns some contextual information about where the error occurred.
	 * <p>
	 * This is done by returning a few characters
	 * ({@value CommandSyntaxException#CONTEXT_AMOUNT}) of the faulty
	 * {@link #getInput()} and a pointer to where the exception happened.
	 *
	 * @return some contextual information about where the error occurred or null if
	 *         {@link #getInput()} or {@link #getCursor()} are null/0.
	 */
	public String getContext() {
		return this.exception.getContext();
	}

	/**
	 * Returns the type of the exception.
	 *
	 * @return the type of the exception
	 */
	public CommandExceptionType getType() {
		return this.exception.getType();
	}

	/**
	 * Returns the input that caused the CommandSyntaxException.
	 *
	 * @return the input that caused the CommandSyntaxException or null if not set
	 */
	public String getInput() {
		return this.exception.getInput();
	}

	/**
	 * Returns the cursor position.
	 *
	 * @return the cursor position or -1 if not set
	 */
	public int getCursor() {
		return this.exception.getCursor();
	}

}
