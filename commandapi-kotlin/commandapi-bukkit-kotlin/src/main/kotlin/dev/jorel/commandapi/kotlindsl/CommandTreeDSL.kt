package dev.jorel.commandapi.kotlindsl

import dev.jorel.commandapi.*
import dev.jorel.commandapi.arguments.*
import dev.jorel.commandapi.wrappers.*
import dev.jorel.commandapi.wrappers.Rotation
import net.kyori.adventure.text.*
import net.kyori.adventure.text.format.*
import net.md_5.bungee.api.chat.*
import org.bukkit.*
import org.bukkit.advancement.*
import org.bukkit.block.*
import org.bukkit.block.data.*
import org.bukkit.command.CommandSender
import org.bukkit.enchantments.*
import org.bukkit.entity.*
import org.bukkit.inventory.*
import org.bukkit.loot.*
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.potion.*
import org.bukkit.scoreboard.*
import java.util.*
import java.util.function.Predicate

inline fun commandTree(name: String, tree: CommandTree.() -> Unit = {}) = CommandTree(name).apply(tree).register()
inline fun commandTree(name: String, namespace: String, tree: CommandTree.() -> Unit = {}) = CommandTree(name).apply(tree).register(namespace)
inline fun commandTree(name: String, namespace: JavaPlugin, tree: CommandTree.() -> Unit = {}) = CommandTree(name).apply(tree).register(namespace)
@Deprecated("This method has been deprecated since version 9.1.0 as it is not needed anymore. See the documentation for more information", ReplaceWith(""), DeprecationLevel.WARNING)
inline fun commandTree(name: String, predicate: Predicate<CommandSender>, tree: CommandTree.() -> Unit = {}) = CommandTree(name).withRequirement(predicate).apply(tree).register()

// CommandTree start
inline fun <reified Type, reified Casted : Type> CommandTree.argument(base: Argument<Type>, crossinline block: Argument<*>.(CommandArgumentGetter<Casted>) -> Unit = {}): CommandTree = then(base.apply {
	this.block { args ->
		args.get(base.nodeName) as Casted
	}
})

inline fun <reified Type, reified Casted : Type> CommandTree.optionalArgument(base: Argument<Type>, crossinline block: Argument<*>.(CommandArgumentGetter<Casted>) -> Unit = {}) = argument<Type, Casted>(base.setOptional(true), block)

// Integer arguments
inline fun CommandTree.integerArgument(nodeName: String, min: Int = Int.MIN_VALUE, max: Int = Int.MAX_VALUE, crossinline block: Argument<*>.(CommandArgumentGetter<Int>) -> Unit = {}) = argument(IntegerArgument(nodeName, min, max), block)
inline fun CommandTree.integerOptionalArgument(nodeName: String, min: Int = Int.MIN_VALUE, max: Int = Int.MAX_VALUE, crossinline block: Argument<*>.(CommandArgumentGetter<Int?>) -> Unit = {}) = optionalArgument(IntegerArgument(nodeName, min, max), block)
inline fun CommandTree.integerRangeArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<IntegerRange>) -> Unit = {}) = argument(IntegerRangeArgument(nodeName), block)
inline fun CommandTree.integerRangeOptionalArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<IntegerRange?>) -> Unit = {}) = optionalArgument(IntegerRangeArgument(nodeName), block)

// Float arguments
inline fun CommandTree.floatArgument(nodeName: String, min: Float = -Float.MAX_VALUE, max: Float = Float.MAX_VALUE, crossinline block: Argument<*>.(CommandArgumentGetter<Float>) -> Unit = {}) = argument(FloatArgument(nodeName, min, max), block)
inline fun CommandTree.floatOptionalArgument(nodeName: String, min: Float = -Float.MAX_VALUE, max: Float = Float.MAX_VALUE, crossinline block: Argument<*>.(CommandArgumentGetter<Float?>) -> Unit = {}) = optionalArgument(FloatArgument(nodeName, min, max), block)
inline fun CommandTree.floatRangeArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<FloatRange>) -> Unit = {}) = argument(FloatRangeArgument(nodeName), block)
inline fun CommandTree.floatRangeOptionalArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<FloatRange?>) -> Unit = {}) = optionalArgument(FloatRangeArgument(nodeName), block)

// Double arguments
inline fun CommandTree.doubleArgument(nodeName: String, min: Double = -Double.MAX_VALUE, max: Double = Double.MAX_VALUE, crossinline block: Argument<*>.(CommandArgumentGetter<Double>) -> Unit = {}) = argument(DoubleArgument(nodeName, min, max), block)
inline fun CommandTree.doubleOptionalArgument(nodeName: String, min: Double = -Double.MAX_VALUE, max: Double = Double.MAX_VALUE, crossinline block: Argument<*>.(CommandArgumentGetter<Double?>) -> Unit = {}) = optionalArgument(DoubleArgument(nodeName, min, max), block)

// Long arguments
inline fun CommandTree.longArgument(nodeName: String, min: Long = Long.MIN_VALUE, max: Long = Long.MAX_VALUE, crossinline block: Argument<*>.(CommandArgumentGetter<Long>) -> Unit = {}) = argument(LongArgument(nodeName, min, max), block)
inline fun CommandTree.longOptionalArgument(nodeName: String, min: Long = Long.MIN_VALUE, max: Long = Long.MAX_VALUE, crossinline block: Argument<*>.(CommandArgumentGetter<Long?>) -> Unit = {}) = optionalArgument(LongArgument(nodeName, min, max), block)

// Boolean argument
inline fun CommandTree.booleanArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<Boolean>) -> Unit = {}) = argument(BooleanArgument(nodeName), block)
inline fun CommandTree.booleanOptionalArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<Boolean?>) -> Unit = {}) = optionalArgument(BooleanArgument(nodeName), block)

// String arguments
inline fun CommandTree.stringArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<String>) -> Unit = {}) = argument(StringArgument(nodeName), block)
inline fun CommandTree.stringOptionalArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<String?>) -> Unit = {}) = optionalArgument(StringArgument(nodeName), block)
inline fun CommandTree.textArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<String>) -> Unit = {}) = argument(TextArgument(nodeName), block)
inline fun CommandTree.textOptionalArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<String?>) -> Unit = {}) = optionalArgument(TextArgument(nodeName), block)
inline fun CommandTree.greedyStringArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<String>) -> Unit = {}) = argument(GreedyStringArgument(nodeName), block)
inline fun CommandTree.greedyStringOptionalArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<String?>) -> Unit = {}) = optionalArgument(GreedyStringArgument(nodeName), block)

