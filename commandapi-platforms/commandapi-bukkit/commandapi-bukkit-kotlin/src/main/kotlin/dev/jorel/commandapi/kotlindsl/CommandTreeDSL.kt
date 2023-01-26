package dev.jorel.commandapi.kotlindsl

import dev.jorel.commandapi.*
import dev.jorel.commandapi.arguments.*
import dev.jorel.commandapi.executors.*
import dev.jorel.commandapi.wrappers.NativeProxyCommandSender
import org.bukkit.command.BlockCommandSender
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.command.ProxiedCommandSender
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import java.util.function.Predicate

inline fun commandTree(name: String, tree: CommandTree.() -> Unit = {}) = CommandTree(name).apply(tree).register()
inline fun commandTree(name: String, predicate: Predicate<CommandSender>, tree: CommandTree.() -> Unit = {}) = CommandTree(name).withRequirement(predicate).apply(tree).register()

// CommandTree start
inline fun CommandTree.argument(base: Argument<*>, block: Argument<*>.() -> Unit = {}): CommandTree = then(base.apply(block))

inline fun CommandTree.optionalArgument(base: Argument<*>, block: Argument<*>.() -> Unit = {}): CommandTree = then(base.setOptional(true).apply(block))

// Integer arguments
inline fun CommandTree.integerArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandTree = then(IntegerArgument(nodeName).apply(block))
inline fun CommandTree.integerArgument(nodeName: String, min: Int, block: Argument<*>.() -> Unit = {}): CommandTree = then(IntegerArgument(nodeName, min).apply(block))
inline fun CommandTree.integerArgument(nodeName: String, min: Int, max: Int, block: Argument<*>.() -> Unit = {}): CommandTree = then(IntegerArgument(nodeName, min, max).apply(block))
inline fun CommandTree.integerRangeArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandTree = then(IntegerRangeArgument(nodeName).apply(block))

// Float arguments
inline fun CommandTree.floatArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandTree = then(FloatArgument(nodeName).apply(block))
inline fun CommandTree.floatArgument(nodeName: String, min: Float, block: Argument<*>.() -> Unit = {}): CommandTree = then(FloatArgument(nodeName, min).apply(block))
inline fun CommandTree.floatArgument(nodeName: String, min: Float, max: Float, block: Argument<*>.() -> Unit = {}): CommandTree = then(FloatArgument(nodeName, min, max).apply(block))
inline fun CommandTree.floatRangeArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandTree = then(FloatRangeArgument(nodeName).apply(block))

// Double arguments
inline fun CommandTree.doubleArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandTree = then(DoubleArgument(nodeName).apply(block))
inline fun CommandTree.doubleArgument(nodeName: String, min: Double, block: Argument<*>.() -> Unit = {}): CommandTree = then(DoubleArgument(nodeName, min).apply(block))
inline fun CommandTree.doubleArgument(nodeName: String, min: Double, max: Double, block: Argument<*>.() -> Unit = {}): CommandTree = then(DoubleArgument(nodeName, min, max).apply(block))

// Long arguments
inline fun CommandTree.longArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandTree = then(LongArgument(nodeName).apply(block))
inline fun CommandTree.longArgument(nodeName: String, min: Long, block: Argument<*>.() -> Unit = {}): CommandTree = then(LongArgument(nodeName, min).apply(block))
inline fun CommandTree.longArgument(nodeName: String, min: Long, max: Long, block: Argument<*>.() -> Unit = {}): CommandTree = then(LongArgument(nodeName, min, max).apply(block))

// Boolean argument
inline fun CommandTree.booleanArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandTree = then(BooleanArgument(nodeName).apply(block))

// String arguments
inline fun CommandTree.stringArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandTree = then(StringArgument(nodeName).apply(block))
inline fun CommandTree.textArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandTree = then(TextArgument(nodeName).apply(block))
inline fun CommandTree.greedyStringArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandTree = then(GreedyStringArgument(nodeName).apply(block))

