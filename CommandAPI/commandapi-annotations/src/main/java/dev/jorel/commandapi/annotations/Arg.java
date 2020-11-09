package dev.jorel.commandapi.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import dev.jorel.commandapi.arguments.Argument;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
@Repeatable(Arguments.class)
public @interface Arg {
	
	String name();
	
	Class<? extends Argument> type();
}