// Positional arguments
inline fun CommandTree.locationArgument(nodeName: String, locationType: LocationType = LocationType.PRECISE_POSITION, centerPosition: Boolean = true, crossinline block: Argument<*>.(CommandArgumentGetter<Location>) -> Unit = {}) = argument(LocationArgument(nodeName, locationType, centerPosition), block)
inline fun CommandTree.locationOptionalArgument(nodeName: String, locationType: LocationType = LocationType.PRECISE_POSITION, centerPosition: Boolean = true, crossinline block: Argument<*>.(CommandArgumentGetter<Location?>) -> Unit = {}) = optionalArgument(LocationArgument(nodeName, locationType, centerPosition), block)
inline fun CommandTree.location2DArgument(nodeName: String, locationType: LocationType = LocationType.PRECISE_POSITION, centerPosition: Boolean = true, crossinline block: Argument<*>.(CommandArgumentGetter<Location2D>) -> Unit = {}) = argument(Location2DArgument(nodeName, locationType, centerPosition), block)
inline fun CommandTree.location2DOptionalArgument(nodeName: String, locationType: LocationType = LocationType.PRECISE_POSITION, centerPosition: Boolean = true, crossinline block: Argument<*>.(CommandArgumentGetter<Location2D?>) -> Unit = {}) = optionalArgument(Location2DArgument(nodeName, locationType, centerPosition), block)
inline fun CommandTree.rotationArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<Rotation>) -> Unit = {}) = argument(RotationArgument(nodeName), block)
inline fun CommandTree.rotationOptionalArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<Rotation?>) -> Unit = {}) = optionalArgument(RotationArgument(nodeName), block)
inline fun CommandTree.axisArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<EnumSet<Axis>>) -> Unit = {}) = argument(AxisArgument(nodeName), block)
inline fun CommandTree.axisOptionalArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<EnumSet<Axis>?>) -> Unit = {}) = optionalArgument(AxisArgument(nodeName), block)

// Chat arguments
inline fun CommandTree.chatColorArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<ChatColor>) -> Unit = {}) = argument(ChatColorArgument(nodeName), block)
inline fun CommandTree.chatColorOptionalArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<ChatColor?>) -> Unit = {}) = optionalArgument(ChatColorArgument(nodeName), block)
inline fun CommandTree.chatComponentArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<Array<BaseComponent>>) -> Unit = {}) = argument(ChatComponentArgument(nodeName), block)
inline fun CommandTree.chatComponentOptionalArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<Array<BaseComponent>?>) -> Unit = {}) = optionalArgument(ChatComponentArgument(nodeName), block)
inline fun CommandTree.chatArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<Array<BaseComponent>>) -> Unit = {}) = argument(ChatArgument(nodeName), block)
inline fun CommandTree.chatOptionalArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<Array<BaseComponent>?>) -> Unit = {}) = optionalArgument(ChatArgument(nodeName), block)
inline fun CommandTree.adventureChatColorArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<TextColor>) -> Unit = {}) = argument(AdventureChatColorArgument(nodeName), block)
inline fun CommandTree.adventureChatColorOptionalArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<TextColor?>) -> Unit = {}) = optionalArgument(AdventureChatColorArgument(nodeName), block)
inline fun CommandTree.adventureChatComponentArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<Component>) -> Unit = {}) = argument(AdventureChatComponentArgument(nodeName), block)
inline fun CommandTree.adventureChatComponentOptionalArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<Component?>) -> Unit = {}) = optionalArgument(AdventureChatComponentArgument(nodeName), block)
inline fun CommandTree.adventureChatArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<Component>) -> Unit = {}) = argument(AdventureChatArgument(nodeName), block)
inline fun CommandTree.adventureChatOptionalArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<Component?>) -> Unit = {}) = optionalArgument(AdventureChatArgument(nodeName), block)

// Entity & Player arguments
inline fun CommandTree.entitySelectorArgumentOneEntity(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<Entity>) -> Unit = {}) = argument(EntitySelectorArgument.OneEntity(nodeName), block)
inline fun CommandTree.entitySelectorOptionalArgumentOneEntity(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<Entity?>) -> Unit = {}) = optionalArgument(EntitySelectorArgument.OneEntity(nodeName), block)
inline fun CommandTree.entitySelectorArgumentManyEntities(nodeName: String, allowEmpty: Boolean = true, crossinline block: Argument<*>.(CommandArgumentGetter<Collection<Entity>>) -> Unit = {}) = argument(EntitySelectorArgument.ManyEntities(nodeName, allowEmpty), block)
inline fun CommandTree.entitySelectorOptionalArgumentManyEntities(nodeName: String, allowEmpty: Boolean = true, crossinline block: Argument<*>.(CommandArgumentGetter<Collection<Entity>?>) -> Unit = {}) = optionalArgument(EntitySelectorArgument.ManyEntities(nodeName, allowEmpty), block)
inline fun CommandTree.entitySelectorArgumentOnePlayer(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<Player>) -> Unit = {}) = argument(EntitySelectorArgument.OnePlayer(nodeName), block)
inline fun CommandTree.entitySelectorOptionalArgumentOnePlayer(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<Player?>) -> Unit = {}) = optionalArgument(EntitySelectorArgument.OnePlayer(nodeName), block)
inline fun CommandTree.entitySelectorArgumentManyPlayers(nodeName: String, allowEmpty: Boolean = true, crossinline block: Argument<*>.(CommandArgumentGetter<Collection<Player>>) -> Unit = {}) = argument(EntitySelectorArgument.ManyPlayers(nodeName, allowEmpty), block)
inline fun CommandTree.entitySelectorOptionalArgumentManyPlayers(nodeName: String, allowEmpty: Boolean = true, crossinline block: Argument<*>.(CommandArgumentGetter<Collection<Player>?>) -> Unit = {}) = optionalArgument(EntitySelectorArgument.ManyPlayers(nodeName, allowEmpty), block)
inline fun CommandTree.playerArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<Player>) -> Unit = {}) = argument(PlayerArgument(nodeName), block)
inline fun CommandTree.playerOptionalArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<Player?>) -> Unit = {}) = optionalArgument(PlayerArgument(nodeName), block)
inline fun CommandTree.offlinePlayerArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<OfflinePlayer>) -> Unit = {}) = argument(OfflinePlayerArgument(nodeName), block)
inline fun CommandTree.offlinePlayerOptionalArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<OfflinePlayer?>) -> Unit = {}) = optionalArgument(OfflinePlayerArgument(nodeName), block)
inline fun CommandTree.entityTypeArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<EntityType>) -> Unit = {}) = argument(EntityTypeArgument(nodeName), block)
inline fun CommandTree.entityTypeOptionalArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<EntityType?>) -> Unit = {}) = optionalArgument(EntityTypeArgument(nodeName), block)

