package dev.jorel.commandapi.kotlindsl

import dev.jorel.commandapi.*
import dev.jorel.commandapi.arguments.*
import dev.jorel.commandapi.arguments.LiteralArgument.of
import dev.jorel.commandapi.arguments.ScoreHolderArgument.ScoreHolderType
import dev.jorel.commandapi.executors.*
import dev.jorel.commandapi.wrappers.NativeProxyCommandSender
import org.bukkit.command.BlockCommandSender
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.command.ProxiedCommandSender
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.Sound
import java.util.function.Predicate

inline fun commandTree(name: String, tree: CommandTree.() -> Unit = {}) = CommandTree(name).apply(tree).register()
inline fun commandTree(name: String, requirement: Predicate<CommandSender>, tree: CommandTree.() -> Unit = {}) = CommandTree(name).withRequirement(requirement).apply(tree).register()

// CommandTree start
inline fun CommandTree.argument(base: Argument<*>, block: ArgumentTree.() -> Unit = {}): CommandTree = then(base.apply(block))

// Integer arguments
inline fun CommandTree.integerArgument(nodeName: String, block: ArgumentTree.() -> Unit = {}): CommandTree = then(IntegerArgument(nodeName).apply(block))
inline fun CommandTree.integerArgument(nodeName: String, min: Int, block: ArgumentTree.() -> Unit = {}): CommandTree = then(IntegerArgument(nodeName, min).apply(block))
inline fun CommandTree.integerArgument(nodeName: String, min: Int, max: Int, block: ArgumentTree.() -> Unit = {}): CommandTree = then(IntegerArgument(nodeName, min, max).apply(block))
inline fun CommandTree.integerRangeArgument(nodeName: String, block: ArgumentTree.() -> Unit = {}): CommandTree = then(IntegerRangeArgument(nodeName).apply(block))

// Float arguments
inline fun CommandTree.floatArgument(nodeName: String, block: ArgumentTree.() -> Unit = {}): CommandTree = then(FloatArgument(nodeName).apply(block))
inline fun CommandTree.floatArgument(nodeName: String, min: Float, block: ArgumentTree.() -> Unit = {}): CommandTree = then(FloatArgument(nodeName, min).apply(block))
inline fun CommandTree.floatArgument(nodeName: String, min: Float, max: Float, block: ArgumentTree.() -> Unit = {}): CommandTree = then(FloatArgument(nodeName, min, max).apply(block))
inline fun CommandTree.floatRangeArgument(nodeName: String, block: ArgumentTree.() -> Unit = {}): CommandTree = then(FloatRangeArgument(nodeName).apply(block))

// Double arguments
inline fun CommandTree.doubleArgument(nodeName: String, block: ArgumentTree.() -> Unit = {}): CommandTree = then(DoubleArgument(nodeName).apply(block))
inline fun CommandTree.doubleArgument(nodeName: String, min: Double, block: ArgumentTree.() -> Unit = {}): CommandTree = then(DoubleArgument(nodeName, min).apply(block))
inline fun CommandTree.doubleArgument(nodeName: String, min: Double, max: Double, block: ArgumentTree.() -> Unit = {}): CommandTree = then(DoubleArgument(nodeName, min, max).apply(block))

// Long arguments
inline fun CommandTree.longArgument(nodeName: String, block: ArgumentTree.() -> Unit = {}): CommandTree = then(LongArgument(nodeName).apply(block))
inline fun CommandTree.longArgument(nodeName: String, min: Long, block: ArgumentTree.() -> Unit = {}): CommandTree = then(LongArgument(nodeName, min).apply(block))
inline fun CommandTree.longArgument(nodeName: String, min: Long, max: Long, block: ArgumentTree.() -> Unit = {}): CommandTree = then(LongArgument(nodeName, min, max).apply(block))

// Boolean argument
inline fun CommandTree.booleanArgument(nodeName: String, block: ArgumentTree.() -> Unit = {}): CommandTree = then(BooleanArgument(nodeName).apply(block))

// String arguments
inline fun CommandTree.stringArgument(nodeName: String, block: ArgumentTree.() -> Unit = {}): CommandTree = then(StringArgument(nodeName).apply(block))
inline fun CommandTree.textArgument(nodeName: String, block: ArgumentTree.() -> Unit = {}): CommandTree = then(TextArgument(nodeName).apply(block))
inline fun CommandTree.greedyStringArgument(nodeName: String, block: ArgumentTree.() -> Unit = {}): CommandTree = then(GreedyStringArgument(nodeName).apply(block))

