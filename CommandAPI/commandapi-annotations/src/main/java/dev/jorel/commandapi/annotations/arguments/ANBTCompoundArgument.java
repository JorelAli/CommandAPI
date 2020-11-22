package dev.jorel.commandapi.annotations.arguments;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Primitive("de.tr7zw.nbtapi.NBTContainer")
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.PARAMETER)
public @interface ANBTCompoundArgument {
}