// Scoreboard arguments
inline fun CommandTree.scoreHolderArgumentSingle(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<String>) -> Unit = {}) = argument(ScoreHolderArgument.Single(nodeName), block)
inline fun CommandTree.scoreHolderOptionalArgumentSingle(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<String?>) -> Unit = {}) = optionalArgument(ScoreHolderArgument.Single(nodeName), block)
inline fun CommandTree.scoreHolderArgumentMultiple(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<Collection<String>>) -> Unit = {}) = argument(ScoreHolderArgument.Multiple(nodeName), block)
inline fun CommandTree.scoreHolderOptionalArgumentMultiple(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<Collection<String>?>) -> Unit = {}) = optionalArgument(ScoreHolderArgument.Multiple(nodeName), block)
inline fun CommandTree.scoreboardSlotArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<ScoreboardSlot>) -> Unit = {}) = argument(ScoreboardSlotArgument(nodeName), block)
inline fun CommandTree.scoreboardSlotOptionalArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<ScoreboardSlot?>) -> Unit = {}) = optionalArgument(ScoreboardSlotArgument(nodeName), block)
inline fun CommandTree.objectiveArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<Objective>) -> Unit = {}) = argument(ObjectiveArgument(nodeName), block)
inline fun CommandTree.objectiveOptionalArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<Objective?>) -> Unit = {}) = optionalArgument(ObjectiveArgument(nodeName), block)
inline fun CommandTree.objectiveCriteriaArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<String>) -> Unit = {}) = argument(ObjectiveCriteriaArgument(nodeName), block)
inline fun CommandTree.objectiveCriteriaOptionalArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<String?>) -> Unit = {}) = optionalArgument(ObjectiveCriteriaArgument(nodeName), block)
inline fun CommandTree.teamArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<Team>) -> Unit = {}) = argument(TeamArgument(nodeName), block)
inline fun CommandTree.teamOptionalArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<Team?>) -> Unit = {}) = optionalArgument(TeamArgument(nodeName), block)

// Miscellaneous arguments
inline fun CommandTree.angleArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<Float>) -> Unit = {}) = argument(AngleArgument(nodeName), block)
inline fun CommandTree.angleOptionalArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<Float?>) -> Unit = {}) = optionalArgument(AngleArgument(nodeName), block)
inline fun CommandTree.advancementArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<Advancement>) -> Unit = {}) = argument(AdvancementArgument(nodeName), block)
inline fun CommandTree.advancementOptionalArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<Advancement?>) -> Unit = {}) = optionalArgument(AdvancementArgument(nodeName), block)

inline fun CommandTree.biomeArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<Biome>) -> Unit = {}) = argument(BiomeArgument(nodeName), block)
inline fun CommandTree.biomeOptionalArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<Biome?>) -> Unit = {}) = optionalArgument(BiomeArgument(nodeName), block)
inline fun CommandTree.biomeNamespacedKeyArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<NamespacedKey>) -> Unit = {}) = argument(BiomeArgument.NamespacedKey(nodeName), block)
inline fun CommandTree.biomeNamespacedKeyOptionalArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<NamespacedKey?>) -> Unit = {}) = optionalArgument(BiomeArgument.NamespacedKey(nodeName), block)

inline fun CommandTree.blockStateArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<BlockData>) -> Unit = {}) = argument(BlockStateArgument(nodeName), block)
inline fun CommandTree.blockStateOptionalArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<BlockData?>) -> Unit = {}) = optionalArgument(BlockStateArgument(nodeName), block)
inline fun CommandTree.commandArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<CommandResult>) -> Unit = {}) = argument(CommandArgument(nodeName), block)
inline fun CommandTree.commandOptionalArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<CommandResult?>) -> Unit = {}) = optionalArgument(CommandArgument(nodeName), block)
inline fun CommandTree.enchantmentArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<Enchantment>) -> Unit = {}) = argument(EnchantmentArgument(nodeName), block)
inline fun CommandTree.enchantmentOptionalArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<Enchantment?>) -> Unit = {}) = optionalArgument(EnchantmentArgument(nodeName), block)

inline fun CommandTree.itemStackArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<ItemStack>) -> Unit = {}) = argument(ItemStackArgument(nodeName), block)
inline fun CommandTree.itemStackOptionalArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<ItemStack?>) -> Unit = {}) = optionalArgument(ItemStackArgument(nodeName), block)
inline fun CommandTree.lootTableArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<LootTable>) -> Unit = {}) = argument(LootTableArgument(nodeName), block)
inline fun CommandTree.lootTableOptionalArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<LootTable?>) -> Unit = {}) = optionalArgument(LootTableArgument(nodeName), block)
inline fun CommandTree.mathOperationArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<MathOperation>) -> Unit = {}) = argument(MathOperationArgument(nodeName), block)
inline fun CommandTree.mathOperationOptionalArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<MathOperation?>) -> Unit = {}) = optionalArgument(MathOperationArgument(nodeName), block)
inline fun CommandTree.namespacedKeyArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<NamespacedKey>) -> Unit = {}) = argument(NamespacedKeyArgument(nodeName), block)
inline fun CommandTree.namespacedKeyOptionalArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<NamespacedKey?>) -> Unit = {}) = optionalArgument(NamespacedKeyArgument(nodeName), block)
inline fun CommandTree.particleArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<ParticleData<*>>) -> Unit = {}) = argument(ParticleArgument(nodeName), block)
inline fun CommandTree.particleOptionalArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<ParticleData<*>?>) -> Unit = {}) = optionalArgument(ParticleArgument(nodeName), block)
inline fun CommandTree.potionEffectArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<PotionEffectType>) -> Unit = {}) = argument(PotionEffectArgument(nodeName), block)
inline fun CommandTree.potionEffectOptionalArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<PotionEffectType?>) -> Unit = {}) = optionalArgument(PotionEffectArgument(nodeName), block)
inline fun CommandTree.recipeArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<Recipe>) -> Unit = {}) = argument(RecipeArgument(nodeName), block)
inline fun CommandTree.recipeOptionalArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<Recipe?>) -> Unit = {}) = optionalArgument(RecipeArgument(nodeName), block)

