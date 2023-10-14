package dev.jorel.commandapi.kotlindsl

import dev.jorel.commandapi.*
import dev.jorel.commandapi.arguments.*
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin
import java.util.function.Predicate

inline fun commandTree(name: String, tree: CommandTree.() -> Unit = {}) = CommandTree(name).apply(tree).register()
inline fun commandTree(name: String, namespace: String, tree: CommandTree.() -> Unit = {}) = CommandTree(name).apply(tree).register(namespace)
inline fun commandTree(name: String, namespace: JavaPlugin, tree: CommandTree.() -> Unit = {}) = CommandTree(name).apply(tree).register(namespace)
@Deprecated("This method has been deprecated since version 9.1.0 as it is not needed anymore. See the documentation for more information", ReplaceWith(""), DeprecationLevel.WARNING)
inline fun commandTree(name: String, predicate: Predicate<CommandSender>, tree: CommandTree.() -> Unit = {}) = CommandTree(name).withRequirement(predicate).apply(tree).register()

// CommandTree start
inline fun CommandTree.argument(base: Argument<*>, block: Argument<*>.() -> Unit = {}): CommandTree = then(base.apply(block))

inline fun CommandTree.optionalArgument(base: Argument<*>, block: Argument<*>.() -> Unit = {}): CommandTree = withOptionalArguments(base.apply(block))

inline fun CommandTree.addArgument(base: Argument<*>, optional: Boolean, block: Argument<*>.() -> Unit): CommandTree = if(optional) { optionalArgument(base, block) } else { argument(base, block) }

// Integer arguments
inline fun CommandTree.integerArgument(nodeName: String, min: Int = Int.MIN_VALUE, max: Int = Int.MAX_VALUE, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): CommandTree = addArgument(IntegerArgument(nodeName, min, max), optional, block)
inline fun CommandTree.integerRangeArgument(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): CommandTree = addArgument(IntegerRangeArgument(nodeName), optional, block)

// Float arguments
inline fun CommandTree.floatArgument(nodeName: String, min: Float = -Float.MAX_VALUE, max: Float = Float.MAX_VALUE, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): CommandTree = addArgument(FloatArgument(nodeName, min, max), optional, block)
inline fun CommandTree.floatRangeArgument(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): CommandTree = addArgument(FloatRangeArgument(nodeName), optional, block)

// Double arguments
inline fun CommandTree.doubleArgument(nodeName: String, min: Double = -Double.MAX_VALUE, max: Double = Double.MAX_VALUE, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): CommandTree = addArgument(DoubleArgument(nodeName, min, max), optional, block)

// Long arguments
inline fun CommandTree.longArgument(nodeName: String, min: Long = Long.MIN_VALUE, max: Long = Long.MAX_VALUE, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): CommandTree = addArgument(LongArgument(nodeName, min, max), optional, block)

// Boolean argument
inline fun CommandTree.booleanArgument(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): CommandTree = addArgument(BooleanArgument(nodeName), optional, block)

// String arguments
inline fun CommandTree.stringArgument(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): CommandTree = addArgument(StringArgument(nodeName), optional, block)
inline fun CommandTree.textArgument(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): CommandTree = addArgument(TextArgument(nodeName), optional, block)
inline fun CommandTree.greedyStringArgument(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): CommandTree = addArgument(GreedyStringArgument(nodeName), optional, block)

// Positional arguments
inline fun CommandTree.locationArgument(nodeName: String, locationType: LocationType = LocationType.PRECISE_POSITION, centerPosition: Boolean = true, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): CommandTree = addArgument(LocationArgument(nodeName, locationType, centerPosition), optional, block)
inline fun CommandTree.location2DArgument(nodeName: String, locationType: LocationType = LocationType.PRECISE_POSITION, centerPosition: Boolean = true, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): CommandTree = addArgument(Location2DArgument(nodeName, locationType, centerPosition), optional, block)
inline fun CommandTree.rotationArgument(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): CommandTree = addArgument(RotationArgument(nodeName), optional, block)
inline fun CommandTree.axisArgument(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): CommandTree = addArgument(AxisArgument(nodeName), optional, block)

