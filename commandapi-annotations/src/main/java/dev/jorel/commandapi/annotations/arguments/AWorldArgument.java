package dev.jorel.commandapi.annotations.arguments;

import dev.jorel.commandapi.arguments.WorldArgument;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation equivalent of the {@link WorldArgument}
 */
@Primitive("org.bukkit.World")
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.PARAMETER)
public @interface AWorldArgument {
}
