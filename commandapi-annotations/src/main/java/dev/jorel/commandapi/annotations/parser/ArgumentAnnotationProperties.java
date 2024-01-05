package dev.jorel.commandapi.annotations.parser;

import java.lang.annotation.Annotation;

import dev.jorel.commandapi.annotations.arguments.AAdvancementArgument;
import dev.jorel.commandapi.annotations.arguments.AAdventureChatArgument;
import dev.jorel.commandapi.annotations.arguments.AAdventureChatComponentArgument;
import dev.jorel.commandapi.annotations.arguments.AAngleArgument;
import dev.jorel.commandapi.annotations.arguments.AAxisArgument;
import dev.jorel.commandapi.annotations.arguments.ABiomeArgument;
import dev.jorel.commandapi.annotations.arguments.ABlockPredicateArgument;
import dev.jorel.commandapi.annotations.arguments.ABlockStateArgument;
import dev.jorel.commandapi.annotations.arguments.ABooleanArgument;
import dev.jorel.commandapi.annotations.arguments.AChatArgument;
import dev.jorel.commandapi.annotations.arguments.AChatColorArgument;
import dev.jorel.commandapi.annotations.arguments.AChatComponentArgument;
import dev.jorel.commandapi.annotations.arguments.ACustomArgument;
import dev.jorel.commandapi.annotations.arguments.ADoubleArgument;
import dev.jorel.commandapi.annotations.arguments.AEnchantmentArgument;
import dev.jorel.commandapi.annotations.arguments.AEntitySelectorArgument;
import dev.jorel.commandapi.annotations.arguments.AEntityTypeArgument;
import dev.jorel.commandapi.annotations.arguments.AFloatArgument;
import dev.jorel.commandapi.annotations.arguments.AFloatRangeArgument;
import dev.jorel.commandapi.annotations.arguments.AFunctionArgument;
import dev.jorel.commandapi.annotations.arguments.AGreedyStringArgument;
import dev.jorel.commandapi.annotations.arguments.AIntegerArgument;
import dev.jorel.commandapi.annotations.arguments.AIntegerRangeArgument;
import dev.jorel.commandapi.annotations.arguments.AItemStackArgument;
import dev.jorel.commandapi.annotations.arguments.AItemStackPredicateArgument;
import dev.jorel.commandapi.annotations.arguments.ALiteralArgument;
import dev.jorel.commandapi.annotations.arguments.ALocation2DArgument;
import dev.jorel.commandapi.annotations.arguments.ALocationArgument;
import dev.jorel.commandapi.annotations.arguments.ALongArgument;
import dev.jorel.commandapi.annotations.arguments.ALootTableArgument;
import dev.jorel.commandapi.annotations.arguments.AMathOperationArgument;
import dev.jorel.commandapi.annotations.arguments.AMultiLiteralArgument;
import dev.jorel.commandapi.annotations.arguments.ANBTCompoundArgument;
import dev.jorel.commandapi.annotations.arguments.ANamespacedKeyArgument;
import dev.jorel.commandapi.annotations.arguments.AObjectiveArgument;
import dev.jorel.commandapi.annotations.arguments.AObjectiveCriteriaArgument;
import dev.jorel.commandapi.annotations.arguments.AOfflinePlayerArgument;
import dev.jorel.commandapi.annotations.arguments.AParticleArgument;
import dev.jorel.commandapi.annotations.arguments.APlayerArgument;
import dev.jorel.commandapi.annotations.arguments.APotionEffectArgument;
import dev.jorel.commandapi.annotations.arguments.ARecipeArgument;
import dev.jorel.commandapi.annotations.arguments.ARotationArgument;
import dev.jorel.commandapi.annotations.arguments.AScoreHolderArgument;
import dev.jorel.commandapi.annotations.arguments.AScoreboardSlotArgument;
import dev.jorel.commandapi.annotations.arguments.ASoundArgument;
import dev.jorel.commandapi.annotations.arguments.AStringArgument;
import dev.jorel.commandapi.annotations.arguments.ATeamArgument;
import dev.jorel.commandapi.annotations.arguments.ATextArgument;
import dev.jorel.commandapi.annotations.arguments.ATimeArgument;
import dev.jorel.commandapi.annotations.arguments.AUUIDArgument;
import dev.jorel.commandapi.annotations.arguments.AWorldArgument;

public class ArgumentAnnotationProperties {

	private Annotation annotation;

	public ArgumentAnnotationProperties(Annotation annotation) {
		this.annotation = annotation;
	}

