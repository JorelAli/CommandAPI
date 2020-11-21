package dev.jorel.commandapi.annotations.arguments;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import dev.jorel.commandapi.arguments.EntitySelectorArgument.EntitySelector;

@Primitive({ "java.util.Collection<org.bukkit.entity.Entity>", // MANY_ENTITIES
		"java.util.Collection<org.bukkit.entity.Player>", // MANY_PLAYERS
		"org.bukkit.entity.ENTITY", // ONE_ENTITY
		"org.bukkit.entity.Player" // ONE_PLAYER
})
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.PARAMETER)
public @interface EntitySelectorArgumentA {
	
	EntitySelector value() default EntitySelector.ONE_ENTITY;
	
}
