package dev.jorel.commandapi.arguments.parseexceptions;

import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;

/**
 * A FunctionalInterface for defining custom behavior when a Brigadier ArgumentType fails to parse.
 * See {@link InitialParseExceptionHandler#handleException(InitialParseExceptionContext)}.
 *
 * @param <T> The class of the object returned by the ArgumentType this object is handling.
 * @param <ExceptionInformation> The class that holds information about the exception.
 */
@FunctionalInterface
public interface InitialParseExceptionHandler<T, ExceptionInformation> {
    /**
     * A method that handles when a Brigadier ArgumentType fails to parse.
     * It can either return an object or throw a different exception.
     *
     * @param context a {@link InitialParseExceptionContext} record that holds information
     *                   about why and when the Argument failed to parse
     * @return A new object in place of the failed parse
     * @throws WrapperCommandSyntaxException A new exception to pass on
     */
    T handleException(InitialParseExceptionContext<ExceptionInformation> context) throws WrapperCommandSyntaxException;
}