// Chat arguments
inline fun CommandTree.chatColorArgument(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): CommandTree = addArgument(ChatColorArgument(nodeName), optional, block)
inline fun CommandTree.chatComponentArgument(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): CommandTree = addArgument(ChatComponentArgument(nodeName), optional, block)
inline fun CommandTree.chatArgument(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): CommandTree = addArgument(ChatArgument(nodeName), optional, block)
inline fun CommandTree.adventureChatColorArgument(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): CommandTree = addArgument(AdventureChatColorArgument(nodeName), optional, block)
inline fun CommandTree.adventureChatComponentArgument(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): CommandTree = addArgument(AdventureChatComponentArgument(nodeName), optional, block)
inline fun CommandTree.adventureChatArgument(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): CommandTree = addArgument(AdventureChatArgument(nodeName), optional, block)

// Entity & Player arguments
inline fun CommandTree.entitySelectorArgumentOneEntity(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): CommandTree = addArgument(EntitySelectorArgument.OneEntity(nodeName), optional, block)
inline fun CommandTree.entitySelectorArgumentManyEntities(nodeName: String, allowEmpty: Boolean = true, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): CommandTree = addArgument(EntitySelectorArgument.ManyEntities(nodeName, allowEmpty), optional, block)
inline fun CommandTree.entitySelectorArgumentOnePlayer(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): CommandTree = addArgument(EntitySelectorArgument.OnePlayer(nodeName), optional, block)
inline fun CommandTree.entitySelectorArgumentManyPlayers(nodeName: String, allowEmpty: Boolean = true, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): CommandTree = addArgument(EntitySelectorArgument.ManyPlayers(nodeName, allowEmpty), optional, block)
inline fun CommandTree.playerArgument(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): CommandTree = addArgument(PlayerArgument(nodeName), optional, block)
inline fun CommandTree.offlinePlayerArgument(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): CommandTree = addArgument(OfflinePlayerArgument(nodeName), optional, block)
inline fun CommandTree.entityTypeArgument(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): CommandTree = addArgument(EntityTypeArgument(nodeName), optional, block)

// Scoreboard arguments
inline fun CommandTree.scoreHolderArgumentSingle(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): CommandTree = addArgument(ScoreHolderArgument.Single(nodeName), optional, block)
inline fun CommandTree.scoreHolderArgumentMultiple(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): CommandTree = addArgument(ScoreHolderArgument.Multiple(nodeName), optional, block)
inline fun CommandTree.scoreboardSlotArgument(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): CommandTree = addArgument(ScoreboardSlotArgument(nodeName), optional, block)
inline fun CommandTree.objectiveArgument(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): CommandTree = addArgument(ObjectiveArgument(nodeName), optional, block)
inline fun CommandTree.objectiveCriteriaArgument(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): CommandTree = addArgument(ObjectiveCriteriaArgument(nodeName), optional, block)
inline fun CommandTree.teamArgument(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): CommandTree = addArgument(TeamArgument(nodeName), optional, block)

// Miscellaneous arguments
inline fun CommandTree.angleArgument(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): CommandTree = addArgument(AngleArgument(nodeName), optional, block)
inline fun CommandTree.advancementArgument(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): CommandTree = addArgument(AdvancementArgument(nodeName), optional, block)

inline fun CommandTree.biomeArgument(nodeName: String, useNamespacedKey: Boolean = false, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): CommandTree =
	if (useNamespacedKey) addArgument(BiomeArgument.NamespacedKey(nodeName), optional, block) else addArgument(BiomeArgument(nodeName), optional, block)

