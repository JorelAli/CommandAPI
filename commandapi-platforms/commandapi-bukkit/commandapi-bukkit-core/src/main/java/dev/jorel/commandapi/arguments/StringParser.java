package dev.jorel.commandapi.arguments;

import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;

@FunctionalInterface
public interface StringParser<T> {
	T parse(String s) throws WrapperCommandSyntaxException;
}