// Positional arguments
inline fun CommandTree.locationArgument(nodeName: String, block: ArgumentTree.() -> Unit = {}): CommandTree = then(LocationArgument(nodeName).apply(block))
inline fun CommandTree.locationArgument(nodeName: String, locationType: LocationType, block: ArgumentTree.() -> Unit = {}): CommandTree = then(LocationArgument(nodeName, locationType).apply(block))
inline fun CommandTree.location2DArgument(nodeName: String, block: ArgumentTree.() -> Unit = {}): CommandTree = then(Location2DArgument(nodeName).apply(block))
inline fun CommandTree.location2DArgument(nodeName: String, locationType: LocationType, block: ArgumentTree.() -> Unit = {}): CommandTree = then(Location2DArgument(nodeName, locationType).apply(block))
inline fun CommandTree.rotationArgument(nodeName: String, block: ArgumentTree.() -> Unit = {}): CommandTree = then(RotationArgument(nodeName).apply(block))
inline fun CommandTree.axisArgument(nodeName: String, block: ArgumentTree.() -> Unit = {}): CommandTree = then(AxisArgument(nodeName).apply(block))

// Chat arguments
inline fun CommandTree.chatColorArgument(nodeName: String, block: ArgumentTree.() -> Unit = {}): CommandTree = then(ChatColorArgument(nodeName).apply(block))
inline fun CommandTree.chatComponentArgument(nodeName: String, block: ArgumentTree.() -> Unit = {}): CommandTree = then(ChatComponentArgument(nodeName).apply(block))
inline fun CommandTree.chatArgument(nodeName: String, block: ArgumentTree.() -> Unit = {}): CommandTree = then(ChatArgument(nodeName).apply(block))
inline fun CommandTree.adventureChatComponentArgument(nodeName: String, block: ArgumentTree.() -> Unit = {}): CommandTree = then(AdventureChatComponentArgument(nodeName).apply(block))
inline fun CommandTree.adventureChatArgument(nodeName: String, block: ArgumentTree.() -> Unit = {}): CommandTree = then(AdventureChatArgument(nodeName).apply(block))

// Entity & Player arguments
inline fun CommandTree.entitySelectorArgument(nodeName: String, block: ArgumentTree.() -> Unit = {}): CommandTree = then(EntitySelectorArgument<Entity>(nodeName).apply(block))
inline fun <T : EntitySelector> CommandTree.entitySelectorArgument(nodeName: String, entitySelector: T, block: ArgumentTree.() -> Unit = {}): CommandTree = then(EntitySelectorArgument<T>(nodeName, entitySelector).apply(block))
inline fun CommandTree.playerArgument(nodeName: String, block: ArgumentTree.() -> Unit = {}): CommandTree = then(PlayerArgument(nodeName).apply(block))
inline fun CommandTree.offlinePlayerArgument(nodeName: String, block: ArgumentTree.() -> Unit = {}): CommandTree = then(OfflinePlayerArgument(nodeName).apply(block))
inline fun CommandTree.entityTypeArgument(nodeName: String, block: ArgumentTree.() -> Unit = {}): CommandTree = then(EntityTypeArgument(nodeName).apply(block))

// Scoreboard arguments
inline fun CommandTree.scoreHolderArgument(nodeName: String, block: ArgumentTree.() -> Unit = {}): CommandTree = then(ScoreHolderArgument<String>(nodeName).apply(block))
inline fun <T : ScoreHolderType> CommandTree.scoreHolderArgument(nodeName: String, scoreHolderType: T, block: ArgumentTree.() -> Unit = {}): CommandTree = then(ScoreHolderArgument<T>(nodeName, scoreHolderType).apply(block))
inline fun CommandTree.scoreboardSlotArgument(nodeName: String, block: ArgumentTree.() -> Unit = {}): CommandTree = then(ScoreboardSlotArgument(nodeName).apply(block))
inline fun CommandTree.objectiveArgument(nodeName: String, block: ArgumentTree.() -> Unit = {}): CommandTree = then(ObjectiveArgument(nodeName).apply(block))
inline fun CommandTree.objectiveCriteriaArgument(nodeName: String, block: ArgumentTree.() -> Unit = {}): CommandTree = then(ObjectiveCriteriaArgument(nodeName).apply(block))
inline fun CommandTree.teamArgument(nodeName: String, block: ArgumentTree.() -> Unit = {}): CommandTree = then(TeamArgument(nodeName).apply(block))

