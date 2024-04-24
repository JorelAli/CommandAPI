@file:Suppress("NOTHING_TO_INLINE")

package dev.jorel.commandapi.kotlindsl

import com.velocitypowered.api.command.CommandSource
import dev.jorel.commandapi.*
import dev.jorel.commandapi.arguments.*
import java.util.function.Predicate

inline fun commandAPICommand(name: String, command: CommandAPICommand.() -> Unit = {}) = CommandAPICommand(name).apply(command).register()
inline fun commandAPICommand(name: String, namespace: String, command: CommandAPICommand.() -> Unit = {}) = CommandAPICommand(name).apply(command).register(namespace)
@Deprecated("This method has been deprecated since version 9.1.0 as it is not needed anymore. See the documentation for more information", ReplaceWith(""), DeprecationLevel.WARNING)
inline fun commandAPICommand(name: String, predicate: Predicate<CommandSource>, command: CommandAPICommand.() -> Unit = {}) = CommandAPICommand(name).withRequirement(predicate).apply(command).register()

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

// Float arguments
inline fun CommandAPICommand.floatArgument(nodeName: String, min: Float = -Float.MAX_VALUE, max: Float = Float.MAX_VALUE, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<Float> = argument(FloatArgument(nodeName, min, max).apply(block))
inline fun CommandAPICommand.floatOptionalArgument(nodeName: String, min: Float = -Float.MAX_VALUE, max: Float = Float.MAX_VALUE, block: Argument<*>.() -> Unit = {}): CommandArgumentGetter<Float?> = optionalArgument(FloatArgument(nodeName, min, max).apply(block))

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

// Requirements
@Deprecated("This method has been deprecated since version 9.1.0 as it is not needed anymore. See the documentation for more information", ReplaceWith(""), DeprecationLevel.WARNING)
inline fun CommandAPICommand.requirement(base: Argument<*>, predicate: Predicate<CommandSource>, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): CommandAPICommand = withArguments(base.setOptional(optional).withRequirement(predicate).apply(block))