inline fun CommandTree.blockStateArgument(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): CommandTree = addArgument(BlockStateArgument(nodeName), optional, block)
inline fun CommandTree.commandArgument(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): CommandTree = addArgument(CommandArgument(nodeName), optional, block)
inline fun CommandTree.enchantmentArgument(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): CommandTree = addArgument(EnchantmentArgument(nodeName), optional, block)

inline fun CommandTree.itemStackArgument(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): CommandTree = addArgument(ItemStackArgument(nodeName), optional, block)
inline fun CommandTree.lootTableArgument(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): CommandTree = addArgument(LootTableArgument(nodeName), optional, block)
inline fun CommandTree.mathOperationArgument(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): CommandTree = addArgument(MathOperationArgument(nodeName), optional, block)
inline fun CommandTree.namespacedKeyArgument(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): CommandTree = addArgument(NamespacedKeyArgument(nodeName), optional, block)
inline fun CommandTree.particleArgument(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): CommandTree = addArgument(ParticleArgument(nodeName), optional, block)
inline fun CommandTree.potionEffectArgument(nodeName: String, useNamespacedKey: Boolean = false, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): CommandTree =
	if (useNamespacedKey) addArgument(PotionEffectArgument.NamespacedKey(nodeName), optional, block) else addArgument(PotionEffectArgument(nodeName), optional, block)
inline fun CommandTree.recipeArgument(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): CommandTree = addArgument(RecipeArgument(nodeName), optional, block)

inline fun CommandTree.soundArgument(nodeName: String, useNamespacedKey: Boolean = false, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): CommandTree =
	if (useNamespacedKey) addArgument(SoundArgument.NamespacedKey(nodeName), optional, block) else addArgument(SoundArgument(nodeName), optional, block)

inline fun CommandTree.timeArgument(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): CommandTree = addArgument(TimeArgument(nodeName), optional, block)
inline fun CommandTree.uuidArgument(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): CommandTree = addArgument(UUIDArgument(nodeName), optional, block)
inline fun CommandTree.worldArgument(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): CommandTree = addArgument(WorldArgument(nodeName), optional, block)

// Predicate arguments
inline fun CommandTree.blockPredicateArgument(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): CommandTree = addArgument(BlockPredicateArgument(nodeName), optional, block)
inline fun CommandTree.itemStackPredicateArgument(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): CommandTree = addArgument(ItemStackPredicateArgument(nodeName), optional, block)

// NBT arguments
inline fun <NBTContainer> CommandTree.nbtCompoundArgument(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): CommandTree = addArgument(NBTCompoundArgument<NBTContainer>(nodeName), optional, block)

// Literal arguments
inline fun CommandTree.literalArgument(literal: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): CommandTree= addArgument(LiteralArgument.of(literal, literal), optional, block)
inline fun CommandTree.literalArgument(nodeName: String, literal: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): CommandTree= addArgument(LiteralArgument.of(nodeName, literal), optional, block)

@Deprecated("This version has been deprecated since version 9.0.2", ReplaceWith("multiLiteralArgument(nodeName, listOf(literals))", "dev.jorel.commandapi.kotlindsl.*"), DeprecationLevel.WARNING)
inline fun CommandTree.multiLiteralArgument(vararg literals: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): CommandTree= addArgument(MultiLiteralArgument(literals), optional, block)
@Deprecated("This method has been deprecated since version 9.1.0", ReplaceWith("multiLiteralArgument(nodeName, literals)", "dev.jorel.commandapi.kotlindsl.*"), DeprecationLevel.WARNING)
inline fun CommandTree.multiLiteralArgument(nodeName: String, literals: List<String>, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): CommandTree= addArgument(MultiLiteralArgument(nodeName, literals), optional, block)

inline fun CommandTree.multiLiteralArgument(nodeName: String, vararg literals: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): CommandTree= addArgument(MultiLiteralArgument(nodeName, *literals), optional, block)

// Function arguments
inline fun CommandTree.functionArgument(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): CommandTree = addArgument(FunctionArgument(nodeName), optional, block)