// Miscellaneous arguments
inline fun CommandTree.angleArgument(nodeName: String, block: ArgumentTree.() -> Unit = {}): CommandTree = then(AngleArgument(nodeName).apply(block))
inline fun CommandTree.advancementArgument(nodeName: String, block: ArgumentTree.() -> Unit = {}): CommandTree = then(AdvancementArgument(nodeName).apply(block))
inline fun CommandTree.biomeArgument(nodeName: String, block: ArgumentTree.() -> Unit = {}): CommandTree = then(BiomeArgument(nodeName).apply(block))
inline fun CommandTree.blockStateArgument(nodeName: String, block: ArgumentTree.() -> Unit = {}): CommandTree = then(BlockStateArgument(nodeName).apply(block))
inline fun CommandTree.commandArgument(nodeName: String, block: ArgumentTree.() -> Unit = {}): CommandTree = then(CommandArgument(nodeName).apply(block))
inline fun CommandTree.enchantmentArgument(nodeName: String, block: ArgumentTree.() -> Unit = {}): CommandTree = then(EnchantmentArgument(nodeName).apply(block))
inline fun CommandTree.environmentArgument(nodeName: String, block: ArgumentTree.() -> Unit = {}): CommandTree = then(EnvironmentArgument(nodeName).apply(block))
inline fun CommandTree.itemStackArgument(nodeName: String, block: ArgumentTree.() -> Unit = {}): CommandTree = then(ItemStackArgument(nodeName).apply(block))
inline fun CommandTree.lootTableArgument(nodeName: String, block: ArgumentTree.() -> Unit = {}): CommandTree = then(LootTableArgument(nodeName).apply(block))
inline fun CommandTree.mathOperationArgument(nodeName: String, block: ArgumentTree.() -> Unit = {}): CommandTree = then(MathOperationArgument(nodeName).apply(block))
inline fun CommandTree.namespacedKeyArgument(nodeName: String, block: ArgumentTree.() -> Unit = {}): CommandTree = then(NamespacedKeyArgument(nodeName).apply(block))
inline fun CommandTree.particleArgument(nodeName: String, block: ArgumentTree.() -> Unit = {}): CommandTree = then(ParticleArgument(nodeName).apply(block))
inline fun CommandTree.potionEffectArgument(nodeName: String, block: ArgumentTree.() -> Unit = {}): CommandTree = then(PotionEffectArgument(nodeName).apply(block))
inline fun CommandTree.recipeArgument(nodeName: String, block: ArgumentTree.() -> Unit = {}): CommandTree = then(RecipeArgument(nodeName).apply(block))
inline fun <SoundOrNamespacedKey> CommandTree.soundArgument(nodeName: String, block: ArgumentTree.() -> Unit = {}): CommandTree = then(SoundArgument<SoundOrNamespacedKey>(nodeName).apply(block))
inline fun <SoundOrNamespacedKey> CommandTree.soundArgument(nodeName: String, soundType: SoundType, block: ArgumentTree.() -> Unit = {}): CommandTree = then(SoundArgument<SoundOrNamespacedKey>(nodeName, soundType).apply(block))
inline fun CommandTree.timeArgument(nodeName: String, block: ArgumentTree.() -> Unit = {}): CommandTree = then(TimeArgument(nodeName).apply(block))
inline fun CommandTree.uuidArgument(nodeName: String, block: ArgumentTree.() -> Unit = {}): CommandTree = then(UUIDArgument(nodeName).apply(block))
inline fun CommandTree.worldArgument(nodeName: String, block: ArgumentTree.() -> Unit = {}): CommandTree = then(WorldArgument(nodeName).apply(block))

// Predicate arguments
inline fun CommandTree.blockPredicateArgument(nodeName: String, block: ArgumentTree.() -> Unit = {}): CommandTree = then(BlockPredicateArgument(nodeName).apply(block))
inline fun CommandTree.itemStackPredicateArgument(nodeName: String, block: ArgumentTree.() -> Unit = {}): CommandTree = then(ItemStackPredicateArgument(nodeName).apply(block))

