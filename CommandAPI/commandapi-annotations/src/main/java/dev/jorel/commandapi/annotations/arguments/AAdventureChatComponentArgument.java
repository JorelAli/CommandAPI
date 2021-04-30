package dev.jorel.commandapi.annotations.arguments;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Primitive("net.kyori.adventure.text.Component")
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.PARAMETER)
public @interface AAdventureChatComponentArgument {		
}