// Positional arguments
inline fun CommandTree.locationArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandTree = then(LocationArgument(nodeName).apply(block))
inline fun CommandTree.locationArgument(nodeName: String, locationType: LocationType, block: Argument<*>.() -> Unit = {}): CommandTree = then(LocationArgument(nodeName, locationType).apply(block))
inline fun CommandTree.location2DArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandTree = then(Location2DArgument(nodeName).apply(block))
inline fun CommandTree.location2DArgument(nodeName: String, locationType: LocationType, block: Argument<*>.() -> Unit = {}): CommandTree = then(Location2DArgument(nodeName, locationType).apply(block))
inline fun CommandTree.rotationArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandTree = then(RotationArgument(nodeName).apply(block))
inline fun CommandTree.axisArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandTree = then(AxisArgument(nodeName).apply(block))

// Chat arguments
inline fun CommandTree.chatColorArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandTree = then(ChatColorArgument(nodeName).apply(block))
inline fun CommandTree.chatComponentArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandTree = then(ChatComponentArgument(nodeName).apply(block))
inline fun CommandTree.chatArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandTree = then(ChatArgument(nodeName).apply(block))
inline fun CommandTree.adventureChatComponentArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandTree = then(AdventureChatComponentArgument(nodeName).apply(block))
inline fun CommandTree.adventureChatArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandTree = then(AdventureChatArgument(nodeName).apply(block))

