package dev.jorel.commandapi.annotations.arguments;

import dev.jorel.commandapi.arguments.MapArgument;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation equivalent of the {@link MapArgument}
 */
@Primitive("java.util.HashMap")
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.PARAMETER)
public @interface AMapArgument {
}