	public boolean optional() {
		if (annotation instanceof AAdventureChatArgument argument) {
			return argument.optional();
		} else if (annotation instanceof AFloatRangeArgument argument) {
			return argument.optional();
		} else if (annotation instanceof ADoubleArgument argument) {
			return argument.optional();
		} else if (annotation instanceof AChatColorArgument argument) {
			return argument.optional();
		} else if (annotation instanceof APotionEffectArgument argument) {
			return argument.optional();
		} else if (annotation instanceof AParticleArgument argument) {
			return argument.optional();
		} else if (annotation instanceof ATeamArgument argument) {
			return argument.optional();
		} else if (annotation instanceof AStringArgument argument) {
			return argument.optional();
		} else if (annotation instanceof AAxisArgument argument) {
			return argument.optional();
		} else if (annotation instanceof AObjectiveCriteriaArgument argument) {
			return argument.optional();
		} else if (annotation instanceof AChatComponentArgument argument) {
			return argument.optional();
		} else if (annotation instanceof AItemStackArgument argument) {
			return argument.optional();
		} else if (annotation instanceof ABlockPredicateArgument argument) {
			return argument.optional();
		} else if (annotation instanceof AMultiLiteralArgument argument) {
			return argument.optional();
		} else if (annotation instanceof AObjectiveArgument argument) {
			return argument.optional();
		} else if (annotation instanceof ARotationArgument argument) {
			return argument.optional();
		} else if (annotation instanceof ALocationArgument argument) {
			return argument.optional();
		} else if (annotation instanceof AChatArgument argument) {
			return argument.optional();
		} else if (annotation instanceof ABiomeArgument argument) {
			return argument.optional();
		} else if (annotation instanceof ATextArgument argument) {
			return argument.optional();
		} else if (annotation instanceof AIntegerArgument argument) {
			return argument.optional();
		} else if (annotation instanceof AEnchantmentArgument argument) {
			return argument.optional();
		} else if (annotation instanceof APlayerArgument argument) {
			return argument.optional();
		} else if (annotation instanceof AUUIDArgument argument) {
			return argument.optional();
		} else if (annotation instanceof ASoundArgument argument) {
			return argument.optional();
		} else if (annotation instanceof AWorldArgument argument) {
			return argument.optional();
		} else if (annotation instanceof AAngleArgument argument) {
			return argument.optional();
		} else if (annotation instanceof AEntitySelectorArgument argument) {
			return argument.optional();
		} else if (annotation instanceof ALocation2DArgument argument) {
			return argument.optional();
		} else if (annotation instanceof AAdventureChatComponentArgument argument) {
			return argument.optional();
		} else if (annotation instanceof AFloatArgument argument) {
			return argument.optional();
		} else if (annotation instanceof ARecipeArgument argument) {
			return argument.optional();
		} else if (annotation instanceof AAdvancementArgument argument) {
			return argument.optional();
		} else if (annotation instanceof AIntegerRangeArgument argument) {
			return argument.optional();
		} else if (annotation instanceof ALongArgument argument) {
			return argument.optional();
		} else if (annotation instanceof ALootTableArgument argument) {
			return argument.optional();
		} else if (annotation instanceof AScoreHolderArgument argument) {
			return argument.optional();
		} else if (annotation instanceof ALiteralArgument argument) {
			return argument.optional();
		} else if (annotation instanceof ANamespacedKeyArgument argument) {
			return argument.optional();
		} else if (annotation instanceof ANBTCompoundArgument argument) {
			return argument.optional();
		} else if (annotation instanceof AScoreboardSlotArgument argument) {
			return argument.optional();
		} else if (annotation instanceof AGreedyStringArgument argument) {
			return argument.optional();
		} else if (annotation instanceof AMathOperationArgument argument) {
			return argument.optional();
		} else if (annotation instanceof AFunctionArgument argument) {
			return argument.optional();
		} else if (annotation instanceof ABooleanArgument argument) {
			return argument.optional();
		} else if (annotation instanceof ABlockStateArgument argument) {
			return argument.optional();
		} else if (annotation instanceof ACustomArgument argument) {
			return argument.optional();
		} else if (annotation instanceof AItemStackPredicateArgument argument) {
			return argument.optional();
		} else if (annotation instanceof ATimeArgument argument) {
			return argument.optional();
		} else if (annotation instanceof AOfflinePlayerArgument argument) {
			return argument.optional();
		} else if (annotation instanceof AEntityTypeArgument argument) {
			return argument.optional();
		} else {
			return false;
		}
	}

}