// NBT arguments
inline fun <NBTContainer> CommandTree.nbtCompoundArgument(nodeName: String, block: ArgumentTree.() -> Unit = {}): CommandTree = then(NBTCompoundArgument<NBTContainer>(nodeName).apply(block))

// Literal arguments
inline fun CommandTree.literalArgument(literal: String, block: ArgumentTree.() -> Unit = {}): CommandTree = then(of(literal).apply(block))
inline fun CommandTree.multiLiteralArgument(vararg literals: String, block: ArgumentTree.() -> Unit = {}): CommandTree = then(MultiLiteralArgument(*literals).apply(block))


// ArgumentTree start
inline fun ArgumentTree.argument(base: Argument<*>, block: ArgumentTree.() -> Unit = {}): ArgumentTree = then(base.apply(block))

// Integer arguments
inline fun ArgumentTree.integerArgument(nodeName: String, block: ArgumentTree.() -> Unit = {}): ArgumentTree = then(IntegerArgument(nodeName).apply(block))
inline fun ArgumentTree.integerArgument(nodeName: String, min: Int, block: ArgumentTree.() -> Unit = {}): ArgumentTree = then(IntegerArgument(nodeName, min).apply(block))
inline fun ArgumentTree.integerArgument(nodeName: String, min: Int, max: Int, block: ArgumentTree.() -> Unit = {}): ArgumentTree = then(IntegerArgument(nodeName, min, max).apply(block))
inline fun ArgumentTree.integerRangeArgument(nodeName: String, block: ArgumentTree.() -> Unit = {}): ArgumentTree = then(IntegerRangeArgument(nodeName).apply(block))

// Float arguments
inline fun ArgumentTree.floatArgument(nodeName: String, block: ArgumentTree.() -> Unit = {}): ArgumentTree = then(FloatArgument(nodeName).apply(block))
inline fun ArgumentTree.floatArgument(nodeName: String, min: Float, block: ArgumentTree.() -> Unit = {}): ArgumentTree = then(FloatArgument(nodeName, min).apply(block))
inline fun ArgumentTree.floatArgument(nodeName: String, min: Float, max: Float, block: ArgumentTree.() -> Unit = {}): ArgumentTree = then(FloatArgument(nodeName, min, max).apply(block))
inline fun ArgumentTree.floatRangeArgument(nodeName: String, block: ArgumentTree.() -> Unit = {}): ArgumentTree = then(FloatRangeArgument(nodeName).apply(block))

// Double arguments
inline fun ArgumentTree.doubleArgument(nodeName: String, block: ArgumentTree.() -> Unit = {}): ArgumentTree = then(DoubleArgument(nodeName).apply(block))
inline fun ArgumentTree.doubleArgument(nodeName: String, min: Double, block: ArgumentTree.() -> Unit = {}): ArgumentTree = then(DoubleArgument(nodeName, min).apply(block))
inline fun ArgumentTree.doubleArgument(nodeName: String, min: Double, max: Double, block: ArgumentTree.() -> Unit = {}): ArgumentTree = then(DoubleArgument(nodeName, min, max).apply(block))

// Long arguments
inline fun ArgumentTree.longArgument(nodeName: String, block: ArgumentTree.() -> Unit = {}): ArgumentTree = then(LongArgument(nodeName).apply(block))
inline fun ArgumentTree.longArgument(nodeName: String, min: Long, block: ArgumentTree.() -> Unit = {}): ArgumentTree = then(LongArgument(nodeName, min).apply(block))
inline fun ArgumentTree.longArgument(nodeName: String, min: Long, max: Long, block: ArgumentTree.() -> Unit = {}): ArgumentTree = then(LongArgument(nodeName, min, max).apply(block))

// Boolean argument
inline fun ArgumentTree.booleanArgument(nodeName: String, block: ArgumentTree.() -> Unit = {}): ArgumentTree = then(BooleanArgument(nodeName).apply(block))

// String arguments
inline fun ArgumentTree.stringArgument(nodeName: String, block: ArgumentTree.() -> Unit = {}): ArgumentTree = then(StringArgument(nodeName).apply(block))
inline fun ArgumentTree.textArgument(nodeName: String, block: ArgumentTree.() -> Unit = {}): ArgumentTree = then(TextArgument(nodeName).apply(block))
inline fun ArgumentTree.greedyStringArgument(nodeName: String, block: ArgumentTree.() -> Unit = {}): ArgumentTree = then(GreedyStringArgument(nodeName).apply(block))

