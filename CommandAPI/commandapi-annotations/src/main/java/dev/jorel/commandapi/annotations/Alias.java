package dev.jorel.commandapi.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The annotation to represent the aliases for a given base command.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface Alias {
	
	/**
	 * The list of aliases to apply to the base command
	 * @return the list of aliases to apply to the base command
	 */
	public String[] value();
	
}
