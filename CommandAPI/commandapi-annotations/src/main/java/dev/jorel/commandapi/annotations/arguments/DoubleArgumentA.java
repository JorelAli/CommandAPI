package dev.jorel.commandapi.annotations.arguments;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Primitive("double")
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.PARAMETER)
public @interface DoubleArgumentA {
	
	public double min() default -Double.MAX_VALUE;
	public double max() default Double.MAX_VALUE;
	
}
