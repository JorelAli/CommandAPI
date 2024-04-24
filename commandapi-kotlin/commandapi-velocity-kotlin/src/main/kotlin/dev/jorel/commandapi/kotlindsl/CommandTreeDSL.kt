package dev.jorel.commandapi.kotlindsl

import com.velocitypowered.api.command.CommandSource
import dev.jorel.commandapi.*
import dev.jorel.commandapi.arguments.*
import java.util.function.Predicate

inline fun commandTree(name: String, tree: CommandTree.() -> Unit = {}) = CommandTree(name).apply(tree).register()
inline fun commandTree(name: String, namespace: String, tree: CommandTree.() -> Unit = {}) = CommandTree(name).apply(tree).register(namespace)
@Deprecated("This method has been deprecated since version 9.1.0 as it is not needed anymore. See the documentation for more information", ReplaceWith(""), DeprecationLevel.WARNING)
inline fun commandTree(name: String, predicate: Predicate<CommandSource>, tree: CommandTree.() -> Unit = {}) = CommandTree(name).withRequirement(predicate).apply(tree).register()

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

// Float arguments
inline fun CommandTree.floatArgument(nodeName: String, min: Float = -Float.MAX_VALUE, max: Float = Float.MAX_VALUE, crossinline block: Argument<*>.(CommandArgumentGetter<Float>) -> Unit = {}) = argument(FloatArgument(nodeName, min, max), block)
inline fun CommandTree.floatOptionalArgument(nodeName: String, min: Float = -Float.MAX_VALUE, max: Float = Float.MAX_VALUE, crossinline block: Argument<*>.(CommandArgumentGetter<Float?>) -> Unit = {}) = optionalArgument(FloatArgument(nodeName, min, max), block)

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

// Float arguments
inline fun Argument<*>.floatArgument(nodeName: String, min: Float = -Float.MAX_VALUE, max: Float = Float.MAX_VALUE, crossinline block: Argument<*>.(CommandArgumentGetter<Float>) -> Unit = {}) = argument(FloatArgument(nodeName, min, max), block)
inline fun Argument<*>.floatOptionalArgument(nodeName: String, min: Float = -Float.MAX_VALUE, max: Float = Float.MAX_VALUE, crossinline block: Argument<*>.(CommandArgumentGetter<Float?>) -> Unit = {}) = optionalArgument(FloatArgument(nodeName, min, max), block)

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

@Deprecated("This method has been deprecated since version 9.1.0 as it is not needed anymore. See the documentation for more information", ReplaceWith(""), DeprecationLevel.WARNING)
inline fun CommandTree.requirement(base: Argument<*>, predicate: Predicate<CommandSource>, block: Argument<*>.() -> Unit = {}): CommandTree = then(base.withRequirement(predicate).apply(block))
@Deprecated("This method has been deprecated since version 9.1.0 as it is not needed anymore. See the documentation for more information", ReplaceWith(""), DeprecationLevel.WARNING)
inline fun Argument<*>.requirement(base: Argument<*>, predicate: Predicate<CommandSource>, block: Argument<*>.() -> Unit = {}): Argument<*> = then(base.withRequirement(predicate).apply(block))