// ArgumentTree start
inline fun Argument<*>.argument(base: Argument<*>, block: Argument<*>.() -> Unit = {}): Argument<*> = then(base.apply(block))

inline fun Argument<*>.optionalArgument(base: Argument<*>, block: Argument<*>.() -> Unit = {}): Argument<*> = withOptionalArguments(base.apply(block))

inline fun Argument<*>.addArgument(base: Argument<*>, optional: Boolean, block: Argument<*>.() -> Unit): Argument<*> = if (optional) { optionalArgument(base, block) } else { argument(base, block) }

// Integer arguments
inline fun Argument<*>.integerArgument(nodeName: String, min: Int = Int.MIN_VALUE, max: Int = Int.MAX_VALUE, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): Argument<*> = addArgument(IntegerArgument(nodeName, min, max), optional, block)
inline fun Argument<*>.integerRangeArgument(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): Argument<*> = addArgument(IntegerRangeArgument(nodeName), optional, block)

// Float arguments
inline fun Argument<*>.floatArgument(nodeName: String, min: Float = -Float.MAX_VALUE, max: Float = Float.MAX_VALUE, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): Argument<*> = addArgument(FloatArgument(nodeName, min, max), optional, block)
inline fun Argument<*>.floatRangeArgument(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): Argument<*> = addArgument(FloatRangeArgument(nodeName), optional, block)

// Double arguments
inline fun Argument<*>.doubleArgument(nodeName: String, min: Double = -Double.MAX_VALUE, max: Double = Double.MAX_VALUE, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): Argument<*> = addArgument(DoubleArgument(nodeName, min, max), optional, block)

// Long arguments
inline fun Argument<*>.longArgument(nodeName: String, min: Long = Long.MIN_VALUE, max: Long = Long.MAX_VALUE, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): Argument<*> = addArgument(LongArgument(nodeName, min, max), optional, block)

// Boolean argument
inline fun Argument<*>.booleanArgument(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): Argument<*> = addArgument(BooleanArgument(nodeName), optional, block)

// String arguments
inline fun Argument<*>.stringArgument(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): Argument<*> = addArgument(StringArgument(nodeName), optional, block)
inline fun Argument<*>.textArgument(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): Argument<*> = addArgument(TextArgument(nodeName), optional, block)
inline fun Argument<*>.greedyStringArgument(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): Argument<*> = addArgument(GreedyStringArgument(nodeName), optional, block)

// Positional arguments
inline fun Argument<*>.locationArgument(nodeName: String, locationType: LocationType = LocationType.PRECISE_POSITION, centerPosition: Boolean = true, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): Argument<*> = addArgument(LocationArgument(nodeName, locationType, centerPosition), optional, block)
inline fun Argument<*>.location2DArgument(nodeName: String, locationType: LocationType = LocationType.PRECISE_POSITION, centerPosition: Boolean = true, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): Argument<*> = addArgument(Location2DArgument(nodeName, locationType, centerPosition), optional, block)
inline fun Argument<*>.rotationArgument(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): Argument<*> = addArgument(RotationArgument(nodeName), optional, block)
inline fun Argument<*>.axisArgument(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): Argument<*> = addArgument(AxisArgument(nodeName), optional, block)

// Chat arguments
inline fun Argument<*>.chatColorArgument(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): Argument<*> = addArgument(ChatColorArgument(nodeName), optional, block)
inline fun Argument<*>.chatComponentArgument(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): Argument<*> = addArgument(ChatComponentArgument(nodeName), optional, block)
inline fun Argument<*>.chatArgument(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): Argument<*> = addArgument(ChatArgument(nodeName), optional, block)
inline fun Argument<*>.adventureChatColorArgument(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): Argument<*> = addArgument(AdventureChatColorArgument(nodeName), optional, block)
inline fun Argument<*>.adventureChatComponentArgument(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): Argument<*> = addArgument(AdventureChatComponentArgument(nodeName), optional, block)
inline fun Argument<*>.adventureChatArgument(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): Argument<*> = addArgument(AdventureChatArgument(nodeName), optional, block)

