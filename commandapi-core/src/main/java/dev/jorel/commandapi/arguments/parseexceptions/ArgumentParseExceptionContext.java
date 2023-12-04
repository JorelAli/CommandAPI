package dev.jorel.commandapi.arguments.parseexceptions;

import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.executors.CommandArguments;

/**
 * A record containing information on why an Argument failed to parse.
 *
 * @param exception The CommandSyntaxException that was thrown when the Argument failed to parse.
 *
 * @param <ExceptionInformation> The class that holds information about the exception.
 * @param exceptionInformation Extra information about the exception.
 *
 * @param <CommandSender> The CommandSender class being used.
 * @param sender The CommandSender who sent the command that caused the exception.
 *
 * @param <Raw> The class that is returned by the initial Brigadier parse for the Argument.
 * @param input The raw object returned by the initial Brigadier parse for the Argument.
 *
 * @param previousArguments A {@link CommandArguments} object holding previously declared (and parsed) arguments. This can
 * 		 be used as if it were arguments in a command executor method.
 */
public record ArgumentParseExceptionContext<Raw, ExceptionInformation, CommandSender>(
        /**
         * @param exception The CommandSyntaxException that was thrown when the Argument failed to parse.
         */
        WrapperCommandSyntaxException exception,
		/**
		 * @param exceptionInformation Extra information about the exception.
		 */
		ExceptionInformation exceptionInformation,
        /**
         * @param sender The CommandSender who sent the command that caused the exception.
         */
        CommandSender sender,
        /**
         * @param input The raw object returned by the initial Brigadier parse for the Argument.
         */
        Raw input,
        /**
         * @param previousArguments A {@link CommandArguments} object holding previously declared (and parsed) arguments.
         *                             This can be used as if it were arguments in a command executor method.
         */
        CommandArguments previousArguments) {
}