// Entity & Player arguments
inline fun CommandTree.entitySelectorArgumentOneEntity(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandTree = then(EntitySelectorArgument.OneEntity(nodeName).apply(block))
inline fun CommandTree.entitySelectorArgumentManyEntities(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandTree = then(EntitySelectorArgument.ManyEntities(nodeName).apply(block))
inline fun CommandTree.entitySelectorArgumentOnePlayer(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandTree = then(EntitySelectorArgument.OnePlayer(nodeName).apply(block))
inline fun CommandTree.entitySelectorArgumentManyPlayers(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandTree = then(EntitySelectorArgument.ManyPlayers(nodeName).apply(block))
inline fun CommandTree.playerArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandTree = then(PlayerArgument(nodeName).apply(block))
inline fun CommandTree.offlinePlayerArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandTree = then(OfflinePlayerArgument(nodeName).apply(block))
inline fun CommandTree.entityTypeArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandTree = then(EntityTypeArgument(nodeName).apply(block))

// Scoreboard arguments
inline fun CommandTree.scoreHolderArgumentSingle(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandTree = then(ScoreHolderArgument.Single(nodeName).apply(block))
inline fun CommandTree.scoreHolderArgumentMultiple(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandTree = then(ScoreHolderArgument.Multiple(nodeName).apply(block))
inline fun CommandTree.scoreboardSlotArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandTree = then(ScoreboardSlotArgument(nodeName).apply(block))
inline fun CommandTree.objectiveArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandTree = then(ObjectiveArgument(nodeName).apply(block))
inline fun CommandTree.objectiveCriteriaArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandTree = then(ObjectiveCriteriaArgument(nodeName).apply(block))
inline fun CommandTree.teamArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandTree = then(TeamArgument(nodeName).apply(block))

// Miscellaneous arguments
inline fun CommandTree.angleArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandTree = then(AngleArgument(nodeName).apply(block))
inline fun CommandTree.advancementArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandTree = then(AdvancementArgument(nodeName).apply(block))

inline fun CommandTree.biomeArgument(nodeName: String, useNamespacedKey: Boolean = false, block: Argument<*>.() -> Unit = {}): CommandTree =
	if (useNamespacedKey) then(BiomeArgument.NamespacedKey(nodeName).apply(block)) else then(BiomeArgument(nodeName).apply(block))

inline fun CommandTree.blockStateArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandTree = then(BlockStateArgument(nodeName).apply(block))
inline fun CommandTree.commandArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandTree = then(CommandArgument(nodeName).apply(block))
inline fun CommandTree.enchantmentArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandTree = then(EnchantmentArgument(nodeName).apply(block))

inline fun CommandTree.itemStackArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandTree = then(ItemStackArgument(nodeName).apply(block))
inline fun CommandTree.lootTableArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandTree = then(LootTableArgument(nodeName).apply(block))
inline fun CommandTree.mathOperationArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandTree = then(MathOperationArgument(nodeName).apply(block))
inline fun CommandTree.namespacedKeyArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandTree = then(NamespacedKeyArgument(nodeName).apply(block))
inline fun CommandTree.particleArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandTree = then(ParticleArgument(nodeName).apply(block))
inline fun CommandTree.potionEffectArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandTree = then(PotionEffectArgument(nodeName).apply(block))
inline fun CommandTree.recipeArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandTree = then(RecipeArgument(nodeName).apply(block))

inline fun CommandTree.soundArgument(nodeName: String, useNamespacedKey: Boolean = false, block: Argument<*>.() -> Unit = {}): CommandTree =
	if (useNamespacedKey) then(SoundArgument.NamespacedKey(nodeName).apply(block)) else then(SoundArgument(nodeName).apply(block))

inline fun CommandTree.timeArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandTree = then(TimeArgument(nodeName).apply(block))
inline fun CommandTree.uuidArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandTree = then(UUIDArgument(nodeName).apply(block))
inline fun CommandTree.worldArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandTree = then(WorldArgument(nodeName).apply(block))

// Predicate arguments
inline fun CommandTree.blockPredicateArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandTree = then(BlockPredicateArgument(nodeName).apply(block))
inline fun CommandTree.itemStackPredicateArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandTree = then(ItemStackPredicateArgument(nodeName).apply(block))

// NBT arguments
inline fun <NBTContainer> CommandTree.nbtCompoundArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandTree = then(NBTCompoundArgument<NBTContainer>(nodeName).apply(block))

// Literal arguments
inline fun CommandTree.literalArgument(literal: String, block: Argument<*>.() -> Unit = {}): CommandTree = then(LiteralArgument(literal).apply(block))
inline fun CommandTree.multiLiteralArgument(vararg literals: String, block: Argument<*>.() -> Unit = {}): CommandTree = then(MultiLiteralArgument(*literals).apply(block))

// Function arguments
inline fun CommandTree.functionArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandTree = then(FunctionArgument(nodeName).apply(block))

// ArgumentTree start
inline fun Argument<*>.argument(base: Argument<*>, block: Argument<*>.() -> Unit = {}): Argument<*> = then(base.apply(block))

inline fun Argument<*>.optionalArgument(base: Argument<*>, block: Argument<*>.() -> Unit = {}): Argument<*> = then(base.setOptional(true).apply(block))

// Integer arguments
inline fun Argument<*>.integerArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): Argument<*> = then(IntegerArgument(nodeName).apply(block))
inline fun Argument<*>.integerArgument(nodeName: String, min: Int, block: Argument<*>.() -> Unit = {}): Argument<*> = then(IntegerArgument(nodeName, min).apply(block))
inline fun Argument<*>.integerArgument(nodeName: String, min: Int, max: Int, block: Argument<*>.() -> Unit = {}): Argument<*> = then(IntegerArgument(nodeName, min, max).apply(block))
inline fun Argument<*>.integerRangeArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): Argument<*> = then(IntegerRangeArgument(nodeName).apply(block))

// Float arguments
inline fun Argument<*>.floatArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): Argument<*> = then(FloatArgument(nodeName).apply(block))
inline fun Argument<*>.floatArgument(nodeName: String, min: Float, block: Argument<*>.() -> Unit = {}): Argument<*> = then(FloatArgument(nodeName, min).apply(block))
inline fun Argument<*>.floatArgument(nodeName: String, min: Float, max: Float, block: Argument<*>.() -> Unit = {}): Argument<*> = then(FloatArgument(nodeName, min, max).apply(block))
inline fun Argument<*>.floatRangeArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): Argument<*> = then(FloatRangeArgument(nodeName).apply(block))

// Double arguments
inline fun Argument<*>.doubleArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): Argument<*> = then(DoubleArgument(nodeName).apply(block))
inline fun Argument<*>.doubleArgument(nodeName: String, min: Double, block: Argument<*>.() -> Unit = {}): Argument<*> = then(DoubleArgument(nodeName, min).apply(block))
inline fun Argument<*>.doubleArgument(nodeName: String, min: Double, max: Double, block: Argument<*>.() -> Unit = {}): Argument<*> = then(DoubleArgument(nodeName, min, max).apply(block))

// Long arguments
inline fun Argument<*>.longArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): Argument<*> = then(LongArgument(nodeName).apply(block))
inline fun Argument<*>.longArgument(nodeName: String, min: Long, block: Argument<*>.() -> Unit = {}): Argument<*> = then(LongArgument(nodeName, min).apply(block))
inline fun Argument<*>.longArgument(nodeName: String, min: Long, max: Long, block: Argument<*>.() -> Unit = {}): Argument<*> = then(LongArgument(nodeName, min, max).apply(block))