// Entity & Player arguments
inline fun Argument<*>.entitySelectorArgumentOneEntity(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): Argument<*> = addArgument(EntitySelectorArgument.OneEntity(nodeName), optional, block)
inline fun Argument<*>.entitySelectorArgumentManyEntities(nodeName: String, allowEmpty: Boolean = true, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): Argument<*> = addArgument(EntitySelectorArgument.ManyEntities(nodeName, allowEmpty), optional, block)
inline fun Argument<*>.entitySelectorArgumentOnePlayer(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): Argument<*> = addArgument(EntitySelectorArgument.OnePlayer(nodeName), optional, block)
inline fun Argument<*>.entitySelectorArgumentManyPlayers(nodeName: String, allowEmpty: Boolean = true, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): Argument<*> = addArgument(EntitySelectorArgument.ManyPlayers(nodeName, allowEmpty), optional, block)
inline fun Argument<*>.playerArgument(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): Argument<*> = addArgument(PlayerArgument(nodeName), optional, block)
inline fun Argument<*>.offlinePlayerArgument(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): Argument<*> = addArgument(OfflinePlayerArgument(nodeName), optional, block)
inline fun Argument<*>.entityTypeArgument(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): Argument<*> = addArgument(EntityTypeArgument(nodeName), optional, block)

// Scoreboard arguments
inline fun Argument<*>.scoreHolderArgumentSingle(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): Argument<*> = addArgument(ScoreHolderArgument.Single(nodeName), optional, block)
inline fun Argument<*>.scoreHolderArgumentMultiple(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): Argument<*> = addArgument(ScoreHolderArgument.Multiple(nodeName), optional, block)
inline fun Argument<*>.scoreboardSlotArgument(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): Argument<*> = addArgument(ScoreboardSlotArgument(nodeName), optional, block)
inline fun Argument<*>.objectiveArgument(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): Argument<*> = addArgument(ObjectiveArgument(nodeName), optional, block)
inline fun Argument<*>.objectiveCriteriaArgument(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): Argument<*> = addArgument(ObjectiveCriteriaArgument(nodeName), optional, block)
inline fun Argument<*>.teamArgument(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): Argument<*> = addArgument(TeamArgument(nodeName), optional, block)

// Miscellaneous arguments
inline fun Argument<*>.angleArgument(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): Argument<*> = addArgument(AngleArgument(nodeName), optional, block)
inline fun Argument<*>.advancementArgument(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): Argument<*> = addArgument(AdvancementArgument(nodeName), optional, block)

inline fun Argument<*>.biomeArgument(nodeName: String, useNamespacedKey: Boolean = false, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): Argument<*> =
	if (useNamespacedKey) addArgument(BiomeArgument.NamespacedKey(nodeName), optional, block) else addArgument(BiomeArgument(nodeName), optional, block)

inline fun Argument<*>.blockStateArgument(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): Argument<*> = addArgument(BlockStateArgument(nodeName), optional, block)
inline fun Argument<*>.commandArgument(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): Argument<*> = addArgument(CommandArgument(nodeName), optional, block)
inline fun Argument<*>.enchantmentArgument(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): Argument<*> = addArgument(EnchantmentArgument(nodeName), optional, block)

inline fun Argument<*>.itemStackArgument(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): Argument<*> = addArgument(ItemStackArgument(nodeName), optional, block)
inline fun Argument<*>.lootTableArgument(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): Argument<*> = addArgument(LootTableArgument(nodeName), optional, block)
inline fun Argument<*>.mathOperationArgument(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): Argument<*> = addArgument(MathOperationArgument(nodeName), optional, block)
inline fun Argument<*>.namespacedKeyArgument(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): Argument<*> = addArgument(NamespacedKeyArgument(nodeName), optional, block)
inline fun Argument<*>.particleArgument(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): Argument<*> = addArgument(ParticleArgument(nodeName), optional, block)
inline fun Argument<*>.potionEffectArgument(nodeName: String, useNamespacedKey: Boolean = false, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): Argument<*> =
	if (useNamespacedKey) addArgument(PotionEffectArgument.NamespacedKey(nodeName), optional, block) else addArgument(PotionEffectArgument(nodeName), optional, block)
