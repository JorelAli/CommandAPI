package dev.jorel.commandapi.arguments;

import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;

@FunctionalInterface
public interface ArgumentParseExceptionHandler<T> {
	T handleException(ArgumentParseExceptionContext context) throws WrapperCommandSyntaxException;
}
