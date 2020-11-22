package dev.jorel.commandapi.annotations.arguments;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import dev.jorel.commandapi.arguments.LocationType;

@Primitive("dev.jorel.commandapi.wrappers.Location2D")
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.PARAMETER)
public @interface ALocation2DArgument {
	
	LocationType value() default LocationType.PRECISE_POSITION;
	
}
