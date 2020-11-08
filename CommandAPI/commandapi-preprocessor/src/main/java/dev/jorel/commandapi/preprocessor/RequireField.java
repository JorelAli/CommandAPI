package dev.jorel.commandapi.preprocessor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
@Repeatable(RequireFields.class)
public @interface RequireField {
	
	/**
	 * The target class where a field is declared
	 */
	Class<?> in();     //e.g. MinecraftServer.class
	
	/**
	 * The name of the field
	 */
	String name();         //e.g. "a"
	
	/**
	 * The type for a field
	 */
	Class<?> ofType(); //e.g. String.class
	
}