inline fun CommandTree.soundArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<Sound>) -> Unit = {}) = argument(SoundArgument(nodeName), block)
inline fun CommandTree.soundOptionalArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<Sound?>) -> Unit = {}) = optionalArgument(SoundArgument(nodeName), block)
inline fun CommandTree.soundNamespacedKeyArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<NamespacedKey>) -> Unit = {}) = argument(SoundArgument.NamespacedKey(nodeName), block)
inline fun CommandTree.soundNamespacedKeyOptionalArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<NamespacedKey?>) -> Unit = {}) = optionalArgument(SoundArgument.NamespacedKey(nodeName), block)

inline fun CommandTree.timeArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<Int>) -> Unit = {}) = argument(TimeArgument(nodeName), block)
inline fun CommandTree.timeOptionalArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<Int?>) -> Unit = {}) = optionalArgument(TimeArgument(nodeName), block)
inline fun CommandTree.uuidArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<UUID>) -> Unit = {}) = argument(UUIDArgument(nodeName), block)
inline fun CommandTree.uuidOptionalArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<UUID?>) -> Unit = {}) = optionalArgument(UUIDArgument(nodeName), block)
inline fun CommandTree.worldArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<World>) -> Unit = {}) = argument(WorldArgument(nodeName), block)
inline fun CommandTree.worldOptionalArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<World?>) -> Unit = {}) = optionalArgument(WorldArgument(nodeName), block)

// Predicate arguments
inline fun CommandTree.blockPredicateArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<Predicate<Block>>) -> Unit = {}) = argument(BlockPredicateArgument(nodeName), block)
inline fun CommandTree.blockPredicateOptionalArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<Predicate<Block>?>) -> Unit = {}) = optionalArgument(BlockPredicateArgument(nodeName), block)
inline fun CommandTree.itemStackPredicateArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<Predicate<ItemStack>>) -> Unit = {}) = argument(ItemStackPredicateArgument(nodeName), block)
inline fun CommandTree.itemStackPredicateOptionalArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<Predicate<ItemStack>?>) -> Unit = {}) = optionalArgument(ItemStackPredicateArgument(nodeName), block)

// NBT arguments
inline fun <reified NBTContainer> CommandTree.nbtCompoundArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<NBTContainer>) -> Unit = {}) = argument(NBTCompoundArgument<NBTContainer>(nodeName), block)
inline fun <reified NBTContainer> CommandTree.nbtCompoundOptionalArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<NBTContainer?>) -> Unit = {}) = optionalArgument(NBTCompoundArgument<NBTContainer>(nodeName), block)

// Literal arguments
inline fun CommandTree.literalArgument(literal: String, crossinline block: Argument<*>.(CommandArgumentGetter<String>) -> Unit = {}) = argument(LiteralArgument.of(literal, literal), block)
inline fun CommandTree.literalOptionalArgument(literal: String, crossinline block: Argument<*>.(CommandArgumentGetter<String?>) -> Unit = {}) = optionalArgument(LiteralArgument.of(literal, literal), block)
inline fun CommandTree.literalArgument(nodeName: String, literal: String, crossinline block: Argument<*>.(CommandArgumentGetter<String>) -> Unit = {}) = argument(LiteralArgument.of(nodeName, literal), block)
inline fun CommandTree.literalOptionalArgument(nodeName: String, literal: String, crossinline block: Argument<*>.(CommandArgumentGetter<String?>) -> Unit = {}) = optionalArgument(LiteralArgument.of(nodeName, literal), block)

@Deprecated("This version has been deprecated since version 9.0.2", ReplaceWith("multiLiteralArgument(nodeName, listOf(literals))", "dev.jorel.commandapi.kotlindsl.*"), DeprecationLevel.WARNING)
inline fun CommandTree.multiLiteralArgument(vararg literals: String, crossinline block: Argument<*>.(CommandArgumentGetter<String>) -> Unit = {}) = argument(MultiLiteralArgument(literals), block)
@Deprecated("This version has been deprecated since version 9.0.2", ReplaceWith("multiLiteralArgument(nodeName, listOf(literals))", "dev.jorel.commandapi.kotlindsl.*"), DeprecationLevel.WARNING)
inline fun CommandTree.multiLiteralOptionalArgument(vararg literals: String, crossinline block: Argument<*>.(CommandArgumentGetter<String?>) -> Unit = {}) = optionalArgument(MultiLiteralArgument(literals), block)
@Deprecated("This method has been deprecated since version 9.1.0", ReplaceWith("multiLiteralArgument(nodeName, literals)", "dev.jorel.commandapi.kotlindsl.*"), DeprecationLevel.WARNING)
inline fun CommandTree.multiLiteralArgument(nodeName: String, literals: List<String>, crossinline block: Argument<*>.(CommandArgumentGetter<String>) -> Unit = {}) = argument(MultiLiteralArgument(nodeName, literals), block)
@Deprecated("This method has been deprecated since version 9.1.0", ReplaceWith("multiLiteralArgument(nodeName, literals)", "dev.jorel.commandapi.kotlindsl.*"), DeprecationLevel.WARNING)
inline fun CommandTree.multiLiteralOptionalArgument(nodeName: String, literals: List<String>, crossinline block: Argument<*>.(CommandArgumentGetter<String?>) -> Unit = {}) = optionalArgument(MultiLiteralArgument(nodeName, literals), block)

inline fun CommandTree.multiLiteralArgument(nodeName: String, vararg literals: String, crossinline block: Argument<*>.(CommandArgumentGetter<String>) -> Unit = {}) = argument(MultiLiteralArgument(nodeName, *literals), block)
inline fun CommandTree.multiLiteralOptionalArgument(nodeName: String, vararg literals: String, crossinline block: Argument<*>.(CommandArgumentGetter<String?>) -> Unit = {}) = optionalArgument(MultiLiteralArgument(nodeName, *literals), block)

