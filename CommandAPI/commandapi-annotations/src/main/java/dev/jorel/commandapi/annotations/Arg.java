package dev.jorel.commandapi.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.EntitySelectorArgument.EntitySelector;
import dev.jorel.commandapi.arguments.LocationType;
import dev.jorel.commandapi.arguments.ScoreHolderArgument.ScoreHolderType;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
@Repeatable(Args.class)

/**
 * An argument.
 * For ScoreHolderArgument, defaults to using ScoreHolderType.SINGLE
 */
public @interface Arg {
	
	String name();
	
	Class<? extends Argument> type();
	
	LocationType locationType() default LocationType.PRECISE_POSITION;
	
	ScoreHolderType scoreHolderType() default ScoreHolderType.SINGLE;
	
	EntitySelector entityType() default EntitySelector.ONE_ENTITY;
}
