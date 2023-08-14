package dev.jorel.commandapi.arguments;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.jorel.commandapi.CommandAPIBukkit;

import java.util.Map;

/**
 * An interface that can be used to extract an exception type from the translation keys of a defined set of
 * {@link CommandSyntaxException}s generated when a Brigadier initial parse failed.
 *
 * @param <ExceptionTypes> The object that encodes the exception's type
 */
public interface InitialParseExceptionTranslationKeyExtractor<ExceptionTypes> {
    /**
     * @param exception The exception to get the type of
     * @return The type of the given exception as defined by {@link #keyToExceptionTypeMap()}
     */
    default ExceptionTypes getExceptionType(CommandSyntaxException exception) {
        String key = CommandAPIBukkit.get().extractTranslationKey(exception);
        if (key == null) {
            throw new IllegalStateException("Unexpected null translation key for " + getClass().getSimpleName() + " initial parse", exception);
        }

        ExceptionTypes type = keyToExceptionTypeMap().get(key);
        if (type == null) {
            throw new IllegalStateException("Unexpected translation key for " + getClass().getSimpleName() + " initial parse: " + key, exception);
        }

        return type;
    }

    /**
     * @return A map from String translation keys to exception type.
     */
    Map<String, ExceptionTypes> keyToExceptionTypeMap();
}
