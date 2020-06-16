package dev.jorel.commandapi.exceptions;

import com.mojang.brigadier.exceptions.CommandSyntaxException;

@SuppressWarnings("serial")
public class WrapperCommandSyntaxException extends Exception {
	
	private CommandSyntaxException exception;
	
	public WrapperCommandSyntaxException(CommandSyntaxException exception) {
		this.exception = exception;
	}
	
	public CommandSyntaxException getException() {
		return this.exception;
	}

}
