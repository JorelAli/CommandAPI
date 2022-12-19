package dev.jorel.commandapi.arguments;

import com.mojang.brigadier.arguments.ArgumentType;
import dev.jorel.commandapi.ChainableBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * An interface indicating that an argument can have an
 * {@link InitialParseExceptionHandler} attached to it.
 * @param <T> The class of the object returned when the {@link ArgumentType}
 *           used by this Argument parses its raw input
 */
public interface InitialParseExceptionArgument<T, Impl extends AbstractArgument<?, Impl, ?, ?>> extends ChainableBuilder<Impl> {
	/**
	 * A map that links Arguments to their ExceptionHandlers. This is basically
	 * equivalent to putting one instance variable in this interface, but Java
	 * doesn't let you put instance variables in interfaces, so we have to do
	 * this instead if we want to provide default implementations of the methods,
	 * overall avoiding the code duplication that comes from implementing these
	 * methods in the inheriting classes.
	 */
	Map<InitialParseExceptionArgument<?, ?>, InitialParseExceptionHandler<?>> exceptionHandlers = new HashMap<>();

	/**
	 * Sets the {@link InitialParseExceptionHandler} this Argument should
	 * use when the {@link ArgumentType} it is using fails to parse.
	 *
	 * @param exceptionHandler The new {@link InitialParseExceptionHandler} this argument should use
	 * @return this current argument
	 */
	default Impl withInitialParseExceptionHandler(InitialParseExceptionHandler<T> exceptionHandler) {
		exceptionHandlers.put(this, exceptionHandler);
		return instance();
	}

	/**
	 * Returns the {@link InitialParseExceptionHandler} this argument is using
	 * @return The {@link InitialParseExceptionHandler} this argument is using
	 */
	default Optional<InitialParseExceptionHandler<T>> getInitialParseExceptionHandler() {
		return Optional.ofNullable((InitialParseExceptionHandler<T>) exceptionHandlers.get(this));
	}
}
