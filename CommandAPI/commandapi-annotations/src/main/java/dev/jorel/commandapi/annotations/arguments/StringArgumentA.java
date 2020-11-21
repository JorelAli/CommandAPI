package dev.jorel.commandapi.annotations.arguments;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.PARAMETER)
public @interface StringArgumentA {
	
	static class Metadata implements ArgumentMetadata {
		@Override
		public Class<?> getPrimitive() {
			return String.class;
		}
	}
		
}