// Function arguments
inline fun CommandTree.functionArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<Array<FunctionWrapper>>) -> Unit = {}) = argument(FunctionArgument(nodeName), block)
inline fun CommandTree.functionOptionalArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<Array<FunctionWrapper>?>) -> Unit = {}) = optionalArgument(FunctionArgument(nodeName), block)

// ArgumentTree start
inline fun <reified Type, reified Casted : Type> Argument<*>.argument(base: Argument<Type>, crossinline block: Argument<*>.(CommandArgumentGetter<Casted>) -> Unit = {}): Argument<*> = then(base.apply {
	this.block { args ->
		args.get(base.nodeName) as Casted
	}
})

inline fun <reified Type, reified Casted : Type> Argument<*>.optionalArgument(base: Argument<Type>, crossinline block: Argument<*>.(CommandArgumentGetter<Casted>) -> Unit = {}) = argument(base.setOptional(true), block)

// Integer arguments
inline fun Argument<*>.integerArgument(nodeName: String, min: Int = Int.MIN_VALUE, max: Int = Int.MAX_VALUE, crossinline block: Argument<*>.(CommandArgumentGetter<Int>) -> Unit = {}) = argument(IntegerArgument(nodeName, min, max), block)
inline fun Argument<*>.integerOptionalArgument(nodeName: String, min: Int = Int.MIN_VALUE, max: Int = Int.MAX_VALUE, crossinline block: Argument<*>.(CommandArgumentGetter<Int?>) -> Unit = {}) = optionalArgument(IntegerArgument(nodeName, min, max), block)
inline fun Argument<*>.integerRangeArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<IntegerRange>) -> Unit = {}) = argument(IntegerRangeArgument(nodeName), block)
inline fun Argument<*>.integerRangeOptionalArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<IntegerRange?>) -> Unit = {}) = optionalArgument(IntegerRangeArgument(nodeName), block)

// Float arguments
inline fun Argument<*>.floatArgument(nodeName: String, min: Float = -Float.MAX_VALUE, max: Float = Float.MAX_VALUE, crossinline block: Argument<*>.(CommandArgumentGetter<Float>) -> Unit = {}) = argument(FloatArgument(nodeName, min, max), block)
inline fun Argument<*>.floatOptionalArgument(nodeName: String, min: Float = -Float.MAX_VALUE, max: Float = Float.MAX_VALUE, crossinline block: Argument<*>.(CommandArgumentGetter<Float?>) -> Unit = {}) = optionalArgument(FloatArgument(nodeName, min, max), block)
inline fun Argument<*>.floatRangeArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<FloatRange>) -> Unit = {}) = argument(FloatRangeArgument(nodeName), block)
inline fun Argument<*>.floatRangeOptionalArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<FloatRange?>) -> Unit = {}) = optionalArgument(FloatRangeArgument(nodeName), block)

// Double arguments
inline fun Argument<*>.doubleArgument(nodeName: String, min: Double = -Double.MAX_VALUE, max: Double = Double.MAX_VALUE, crossinline block: Argument<*>.(CommandArgumentGetter<Double>) -> Unit = {}) = argument(DoubleArgument(nodeName, min, max), block)
inline fun Argument<*>.doubleOptionalArgument(nodeName: String, min: Double = -Double.MAX_VALUE, max: Double = Double.MAX_VALUE, crossinline block: Argument<*>.(CommandArgumentGetter<Double?>) -> Unit = {}) = optionalArgument(DoubleArgument(nodeName, min, max), block)

// Long arguments
inline fun Argument<*>.longArgument(nodeName: String, min: Long = Long.MIN_VALUE, max: Long = Long.MAX_VALUE, crossinline block: Argument<*>.(CommandArgumentGetter<Long>) -> Unit = {}) = argument(LongArgument(nodeName, min, max), block)
inline fun Argument<*>.longOptionalArgument(nodeName: String, min: Long = Long.MIN_VALUE, max: Long = Long.MAX_VALUE, crossinline block: Argument<*>.(CommandArgumentGetter<Long?>) -> Unit = {}) = optionalArgument(LongArgument(nodeName, min, max), block)

// Boolean argument
inline fun Argument<*>.booleanArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<Boolean>) -> Unit = {}) = argument(BooleanArgument(nodeName), block)
inline fun Argument<*>.booleanOptionalArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<Boolean?>) -> Unit = {}) = optionalArgument(BooleanArgument(nodeName), block)

// String arguments
inline fun Argument<*>.stringArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<String>) -> Unit = {}) = argument(StringArgument(nodeName), block)
inline fun Argument<*>.stringOptionalArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<String?>) -> Unit = {}) = optionalArgument(StringArgument(nodeName), block)
inline fun Argument<*>.textArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<String>) -> Unit = {}) = argument(TextArgument(nodeName), block)
inline fun Argument<*>.textOptionalArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<String?>) -> Unit = {}) = optionalArgument(TextArgument(nodeName), block)
inline fun Argument<*>.greedyStringArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<String>) -> Unit = {}) = argument(GreedyStringArgument(nodeName), block)
inline fun Argument<*>.greedyStringOptionalArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<String?>) -> Unit = {}) = optionalArgument(GreedyStringArgument(nodeName), block)

// Positional arguments
inline fun Argument<*>.locationArgument(nodeName: String, locationType: LocationType = LocationType.PRECISE_POSITION, centerPosition: Boolean = true, crossinline block: Argument<*>.(CommandArgumentGetter<Location>) -> Unit = {}) = argument(LocationArgument(nodeName, locationType, centerPosition), block)
inline fun Argument<*>.locationOptionalArgument(nodeName: String, locationType: LocationType = LocationType.PRECISE_POSITION, centerPosition: Boolean = true, crossinline block: Argument<*>.(CommandArgumentGetter<Location?>) -> Unit = {}) = optionalArgument(LocationArgument(nodeName, locationType, centerPosition), block)
inline fun Argument<*>.location2DArgument(nodeName: String, locationType: LocationType = LocationType.PRECISE_POSITION, centerPosition: Boolean = true, crossinline block: Argument<*>.(CommandArgumentGetter<Location2D>) -> Unit = {}) = argument(Location2DArgument(nodeName, locationType, centerPosition), block)
inline fun Argument<*>.location2DOptionalArgument(nodeName: String, locationType: LocationType = LocationType.PRECISE_POSITION, centerPosition: Boolean = true, crossinline block: Argument<*>.(CommandArgumentGetter<Location2D?>) -> Unit = {}) = optionalArgument(Location2DArgument(nodeName, locationType, centerPosition), block)
inline fun Argument<*>.rotationArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<Rotation>) -> Unit = {}) = argument(RotationArgument(nodeName), block)
inline fun Argument<*>.rotationOptionalArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<Rotation?>) -> Unit = {}) = optionalArgument(RotationArgument(nodeName), block)
inline fun Argument<*>.axisArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<EnumSet<Axis>>) -> Unit = {}) = argument(AxisArgument(nodeName), block)
inline fun Argument<*>.axisOptionalArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<EnumSet<Axis>?>) -> Unit = {}) = optionalArgument(AxisArgument(nodeName), block)

