package dev.jorel.commandapi.arguments;

import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;

/**
 * A FunctionalInterface for defining custom behavior when an Argument fails to parse.
 * See {@link ArgumentParseExceptionHandler#handleException(ArgumentParseExceptionContext)}.
 *
 * @param <T> The class of the object that can be substituted instead of an exception when the Argument fails to parse.
 * @param <Raw> The class of the object returned by the initial Brigadier parse for the Argument.
 * @param <ExceptionInformation> The class that holds information about the exception.
 * @param <CommandSender> The CommandSender class being used.
 */
@FunctionalInterface
public interface ArgumentParseExceptionHandler<T, Raw, ExceptionInformation, CommandSender> {
    /**
     * A method that handles when an Argument fails to parse.
     * It can either return an object or throw a different exception.
     *
     * @param context a {@link ArgumentParseExceptionContext} record that holds information
     *                   about why and when the Argument failed to parse
     * @return A new object in place of the failed parse
     * @throws WrapperCommandSyntaxException A new exception to pass on
     */
    T handleException(ArgumentParseExceptionContext<Raw, ExceptionInformation, CommandSender> context) throws WrapperCommandSyntaxException;
}