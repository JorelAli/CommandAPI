/*******************************************************************************
 * Copyright 2018, 2020 Jorel Ali (Skepter) - MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *******************************************************************************/
package dev.jorel.commandapi.annotations.reloaded.arguments;

import dev.jorel.commandapi.annotations.reloaded.arguments.utils.ArgumentAnnotation;
import dev.jorel.commandapi.annotations.reloaded.arguments.utils.ArgumentAnnotationProperties;
import dev.jorel.commandapi.annotations.reloaded.arguments.utils.ArgumentListedOption;
import dev.jorel.commandapi.annotations.reloaded.arguments.utils.CustomArgumentAnnotationProperties;
import dev.jorel.commandapi.annotations.reloaded.arguments.utils.DoubleArgumentAnnotationProperties;
import dev.jorel.commandapi.annotations.reloaded.arguments.utils.FloatArgumentAnnotationProperties;
import dev.jorel.commandapi.annotations.reloaded.arguments.utils.IntegerArgumentAnnotationProperties;
import dev.jorel.commandapi.annotations.reloaded.arguments.utils.LocationArgumentAnnotationProperties;
import dev.jorel.commandapi.annotations.reloaded.arguments.utils.LongArgumentAnnotationProperties;
import dev.jorel.commandapi.annotations.reloaded.arguments.utils.MultiLiteralArgumentAnnotationProperties;
import dev.jorel.commandapi.annotations.reloaded.arguments.utils.PrimitiveType;
import dev.jorel.commandapi.arguments.AdvancementArgument;
import dev.jorel.commandapi.arguments.AdventureChatArgument;
import dev.jorel.commandapi.arguments.AdventureChatComponentArgument;
import dev.jorel.commandapi.arguments.AngleArgument;
import dev.jorel.commandapi.arguments.AxisArgument;
import dev.jorel.commandapi.arguments.BiomeArgument;
import dev.jorel.commandapi.arguments.BlockPredicateArgument;
import dev.jorel.commandapi.arguments.BlockStateArgument;
import dev.jorel.commandapi.arguments.BooleanArgument;
import dev.jorel.commandapi.arguments.ChatArgument;
import dev.jorel.commandapi.arguments.ChatColorArgument;
import dev.jorel.commandapi.arguments.ChatComponentArgument;
import dev.jorel.commandapi.arguments.CustomArgument;
import dev.jorel.commandapi.arguments.DoubleArgument;
import dev.jorel.commandapi.arguments.EnchantmentArgument;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.EntityTypeArgument;
import dev.jorel.commandapi.arguments.FloatArgument;
import dev.jorel.commandapi.arguments.FloatRangeArgument;
import dev.jorel.commandapi.arguments.FunctionArgument;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.IntegerRangeArgument;
import dev.jorel.commandapi.arguments.ItemStackArgument;
import dev.jorel.commandapi.arguments.ItemStackPredicateArgument;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.arguments.Location2DArgument;
import dev.jorel.commandapi.arguments.LocationArgument;
import dev.jorel.commandapi.arguments.LongArgument;
import dev.jorel.commandapi.arguments.LootTableArgument;
import dev.jorel.commandapi.arguments.MathOperationArgument;
import dev.jorel.commandapi.arguments.MultiLiteralArgument;
import dev.jorel.commandapi.arguments.NBTCompoundArgument;
import dev.jorel.commandapi.arguments.NamespacedKeyArgument;
import dev.jorel.commandapi.arguments.ObjectiveArgument;
import dev.jorel.commandapi.arguments.ObjectiveCriteriaArgument;
import dev.jorel.commandapi.arguments.OfflinePlayerArgument;
import dev.jorel.commandapi.arguments.ParticleArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;
import dev.jorel.commandapi.arguments.PotionEffectArgument;
import dev.jorel.commandapi.arguments.RecipeArgument;
import dev.jorel.commandapi.arguments.RotationArgument;
import dev.jorel.commandapi.arguments.ScoreHolderArgument;
import dev.jorel.commandapi.arguments.ScoreboardSlotArgument;
import dev.jorel.commandapi.arguments.SoundArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import dev.jorel.commandapi.arguments.TeamArgument;
import dev.jorel.commandapi.arguments.TextArgument;
import dev.jorel.commandapi.arguments.TimeArgument;
import dev.jorel.commandapi.arguments.UUIDArgument;
import dev.jorel.commandapi.arguments.WorldArgument;

