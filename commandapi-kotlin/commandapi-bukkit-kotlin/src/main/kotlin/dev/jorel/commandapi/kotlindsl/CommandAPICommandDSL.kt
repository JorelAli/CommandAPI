@file:Suppress("NOTHING_TO_INLINE")

package dev.jorel.commandapi.kotlindsl

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.*
import dev.jorel.commandapi.wrappers.*
import dev.jorel.commandapi.wrappers.Rotation
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.md_5.bungee.api.chat.BaseComponent
import org.bukkit.*
import org.bukkit.advancement.Advancement
import org.bukkit.block.Biome
import org.bukkit.block.Block
import org.bukkit.block.data.BlockData
import org.bukkit.command.CommandSender
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.Recipe
import org.bukkit.loot.LootTable
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.potion.PotionEffectType
import org.bukkit.scoreboard.Objective
import org.bukkit.scoreboard.Team
import java.util.*
import java.util.function.Predicate

inline fun commandAPICommand(name: String, command: CommandAPICommand.() -> Unit = {}) = CommandAPICommand(name).apply(command).register()
inline fun commandAPICommand(name: String, namespace: String, command: CommandAPICommand.() -> Unit = {}) = CommandAPICommand(name).apply(command).register(namespace)
inline fun commandAPICommand(name: String, namespace: JavaPlugin, command: CommandAPICommand.() -> Unit = {}) = CommandAPICommand(name).apply(command).register(namespace)
@Deprecated("This method has been deprecated since version 9.1.0 as it is not needed anymore. See the documentation for more information", ReplaceWith(""), DeprecationLevel.WARNING)
inline fun commandAPICommand(name: String, predicate: Predicate<CommandSender>, command: CommandAPICommand.() -> Unit = {}) = CommandAPICommand(name).withRequirement(predicate).apply(command).register()