// Positional arguments
inline fun ArgumentTree.locationArgument(nodeName: String, block: ArgumentTree.() -> Unit = {}): ArgumentTree = then(LocationArgument(nodeName).apply(block))
inline fun ArgumentTree.locationArgument(nodeName: String, locationType: LocationType, block: ArgumentTree.() -> Unit = {}): ArgumentTree = then(LocationArgument(nodeName, locationType).apply(block))
inline fun ArgumentTree.location2DArgument(nodeName: String, block: ArgumentTree.() -> Unit = {}): ArgumentTree = then(Location2DArgument(nodeName).apply(block))
inline fun ArgumentTree.location2DArgument(nodeName: String, locationType: LocationType, block: ArgumentTree.() -> Unit = {}): ArgumentTree = then(Location2DArgument(nodeName, locationType).apply(block))
inline fun ArgumentTree.rotationArgument(nodeName: String, block: ArgumentTree.() -> Unit = {}): ArgumentTree = then(RotationArgument(nodeName).apply(block))
inline fun ArgumentTree.axisArgument(nodeName: String, block: ArgumentTree.() -> Unit = {}): ArgumentTree = then(AxisArgument(nodeName).apply(block))

// Chat arguments
inline fun ArgumentTree.chatColorArgument(nodeName: String, block: ArgumentTree.() -> Unit = {}): ArgumentTree = then(ChatColorArgument(nodeName).apply(block))
inline fun ArgumentTree.chatComponentArgument(nodeName: String, block: ArgumentTree.() -> Unit = {}): ArgumentTree = then(ChatComponentArgument(nodeName).apply(block))
inline fun ArgumentTree.chatArgument(nodeName: String, block: ArgumentTree.() -> Unit = {}): ArgumentTree = then(ChatArgument(nodeName).apply(block))
inline fun ArgumentTree.adventureChatComponentArgument(nodeName: String, block: ArgumentTree.() -> Unit = {}): ArgumentTree = then(AdventureChatComponentArgument(nodeName).apply(block))
inline fun ArgumentTree.adventureChatArgument(nodeName: String, block: ArgumentTree.() -> Unit = {}): ArgumentTree = then(AdventureChatArgument(nodeName).apply(block))

// Entity & Player arguments
inline fun ArgumentTree.entitySelectorArgument(nodeName: String, block: ArgumentTree.() -> Unit = {}): ArgumentTree = then(EntitySelectorArgument<Entity>(nodeName).apply(block))
inline fun <T : EntitySelector> ArgumentTree.entitySelectorArgument(nodeName: String, entitySelector: T, block: ArgumentTree.() -> Unit = {}): ArgumentTree = then(EntitySelectorArgument<T>(nodeName, entitySelector).apply(block))
inline fun ArgumentTree.playerArgument(nodeName: String, block: ArgumentTree.() -> Unit = {}): ArgumentTree = then(PlayerArgument(nodeName).apply(block))
inline fun ArgumentTree.offlinePlayerArgument(nodeName: String, block: ArgumentTree.() -> Unit = {}): ArgumentTree = then(OfflinePlayerArgument(nodeName).apply(block))
inline fun ArgumentTree.entityTypeArgument(nodeName: String, block: ArgumentTree.() -> Unit = {}): ArgumentTree = then(EntityTypeArgument(nodeName).apply(block))

// Scoreboard arguments
inline fun ArgumentTree.scoreHolderArgument(nodeName: String, block: ArgumentTree.() -> Unit = {}): ArgumentTree = then(ScoreHolderArgument<String>(nodeName).apply(block))
inline fun <T : ScoreHolderType> ArgumentTree.scoreHolderArgument(nodeName: String, scoreHolderType: T, block: ArgumentTree.() -> Unit = {}): ArgumentTree = then(ScoreHolderArgument<T>(nodeName, scoreHolderType).apply(block))
inline fun ArgumentTree.scoreboardSlotArgument(nodeName: String, block: ArgumentTree.() -> Unit = {}): ArgumentTree = then(ScoreboardSlotArgument(nodeName).apply(block))
inline fun ArgumentTree.objectiveArgument(nodeName: String, block: ArgumentTree.() -> Unit = {}): ArgumentTree = then(ObjectiveArgument(nodeName).apply(block))
inline fun ArgumentTree.objectiveCriteriaArgument(nodeName: String, block: ArgumentTree.() -> Unit = {}): ArgumentTree = then(ObjectiveCriteriaArgument(nodeName).apply(block))
inline fun ArgumentTree.teamArgument(nodeName: String, block: ArgumentTree.() -> Unit = {}): ArgumentTree = then(TeamArgument(nodeName).apply(block))