// Chat arguments
inline fun Argument<*>.chatColorArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<ChatColor>) -> Unit = {}) = argument(ChatColorArgument(nodeName), block)
inline fun Argument<*>.chatColorOptionalArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<ChatColor?>) -> Unit = {}) = optionalArgument(ChatColorArgument(nodeName), block)
inline fun Argument<*>.chatComponentArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<Array<BaseComponent>>) -> Unit = {}) = argument(ChatComponentArgument(nodeName), block)
inline fun Argument<*>.chatComponentOptionalArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<Array<BaseComponent>?>) -> Unit = {}) = optionalArgument(ChatComponentArgument(nodeName), block)
inline fun Argument<*>.chatArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<Array<BaseComponent>>) -> Unit = {}) = argument(ChatArgument(nodeName), block)
inline fun Argument<*>.chatOptionalArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<Array<BaseComponent>?>) -> Unit = {}) = optionalArgument(ChatArgument(nodeName), block)
inline fun Argument<*>.adventureChatColorArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<TextColor>) -> Unit = {}) = argument(AdventureChatColorArgument(nodeName), block)
inline fun Argument<*>.adventureChatColorOptionalArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<TextColor?>) -> Unit = {}) = optionalArgument(AdventureChatColorArgument(nodeName), block)
inline fun Argument<*>.adventureChatComponentArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<Component>) -> Unit = {}) = argument(AdventureChatComponentArgument(nodeName), block)
inline fun Argument<*>.adventureChatComponentOptionalArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<Component?>) -> Unit = {}) = optionalArgument(AdventureChatComponentArgument(nodeName), block)
inline fun Argument<*>.adventureChatArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<Component>) -> Unit = {}) = argument(AdventureChatArgument(nodeName), block)
inline fun Argument<*>.adventureChatOptionalArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<Component?>) -> Unit = {}) = optionalArgument(AdventureChatArgument(nodeName), block)

// Entity & Player arguments
inline fun Argument<*>.entitySelectorArgumentOneEntity(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<Entity>) -> Unit = {}) = argument(EntitySelectorArgument.OneEntity(nodeName), block)
inline fun Argument<*>.entitySelectorOptionalArgumentOneEntity(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<Entity?>) -> Unit = {}) = optionalArgument(EntitySelectorArgument.OneEntity(nodeName), block)
inline fun Argument<*>.entitySelectorArgumentManyEntities(nodeName: String, allowEmpty: Boolean = true, crossinline block: Argument<*>.(CommandArgumentGetter<Collection<Entity>>) -> Unit = {}) = argument(EntitySelectorArgument.ManyEntities(nodeName, allowEmpty), block)
inline fun Argument<*>.entitySelectorOptionalArgumentManyEntities(nodeName: String, allowEmpty: Boolean = true, crossinline block: Argument<*>.(CommandArgumentGetter<Collection<Entity>?>) -> Unit = {}) = optionalArgument(EntitySelectorArgument.ManyEntities(nodeName, allowEmpty), block)
inline fun Argument<*>.entitySelectorArgumentOnePlayer(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<Player>) -> Unit = {}) = argument(EntitySelectorArgument.OnePlayer(nodeName), block)
inline fun Argument<*>.entitySelectorOptionalArgumentOnePlayer(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<Player?>) -> Unit = {}) = optionalArgument(EntitySelectorArgument.OnePlayer(nodeName), block)
inline fun Argument<*>.entitySelectorArgumentManyPlayers(nodeName: String, allowEmpty: Boolean = true, crossinline block: Argument<*>.(CommandArgumentGetter<Collection<Player>>) -> Unit = {}) = argument(EntitySelectorArgument.ManyPlayers(nodeName, allowEmpty), block)
inline fun Argument<*>.entitySelectorOptionalArgumentManyPlayers(nodeName: String, allowEmpty: Boolean = true, crossinline block: Argument<*>.(CommandArgumentGetter<Collection<Player>?>) -> Unit = {}) = optionalArgument(EntitySelectorArgument.ManyPlayers(nodeName, allowEmpty), block)
inline fun Argument<*>.playerArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<Player>) -> Unit = {}) = argument(PlayerArgument(nodeName), block)
inline fun Argument<*>.playerOptionalArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<Player?>) -> Unit = {}) = optionalArgument(PlayerArgument(nodeName), block)
inline fun Argument<*>.offlinePlayerArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<OfflinePlayer>) -> Unit = {}) = argument(OfflinePlayerArgument(nodeName), block)
inline fun Argument<*>.offlinePlayerOptionalArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<OfflinePlayer?>) -> Unit = {}) = optionalArgument(OfflinePlayerArgument(nodeName), block)
inline fun Argument<*>.entityTypeArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<EntityType>) -> Unit = {}) = argument(EntityTypeArgument(nodeName), block)
inline fun Argument<*>.entityTypeOptionalArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<EntityType?>) -> Unit = {}) = optionalArgument(EntityTypeArgument(nodeName), block)

