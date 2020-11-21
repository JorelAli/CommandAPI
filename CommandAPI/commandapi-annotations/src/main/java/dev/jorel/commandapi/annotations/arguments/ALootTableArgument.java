package dev.jorel.commandapi.annotations.arguments;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Primitive("org.bukkit.loot.LootTable")
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.PARAMETER)
public @interface ALootTableArgument {
}
