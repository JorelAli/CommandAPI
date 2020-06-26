package dev.jorel.commandapi.exceptions;

import com.mojang.brigadier.exceptions.CommandSyntaxException;

/**
 * A wrapper for the CommandSyntaxException so other developers don't have to import Mojang's brigadier
 */
@SuppressWarnings("serial")
public class WrapperCommandSyntaxException extends Exception {
	
	private CommandSyntaxException exception;
	
	/**
	 * Creates a WrapperCommandSyntaxException
	 * @param exception the exception to wrap
	 */
	public WrapperCommandSyntaxException(CommandSyntaxException exception) {
		this.exception = exception;
	}
	
	/**
	 * Returns the wrapped CommandSyntaxException
	 * @return the wrapped CommandSyntaxException
	 */
	public CommandSyntaxException getException() {
		return this.exception;
	}

}