// Miscellaneous arguments
inline fun ArgumentTree.angleArgument(nodeName: String, block: ArgumentTree.() -> Unit = {}): ArgumentTree = then(AngleArgument(nodeName).apply(block))
inline fun ArgumentTree.advancementArgument(nodeName: String, block: ArgumentTree.() -> Unit = {}): ArgumentTree = then(AdvancementArgument(nodeName).apply(block))
inline fun ArgumentTree.biomeArgument(nodeName: String, block: ArgumentTree.() -> Unit = {}): ArgumentTree = then(BiomeArgument(nodeName).apply(block))
inline fun ArgumentTree.blockStateArgument(nodeName: String, block: ArgumentTree.() -> Unit = {}): ArgumentTree = then(BlockStateArgument(nodeName).apply(block))
inline fun ArgumentTree.commandArgument(nodeName: String, block: ArgumentTree.() -> Unit = {}): ArgumentTree = then(CommandArgument(nodeName).apply(block))
inline fun ArgumentTree.enchantmentArgument(nodeName: String, block: ArgumentTree.() -> Unit = {}): ArgumentTree = then(EnchantmentArgument(nodeName).apply(block))
inline fun ArgumentTree.environmentArgument(nodeName: String, block: ArgumentTree.() -> Unit = {}): ArgumentTree = then(EnvironmentArgument(nodeName).apply(block))
inline fun ArgumentTree.itemStackArgument(nodeName: String, block: ArgumentTree.() -> Unit = {}): ArgumentTree = then(ItemStackArgument(nodeName).apply(block))
inline fun ArgumentTree.lootTableArgument(nodeName: String, block: ArgumentTree.() -> Unit = {}): ArgumentTree = then(LootTableArgument(nodeName).apply(block))
inline fun ArgumentTree.mathOperationArgument(nodeName: String, block: ArgumentTree.() -> Unit = {}): ArgumentTree = then(MathOperationArgument(nodeName).apply(block))
inline fun ArgumentTree.namespacedKeyArgument(nodeName: String, block: ArgumentTree.() -> Unit = {}): ArgumentTree = then(NamespacedKeyArgument(nodeName).apply(block))
inline fun ArgumentTree.particleArgument(nodeName: String, block: ArgumentTree.() -> Unit = {}): ArgumentTree = then(ParticleArgument(nodeName).apply(block))
inline fun ArgumentTree.potionEffectArgument(nodeName: String, block: ArgumentTree.() -> Unit = {}): ArgumentTree = then(PotionEffectArgument(nodeName).apply(block))
inline fun ArgumentTree.recipeArgument(nodeName: String, block: ArgumentTree.() -> Unit = {}): ArgumentTree = then(RecipeArgument(nodeName).apply(block))
inline fun <SoundOrNamespacedKey> ArgumentTree.soundArgument(nodeName: String, block: ArgumentTree.() -> Unit = {}): ArgumentTree = then(SoundArgument<SoundOrNamespacedKey>(nodeName).apply(block))
inline fun <SoundOrNamespacedKey> ArgumentTree.soundArgument(nodeName: String, soundType: SoundType, block: ArgumentTree.() -> Unit = {}): ArgumentTree = then(SoundArgument<SoundOrNamespacedKey>(nodeName, soundType).apply(block))
inline fun ArgumentTree.timeArgument(nodeName: String, block: ArgumentTree.() -> Unit = {}): ArgumentTree = then(TimeArgument(nodeName).apply(block))
inline fun ArgumentTree.uuidArgument(nodeName: String, block: ArgumentTree.() -> Unit = {}): ArgumentTree = then(UUIDArgument(nodeName).apply(block))
inline fun ArgumentTree.worldArgument(nodeName: String, block: ArgumentTree.() -> Unit = {}): ArgumentTree = then(WorldArgument(nodeName).apply(block))

// Predicate arguments
inline fun ArgumentTree.blockPredicateArgument(nodeName: String, block: ArgumentTree.() -> Unit = {}): ArgumentTree = then(BlockPredicateArgument(nodeName).apply(block))
inline fun ArgumentTree.itemStackPredicateArgument(nodeName: String, block: ArgumentTree.() -> Unit = {}): ArgumentTree = then(ItemStackPredicateArgument(nodeName).apply(block))