// Boolean argument
inline fun Argument<*>.booleanArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): Argument<*> = then(BooleanArgument(nodeName).apply(block))

// String arguments
inline fun Argument<*>.stringArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): Argument<*> = then(StringArgument(nodeName).apply(block))
inline fun Argument<*>.textArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): Argument<*> = then(TextArgument(nodeName).apply(block))
inline fun Argument<*>.greedyStringArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): Argument<*> = then(GreedyStringArgument(nodeName).apply(block))

// Positional arguments
inline fun Argument<*>.locationArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): Argument<*> = then(LocationArgument(nodeName).apply(block))
inline fun Argument<*>.locationArgument(nodeName: String, locationType: LocationType, block: Argument<*>.() -> Unit = {}): Argument<*> = then(LocationArgument(nodeName, locationType).apply(block))
inline fun Argument<*>.location2DArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): Argument<*> = then(Location2DArgument(nodeName).apply(block))
inline fun Argument<*>.location2DArgument(nodeName: String, locationType: LocationType, block: Argument<*>.() -> Unit = {}): Argument<*> = then(Location2DArgument(nodeName, locationType).apply(block))
inline fun Argument<*>.rotationArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): Argument<*> = then(RotationArgument(nodeName).apply(block))
inline fun Argument<*>.axisArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): Argument<*> = then(AxisArgument(nodeName).apply(block))

// Chat arguments
inline fun Argument<*>.chatColorArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): Argument<*> = then(ChatColorArgument(nodeName).apply(block))
inline fun Argument<*>.chatComponentArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): Argument<*> = then(ChatComponentArgument(nodeName).apply(block))
inline fun Argument<*>.chatArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): Argument<*> = then(ChatArgument(nodeName).apply(block))
inline fun Argument<*>.adventureChatComponentArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): Argument<*> = then(AdventureChatComponentArgument(nodeName).apply(block))
inline fun Argument<*>.adventureChatArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): Argument<*> = then(AdventureChatArgument(nodeName).apply(block))

// Entity & Player arguments
inline fun Argument<*>.entitySelectorArgumentOneEntity(nodeName: String, block: Argument<*>.() -> Unit = {}): Argument<*> = then(EntitySelectorArgument.OneEntity(nodeName).apply(block))
inline fun Argument<*>.entitySelectorArgumentManyEntities(nodeName: String, block: Argument<*>.() -> Unit = {}): Argument<*> = then(EntitySelectorArgument.ManyEntities(nodeName).apply(block))
inline fun Argument<*>.entitySelectorArgumentOnePlayer(nodeName: String, block: Argument<*>.() -> Unit = {}): Argument<*> = then(EntitySelectorArgument.OnePlayer(nodeName).apply(block))
inline fun Argument<*>.entitySelectorArgumentManyPlayers(nodeName: String, block: Argument<*>.() -> Unit = {}): Argument<*> = then(EntitySelectorArgument.ManyPlayers(nodeName).apply(block))
inline fun Argument<*>.playerArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): Argument<*> = then(PlayerArgument(nodeName).apply(block))
inline fun Argument<*>.offlinePlayerArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): Argument<*> = then(OfflinePlayerArgument(nodeName).apply(block))
inline fun Argument<*>.entityTypeArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): Argument<*> = then(EntityTypeArgument(nodeName).apply(block))

// Scoreboard arguments
inline fun Argument<*>.scoreHolderArgumentSingle(nodeName: String, block: Argument<*>.() -> Unit = {}): Argument<*> = then(ScoreHolderArgument.Single(nodeName).apply(block))
inline fun Argument<*>.scoreHolderArgumentMultiple(nodeName: String, block: Argument<*>.() -> Unit = {}): Argument<*> = then(ScoreHolderArgument.Multiple(nodeName).apply(block))
inline fun Argument<*>.scoreboardSlotArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): Argument<*> = then(ScoreboardSlotArgument(nodeName).apply(block))
inline fun Argument<*>.objectiveArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): Argument<*> = then(ObjectiveArgument(nodeName).apply(block))
inline fun Argument<*>.objectiveCriteriaArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): Argument<*> = then(ObjectiveCriteriaArgument(nodeName).apply(block))
inline fun Argument<*>.teamArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): Argument<*> = then(TeamArgument(nodeName).apply(block))

