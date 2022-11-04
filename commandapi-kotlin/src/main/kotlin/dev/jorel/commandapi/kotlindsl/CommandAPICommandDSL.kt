package dev.jorel.commandapi.kotlindsl

import dev.jorel.commandapi.*
import dev.jorel.commandapi.arguments.*
import dev.jorel.commandapi.executors.CommandBlockCommandExecutor
import dev.jorel.commandapi.executors.CommandExecutor
import dev.jorel.commandapi.executors.ConsoleCommandExecutor
import dev.jorel.commandapi.executors.NativeCommandExecutor
import dev.jorel.commandapi.executors.PlayerCommandExecutor
import dev.jorel.commandapi.executors.ProxyCommandExecutor
import dev.jorel.commandapi.wrappers.NativeProxyCommandSender
import org.bukkit.command.BlockCommandSender
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.command.ProxiedCommandSender
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import java.util.function.Predicate

inline fun commandAPICommand(name: String, command: CommandAPICommand.() -> Unit = {}) = CommandAPICommand(name).apply(command).register()
inline fun commandAPICommand(name: String, predicate: Predicate<CommandSender>, command: CommandAPICommand.() -> Unit = {}) = CommandAPICommand(name).withRequirement(predicate).apply(command).register()

inline fun CommandAPICommand.argument(base: Argument<*>, block: Argument<*>.() -> Unit = {}): CommandAPICommand = withArguments(base.apply(block))
inline fun CommandAPICommand.subcommand(name: String, command: CommandAPICommand.() -> Unit = {}): CommandAPICommand = withSubcommand(CommandAPICommand(name)).apply(command)

// Integer arguments
inline fun CommandAPICommand.integerArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandAPICommand = withArguments(IntegerArgument(nodeName).apply(block))
inline fun CommandAPICommand.integerArgument(nodeName: String, min: Int, block: Argument<*>.() -> Unit = {}): CommandAPICommand = withArguments(IntegerArgument(nodeName, min).apply(block))
inline fun CommandAPICommand.integerArgument(nodeName: String, min: Int, max: Int, block: Argument<*>.() -> Unit = {}): CommandAPICommand = withArguments(IntegerArgument(nodeName, min, max).apply(block))
inline fun CommandAPICommand.integerRangeArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandAPICommand = withArguments(IntegerRangeArgument(nodeName).apply(block))

// Float arguments
inline fun CommandAPICommand.floatArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandAPICommand = withArguments(FloatArgument(nodeName).apply(block))
inline fun CommandAPICommand.floatArgument(nodeName: String, min: Float, block: Argument<*>.() -> Unit = {}): CommandAPICommand = withArguments(FloatArgument(nodeName, min).apply(block))
inline fun CommandAPICommand.floatArgument(nodeName: String, min: Float, max: Float, block: Argument<*>.() -> Unit = {}): CommandAPICommand = withArguments(FloatArgument(nodeName, min, max).apply(block))
inline fun CommandAPICommand.floatRangeArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandAPICommand = withArguments(FloatRangeArgument(nodeName).apply(block))

// Double arguments
inline fun CommandAPICommand.doubleArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandAPICommand = withArguments(DoubleArgument(nodeName).apply(block))
inline fun CommandAPICommand.doubleArgument(nodeName: String, min: Double, block: Argument<*>.() -> Unit = {}): CommandAPICommand = withArguments(DoubleArgument(nodeName, min).apply(block))
inline fun CommandAPICommand.doubleArgument(nodeName: String, min: Double, max: Double, block: Argument<*>.() -> Unit = {}): CommandAPICommand = withArguments(DoubleArgument(nodeName, min, max).apply(block))

// Long arguments
inline fun CommandAPICommand.longArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandAPICommand = withArguments(LongArgument(nodeName).apply(block))
inline fun CommandAPICommand.longArgument(nodeName: String, min: Long, block: Argument<*>.() -> Unit = {}): CommandAPICommand = withArguments(LongArgument(nodeName, min).apply(block))
inline fun CommandAPICommand.longArgument(nodeName: String, min: Long, max: Long, block: Argument<*>.() -> Unit = {}): CommandAPICommand = withArguments(LongArgument(nodeName, min, max).apply(block))

// Boolean argument
inline fun CommandAPICommand.booleanArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandAPICommand = withArguments(BooleanArgument(nodeName).apply(block))

// String arguments
inline fun CommandAPICommand.stringArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandAPICommand = withArguments(StringArgument(nodeName).apply(block))
inline fun CommandAPICommand.textArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandAPICommand = withArguments(TextArgument(nodeName).apply(block))
inline fun CommandAPICommand.greedyArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandAPICommand = withArguments(GreedyStringArgument(nodeName).apply(block))

