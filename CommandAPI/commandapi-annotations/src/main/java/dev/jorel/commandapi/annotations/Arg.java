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

/**
 * The annotation to use to represent an argument
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
@Repeatable(Args.class)
public @interface Arg {
	
	/**
	 * The node name of this argument
	 * @return the node name of this argument
	 */
	public String name();
	
	/**
	 * The type of this argument. The type must be an Argument class.
	 * @return the underlying base Argument
	 */
	public Class<? extends Argument> type();
	
	/**
	 * The location type that this argument represents. By default, this is
	 * LocationType.PRECISE_POSITION.
	 * 
	 * @return the location type that this argument represents.
	 */
	public LocationType locationType() default LocationType.PRECISE_POSITION;
	
	/**
	 * The scoreholder type that this argument represents. By default, this
	 * is ScoreHolderType.SINGLE.
	 * @return the scoreholder type that this argument represents.
	 */
	public ScoreHolderType scoreHolderType() default ScoreHolderType.SINGLE;
	
	/**
	 * The entity selector that this argument represents. By default, this
	 * is EntitySelector.ONE_ENTITY.
	 * @return the entity selector that this argument represents.
	 */
	public EntitySelector entityType() default EntitySelector.ONE_ENTITY;
}