// Miscellaneous arguments
inline fun Argument<*>.angleArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): Argument<*> = then(AngleArgument(nodeName).apply(block))
inline fun Argument<*>.advancementArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): Argument<*> = then(AdvancementArgument(nodeName).apply(block))

inline fun Argument<*>.biomeArgument(nodeName: String, useNamespacedKey: Boolean = false, block: Argument<*>.() -> Unit = {}): Argument<*> =
	if (useNamespacedKey) then(BiomeArgument.NamespacedKey(nodeName).apply(block)) else then(BiomeArgument(nodeName).apply(block))

inline fun Argument<*>.blockStateArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): Argument<*> = then(BlockStateArgument(nodeName).apply(block))
inline fun Argument<*>.commandArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): Argument<*> = then(CommandArgument(nodeName).apply(block))
inline fun Argument<*>.enchantmentArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): Argument<*> = then(EnchantmentArgument(nodeName).apply(block))

inline fun Argument<*>.itemStackArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): Argument<*> = then(ItemStackArgument(nodeName).apply(block))
inline fun Argument<*>.lootTableArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): Argument<*> = then(LootTableArgument(nodeName).apply(block))
inline fun Argument<*>.mathOperationArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): Argument<*> = then(MathOperationArgument(nodeName).apply(block))
inline fun Argument<*>.namespacedKeyArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): Argument<*> = then(NamespacedKeyArgument(nodeName).apply(block))
inline fun Argument<*>.particleArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): Argument<*> = then(ParticleArgument(nodeName).apply(block))
inline fun Argument<*>.potionEffectArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): Argument<*> = then(PotionEffectArgument(nodeName).apply(block))
inline fun Argument<*>.recipeArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): Argument<*> = then(RecipeArgument(nodeName).apply(block))

inline fun Argument<*>.soundArgument(nodeName: String, useNamespacedKey: Boolean = false, block: Argument<*>.() -> Unit = {}): Argument<*> =
	if (useNamespacedKey) then(SoundArgument.NamespacedKey(nodeName).apply(block)) else then(SoundArgument(nodeName).apply(block))

inline fun Argument<*>.timeArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): Argument<*> = then(TimeArgument(nodeName).apply(block))
inline fun Argument<*>.uuidArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): Argument<*> = then(UUIDArgument(nodeName).apply(block))
inline fun Argument<*>.worldArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): Argument<*> = then(WorldArgument(nodeName).apply(block))

// Predicate arguments
inline fun Argument<*>.blockPredicateArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): Argument<*> = then(BlockPredicateArgument(nodeName).apply(block))
inline fun Argument<*>.itemStackPredicateArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): Argument<*> = then(ItemStackPredicateArgument(nodeName).apply(block))

// NBT arguments
inline fun <NBTContainer> Argument<*>.nbtCompoundArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): Argument<*> = then(NBTCompoundArgument<NBTContainer>(nodeName).apply(block))

// Literal arguments
inline fun Argument<*>.literalArgument(literal: String, block: Argument<*>.() -> Unit = {}): Argument<*> = then(LiteralArgument(literal).apply(block))
inline fun Argument<*>.multiLiteralArgument(vararg literals: String, block: Argument<*>.() -> Unit = {}): Argument<*> = then(MultiLiteralArgument(*literals).apply(block))

// Function arguments
inline fun Argument<*>.functionArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): Argument<*> = then(FunctionArgument(nodeName).apply(block))

inline fun CommandTree.requirement(base: Argument<*>, predicate: Predicate<CommandSender>, block: Argument<*>.() -> Unit = {}): CommandTree = then(base.withRequirement(predicate).apply(block))
inline fun Argument<*>.requirement(base: Argument<*>, predicate: Predicate<CommandSender>, block: Argument<*>.() -> Unit = {}): Argument<*> = then(base.withRequirement(predicate).apply(block))