// Positional arguments
inline fun CommandAPICommand.locationArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandAPICommand = withArguments(LocationArgument(nodeName).apply(block))
inline fun CommandAPICommand.locationArgument(nodeName: String, locationType: LocationType, block: Argument<*>.() -> Unit = {}): CommandAPICommand = withArguments(LocationArgument(nodeName, locationType).apply(block))
inline fun CommandAPICommand.location2DArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandAPICommand = withArguments(Location2DArgument(nodeName).apply(block))
inline fun CommandAPICommand.location2DArgument(nodeName: String, locationType: LocationType, block: Argument<*>.() -> Unit = {}): CommandAPICommand = withArguments(Location2DArgument(nodeName, locationType).apply(block))
inline fun CommandAPICommand.rotationArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandAPICommand = withArguments(RotationArgument(nodeName).apply(block))
inline fun CommandAPICommand.axisArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandAPICommand = withArguments(AxisArgument(nodeName).apply(block))

// Chat arguments
inline fun CommandAPICommand.chatColorArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandAPICommand = withArguments(ChatColorArgument(nodeName).apply(block))
inline fun CommandAPICommand.chatComponentArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandAPICommand = withArguments(ChatComponentArgument(nodeName).apply(block))
inline fun CommandAPICommand.chatArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandAPICommand = withArguments(ChatArgument(nodeName).apply(block))
inline fun CommandAPICommand.adventureChatComponentArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandAPICommand = withArguments(AdventureChatComponentArgument(nodeName).apply(block))
inline fun CommandAPICommand.adventureChatArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandAPICommand = withArguments(AdventureChatArgument(nodeName).apply(block))

// Entity & Player arguments
inline fun CommandAPICommand.entitySelectorArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandAPICommand = withArguments(EntitySelectorArgument<Entity>(nodeName).apply(block))
inline fun <T : EntitySelector> CommandAPICommand.entitySelectorArgument(nodeName: String, entitySelector: T, block: Argument<*>.() -> Unit = {}): CommandAPICommand = withArguments(EntitySelectorArgument<T>(nodeName, entitySelector).apply(block))
inline fun CommandAPICommand.playerArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandAPICommand = withArguments(PlayerArgument(nodeName).apply(block))
inline fun CommandAPICommand.offlinePlayerArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandAPICommand = withArguments(OfflinePlayerArgument(nodeName).apply(block))
inline fun CommandAPICommand.entityTypeArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandAPICommand = withArguments(EntityTypeArgument(nodeName).apply(block))

// Scoreboard arguments
inline fun CommandAPICommand.scoreHolderArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandAPICommand = withArguments(ScoreHolderArgument<String>(nodeName).apply(block))
inline fun <T : ScoreHolderArgument.ScoreHolderType> CommandAPICommand.scoreHolderArgument(nodeName: String, scoreHolderType: T, block: Argument<*>.() -> Unit = {}): CommandAPICommand = withArguments(ScoreHolderArgument<T>(nodeName, scoreHolderType).apply(block))
inline fun CommandAPICommand.scoreboardSlotArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandAPICommand = withArguments(ScoreboardSlotArgument(nodeName).apply(block))
inline fun CommandAPICommand.objectiveArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandAPICommand = withArguments(ObjectiveArgument(nodeName).apply(block))
inline fun CommandAPICommand.objectiveCriteriaArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandAPICommand = withArguments(ObjectiveCriteriaArgument(nodeName).apply(block))
inline fun CommandAPICommand.teamArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandAPICommand = withArguments(TeamArgument(nodeName).apply(block))

// Miscellaneous arguments
inline fun CommandAPICommand.angleArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandAPICommand = withArguments(AngleArgument(nodeName).apply(block))
inline fun CommandAPICommand.advancementArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandAPICommand = withArguments(AdvancementArgument(nodeName).apply(block))
inline fun CommandAPICommand.biomeArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandAPICommand = withArguments(BiomeArgument(nodeName).apply(block))
inline fun CommandAPICommand.blockStateArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandAPICommand = withArguments(BlockStateArgument(nodeName).apply(block))
inline fun CommandAPICommand.commandArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandAPICommand = withArguments(CommandArgument(nodeName).apply(block))
inline fun CommandAPICommand.enchantmentArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandAPICommand = withArguments(EnchantmentArgument(nodeName).apply(block))
inline fun CommandAPICommand.environmentArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandAPICommand = withArguments(EnvironmentArgument(nodeName).apply(block))
inline fun CommandAPICommand.itemStackArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandAPICommand = withArguments(ItemStackArgument(nodeName).apply(block))
inline fun CommandAPICommand.lootTableArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandAPICommand = withArguments(LootTableArgument(nodeName).apply(block))
inline fun CommandAPICommand.mathOperationArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandAPICommand = withArguments(MathOperationArgument(nodeName).apply(block))
inline fun CommandAPICommand.namespacedKeyArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandAPICommand = withArguments(NamespacedKeyArgument(nodeName).apply(block))
inline fun CommandAPICommand.particleArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandAPICommand = withArguments(ParticleArgument(nodeName).apply(block))
inline fun CommandAPICommand.potionEffectArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandAPICommand = withArguments(PotionEffectArgument(nodeName).apply(block))
inline fun CommandAPICommand.recipeArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandAPICommand = withArguments(RecipeArgument(nodeName).apply(block))
inline fun CommandAPICommand.soundArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandAPICommand = withArguments(SoundArgument(nodeName).apply(block))
inline fun CommandAPICommand.timeArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandAPICommand = withArguments(TimeArgument(nodeName).apply(block))
inline fun CommandAPICommand.uuidArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandAPICommand = withArguments(UUIDArgument(nodeName).apply(block))