import java.lang.annotation.Annotation;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * TODO: This class should be generated automatically
 */
@SuppressWarnings("deprecation")
public class ArgumentAnnotations {
	/**
	 * Using a LinkedHashSet to maintain order. We want to have primitives early
	 * on in the list so that they are chosen early in predictions
	 */
	public static final Set<Class<? extends Annotation>> ALL = Stream.of(
			AAdvancementArgument.class, AAdventureChatArgument.class, AAdventureChatComponentArgument.class,
			AAngleArgument.class, AAxisArgument.class, ABiomeArgument.class, ABlockPredicateArgument.class,
			ABlockStateArgument.class, ABooleanArgument.class, AChatArgument.class, AChatColorArgument.class,
			AChatComponentArgument.class, ADoubleArgument.class, AEnchantmentArgument.class,

			AEntitySelectorArgument.ManyPlayers.class,
			AEntitySelectorArgument.OnePlayer.class,
			AEntitySelectorArgument.ManyEntities.class,
			AEntitySelectorArgument.OneEntity.class,

			AEntityTypeArgument.class, AFloatArgument.class, AFloatRangeArgument.class, AFunctionArgument.class,
			AGreedyStringArgument.class, AIntegerArgument.class, AIntegerRangeArgument.class,
			AItemStackArgument.class, AItemStackPredicateArgument.class, ALiteralArgument.class,
			ALocation2DArgument.class, ALocationArgument.class, ALongArgument.class, ALootTableArgument.class,
			AMathOperationArgument.class, AMultiLiteralArgument.class, ANamespacedKeyArgument.class,
			ANBTCompoundArgument.class, AObjectiveArgument.class, AObjectiveCriteriaArgument.class,
			AOfflinePlayerArgument.class, AParticleArgument.class, APlayerArgument.class,
			APotionEffectArgument.class, ARecipeArgument.class, ARotationArgument.class,
			AScoreboardSlotArgument.class,

			AScoreHolderArgument.Single.class,
			AScoreHolderArgument.Multiple.class,

			ASoundArgument.class,
			AStringArgument.class, ATeamArgument.class, ATextArgument.class, ATimeArgument.class,
			AUUIDArgument.class, AWorldArgument.class
		)
		.collect(Collectors.toCollection(LinkedHashSet::new));

