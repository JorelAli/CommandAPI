package dev.jorel.commandapi.arguments;

import com.mojang.brigadier.arguments.ArgumentType;

/**
 * An interface that indicates this Argument wraps another Argument. For example, a CustomArgument wraps another
 * Argument, with the purpose of adding additional parsing onto it's 'base Argument'.
 * <p>
 * Note: A WrapperArgument should set its {@link ArgumentType} to that of its base Argument.
 *
 * @param <Argument> The Argument class this Argument is wrapping
 */
public interface WrapperArgument<Argument> {
    /**
     * @return The base Argument that this Argument has wrapped.
     */
    Argument getBaseArgument();
}