// Predicate arguments
inline fun CommandAPICommand.blockPredicateArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandAPICommand = withArguments(BlockPredicateArgument(nodeName).apply(block))
inline fun CommandAPICommand.itemStackPredicateArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandAPICommand = withArguments(ItemStackPredicateArgument(nodeName).apply(block))

// NBT arguments
inline fun <NBTContainer> CommandAPICommand.nbtCompoundArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandAPICommand = withArguments(NBTCompoundArgument<NBTContainer>(nodeName).apply(block))

// Literal arguments
inline fun CommandAPICommand.literalArgument(literal: String, block: Argument<*>.() -> Unit = {}): CommandAPICommand = withArguments(LiteralArgument.of(literal).apply(block))
inline fun CommandAPICommand.multiLiteralArgument(vararg literals: String, block: Argument<*>.() -> Unit = {}): CommandAPICommand = withArguments(MultiLiteralArgument(*literals).apply(block))

// Requirements
inline fun CommandAPICommand.requirement(base: Argument<*>, predicate: Predicate<CommandSender>, block: Argument<*>.() -> Unit = {}): CommandAPICommand = withArguments(base.withRequirement(predicate).apply(block))

// Command execution
fun CommandAPICommand.anyExecutor(any: (CommandSender, Array<Any>) -> Unit) = CommandAPICommandExecution().any(any).executes(this)
fun CommandAPICommand.playerExecutor(player: (Player, Array<Any>) -> Unit) = CommandAPICommandExecution().player(player).executes(this)
fun CommandAPICommand.consoleExecutor(console: (ConsoleCommandSender, Array<Any>) -> Unit) = CommandAPICommandExecution().console(console).executes(this)
fun CommandAPICommand.commandBlockExecutor(block: (BlockCommandSender, Array<Any>) -> Unit) = CommandAPICommandExecution().block(block).executes(this)
fun CommandAPICommand.proxyExecutor(proxy: (ProxiedCommandSender, Array<Any>) -> Unit) = CommandAPICommandExecution().proxy(proxy).executes(this)
fun CommandAPICommand.nativeExecutor(native: (NativeProxyCommandSender, Array<Any>) -> Unit) = CommandAPICommandExecution().native(native).executes(this)

class CommandAPICommandExecution {

	private var any: ((CommandSender, Array<Any>) -> Unit)? = null
	private var player: ((Player, Array<Any>) -> Unit)? = null
	private var console: ((ConsoleCommandSender, Array<Any>) -> Unit)? = null
	private var block: ((BlockCommandSender, Array<Any>) -> Unit)? = null
	private var proxy: ((ProxiedCommandSender, Array<Any>) -> Unit)? = null
	private var native: ((NativeProxyCommandSender, Array<Any>) -> Unit)? = null

	fun any(any: (CommandSender, Array<Any>) -> Unit): CommandAPICommandExecution {
		this.any = any
		return this
	}

	fun player(player: (Player, Array<Any>) -> Unit): CommandAPICommandExecution {
		this.player = player
		return this
	}

	fun console(console: (ConsoleCommandSender, Array<Any>) -> Unit): CommandAPICommandExecution {
		this.console = console
		return this
	}

	fun block(block: (BlockCommandSender, Array<Any>) -> Unit): CommandAPICommandExecution {
		this.block = block
		return this
	}

	fun proxy(proxy: (ProxiedCommandSender, Array<Any>) -> Unit): CommandAPICommandExecution {
		this.proxy = proxy
		return this
	}

	fun native(native: (NativeProxyCommandSender, Array<Any>) -> Unit): CommandAPICommandExecution {
		this.native = native
		return this
	}

	fun executes(command: CommandAPICommand) {
		if (any != null) {
			command.executes(CommandExecutor { sender, args ->
				any?.invoke(sender, args)
			})
			return
		}
		if (player != null) {
			command.executesPlayer(PlayerCommandExecutor { player, args ->
				this.player?.invoke(player, args)
			})
			return
		}
		if (console != null) {
			command.executesConsole(ConsoleCommandExecutor { console, args ->
				this.console?.invoke(console, args)
			})
			return
		}
		if (block != null) {
			command.executesCommandBlock(CommandBlockCommandExecutor { block, args ->
				this.block?.invoke(block, args)
			})
			return
		}
		if (proxy != null) {
			command.executesProxy(ProxyCommandExecutor { proxy, args ->
				this.proxy?.invoke(proxy, args)
			})
			return
		}
		if (native != null) {
			command.executesNative(NativeCommandExecutor { native, args ->
				this.native?.invoke(native, args)
			})
			return
		}
	}
}