package dev.jorel.commandapi.annotations.arguments;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.PARAMETER)
public @interface IntegerArgumentA {
	
	class Metadata {
		static int x = 2;
	}
	
	public int min() default Integer.MIN_VALUE;
	public int max() default Integer.MAX_VALUE;
	
}