	public static ArgumentAnnotation from(Annotation annotation, String nodeName) {
        Class<? extends Annotation> type = annotation.annotationType();
        PrimitiveType primitiveType = new PrimitiveType(type.getAnnotation(Primitive.class).value());
		if (annotation instanceof AAdvancementArgument arg) { return new ArgumentAnnotationProperties(type, AdvancementArgument.class, primitiveType, nodeName, arg.optional());
		} else if (annotation instanceof AAdventureChatArgument arg) { return new ArgumentAnnotationProperties(type, AdventureChatArgument.class, primitiveType, nodeName, arg.optional());
		} else if (annotation instanceof AAdventureChatComponentArgument arg) { return new ArgumentAnnotationProperties(type, AdventureChatComponentArgument.class, primitiveType, nodeName, arg.optional());
		} else if (annotation instanceof AAngleArgument arg) { return new ArgumentAnnotationProperties(type, AngleArgument.class, primitiveType, nodeName, arg.optional());
		} else if (annotation instanceof AAxisArgument arg) { return new ArgumentAnnotationProperties(type, AxisArgument.class, primitiveType, nodeName, arg.optional());
		} else if (annotation instanceof ABiomeArgument arg) { return new ArgumentAnnotationProperties(type, BiomeArgument.class, primitiveType, nodeName, arg.optional());
		} else if (annotation instanceof ABlockPredicateArgument arg) { return new ArgumentAnnotationProperties(type, BlockPredicateArgument.class, primitiveType, nodeName, arg.optional());
		} else if (annotation instanceof ABlockStateArgument arg) { return new ArgumentAnnotationProperties(type, BlockStateArgument.class, primitiveType, nodeName, arg.optional());
		} else if (annotation instanceof ABooleanArgument arg) { return new ArgumentAnnotationProperties(type, BooleanArgument.class, primitiveType, nodeName, arg.optional());
		} else if (annotation instanceof AChatArgument arg) { return new ArgumentAnnotationProperties(type, ChatArgument.class, primitiveType, nodeName, arg.optional());
		} else if (annotation instanceof AChatColorArgument arg) { return new ArgumentAnnotationProperties(type, ChatColorArgument.class, primitiveType, nodeName, arg.optional());
		} else if (annotation instanceof AChatComponentArgument arg) { return new ArgumentAnnotationProperties(type, ChatComponentArgument.class, primitiveType, nodeName, arg.optional());
		} else if (annotation instanceof ACustomArgument arg) { return new CustomArgumentAnnotationProperties(type, CustomArgument.class, primitiveType, nodeName, arg.optional(), arg.value(), arg.keyed());
		} else if (annotation instanceof ADoubleArgument arg) { return new DoubleArgumentAnnotationProperties(type, DoubleArgument.class, primitiveType, nodeName, arg.optional(), arg.min(), arg.max());
		} else if (annotation instanceof AEnchantmentArgument arg) { return new ArgumentAnnotationProperties(type, EnchantmentArgument.class, primitiveType, nodeName, arg.optional());
		} else if (annotation instanceof AEntitySelectorArgument.ManyEntities arg) { return new ArgumentAnnotationProperties(type, EntitySelectorArgument.ManyEntities.class, primitiveType, nodeName, arg.optional());
		} else if (annotation instanceof AEntitySelectorArgument.ManyPlayers arg) { return new ArgumentAnnotationProperties(type, EntitySelectorArgument.ManyPlayers.class, primitiveType, nodeName, arg.optional());
		} else if (annotation instanceof AEntitySelectorArgument.OneEntity arg) { return new ArgumentAnnotationProperties(type, EntitySelectorArgument.OneEntity.class, primitiveType, nodeName, arg.optional());
		} else if (annotation instanceof AEntitySelectorArgument.OnePlayer arg) { return new ArgumentAnnotationProperties(type, EntitySelectorArgument.OnePlayer.class, primitiveType, nodeName, arg.optional());
		} else if (annotation instanceof AEntityTypeArgument arg) { return new ArgumentAnnotationProperties(type, EntityTypeArgument.class, primitiveType, nodeName, arg.optional());
		} else if (annotation instanceof AFloatArgument arg) { return new FloatArgumentAnnotationProperties(type, FloatArgument.class, primitiveType, nodeName, arg.optional(), arg.min(), arg.max());
		} else if (annotation instanceof AFloatRangeArgument arg) { return new ArgumentAnnotationProperties(type, FloatRangeArgument.class, primitiveType, nodeName, arg.optional());
		} else if (annotation instanceof AFunctionArgument arg) { return new ArgumentAnnotationProperties(type, FunctionArgument.class, primitiveType, nodeName, arg.optional());
		} else if (annotation instanceof AGreedyStringArgument arg) { return new ArgumentAnnotationProperties(type, GreedyStringArgument.class, primitiveType, nodeName, arg.optional());
		} else if (annotation instanceof AIntegerArgument arg) { return new IntegerArgumentAnnotationProperties(type, IntegerArgument.class, primitiveType, nodeName, arg.optional(), arg.min(), arg.max());
		} else if (annotation instanceof AIntegerRangeArgument arg) { return new ArgumentAnnotationProperties(type, IntegerRangeArgument.class, primitiveType, nodeName, arg.optional());
		} else if (annotation instanceof AItemStackArgument arg) { return new ArgumentAnnotationProperties(type, ItemStackArgument.class, primitiveType, nodeName, arg.optional());
		} else if (annotation instanceof AItemStackPredicateArgument arg) { return new ArgumentAnnotationProperties(type, ItemStackPredicateArgument.class, primitiveType, nodeName, arg.optional());
		} else if (annotation instanceof ALiteralArgument arg) { return new ArgumentAnnotationProperties(type, LiteralArgument.class, primitiveType, nodeName, arg.optional());
		} else if (annotation instanceof ALocation2DArgument arg) { return new LocationArgumentAnnotationProperties(type, Location2DArgument.class, primitiveType, nodeName, arg.optional(), arg.value());
		} else if (annotation instanceof ALocationArgument arg) { return new LocationArgumentAnnotationProperties(type, LocationArgument.class, primitiveType, nodeName, arg.optional(), arg.value());
		} else if (annotation instanceof ALongArgument arg) { return new LongArgumentAnnotationProperties(type, LongArgument.class, primitiveType, nodeName, arg.optional(), arg.min(), arg.max());
		} else if (annotation instanceof ALootTableArgument arg) { return new ArgumentAnnotationProperties(type, LootTableArgument.class, primitiveType, nodeName, arg.optional());
		} else if (annotation instanceof AMathOperationArgument arg) { return new ArgumentAnnotationProperties(type, MathOperationArgument.class, primitiveType, nodeName, arg.optional());
		} else if (annotation instanceof AMultiLiteralArgument arg) { return new MultiLiteralArgumentAnnotationProperties(type, MultiLiteralArgument.class, primitiveType, nodeName, ArgumentListedOption.DEFAULT, arg.optional(), arg.value());
		} else if (annotation instanceof ANBTCompoundArgument arg) { return new ArgumentAnnotationProperties(type, NBTCompoundArgument.class, primitiveType, nodeName, arg.optional());
		} else if (annotation instanceof ANamespacedKeyArgument arg) { return new ArgumentAnnotationProperties(type, NamespacedKeyArgument.class, primitiveType, nodeName, arg.optional());
		} else if (annotation instanceof AObjectiveArgument arg) { return new ArgumentAnnotationProperties(type, ObjectiveArgument.class, primitiveType, nodeName, arg.optional());
		} else if (annotation instanceof AObjectiveCriteriaArgument arg) { return new ArgumentAnnotationProperties(type, ObjectiveCriteriaArgument.class, primitiveType, nodeName, arg.optional());
		} else if (annotation instanceof AOfflinePlayerArgument arg) { return new ArgumentAnnotationProperties(type, OfflinePlayerArgument.class, primitiveType, nodeName, arg.optional());
		} else if (annotation instanceof AParticleArgument arg) { return new ArgumentAnnotationProperties(type, ParticleArgument.class, primitiveType, nodeName, arg.optional());
		} else if (annotation instanceof APlayerArgument arg) { return new ArgumentAnnotationProperties(type, PlayerArgument.class, primitiveType, nodeName, arg.optional());
		} else if (annotation instanceof APotionEffectArgument arg) { return new ArgumentAnnotationProperties(type, PotionEffectArgument.class, primitiveType, nodeName, arg.optional());
		} else if (annotation instanceof ARecipeArgument arg) { return new ArgumentAnnotationProperties(type, RecipeArgument.class, primitiveType, nodeName, arg.optional());
		} else if (annotation instanceof ARotationArgument arg) { return new ArgumentAnnotationProperties(type, RotationArgument.class, primitiveType, nodeName, arg.optional());
		} else if (annotation instanceof AScoreHolderArgument.Multiple arg) { return new ArgumentAnnotationProperties(type, ScoreHolderArgument.Multiple.class, primitiveType, nodeName, arg.optional());
		} else if (annotation instanceof AScoreHolderArgument.Single arg) { return new ArgumentAnnotationProperties(type, ScoreHolderArgument.Single.class, primitiveType, nodeName, arg.optional());
		} else if (annotation instanceof AScoreboardSlotArgument arg) { return new ArgumentAnnotationProperties(type, ScoreboardSlotArgument.class, primitiveType, nodeName, arg.optional());
		} else if (annotation instanceof ASoundArgument arg) { return new ArgumentAnnotationProperties(type, SoundArgument.class, primitiveType, nodeName, arg.optional());
		} else if (annotation instanceof AStringArgument arg) { return new ArgumentAnnotationProperties(type, StringArgument.class, primitiveType, nodeName, arg.optional());
		} else if (annotation instanceof ATeamArgument arg) { return new ArgumentAnnotationProperties(type, TeamArgument.class, primitiveType, nodeName, arg.optional());
		} else if (annotation instanceof ATextArgument arg) { return new ArgumentAnnotationProperties(type, TextArgument.class, primitiveType, nodeName, arg.optional());
		} else if (annotation instanceof ATimeArgument arg) { return new ArgumentAnnotationProperties(type, TimeArgument.class, primitiveType, nodeName, arg.optional());
		} else if (annotation instanceof AUUIDArgument arg) { return new ArgumentAnnotationProperties(type, UUIDArgument.class, primitiveType, nodeName, arg.optional());
		} else if (annotation instanceof AWorldArgument arg) { return new ArgumentAnnotationProperties(type, WorldArgument.class, primitiveType, nodeName, arg.optional());
		}
		throw new IllegalArgumentException("%s is not a %s"
			.formatted(annotation, ArgumentAnnotation.class));
	}
}
