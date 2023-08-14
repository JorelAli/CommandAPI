package dev.jorel.commandapi.arguments.parseexceptions;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.jorel.commandapi.ChainableBuilder;
import dev.jorel.commandapi.arguments.AbstractArgument;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * An interface that indicates an argument can have an {@link InitialParseExceptionHandler} attached to it.
 *
 * @param <T> The class of the object returned when the {@link ArgumentType}
 *           used by this Argument parses its raw input.
 * @param <ExceptionInformation> The class that holds information about the exception.
 * @param <Impl> The class extending this class, used as the return type in chained calls.
 */
public interface InitialParseExceptionArgument<T, ExceptionInformation, Impl extends AbstractArgument<?, Impl, ?, ?>> extends ChainableBuilder<Impl> {
    /**
     * A map that links Arguments to their ExceptionHandlers. This is basically
     * equivalent to putting one instance variable in this interface, but Java
     * doesn't let you put instance variables in interfaces, so we have to do
     * this instead if we want to provide default implementations of the methods,
     * overall avoiding the code duplication that comes from implementing these
     * methods in the inheriting classes.
     */
    // TODO: Maybe this can be a WeakHashMap, so once the Argument objects aren't being used anywhere else we can forget about
    //  them and not store them anymore. I'm not entirely sure that is what WeakHashMap does though. Are Arguments ever GC'd
    //  anyway, or do they stick around somewhere?
    Map<InitialParseExceptionArgument<?, ?, ?>, InitialParseExceptionHandler<?, ?>> exceptionHandlers = new HashMap<>();

    /**
     * Sets the {@link InitialParseExceptionHandler} this Argument should
     * use when the {@link ArgumentType} it is using fails to parse.
     *
     * @param exceptionHandler The new {@link InitialParseExceptionHandler} this argument should use
     * @return this current argument
     */
    default Impl withInitialParseExceptionHandler(InitialParseExceptionHandler<T, ExceptionInformation> exceptionHandler) {
        exceptionHandlers.put(this, exceptionHandler);
        return instance();
    }

    /**
     * Returns the {@link InitialParseExceptionHandler} this argument is using
     *
     * @return The {@link InitialParseExceptionHandler} this argument is using
     */
    default Optional<InitialParseExceptionHandler<T, ExceptionInformation>> getInitialParseExceptionHandler() {
        return Optional.ofNullable((InitialParseExceptionHandler<T, ExceptionInformation>) exceptionHandlers.get(this));
    }

    /**
     * Extracts information about an exception that was thrown when the {@link ArgumentType} for this Argument
     * failed to parse.
     *
     * @param exception The {@link CommandSyntaxException} that was thrown.
     * @param reader The {@link StringReader} that was reading this Argument.
     * @return An {@link ExceptionInformation} object that holds information about the given exception.
     */
    ExceptionInformation parseInitialParseException(CommandSyntaxException exception, StringReader reader);
}