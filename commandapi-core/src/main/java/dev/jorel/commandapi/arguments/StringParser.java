package dev.jorel.commandapi.arguments;

import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;

/**
 * A class that represents a function that parses a String into another object. See {@link StringParser#parse(String)}
 *
 * @param <T> The class that this {@link StringParser} turns strings into.
 */
@FunctionalInterface
public interface StringParser<T> {
	/**
	 * A method that turns a String into an object of type T.
	 *
	 * @param s The String to parse
	 * @return The resulting parsed object
	 * @throws WrapperCommandSyntaxException If there is a problem with the syntax of the String that prevents it from being turned into an object of type T.
	 */
	T parse(String s) throws WrapperCommandSyntaxException;
}