// CommandTree execution
fun CommandTree.anyExecutor(any: (CommandSender, CommandArguments) -> Unit) = CommandTreeExecution().any(any).executes(this)
fun CommandTree.playerExecutor(player: (Player, CommandArguments) -> Unit) = CommandTreeExecution().player(player).executes(this)
fun CommandTree.entityExecutor(entity: (Entity, CommandArguments) -> Unit) = CommandTreeExecution().entity(entity).executes(this)
fun CommandTree.consoleExecutor(console: (ConsoleCommandSender, CommandArguments) -> Unit) = CommandTreeExecution().console(console).executes(this)
fun CommandTree.commandBlockExecutor(block: (BlockCommandSender, CommandArguments) -> Unit) = CommandTreeExecution().block(block).executes(this)
fun CommandTree.proxyExecutor(proxy: (ProxiedCommandSender, CommandArguments) -> Unit) = CommandTreeExecution().proxy(proxy).executes(this)
fun CommandTree.nativeExecutor(native: (NativeProxyCommandSender, CommandArguments) -> Unit) = CommandTreeExecution().native(native).executes(this)

fun CommandTree.anyResultingExecutor(any: (CommandSender, CommandArguments) -> Int) = CommandTreeResultingExecution().any(any).executes(this)
fun CommandTree.playerResultingExecutor(player: (Player, CommandArguments) -> Int) = CommandTreeResultingExecution().player(player).executes(this)
fun CommandTree.entityResultingExecutor(entity: (Entity, CommandArguments) -> Int) = CommandTreeResultingExecution().entity(entity).executes(this)
fun CommandTree.consoleResultingExecutor(console: (ConsoleCommandSender, CommandArguments) -> Int) = CommandTreeResultingExecution().console(console).executes(this)
fun CommandTree.commandBlockResultingExecutor(block: (BlockCommandSender, CommandArguments) -> Int) = CommandTreeResultingExecution().block(block).executes(this)
fun CommandTree.proxyResultingExecutor(proxy: (ProxiedCommandSender, CommandArguments) -> Int) = CommandTreeResultingExecution().proxy(proxy).executes(this)
fun CommandTree.nativeResultingExecutor(native: (NativeProxyCommandSender, CommandArguments) -> Int) = CommandTreeResultingExecution().native(native).executes(this)

// ArgumentTree execution
fun Argument<*>.anyExecutor(any: (CommandSender, CommandArguments) -> Unit) = CommandTreeExecution().any(any).executes(this)
fun Argument<*>.playerExecutor(player: (Player, CommandArguments) -> Unit) = CommandTreeExecution().player(player).executes(this)
fun Argument<*>.entityExecutor(entity: (Entity, CommandArguments) -> Unit) = CommandTreeExecution().entity(entity).executes(this)
fun Argument<*>.consoleExecutor(console: (ConsoleCommandSender, CommandArguments) -> Unit) = CommandTreeExecution().console(console).executes(this)
fun Argument<*>.commandBlockExecutor(block: (BlockCommandSender, CommandArguments) -> Unit) = CommandTreeExecution().block(block).executes(this)
fun Argument<*>.proxyExecutor(proxy: (ProxiedCommandSender, CommandArguments) -> Unit) = CommandTreeExecution().proxy(proxy).executes(this)
fun Argument<*>.nativeExecutor(native: (NativeProxyCommandSender, CommandArguments) -> Unit) = CommandTreeExecution().native(native).executes(this)

fun Argument<*>.anyResultingExecutor(any: (CommandSender, CommandArguments) -> Int) = CommandTreeResultingExecution().any(any).executes(this)
fun Argument<*>.playerResultingExecutor(player: (Player, CommandArguments) -> Int) = CommandTreeResultingExecution().player(player).executes(this)
fun Argument<*>.entityResultingExecutor(entity: (Entity, CommandArguments) -> Int) = CommandTreeResultingExecution().entity(entity).executes(this)
fun Argument<*>.consoleResultingExecutor(console: (ConsoleCommandSender, CommandArguments) -> Int) = CommandTreeResultingExecution().console(console).executes(this)
fun Argument<*>.commandBlockResultingExecutor(block: (BlockCommandSender, CommandArguments) -> Int) = CommandTreeResultingExecution().block(block).executes(this)
fun Argument<*>.proxyResultingExecutor(proxy: (ProxiedCommandSender, CommandArguments) -> Int) = CommandTreeResultingExecution().proxy(proxy).executes(this)
fun Argument<*>.nativeResultingExecutor(native: (NativeProxyCommandSender, CommandArguments) -> Int) = CommandTreeResultingExecution().native(native).executes(this)


