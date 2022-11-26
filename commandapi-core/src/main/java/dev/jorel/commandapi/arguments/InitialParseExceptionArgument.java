package dev.jorel.commandapi.arguments;

import com.mojang.brigadier.arguments.ArgumentType;

import java.util.Optional;

/**
 * An interface indicating that an argument can have an
 * {@link InitialParseExceptionHandler} attached to it.
 * @param <T> The class of the object returned when the {@link ArgumentType}
 *           used by this Argument parses its raw input
 */
public interface InitialParseExceptionArgument<T, Impl extends Argument<?>> {
	Impl withInitialParseExceptionHandler(InitialParseExceptionHandler<T> exceptionHandler);

	Optional<InitialParseExceptionHandler<T>> getInitialParseExceptionHandler();
}