// Scoreboard arguments
inline fun Argument<*>.scoreHolderArgumentSingle(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<String>) -> Unit = {}) = argument(ScoreHolderArgument.Single(nodeName), block)
inline fun Argument<*>.scoreHolderOptionalArgumentSingle(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<String?>) -> Unit = {}) = optionalArgument(ScoreHolderArgument.Single(nodeName), block)
inline fun Argument<*>.scoreHolderArgumentMultiple(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<Collection<String>>) -> Unit = {}) = argument(ScoreHolderArgument.Multiple(nodeName), block)
inline fun Argument<*>.scoreHolderOptionalArgumentMultiple(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<Collection<String>?>) -> Unit = {}) = optionalArgument(ScoreHolderArgument.Multiple(nodeName), block)
inline fun Argument<*>.scoreboardSlotArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<ScoreboardSlot>) -> Unit = {}) = argument(ScoreboardSlotArgument(nodeName), block)
inline fun Argument<*>.scoreboardSlotOptionalArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<ScoreboardSlot?>) -> Unit = {}) = optionalArgument(ScoreboardSlotArgument(nodeName), block)
inline fun Argument<*>.objectiveArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<Objective>) -> Unit = {}) = argument(ObjectiveArgument(nodeName), block)
inline fun Argument<*>.objectiveOptionalArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<Objective?>) -> Unit = {}) = optionalArgument(ObjectiveArgument(nodeName), block)
inline fun Argument<*>.objectiveCriteriaArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<String>) -> Unit = {}) = argument(ObjectiveCriteriaArgument(nodeName), block)
inline fun Argument<*>.objectiveCriteriaOptionalArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<String?>) -> Unit = {}) = optionalArgument(ObjectiveCriteriaArgument(nodeName), block)
inline fun Argument<*>.teamArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<Team>) -> Unit = {}) = argument(TeamArgument(nodeName), block)
inline fun Argument<*>.teamOptionalArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<Team?>) -> Unit = {}) = optionalArgument(TeamArgument(nodeName), block)

// Miscellaneous arguments
inline fun Argument<*>.angleArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<Float>) -> Unit = {}) = argument(AngleArgument(nodeName), block)
inline fun Argument<*>.angleOptionalArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<Float?>) -> Unit = {}) = optionalArgument(AngleArgument(nodeName), block)
inline fun Argument<*>.advancementArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<Advancement>) -> Unit = {}) = argument(AdvancementArgument(nodeName), block)
inline fun Argument<*>.advancementOptionalArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<Advancement?>) -> Unit = {}) = optionalArgument(AdvancementArgument(nodeName), block)

inline fun Argument<*>.biomeArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<Biome>) -> Unit = {}) = argument(BiomeArgument(nodeName), block)
inline fun Argument<*>.biomeOptionalArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<Biome?>) -> Unit = {}) = optionalArgument(BiomeArgument(nodeName), block)
inline fun Argument<*>.biomeNamespacedKeyArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<NamespacedKey>) -> Unit = {}) = argument(BiomeArgument.NamespacedKey(nodeName), block)
inline fun Argument<*>.biomeNamespacedKeyOptionalArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<NamespacedKey?>) -> Unit = {}) = optionalArgument(BiomeArgument.NamespacedKey(nodeName), block)

inline fun Argument<*>.blockStateArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<BlockData>) -> Unit = {}) = argument(BlockStateArgument(nodeName), block)
inline fun Argument<*>.blockStateOptionalArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<BlockData?>) -> Unit = {}) = optionalArgument(BlockStateArgument(nodeName), block)
inline fun Argument<*>.commandArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<CommandResult>) -> Unit = {}) = argument(CommandArgument(nodeName), block)
inline fun Argument<*>.commandOptionalArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<CommandResult?>) -> Unit = {}) = optionalArgument(CommandArgument(nodeName), block)
inline fun Argument<*>.enchantmentArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<Enchantment>) -> Unit = {}) = argument(EnchantmentArgument(nodeName), block)
inline fun Argument<*>.enchantmentOptionalArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<Enchantment?>) -> Unit = {}) = optionalArgument(EnchantmentArgument(nodeName), block)

inline fun Argument<*>.itemStackArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<ItemStack>) -> Unit = {}) = argument(ItemStackArgument(nodeName), block)
inline fun Argument<*>.itemStackOptionalArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<ItemStack?>) -> Unit = {}) = optionalArgument(ItemStackArgument(nodeName), block)
inline fun Argument<*>.lootTableArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<LootTable>) -> Unit = {}) = argument(LootTableArgument(nodeName), block)
inline fun Argument<*>.lootTableOptionalArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<LootTable?>) -> Unit = {}) = optionalArgument(LootTableArgument(nodeName), block)
inline fun Argument<*>.mathOperationArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<MathOperation>) -> Unit = {}) = argument(MathOperationArgument(nodeName), block)
inline fun Argument<*>.mathOperationOptionalArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<MathOperation?>) -> Unit = {}) = optionalArgument(MathOperationArgument(nodeName), block)
inline fun Argument<*>.namespacedKeyArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<NamespacedKey>) -> Unit = {}) = argument(NamespacedKeyArgument(nodeName), block)
inline fun Argument<*>.namespacedKeyOptionalArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<NamespacedKey?>) -> Unit = {}) = optionalArgument(NamespacedKeyArgument(nodeName), block)
inline fun Argument<*>.particleArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<ParticleData<*>>) -> Unit = {}) = argument(ParticleArgument(nodeName), block)
inline fun Argument<*>.particleOptionalArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<ParticleData<*>?>) -> Unit = {}) = optionalArgument(ParticleArgument(nodeName), block)
inline fun Argument<*>.potionEffectArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<PotionEffectType>) -> Unit = {}) = argument(PotionEffectArgument(nodeName), block)
inline fun Argument<*>.potionEffectOptionalArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<PotionEffectType?>) -> Unit = {}) = optionalArgument(PotionEffectArgument(nodeName), block)
inline fun Argument<*>.recipeArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<Recipe>) -> Unit = {}) = argument(RecipeArgument(nodeName), block)
inline fun Argument<*>.recipeOptionalArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<Recipe?>) -> Unit = {}) = optionalArgument(RecipeArgument(nodeName), block)

inline fun Argument<*>.soundArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<Sound>) -> Unit = {}) = argument(SoundArgument(nodeName), block)
inline fun Argument<*>.soundOptionalArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<Sound?>) -> Unit = {}) = optionalArgument(SoundArgument(nodeName), block)
inline fun Argument<*>.soundNamespacedKeyArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<NamespacedKey>) -> Unit = {}) = argument(SoundArgument.NamespacedKey(nodeName), block)
inline fun Argument<*>.soundNamespacedKeyOptionalArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<NamespacedKey?>) -> Unit = {}) = optionalArgument(SoundArgument.NamespacedKey(nodeName), block)