class CommandTreeExecution {

	private var any: ((CommandSender, CommandArguments) -> Unit)? = null
	private var player: ((Player, CommandArguments) -> Unit)? = null
	private var entity: ((Entity, CommandArguments) -> Unit)? = null
	private var console: ((ConsoleCommandSender, CommandArguments) -> Unit)? = null
	private var block: ((BlockCommandSender, CommandArguments) -> Unit)? = null
	private var proxy: ((ProxiedCommandSender, CommandArguments) -> Unit)? = null
	private var native: ((NativeProxyCommandSender, CommandArguments) -> Unit)? = null

	fun any(any: (CommandSender, CommandArguments) -> Unit): CommandTreeExecution {
		this.any = any
		return this
	}

	fun player(player: (Player, CommandArguments) -> Unit): CommandTreeExecution {
		this.player = player
		return this
	}

	fun entity(entity: (Entity, CommandArguments) -> Unit): CommandTreeExecution {
		this.entity = entity
		return this
	}

	fun console(console: (ConsoleCommandSender, CommandArguments) -> Unit): CommandTreeExecution {
		this.console = console
		return this
	}

	fun block(block: (BlockCommandSender, CommandArguments) -> Unit): CommandTreeExecution {
		this.block = block
		return this
	}

	fun proxy(proxy: (ProxiedCommandSender, CommandArguments) -> Unit): CommandTreeExecution {
		this.proxy = proxy
		return this
	}

	fun native(native: (NativeProxyCommandSender, CommandArguments) -> Unit): CommandTreeExecution {
		this.native = native
		return this
	}

	fun executes(tree: Argument<*>) {
		if (any != null) {
			tree.executes(CommandExecutor { sender, args ->
				any?.invoke(sender, args)
			})
			return
		}
		if (player != null) {
			tree.executesPlayer(PlayerCommandExecutor { player, args ->
				this.player?.invoke(player, args)
			})
			return
		}
		if (entity != null) {
			tree.executesEntity(EntityCommandExecutor { entity, args ->
				this.entity?.invoke(entity, args)
			})
			return
		}
		if (console != null) {
			tree.executesConsole(ConsoleCommandExecutor { console, args ->
				this.console?.invoke(console, args)
			})
			return
		}
		if (block != null) {
			tree.executesCommandBlock(CommandBlockCommandExecutor { block, args ->
				this.block?.invoke(block, args)
			})
			return
		}
		if (proxy != null) {
			tree.executesProxy(ProxyCommandExecutor { proxy, args ->
				this.proxy?.invoke(proxy, args)
			})
			return
		}
		if (native != null) {
			tree.executesNative(NativeCommandExecutor { native, args ->
				this.native?.invoke(native, args)
			})
			return
		}
	}

	fun executes(tree: CommandTree) {
		if (any != null) {
			tree.executes(CommandExecutor { sender, args ->
				any?.invoke(sender, args)
			})
			return
		}
		if (player != null) {
			tree.executesPlayer(PlayerCommandExecutor { player, args ->
				this.player?.invoke(player, args)
			})
			return
		}
		if (entity != null) {
			tree.executesEntity(EntityCommandExecutor { entity, args ->
				this.entity?.invoke(entity, args)
			})
			return
		}
		if (console != null) {
			tree.executesConsole(ConsoleCommandExecutor { console, args ->
				this.console?.invoke(console, args)
			})
			return
		}
		if (block != null) {
			tree.executesCommandBlock(CommandBlockCommandExecutor { block, args ->
				this.block?.invoke(block, args)
			})
			return
		}
		if (proxy != null) {
			tree.executesProxy(ProxyCommandExecutor { proxy, args ->
				this.proxy?.invoke(proxy, args)
			})
			return
		}
		if (native != null) {
			tree.executesNative(NativeCommandExecutor { native, args ->
				this.native?.invoke(native, args)
			})
			return
		}
	}
}

