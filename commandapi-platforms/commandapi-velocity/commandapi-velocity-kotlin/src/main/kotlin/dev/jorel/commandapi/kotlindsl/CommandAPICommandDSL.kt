package dev.jorel.commandapi.kotlindsl

import com.velocitypowered.api.command.CommandSource
import dev.jorel.commandapi.*
import dev.jorel.commandapi.arguments.*
import java.util.function.Predicate

inline fun commandAPICommand(name: String, command: CommandAPICommand.() -> Unit = {}) = CommandAPICommand(name).apply(command).register()
inline fun commandAPICommand(name: String, predicate: Predicate<CommandSource>, command: CommandAPICommand.() -> Unit = {}) = CommandAPICommand(name).withRequirement(predicate).apply(command).register()

inline fun CommandAPICommand.argument(base: Argument<*>, block: Argument<*>.() -> Unit = {}): CommandAPICommand = withArguments(base.apply(block))
fun CommandAPICommand.arguments(vararg arguments: Argument<*>): CommandAPICommand = withArguments(*arguments)

inline fun CommandAPICommand.optionalArgument(base: Argument<*>, block: Argument<*>.() -> Unit = {}): CommandAPICommand = withOptionalArguments(base.apply(block))
fun CommandAPICommand.optionalArguments(vararg arguments: Argument<*>): CommandAPICommand = withOptionalArguments(*arguments)

inline fun subcommand(name: String, command: CommandAPICommand.() -> Unit = {}): CommandAPICommand = CommandAPICommand(name).apply(command)
fun CommandAPICommand.subcommand(command: CommandAPICommand): CommandAPICommand = withSubcommand(command)
inline fun CommandAPICommand.subcommand(name: String, command: CommandAPICommand.() -> Unit = {}): CommandAPICommand = withSubcommand(CommandAPICommand(name).apply(command))

// Integer arguments
inline fun CommandAPICommand.integerArgument(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): CommandAPICommand = withArguments(IntegerArgument(nodeName).setOptional(optional).apply(block))
inline fun CommandAPICommand.integerArgument(nodeName: String, min: Int, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): CommandAPICommand = withArguments(IntegerArgument(nodeName, min).setOptional(optional).apply(block))
inline fun CommandAPICommand.integerArgument(nodeName: String, min: Int, max: Int, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): CommandAPICommand = withArguments(IntegerArgument(nodeName, min, max).setOptional(optional).apply(block))

// Float arguments
inline fun CommandAPICommand.floatArgument(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): CommandAPICommand = withArguments(FloatArgument(nodeName).setOptional(optional).apply(block))
inline fun CommandAPICommand.floatArgument(nodeName: String, min: Float, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): CommandAPICommand = withArguments(FloatArgument(nodeName, min).setOptional(optional).apply(block))
inline fun CommandAPICommand.floatArgument(nodeName: String, min: Float, max: Float, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): CommandAPICommand = withArguments(FloatArgument(nodeName, min, max).setOptional(optional).apply(block))

// Double arguments
inline fun CommandAPICommand.doubleArgument(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): CommandAPICommand = withArguments(DoubleArgument(nodeName).setOptional(optional).apply(block))
inline fun CommandAPICommand.doubleArgument(nodeName: String, min: Double, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): CommandAPICommand = withArguments(DoubleArgument(nodeName, min).setOptional(optional).apply(block))
inline fun CommandAPICommand.doubleArgument(nodeName: String, min: Double, max: Double, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): CommandAPICommand = withArguments(DoubleArgument(nodeName, min, max).setOptional(optional).apply(block))

// Long arguments
inline fun CommandAPICommand.longArgument(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): CommandAPICommand = withArguments(LongArgument(nodeName).setOptional(optional).apply(block))
inline fun CommandAPICommand.longArgument(nodeName: String, min: Long, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): CommandAPICommand = withArguments(LongArgument(nodeName, min).setOptional(optional).apply(block))
inline fun CommandAPICommand.longArgument(nodeName: String, min: Long, max: Long, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): CommandAPICommand = withArguments(LongArgument(nodeName, min, max).setOptional(optional).apply(block))

// Boolean argument
inline fun CommandAPICommand.booleanArgument(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): CommandAPICommand = withArguments(BooleanArgument(nodeName).setOptional(optional).apply(block))

// String arguments
inline fun CommandAPICommand.stringArgument(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): CommandAPICommand = withArguments(StringArgument(nodeName).setOptional(optional).apply(block))
inline fun CommandAPICommand.textArgument(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): CommandAPICommand = withArguments(TextArgument(nodeName).setOptional(optional).apply(block))
inline fun CommandAPICommand.greedyStringArgument(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): CommandAPICommand = withArguments(GreedyStringArgument(nodeName).setOptional(optional).apply(block))

// Literal arguments
inline fun CommandAPICommand.literalArgument(literal: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): CommandAPICommand = withArguments(LiteralArgument.of(literal).setOptional(optional).apply(block))
inline fun CommandAPICommand.multiLiteralArgument(vararg literals: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): CommandAPICommand = withArguments(MultiLiteralArgument(*literals).setOptional(optional).apply(block))

// Requirements
inline fun CommandAPICommand.requirement(base: Argument<*>, predicate: Predicate<CommandSource>, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): CommandAPICommand = withArguments(base.setOptional(optional).withRequirement(predicate).apply(block))