inline fun Argument<*>.timeArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<Int>) -> Unit = {}) = argument(TimeArgument(nodeName), block)
inline fun Argument<*>.timeOptionalArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<Int?>) -> Unit = {}) = optionalArgument(TimeArgument(nodeName), block)
inline fun Argument<*>.uuidArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<UUID>) -> Unit = {}) = argument(UUIDArgument(nodeName), block)
inline fun Argument<*>.uuidOptionalArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<UUID?>) -> Unit = {}) = optionalArgument(UUIDArgument(nodeName), block)
inline fun Argument<*>.worldArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<World>) -> Unit = {}) = argument(WorldArgument(nodeName), block)
inline fun Argument<*>.worldOptionalArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<World?>) -> Unit = {}) = optionalArgument(WorldArgument(nodeName), block)

// Predicate arguments
inline fun Argument<*>.blockPredicateArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<Predicate<Block>>) -> Unit = {}) = argument(BlockPredicateArgument(nodeName), block)
inline fun Argument<*>.blockPredicateOptionalArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<Predicate<Block>?>) -> Unit = {}) = optionalArgument(BlockPredicateArgument(nodeName), block)
inline fun Argument<*>.itemStackPredicateArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<Predicate<ItemStack>>) -> Unit = {}) = argument(ItemStackPredicateArgument(nodeName), block)
inline fun Argument<*>.itemStackPredicateOptionalArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<Predicate<ItemStack>?>) -> Unit = {}) = optionalArgument(ItemStackPredicateArgument(nodeName), block)

// NBT arguments
inline fun <reified NBTContainer> Argument<*>.nbtCompoundArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<NBTContainer>) -> Unit = {}) = argument(NBTCompoundArgument<NBTContainer>(nodeName), block)
inline fun <reified NBTContainer> Argument<*>.nbtCompoundOptionalArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<NBTContainer?>) -> Unit = {}) = optionalArgument(NBTCompoundArgument<NBTContainer>(nodeName), block)

// Literal arguments
inline fun Argument<*>.literalArgument(literal: String, crossinline block: Argument<*>.(CommandArgumentGetter<String>) -> Unit = {}) = argument(LiteralArgument.of(literal, literal), block)
inline fun Argument<*>.literalOptionalArgument(literal: String, crossinline block: Argument<*>.(CommandArgumentGetter<String?>) -> Unit = {}) = optionalArgument(LiteralArgument.of(literal, literal), block)
inline fun Argument<*>.literalArgument(nodeName: String, literal: String, crossinline block: Argument<*>.(CommandArgumentGetter<String>) -> Unit = {}) = argument(LiteralArgument.of(nodeName, literal), block)
inline fun Argument<*>.literalOptionalArgument(nodeName: String, literal: String, crossinline block: Argument<*>.(CommandArgumentGetter<String?>) -> Unit = {}) = optionalArgument(LiteralArgument.of(nodeName, literal), block)

@Deprecated("This version has been deprecated since version 9.0.2", ReplaceWith("multiLiteralArgument(nodeName, listOf(literals))", "dev.jorel.commandapi.kotlindsl.*"), DeprecationLevel.WARNING)
inline fun Argument<*>.multiLiteralArgument(vararg literals: String, crossinline block: Argument<*>.(CommandArgumentGetter<String>) -> Unit = {}) = argument(MultiLiteralArgument(literals), block)
inline fun Argument<*>.multiLiteralOptionalArgument(vararg literals: String, crossinline block: Argument<*>.(CommandArgumentGetter<String?>) -> Unit = {}) = optionalArgument(MultiLiteralArgument(literals), block)
@Deprecated("This method has been deprecated since version 9.1.0", ReplaceWith("multiLiteralArgument(nodeName, literals)", "dev.jorel.commandapi.kotlindsl.*"), DeprecationLevel.WARNING)
inline fun Argument<*>.multiLiteralArgument(nodeName: String, literals: List<String>, crossinline block: Argument<*>.(CommandArgumentGetter<String>) -> Unit = {}) = argument(MultiLiteralArgument(nodeName, literals), block)
inline fun Argument<*>.multiLiteralOptionalArgument(nodeName: String, literals: List<String>, crossinline block: Argument<*>.(CommandArgumentGetter<String?>) -> Unit = {}) = optionalArgument(MultiLiteralArgument(nodeName, literals), block)

inline fun Argument<*>.multiLiteralArgument(nodeName: String, vararg literals: String, crossinline block: Argument<*>.(CommandArgumentGetter<String>) -> Unit = {}) = argument(MultiLiteralArgument(nodeName, *literals), block)
inline fun Argument<*>.multiLiteralOptionalArgument(nodeName: String, vararg literals: String, crossinline block: Argument<*>.(CommandArgumentGetter<String?>) -> Unit = {}) = optionalArgument(MultiLiteralArgument(nodeName, *literals), block)

// Function arguments
inline fun Argument<*>.functionArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<Array<FunctionWrapper>>) -> Unit = {}) = argument(FunctionArgument(nodeName), block)
inline fun Argument<*>.functionOptionalArgument(nodeName: String, crossinline block: Argument<*>.(CommandArgumentGetter<Array<FunctionWrapper>?>) -> Unit = {}) = optionalArgument(FunctionArgument(nodeName), block)

@Deprecated("This method has been deprecated since version 9.1.0 as it is not needed anymore. See the documentation for more information", ReplaceWith(""), DeprecationLevel.WARNING)
inline fun <reified Type, reified Casted : Type> CommandTree.requirement(base: Argument<Type>, predicate: Predicate<CommandSender>, crossinline block: Argument<*>.(CommandArgumentGetter<Casted>) -> Unit = {}) = argument(base.withRequirement(predicate), block)
@Deprecated("This method has been deprecated since version 9.1.0 as it is not needed anymore. See the documentation for more information", ReplaceWith(""), DeprecationLevel.WARNING)
inline fun <reified Type, reified Casted : Type> Argument<*>.requirement(base: Argument<Type>, predicate: Predicate<CommandSender>, crossinline block: Argument<*>.(CommandArgumentGetter<Casted>) -> Unit = {}) = argument(base.withRequirement(predicate), block)
