package dev.jorel.commandapi.annotations.arguments;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Primitive("net.md_5.bungee.api.chat.BaseComponent[]")
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.PARAMETER)
public @interface ChatArgumentA {		
}
