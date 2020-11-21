package dev.jorel.commandapi.annotations.arguments;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.PARAMETER)
@Primitive("int")
public @interface IntegerArgumentA {
	
	public int min() default Integer.MIN_VALUE;
	public int max() default Integer.MAX_VALUE;
	
}