// NBT arguments
inline fun <NBTContainer> ArgumentTree.nbtCompoundArgument(nodeName: String, block: ArgumentTree.() -> Unit = {}): ArgumentTree = then(NBTCompoundArgument<NBTContainer>(nodeName).apply(block))

// Literal arguments
inline fun ArgumentTree.literalArgument(literal: String, block: ArgumentTree.() -> Unit = {}): ArgumentTree = then(of(literal).apply(block))
inline fun ArgumentTree.multiLiteralArgument(vararg literals: String, block: ArgumentTree.() -> Unit = {}): ArgumentTree = then(MultiLiteralArgument(*literals).apply(block))

inline fun CommandTree.requirement(base: Argument<*>, predicate: Predicate<CommandSender>, block: ArgumentTree.() -> Unit = {}): CommandTree = then(base.withRequirement(predicate).apply(block))
inline fun ArgumentTree.requirement(base: Argument<*>, predicate: Predicate<CommandSender>, block: ArgumentTree.() -> Unit = {}): ArgumentTree = then(base.withRequirement(predicate).apply(block))

// CommandTree execution
fun CommandTree.anyExecutor(any: (CommandSender, CommandArguments) -> Unit) = Executions().any(any).executes(this)
fun CommandTree.playerExecutor(player: (Player, CommandArguments) -> Unit) = Executions().player(player).executes(this)
fun CommandTree.consoleExecutor(console: (ConsoleCommandSender, CommandArguments) -> Unit) = Executions().console(console).executes(this)
fun CommandTree.commandBlockExecutor(block: (BlockCommandSender, CommandArguments) -> Unit) = Executions().block(block).executes(this)
fun CommandTree.proxyExecutor(proxy: (ProxiedCommandSender, CommandArguments) -> Unit) = Executions().proxy(proxy).executes(this)
fun CommandTree.nativeExecutor(native: (NativeProxyCommandSender, CommandArguments) -> Unit) = Executions().native(native).executes(this)

// ArgumentTree execution
fun ArgumentTree.anyExecutor(any: (CommandSender, CommandArguments) -> Unit) = Executions().any(any).executes(this)
fun ArgumentTree.playerExecutor(player: (Player, CommandArguments) -> Unit) = Executions().player(player).executes(this)
fun ArgumentTree.consoleExecutor(console: (ConsoleCommandSender, CommandArguments) -> Unit) = Executions().console(console).executes(this)
fun ArgumentTree.commandBlockExecutor(block: (BlockCommandSender, CommandArguments) -> Unit) = Executions().block(block).executes(this)
fun ArgumentTree.proxyExecutor(proxy: (ProxiedCommandSender, CommandArguments) -> Unit) = Executions().proxy(proxy).executes(this)
fun ArgumentTree.nativeExecutor(native: (NativeProxyCommandSender, CommandArguments) -> Unit) = Executions().native(native).executes(this)


class Executions {

	private var any: ((CommandSender, CommandArguments) -> Unit)? = null
	private var player: ((Player, CommandArguments) -> Unit)? = null
	private var console: ((ConsoleCommandSender, CommandArguments) -> Unit)? = null
	private var block: ((BlockCommandSender, CommandArguments) -> Unit)? = null
	private var proxy: ((ProxiedCommandSender, CommandArguments) -> Unit)? = null
	private var native: ((NativeProxyCommandSender, CommandArguments) -> Unit)? = null

	fun any(any: (CommandSender, CommandArguments) -> Unit): Executions {
		this.any = any
		return this
	}

	fun player(player: (Player, CommandArguments) -> Unit): Executions {
		this.player = player
		return this
	}

	fun console(console: (ConsoleCommandSender, CommandArguments) -> Unit): Executions {
		this.console = console
		return this
	}

	fun block(block: (BlockCommandSender, CommandArguments) -> Unit): Executions {
		this.block = block
		return this
	}

	fun proxy(proxy: (ProxiedCommandSender, CommandArguments) -> Unit): Executions {
		this.proxy = proxy
		return this
	}

	fun native(native: (NativeProxyCommandSender, CommandArguments) -> Unit): Executions {
		this.native = native
		return this
	}

	fun executes(tree: ArgumentTree) {
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