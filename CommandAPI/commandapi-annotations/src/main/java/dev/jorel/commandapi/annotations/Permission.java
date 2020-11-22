package dev.jorel.commandapi.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The annotation to apply a permission to a command or subcommand
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.SOURCE)
public @interface Permission {

	/**
	 * The permission literal that this argument represents
	 * @return the permission literal that this argument represents
	 */
	public String value();
	
}
