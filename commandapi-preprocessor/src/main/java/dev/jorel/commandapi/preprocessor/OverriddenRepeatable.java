package dev.jorel.commandapi.preprocessor;

import java.lang.annotation.*;

/**
 * The repeatable version of {@link Overridden}
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
public @interface OverriddenRepeatable {
	Overridden[] value();
}
