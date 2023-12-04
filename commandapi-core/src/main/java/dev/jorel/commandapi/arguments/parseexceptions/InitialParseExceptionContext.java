package dev.jorel.commandapi.arguments.parseexceptions;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.wrappers.WrapperStringReader;

/**
 * A record containing information on why a Brigadier {@link ArgumentType} failed to parse its raw input.
 *
 * @param exception The {@link CommandSyntaxException} that was thrown when the ArgumentType failed to parse.
 *
 * @param <ExceptionInformation> The class that holds information about the exception.
 * @param exceptionInformation Extra information about the exception.
 *
 * @param stringReader The {@link StringReader} used for reading in the command.
 *                     The cursor will be at the beginning of the argument.
 * @param cursorStart The location the {@link StringReader}'s cursor was at when the ArgumentType started its parse.
 */
public record InitialParseExceptionContext<ExceptionInformation>(
        /**
         * @param exception The {@link CommandSyntaxException} that was thrown when the ArgumentType failed to parse.
         */
        WrapperCommandSyntaxException exception,
		/**
		 * @param exceptionInformation Extra information about the exception.
		 */
		ExceptionInformation exceptionInformation,
        /**
         * @param stringReader The {@link StringReader} used for reading in the command.
         *                     The cursor will be at the beginning of the argument.
         */
        WrapperStringReader stringReader,
		/**
		 * @param cursorStart The location the {@link StringReader}'s cursor was at when the ArgumentType started its parse.
		 */
		int cursorStart) {
}