class CommandTreeResultingExecution {

	private var any: ((CommandSender, CommandArguments) -> Int)? = null
	private var player: ((Player, CommandArguments) -> Int)? = null
	private var entity: ((Entity, CommandArguments) -> Int)? = null
	private var console: ((ConsoleCommandSender, CommandArguments) -> Int)? = null
	private var block: ((BlockCommandSender, CommandArguments) -> Int)? = null
	private var proxy: ((ProxiedCommandSender, CommandArguments) -> Int)? = null
	private var native: ((NativeProxyCommandSender, CommandArguments) -> Int)? = null

	fun any(any: (CommandSender, CommandArguments) -> Int): CommandTreeResultingExecution {
		this.any = any
		return this
	}

	fun player(player: (Player, CommandArguments) -> Int): CommandTreeResultingExecution {
		this.player = player
		return this
	}

	fun entity(entity: (Entity, CommandArguments) -> Int): CommandTreeResultingExecution {
		this.entity = entity
		return this
	}

	fun console(console: (ConsoleCommandSender, CommandArguments) -> Int): CommandTreeResultingExecution {
		this.console = console
		return this
	}

	fun block(block: (BlockCommandSender, CommandArguments) -> Int): CommandTreeResultingExecution {
		this.block = block
		return this
	}

	fun proxy(proxy: (ProxiedCommandSender, CommandArguments) -> Int): CommandTreeResultingExecution {
		this.proxy = proxy
		return this
	}

	fun native(native: (NativeProxyCommandSender, CommandArguments) -> Int): CommandTreeResultingExecution {
		this.native = native
		return this
	}

	fun executes(tree: Argument<*>) {
		if (any != null) {
			tree.executes(ResultingCommandExecutor { sender, args ->
				any!!.invoke(sender, args)
			})
			return
		}
		if (player != null) {
			tree.executesPlayer(PlayerResultingCommandExecutor { player, args ->
				this.player!!.invoke(player, args)
			})
			return
		}
		if (entity != null) {
			tree.executesEntity(EntityResultingCommandExecutor { entity, args ->
				this.entity!!.invoke(entity, args)
			})
			return
		}
		if (console != null) {
			tree.executesConsole(ConsoleResultingCommandExecutor { console, args ->
				this.console!!.invoke(console, args)
			})
			return
		}
		if (block != null) {
			tree.executesCommandBlock(CommandBlockResultingCommandExecutor { block, args ->
				this.block!!.invoke(block, args)
			})
			return
		}
		if (proxy != null) {
			tree.executesProxy(ProxyResultingCommandExecutor { proxy, args ->
				this.proxy!!.invoke(proxy, args)
			})
			return
		}
		if (native != null) {
			tree.executesNative(NativeResultingCommandExecutor { native, args ->
				this.native!!.invoke(native, args)
			})
			return
		}
	}

	fun executes(tree: CommandTree) {
		if (any != null) {
			tree.executes(ResultingCommandExecutor { sender, args ->
				any!!.invoke(sender, args)
			})
			return
		}
		if (player != null) {
			tree.executesPlayer(PlayerResultingCommandExecutor { player, args ->
				this.player!!.invoke(player, args)
			})
			return
		}
		if (entity != null) {
			tree.executesEntity(EntityResultingCommandExecutor { entity, args ->
				this.entity!!.invoke(entity, args)
			})
			return
		}
		if (console != null) {
			tree.executesConsole(ConsoleResultingCommandExecutor { console, args ->
				this.console!!.invoke(console, args)
			})
			return
		}
		if (block != null) {
			tree.executesCommandBlock(CommandBlockResultingCommandExecutor { block, args ->
				this.block!!.invoke(block, args)
			})
			return
		}
		if (proxy != null) {
			tree.executesProxy(ProxyResultingCommandExecutor { proxy, args ->
				this.proxy!!.invoke(proxy, args)
			})
			return
		}
		if (native != null) {
			tree.executesNative(NativeResultingCommandExecutor { native, args ->
				this.native!!.invoke(native, args)
			})
			return
		}
	}
}
