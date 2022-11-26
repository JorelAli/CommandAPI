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

	/**
	 * Sets the {@link InitialParseExceptionHandler} this Argument should
	 * use when the {@link ArgumentType} it is using fails to parse.
	 *
	 * @param exceptionHandler The new {@link InitialParseExceptionHandler} this argument should use
	 * @return this current argument
	 */
	Impl withInitialParseExceptionHandler(InitialParseExceptionHandler<T> exceptionHandler);

	/**
	 * Returns the {@link InitialParseExceptionHandler} this argument is using
	 * @return The {@link InitialParseExceptionHandler} this argument is using
	 */
	Optional<InitialParseExceptionHandler<T>> getInitialParseExceptionHandler();
}