inline fun Argument<*>.recipeArgument(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): Argument<*> = addArgument(RecipeArgument(nodeName), optional, block)

inline fun Argument<*>.soundArgument(nodeName: String, useNamespacedKey: Boolean = false, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): Argument<*> =
	if (useNamespacedKey) addArgument(SoundArgument.NamespacedKey(nodeName), optional, block) else addArgument(SoundArgument(nodeName), optional, block)

inline fun Argument<*>.timeArgument(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): Argument<*> = addArgument(TimeArgument(nodeName), optional, block)
inline fun Argument<*>.uuidArgument(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): Argument<*> = addArgument(UUIDArgument(nodeName), optional, block)
inline fun Argument<*>.worldArgument(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): Argument<*> = addArgument(WorldArgument(nodeName), optional, block)

// Predicate arguments
inline fun Argument<*>.blockPredicateArgument(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): Argument<*> = addArgument(BlockPredicateArgument(nodeName), optional, block)
inline fun Argument<*>.itemStackPredicateArgument(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): Argument<*> = addArgument(ItemStackPredicateArgument(nodeName), optional, block)

// NBT arguments
inline fun <NBTContainer> Argument<*>.nbtCompoundArgument(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): Argument<*> = addArgument(NBTCompoundArgument<NBTContainer>(nodeName), optional, block)

// Literal arguments
inline fun Argument<*>.literalArgument(literal: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): Argument<*> = addArgument(LiteralArgument.of(literal, literal), optional, block)
inline fun Argument<*>.literalArgument(nodeName: String, literal: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): Argument<*> = addArgument(LiteralArgument.of(nodeName, literal), optional, block)

@Deprecated("This version has been deprecated since version 9.0.2", ReplaceWith("multiLiteralArgument(nodeName, listOf(literals))", "dev.jorel.commandapi.kotlindsl.*"), DeprecationLevel.WARNING)
inline fun Argument<*>.multiLiteralArgument(vararg literals: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): Argument<*> = addArgument(MultiLiteralArgument(literals), optional, block)
@Deprecated("This method has been deprecated since version 9.1.0", ReplaceWith("multiLiteralArgument(nodeName, literals)", "dev.jorel.commandapi.kotlindsl.*"), DeprecationLevel.WARNING)
inline fun Argument<*>.multiLiteralArgument(nodeName: String, literals: List<String>, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): Argument<*> = addArgument(MultiLiteralArgument(nodeName, literals), optional, block)

inline fun Argument<*>.multiLiteralArgument(nodeName: String, vararg literals: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): Argument<*> = addArgument(MultiLiteralArgument(nodeName, *literals), optional, block)

// Function arguments
inline fun Argument<*>.functionArgument(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): Argument<*> = addArgument(FunctionArgument(nodeName), optional, block)

@Deprecated("This method has been deprecated since version 9.1.0 as it is not needed anymore. See the documentation for more information", ReplaceWith(""), DeprecationLevel.WARNING)
inline fun CommandTree.requirement(base: Argument<*>, predicate: Predicate<CommandSender>, block: Argument<*>.() -> Unit = {}): CommandTree = then(base.withRequirement(predicate).apply(block))
@Deprecated("This method has been deprecated since version 9.1.0 as it is not needed anymore. See the documentation for more information", ReplaceWith(""), DeprecationLevel.WARNING)
inline fun Argument<*>.requirement(base: Argument<*>, predicate: Predicate<CommandSender>, block: Argument<*>.() -> Unit = {}): Argument<*> = then(base.withRequirement(predicate).apply(block))
