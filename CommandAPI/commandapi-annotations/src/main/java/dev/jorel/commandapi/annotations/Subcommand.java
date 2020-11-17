package dev.jorel.commandapi.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The annotation to indicate that this method is a subcommand
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
public @interface Subcommand {

	/**
	 * The names (and thus, aliases) of this subcommand
	 * @return the names that this subcommand produces
	 */
	public String[] value();
	
}
