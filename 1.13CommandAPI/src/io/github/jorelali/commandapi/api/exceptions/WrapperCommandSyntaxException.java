package io.github.jorelali.commandapi.api.exceptions;

import com.mojang.brigadier.exceptions.CommandSyntaxException;

public class WrapperCommandSyntaxException extends Exception {

	private static final long serialVersionUID = -7854667406738356818L;
	
	private CommandSyntaxException exception;
	
	public WrapperCommandSyntaxException(CommandSyntaxException exception) {
		this.exception = exception;
	}
	
	public CommandSyntaxException getException() {
		return this.exception;
	}

}
