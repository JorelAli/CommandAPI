package dev.jorel.commandapi.annotations.arguments;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.PARAMETER)
@Primitive("long")
public @interface LongArgumentA {
	
	public long min() default Long.MIN_VALUE;
	public long max() default Long.MAX_VALUE;
	
}