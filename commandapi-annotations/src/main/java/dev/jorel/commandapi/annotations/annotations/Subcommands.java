package dev.jorel.commandapi.annotations.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE)
@Target( { ElementType.METHOD, ElementType.TYPE })
public @interface Subcommands {

	Subcommand[] value();

}