inline fun <reified Type, reified Casted : Type> CommandAPICommand.argument(base: Argument<Type>, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<Casted> = withArguments(base.apply(block)).run {
	{ args ->
		args.get(base.nodeName) as Casted
	}
}
inline fun CommandAPICommand.arguments(vararg arguments: Argument<*>): CommandAPICommand = withArguments(*arguments)

inline fun <reified Type, reified Casted : Type> CommandAPICommand.optionalArgument(base: Argument<Type>, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<Casted?> = withOptionalArguments(base.apply(block)).run {
	{ args ->
		args.get(base.nodeName) as Casted?
	}
}
inline fun CommandAPICommand.optionalArguments(vararg arguments: Argument<*>): CommandAPICommand = withOptionalArguments(*arguments)

inline fun subcommand(name: String, command: CommandAPICommand.() -> Unit = {}): CommandAPICommand = CommandAPICommand(name).apply(command)
inline fun CommandAPICommand.subcommand(command: CommandAPICommand): CommandAPICommand = withSubcommand(command)
inline fun CommandAPICommand.subcommand(name: String, command: CommandAPICommand.() -> Unit = {}): CommandAPICommand = withSubcommand(CommandAPICommand(name).apply(command))

// Integer arguments
inline fun CommandAPICommand.integerArgument(nodeName: String, min: Int = Int.MIN_VALUE, max: Int = Int.MAX_VALUE, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<Int> = argument(IntegerArgument(nodeName, min, max).apply(block))
inline fun CommandAPICommand.integerOptionalArgument(nodeName: String, min: Int = Int.MIN_VALUE, max: Int = Int.MAX_VALUE, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<Int?> = optionalArgument(IntegerArgument(nodeName, min, max).apply(block))
inline fun CommandAPICommand.integerRangeArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<IntegerRange> = argument(IntegerRangeArgument(nodeName).apply(block))
inline fun CommandAPICommand.integerRangeOptionalArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<IntegerRange?> = optionalArgument(IntegerRangeArgument(nodeName).apply(block))

// Float arguments
inline fun CommandAPICommand.floatArgument(nodeName: String, min: Float = -Float.MAX_VALUE, max: Float = Float.MAX_VALUE, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<Float> = argument(FloatArgument(nodeName, min, max).apply(block))
inline fun CommandAPICommand.floatOptionalArgument(nodeName: String, min: Float = -Float.MAX_VALUE, max: Float = Float.MAX_VALUE, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<Float?> = optionalArgument(FloatArgument(nodeName, min, max).apply(block))
inline fun CommandAPICommand.floatRangeArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<FloatRange> = argument(FloatRangeArgument(nodeName).apply(block))
inline fun CommandAPICommand.floatRangeOptionalArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<FloatRange?> = optionalArgument(FloatRangeArgument(nodeName).apply(block))

// Double arguments
inline fun CommandAPICommand.doubleArgument(nodeName: String, min: Double = -Double.MAX_VALUE, max: Double = Double.MAX_VALUE, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<Double> = argument(DoubleArgument(nodeName, min, max).apply(block))
inline fun CommandAPICommand.doubleOptionalArgument(nodeName: String, min: Double = -Double.MAX_VALUE, max: Double = Double.MAX_VALUE, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<Double?> = optionalArgument(DoubleArgument(nodeName, min, max).apply(block))

// Long arguments
inline fun CommandAPICommand.longArgument(nodeName: String, min: Long = Long.MIN_VALUE, max: Long = Long.MAX_VALUE, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<Long> = argument(LongArgument(nodeName, min, max).apply(block))
inline fun CommandAPICommand.longOptionalArgument(nodeName: String, min: Long = Long.MIN_VALUE, max: Long = Long.MAX_VALUE, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<Long?> = optionalArgument(LongArgument(nodeName, min, max).apply(block))

// Boolean argument
inline fun CommandAPICommand.booleanArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<Boolean> = argument(BooleanArgument(nodeName).apply(block))
inline fun CommandAPICommand.booleanOptionalArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<Boolean?> = optionalArgument(BooleanArgument(nodeName).apply(block))

// String arguments
inline fun CommandAPICommand.stringArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<String> = argument(StringArgument(nodeName).apply(block))
inline fun CommandAPICommand.stringOptionalArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<String?> = optionalArgument(StringArgument(nodeName).apply(block))
inline fun CommandAPICommand.textArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<String> = argument(TextArgument(nodeName).apply(block))
inline fun CommandAPICommand.textOptionalArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<String?> = optionalArgument(TextArgument(nodeName).apply(block))
inline fun CommandAPICommand.greedyStringArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<String> = argument(GreedyStringArgument(nodeName).apply(block))
inline fun CommandAPICommand.greedyStringOptionalArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<String?> = optionalArgument(GreedyStringArgument(nodeName).apply(block))

// Positional arguments
inline fun CommandAPICommand.locationArgument(nodeName: String, locationType: LocationType = LocationType.PRECISE_POSITION, centerPosition: Boolean = true, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<Location> = argument(LocationArgument(nodeName, locationType, centerPosition).apply(block))
inline fun CommandAPICommand.locationOptionalArgument(nodeName: String, locationType: LocationType = LocationType.PRECISE_POSITION, centerPosition: Boolean = true, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<Location?> = optionalArgument(LocationArgument(nodeName, locationType, centerPosition).apply(block))
inline fun CommandAPICommand.location2DArgument(nodeName: String, locationType: LocationType = LocationType.PRECISE_POSITION, centerPosition: Boolean = true, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<Location2D> = argument(Location2DArgument(nodeName, locationType, centerPosition).apply(block))
inline fun CommandAPICommand.location2DOptionalArgument(nodeName: String, locationType: LocationType = LocationType.PRECISE_POSITION, centerPosition: Boolean = true, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<Location2D?> = optionalArgument(Location2DArgument(nodeName, locationType, centerPosition).apply(block))
inline fun CommandAPICommand.rotationArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<Rotation> = argument(RotationArgument(nodeName).apply(block))
inline fun CommandAPICommand.rotationOptionalArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<Rotation?> = optionalArgument(RotationArgument(nodeName).apply(block))
inline fun CommandAPICommand.axisArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<EnumSet<Axis>> = argument(AxisArgument(nodeName).apply(block))
inline fun CommandAPICommand.axisOptionalArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<EnumSet<Axis>?> = optionalArgument(AxisArgument(nodeName).apply(block))

// Chat arguments
inline fun CommandAPICommand.chatColorArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<ChatColor> = argument(ChatColorArgument(nodeName).apply(block))
inline fun CommandAPICommand.chatColorOptionalArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<ChatColor?> = optionalArgument(ChatColorArgument(nodeName).apply(block))
inline fun CommandAPICommand.chatComponentArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<Array<BaseComponent>> = argument(ChatComponentArgument(nodeName).apply(block))
inline fun CommandAPICommand.chatComponentOptionalArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<Array<BaseComponent>?> = optionalArgument(ChatComponentArgument(nodeName).apply(block))
inline fun CommandAPICommand.chatArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<Array<BaseComponent>> = argument(ChatArgument(nodeName).apply(block))
inline fun CommandAPICommand.chatOptionalArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<Array<BaseComponent>?> = optionalArgument(ChatArgument(nodeName).apply(block))
inline fun CommandAPICommand.adventureChatColorArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<TextColor> = argument(AdventureChatColorArgument(nodeName).apply(block))
inline fun CommandAPICommand.adventureChatColorOptionalArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<TextColor?> = optionalArgument(AdventureChatColorArgument(nodeName).apply(block))
inline fun CommandAPICommand.adventureChatComponentArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<Component> = argument(AdventureChatComponentArgument(nodeName).apply(block))
inline fun CommandAPICommand.adventureChatComponentOptionalArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<Component?> = optionalArgument(AdventureChatComponentArgument(nodeName).apply(block))
inline fun CommandAPICommand.adventureChatArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<Component> = argument(AdventureChatArgument(nodeName).apply(block))
inline fun CommandAPICommand.adventureChatOptionalArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<Component?> = optionalArgument(AdventureChatArgument(nodeName).apply(block))

// Entity & Player arguments
inline fun CommandAPICommand.entitySelectorArgumentOneEntity(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<Entity> = argument(EntitySelectorArgument.OneEntity(nodeName).apply(block))
inline fun CommandAPICommand.entitySelectorOptionalArgumentOneEntity(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<Entity?> = optionalArgument(EntitySelectorArgument.OneEntity(nodeName).apply(block))
inline fun CommandAPICommand.entitySelectorArgumentManyEntities(nodeName: String, allowEmpty: Boolean = true, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<Collection<Entity>> = argument(EntitySelectorArgument.ManyEntities(nodeName, allowEmpty).apply(block))
inline fun CommandAPICommand.entitySelectorOptionalArgumentManyEntities(nodeName: String, allowEmpty: Boolean = true, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<Collection<Entity>?> = optionalArgument(EntitySelectorArgument.ManyEntities(nodeName, allowEmpty).apply(block))
inline fun CommandAPICommand.entitySelectorArgumentOnePlayer(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<Player> = argument(EntitySelectorArgument.OnePlayer(nodeName).apply(block))
inline fun CommandAPICommand.entitySelectorOptionalArgumentOnePlayer(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<Player?> = optionalArgument(EntitySelectorArgument.OnePlayer(nodeName).apply(block))
inline fun CommandAPICommand.entitySelectorArgumentManyPlayers(nodeName: String, allowEmpty: Boolean = true, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<Collection<Player>> = argument(EntitySelectorArgument.ManyPlayers(nodeName, allowEmpty).apply(block))
inline fun CommandAPICommand.entitySelectorOptionalArgumentManyPlayers(nodeName: String, allowEmpty: Boolean = true, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<Collection<Player>?> = optionalArgument(EntitySelectorArgument.ManyPlayers(nodeName, allowEmpty).apply(block))
inline fun CommandAPICommand.playerArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<Player> = argument(PlayerArgument(nodeName).apply(block))
inline fun CommandAPICommand.playerOptionalArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<Player?> = optionalArgument(PlayerArgument(nodeName).apply(block))
inline fun CommandAPICommand.offlinePlayerArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<OfflinePlayer> = argument(OfflinePlayerArgument(nodeName).apply(block))
inline fun CommandAPICommand.offlinePlayerOptionalArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<OfflinePlayer?> = optionalArgument(OfflinePlayerArgument(nodeName).apply(block))
inline fun CommandAPICommand.entityTypeArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<EntityType> = argument(EntityTypeArgument(nodeName).apply(block))
inline fun CommandAPICommand.entityTypeOptionalArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<EntityType?> = optionalArgument(EntityTypeArgument(nodeName).apply(block))

// Scoreboard arguments
inline fun CommandAPICommand.scoreHolderArgumentSingle(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<String> = argument(ScoreHolderArgument.Single(nodeName).apply(block))
inline fun CommandAPICommand.scoreHolderOptionalArgumentSingle(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<String?> = optionalArgument(ScoreHolderArgument.Single(nodeName).apply(block))
inline fun CommandAPICommand.scoreHolderArgumentMultiple(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<Collection<String>> = argument(ScoreHolderArgument.Multiple(nodeName).apply(block))
inline fun CommandAPICommand.scoreHolderOptionalArgumentMultiple(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<Collection<String>?> = optionalArgument(ScoreHolderArgument.Multiple(nodeName).apply(block))
inline fun CommandAPICommand.scoreboardSlotArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<ScoreboardSlot> = argument(ScoreboardSlotArgument(nodeName).apply(block))
inline fun CommandAPICommand.scoreboardSlotOptionalArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<ScoreboardSlot?> = optionalArgument(ScoreboardSlotArgument(nodeName).apply(block))
inline fun CommandAPICommand.objectiveArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<Objective> = argument(ObjectiveArgument(nodeName).apply(block))
inline fun CommandAPICommand.objectiveOptionalArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<Objective?> = optionalArgument(ObjectiveArgument(nodeName).apply(block))
inline fun CommandAPICommand.objectiveCriteriaArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<String> = argument(ObjectiveCriteriaArgument(nodeName).apply(block))
inline fun CommandAPICommand.objectiveCriteriaOptionalArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<String?> = optionalArgument(ObjectiveCriteriaArgument(nodeName).apply(block))
inline fun CommandAPICommand.teamArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<Team> = argument(TeamArgument(nodeName).apply(block))
inline fun CommandAPICommand.teamOptionalArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<Team?> = optionalArgument(TeamArgument(nodeName).apply(block))

// Miscellaneous arguments
inline fun CommandAPICommand.angleArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<Float> = argument(AngleArgument(nodeName).apply(block))
inline fun CommandAPICommand.angleOptionalArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<Float?> = optionalArgument(AngleArgument(nodeName).apply(block))
inline fun CommandAPICommand.advancementArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<Advancement> = argument(AdvancementArgument(nodeName).apply(block))
inline fun CommandAPICommand.advancementOptionalArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<Advancement?> = optionalArgument(AdvancementArgument(nodeName).apply(block))

inline fun CommandAPICommand.biomeArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<Biome> = argument(BiomeArgument(nodeName).apply(block))
inline fun CommandAPICommand.biomeOptionalArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<Biome?> = optionalArgument(BiomeArgument(nodeName).apply(block))
inline fun CommandAPICommand.biomeNamespacedKeyArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<NamespacedKey> = argument(BiomeArgument.NamespacedKey(nodeName).apply(block))
inline fun CommandAPICommand.biomeNamespacedKeyOptionalArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<NamespacedKey?> = optionalArgument(BiomeArgument.NamespacedKey(nodeName).apply(block))

inline fun CommandAPICommand.blockStateArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<BlockData> = argument(BlockStateArgument(nodeName).apply(block))
inline fun CommandAPICommand.blockStateOptionalArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<BlockData?> = optionalArgument(BlockStateArgument(nodeName).apply(block))
inline fun CommandAPICommand.commandArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<CommandResult> = argument(CommandArgument(nodeName).apply(block))
inline fun CommandAPICommand.commandOptionalArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<CommandResult?> = optionalArgument(CommandArgument(nodeName).apply(block))
inline fun CommandAPICommand.enchantmentArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<Enchantment> = argument(EnchantmentArgument(nodeName).apply(block))
inline fun CommandAPICommand.enchantmentOptionalArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<Enchantment?> = optionalArgument(EnchantmentArgument(nodeName).apply(block))

inline fun CommandAPICommand.itemStackArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<ItemStack> = argument(ItemStackArgument(nodeName).apply(block))
inline fun CommandAPICommand.itemStackOptionalArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<ItemStack?> = optionalArgument(ItemStackArgument(nodeName).apply(block))
inline fun CommandAPICommand.lootTableArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<LootTable> = argument(LootTableArgument(nodeName).apply(block))
inline fun CommandAPICommand.lootTableOptionalArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<LootTable?> = optionalArgument(LootTableArgument(nodeName).apply(block))
inline fun CommandAPICommand.mathOperationArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<MathOperation> = argument(MathOperationArgument(nodeName).apply(block))
inline fun CommandAPICommand.mathOperationOptionalArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<MathOperation?> = optionalArgument(MathOperationArgument(nodeName).apply(block))
inline fun CommandAPICommand.namespacedKeyArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<NamespacedKey> = argument(NamespacedKeyArgument(nodeName).apply(block))
inline fun CommandAPICommand.namespacedKeyOptionalArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<NamespacedKey?> = optionalArgument(NamespacedKeyArgument(nodeName).apply(block))
inline fun CommandAPICommand.particleArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<ParticleData<*>> = argument(ParticleArgument(nodeName).apply(block))
inline fun CommandAPICommand.particleOptionalArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<ParticleData<*>?> = optionalArgument(ParticleArgument(nodeName).apply(block))
inline fun CommandAPICommand.potionEffectArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<PotionEffectType> = argument(PotionEffectArgument(nodeName).apply(block))
inline fun CommandAPICommand.potionEffectOptionalArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<PotionEffectType?> = optionalArgument(PotionEffectArgument(nodeName).apply(block))
inline fun CommandAPICommand.recipeArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<Recipe> = argument(RecipeArgument(nodeName).apply(block))
inline fun CommandAPICommand.recipeOptionalArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<Recipe?> = optionalArgument(RecipeArgument(nodeName).apply(block))

inline fun CommandAPICommand.soundArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<Sound> = argument(SoundArgument(nodeName).apply(block))
inline fun CommandAPICommand.soundOptionalArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<Sound?> = optionalArgument(SoundArgument(nodeName).apply(block))
inline fun CommandAPICommand.soundNamespacedKeyArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<NamespacedKey> = argument(SoundArgument.NamespacedKey(nodeName).apply(block))
inline fun CommandAPICommand.soundNamespacedKeyOptionalArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<NamespacedKey?> = optionalArgument(SoundArgument.NamespacedKey(nodeName).apply(block))

inline fun CommandAPICommand.timeArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<Int> = argument(TimeArgument(nodeName).apply(block))
inline fun CommandAPICommand.timeOptionalArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<Int?> = optionalArgument(TimeArgument(nodeName).apply(block))
inline fun CommandAPICommand.uuidArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<UUID> = argument(UUIDArgument(nodeName).apply(block))
inline fun CommandAPICommand.uuidOptionalArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<UUID?> = optionalArgument(UUIDArgument(nodeName).apply(block))
inline fun CommandAPICommand.worldArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<World> = argument(WorldArgument(nodeName).apply(block))
inline fun CommandAPICommand.worldOptionalArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<World?> = optionalArgument(WorldArgument(nodeName).apply(block))

// Predicate arguments
inline fun CommandAPICommand.blockPredicateArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<Predicate<Block>> = argument(BlockPredicateArgument(nodeName).apply(block))
inline fun CommandAPICommand.blockPredicateOptionalArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<Predicate<Block>?> = optionalArgument(BlockPredicateArgument(nodeName).apply(block))
inline fun CommandAPICommand.itemStackPredicateArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<Predicate<ItemStack>> = argument(ItemStackPredicateArgument(nodeName).apply(block))
inline fun CommandAPICommand.itemStackPredicateOptionalArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<Predicate<ItemStack>?> = optionalArgument(ItemStackPredicateArgument(nodeName).apply(block))

// NBT arguments
inline fun <reified NBTContainer> CommandAPICommand.nbtCompoundArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<NBTContainer> = argument(NBTCompoundArgument<NBTContainer>(nodeName).apply(block))
inline fun <reified NBTContainer> CommandAPICommand.nbtCompoundOptionalArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<NBTContainer?> = optionalArgument(NBTCompoundArgument<NBTContainer>(nodeName).apply(block))

// Literal arguments
inline fun CommandAPICommand.literalArgument(literal: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<String> = argument(LiteralArgument.of(literal, literal).apply(block))
inline fun CommandAPICommand.literalOptionalArgument(literal: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<String?> = optionalArgument(LiteralArgument.of(literal, literal).apply(block))
inline fun CommandAPICommand.literalArgument(nodeName: String, literal: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<String> = argument(LiteralArgument.of(nodeName, literal).apply(block))
inline fun CommandAPICommand.literalOptionalArgument(nodeName: String, literal: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<String?> = optionalArgument(LiteralArgument.of(nodeName, literal).apply(block))

@Deprecated("This version has been deprecated since version 9.0.2", ReplaceWith("multiLiteralArgument(nodeName, listOf(literals))", "dev.jorel.commandapi.kotlindsl.*"), DeprecationLevel.WARNING)
inline fun CommandAPICommand.multiLiteralArgument(vararg literals: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<String> = argument(MultiLiteralArgument(literals).apply(block))
@Deprecated("This version has been deprecated since version 9.0.2", ReplaceWith("multiLiteralArgument(nodeName, listOf(literals))", "dev.jorel.commandapi.kotlindsl.*"), DeprecationLevel.WARNING)
inline fun CommandAPICommand.multiLiteralOptionalArgument(vararg literals: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<String?> = optionalArgument(MultiLiteralArgument(literals).apply(block))
@Deprecated("This method has been deprecated since version 9.1.0", ReplaceWith("multiLiteralArgument(nodeName, literals)", "dev.jorel.commandapi.kotlindsl.*"), DeprecationLevel.WARNING)
inline fun CommandAPICommand.multiLiteralArgument(nodeName: String, literals: List<String>, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<String> = argument(MultiLiteralArgument(nodeName, literals).apply(block))
@Deprecated("This method has been deprecated since version 9.1.0", ReplaceWith("multiLiteralArgument(nodeName, literals)", "dev.jorel.commandapi.kotlindsl.*"), DeprecationLevel.WARNING)
inline fun CommandAPICommand.multiLiteralOptionalArgument(nodeName: String, literals: List<String>, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<String?> = optionalArgument(MultiLiteralArgument(nodeName, literals).apply(block))

inline fun CommandAPICommand.multiLiteralArgument(nodeName: String, vararg literals: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<String> = argument(MultiLiteralArgument(nodeName, *literals).apply(block))
inline fun CommandAPICommand.multiLiteralOptionalArgument(nodeName: String, vararg literals: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<String?> = optionalArgument(MultiLiteralArgument(nodeName, *literals).apply(block))

// Function arguments
inline fun CommandAPICommand.functionArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<Array<FunctionWrapper>> = argument(FunctionArgument(nodeName).apply(block))
inline fun CommandAPICommand.functionOptionalArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<Array<FunctionWrapper>?> = optionalArgument(FunctionArgument(nodeName).apply(block))

// Requirements
@Deprecated("This method has been deprecated since version 9.1.0 as it is not needed anymore. See the documentation for more information", ReplaceWith(""), DeprecationLevel.WARNING)
inline fun <reified Type, reified Casted : Type> CommandAPICommand.requirement(base: Argument<Type>, predicate: Predicate<CommandSender>, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<Casted> = argument(base.withRequirement(predicate).apply(block))
