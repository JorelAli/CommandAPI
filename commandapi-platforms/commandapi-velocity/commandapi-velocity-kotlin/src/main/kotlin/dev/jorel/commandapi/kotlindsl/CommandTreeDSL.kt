package dev.jorel.commandapi.kotlindsl

import com.velocitypowered.api.command.CommandSource
import com.velocitypowered.api.proxy.ConsoleCommandSource
import com.velocitypowered.api.proxy.Player
import dev.jorel.commandapi.*
import dev.jorel.commandapi.arguments.*
import dev.jorel.commandapi.executors.*
import java.util.function.Predicate

inline fun commandTree(name: String, tree: CommandTree.() -> Unit = {}) = CommandTree(name).apply(tree).register()
inline fun commandTree(name: String, predicate: Predicate<CommandSource>, tree: CommandTree.() -> Unit = {}) = CommandTree(name).withRequirement(predicate).apply(tree).register()

// CommandTree start
inline fun CommandTree.argument(base: Argument<*>, block: Argument<*>.() -> Unit = {}): CommandTree = then(base.apply(block))

// Integer arguments
inline fun CommandTree.integerArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandTree = then(IntegerArgument(nodeName).apply(block))
inline fun CommandTree.integerArgument(nodeName: String, min: Int, block: Argument<*>.() -> Unit = {}): CommandTree = then(IntegerArgument(nodeName, min).apply(block))
inline fun CommandTree.integerArgument(nodeName: String, min: Int, max: Int, block: Argument<*>.() -> Unit = {}): CommandTree = then(IntegerArgument(nodeName, min, max).apply(block))

// Float arguments
inline fun CommandTree.floatArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): CommandTree = then(FloatArgument(nodeName).apply(block))
inline fun CommandTree.floatArgument(nodeName: String, min: Float, block: Argument<*>.() -> Unit = {}): CommandTree = then(FloatArgument(nodeName, min).apply(block))
inline fun CommandTree.floatArgument(nodeName: String, min: Float, max: Float, block: Argument<*>.() -> Unit = {}): CommandTree = then(FloatArgument(nodeName, min, max).apply(block))

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

// Literal arguments
inline fun CommandTree.literalArgument(literal: String, block: Argument<*>.() -> Unit = {}): CommandTree = then(LiteralArgument.of(literal).apply(block))
inline fun CommandTree.multiLiteralArgument(vararg literals: String, block: Argument<*>.() -> Unit = {}): CommandTree = then(MultiLiteralArgument(*literals).apply(block))


// ArgumentTree start
inline fun Argument<*>.argument(base: Argument<*>, block: Argument<*>.() -> Unit = {}): Argument<*> = then(base.apply(block))

// Integer arguments
inline fun Argument<*>.integerArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): Argument<*> = then(IntegerArgument(nodeName).apply(block))
inline fun Argument<*>.integerArgument(nodeName: String, min: Int, block: Argument<*>.() -> Unit = {}): Argument<*> = then(IntegerArgument(nodeName, min).apply(block))
inline fun Argument<*>.integerArgument(nodeName: String, min: Int, max: Int, block: Argument<*>.() -> Unit = {}): Argument<*> = then(IntegerArgument(nodeName, min, max).apply(block))

// Float arguments
inline fun Argument<*>.floatArgument(nodeName: String, block: Argument<*>.() -> Unit = {}): Argument<*> = then(FloatArgument(nodeName).apply(block))
inline fun Argument<*>.floatArgument(nodeName: String, min: Float, block: Argument<*>.() -> Unit = {}): Argument<*> = then(FloatArgument(nodeName, min).apply(block))
inline fun Argument<*>.floatArgument(nodeName: String, min: Float, max: Float, block: Argument<*>.() -> Unit = {}): Argument<*> = then(FloatArgument(nodeName, min, max).apply(block))

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

// Literal arguments
inline fun Argument<*>.literalArgument(literal: String, block: Argument<*>.() -> Unit = {}): Argument<*> = then(LiteralArgument.of(literal).apply(block))
inline fun Argument<*>.multiLiteralArgument(vararg literals: String, block: Argument<*>.() -> Unit = {}): Argument<*> = then(MultiLiteralArgument(*literals).apply(block))

