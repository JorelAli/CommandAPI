package dev.jorel.commandapi.annotations.arguments;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import dev.jorel.commandapi.arguments.ScoreHolderArgument.ScoreHolderType;

@Primitive({ "java.util.Collection<String>", // ScoreHolderType.MULTIPLE
		"String" // ScoreHolderType.SINGLE
})
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.PARAMETER)
public @interface AScoreHolderArgument {

	ScoreHolderType value() default ScoreHolderType.SINGLE;

}
