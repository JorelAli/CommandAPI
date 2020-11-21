package dev.jorel.commandapi.annotations.arguments;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Primitive("java.util.EnumSet<org.bukkit.Axis>")
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.PARAMETER)
public @interface AAxisArgument {		
}