inline fun CommandTree.requirement(base: Argument<*>, predicate: Predicate<CommandSource>, block: Argument<*>.() -> Unit = {}): CommandTree = then(base.withRequirement(predicate).apply(block))
inline fun Argument<*>.requirement(base: Argument<*>, predicate: Predicate<CommandSource>, block: Argument<*>.() -> Unit = {}): Argument<*> = then(base.withRequirement(predicate).apply(block))

// CommandTree execution
fun CommandTree.anyExecutor(any: (CommandSource, CommandArguments) -> Unit) = CommandTreeExecution().any(any).executes(this)
fun CommandTree.playerExecutor(player: (Player, CommandArguments) -> Unit) = CommandTreeExecution().player(player).executes(this)
fun CommandTree.consoleExecutor(console: (ConsoleCommandSource, CommandArguments) -> Unit) = CommandTreeExecution().console(console).executes(this)

fun CommandTree.anyResultingExecutor(any: (CommandSource, CommandArguments) -> Int) = CommandTreeResultingExecution().any(any).executes(this)
fun CommandTree.playerResultingExecutor(player: (Player, CommandArguments) -> Int) = CommandTreeResultingExecution().player(player).executes(this)
fun CommandTree.consoleResultingExecutor(console: (ConsoleCommandSource, CommandArguments) -> Int) = CommandTreeResultingExecution().console(console).executes(this)

// ArgumentTree execution
fun Argument<*>.anyExecutor(any: (CommandSource, CommandArguments) -> Unit) = CommandTreeExecution().any(any).executes(this)
fun Argument<*>.playerExecutor(player: (Player, CommandArguments) -> Unit) = CommandTreeExecution().player(player).executes(this)
fun Argument<*>.consoleExecutor(console: (ConsoleCommandSource, CommandArguments) -> Unit) = CommandTreeExecution().console(console).executes(this)

fun Argument<*>.anyResultingExecutor(any: (CommandSource, CommandArguments) -> Int) = CommandTreeResultingExecution().any(any).executes(this)
fun Argument<*>.playerResultingExecutor(player: (Player, CommandArguments) -> Int) = CommandTreeResultingExecution().player(player).executes(this)
fun Argument<*>.consoleResultingExecutor(console: (ConsoleCommandSource, CommandArguments) -> Int) = CommandTreeResultingExecution().console(console).executes(this)


class CommandTreeExecution {

	private var any: ((CommandSource, CommandArguments) -> Unit)? = null
	private var player: ((Player, CommandArguments) -> Unit)? = null
	private var console: ((ConsoleCommandSource, CommandArguments) -> Unit)? = null

	fun any(any: (CommandSource, CommandArguments) -> Unit): CommandTreeExecution {
		this.any = any
		return this
	}

	fun player(player: (Player, CommandArguments) -> Unit): CommandTreeExecution {
		this.player = player
		return this
	}

	fun console(console: (ConsoleCommandSource, CommandArguments) -> Unit): CommandTreeExecution {
		this.console = console
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
		if (console != null) {
			tree.executesConsole(ConsoleCommandExecutor { console, args ->
				this.console?.invoke(console, args)
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
	}
}

class CommandTreeResultingExecution {

	private var any: ((CommandSource, CommandArguments) -> Int)? = null
	private var player: ((Player, CommandArguments) -> Int)? = null
	private var console: ((ConsoleCommandSource, CommandArguments) -> Int)? = null

	fun any(any: (CommandSource, CommandArguments) -> Int): CommandTreeResultingExecution {
		this.any = any
		return this
	}

	fun player(player: (Player, CommandArguments) -> Int): CommandTreeResultingExecution {
		this.player = player
		return this
	}

	fun console(console: (ConsoleCommandSource, CommandArguments) -> Int): CommandTreeResultingExecution {
		this.console = console
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
		if (console != null) {
			tree.executesConsole(ConsoleResultingCommandExecutor { console, args ->
				this.console!!.invoke(console, args)
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
		if (console != null) {
			tree.executesConsole(ConsoleResultingCommandExecutor { console, args ->
				this.console!!.invoke(console, args)
			})
			return
		}
	}
}
