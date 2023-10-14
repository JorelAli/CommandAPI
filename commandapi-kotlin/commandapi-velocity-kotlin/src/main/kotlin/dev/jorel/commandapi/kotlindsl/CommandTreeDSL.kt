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
inline fun CommandTree.argument(base: Argument<*>, block: Argument<*>.() -> Unit = {}): CommandTree = then(base.apply(block))

inline fun CommandTree.optionalArgument(base: Argument<*>, block: Argument<*>.() -> Unit = {}): CommandTree = withOptionalArguments(base.apply(block))

inline fun CommandTree.addArgument(base: Argument<*>, optional: Boolean, block: Argument<*>.() -> Unit): CommandTree = if(optional) optionalArgument(base, block) else argument(base, block)

// Integer arguments
inline fun CommandTree.integerArgument(nodeName: String, min: Int = Int.MIN_VALUE, max: Int = Int.MAX_VALUE, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): CommandTree = addArgument(IntegerArgument(nodeName, min, max), optional, block)

// Float arguments
inline fun CommandTree.floatArgument(nodeName: String, min: Float = -Float.MAX_VALUE, max: Float = Float.MAX_VALUE, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): CommandTree = addArgument(FloatArgument(nodeName, min, max), optional, block)

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

// Literal arguments
inline fun CommandTree.literalArgument(literal: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): CommandTree= addArgument(LiteralArgument.of(literal, literal), optional, block)
inline fun CommandTree.literalArgument(nodeName: String, literal: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): CommandTree= addArgument(LiteralArgument.of(nodeName, literal), optional, block)

@Deprecated("This version has been deprecated since version 9.0.2", ReplaceWith("multiLiteralArgument(nodeName, listOf(literals))", "dev.jorel.commandapi.kotlindsl.*"), DeprecationLevel.WARNING)
inline fun CommandTree.multiLiteralArgument(vararg literals: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): CommandTree= addArgument(MultiLiteralArgument(literals), optional, block)
@Deprecated("This method has been deprecated since version 9.1.0", ReplaceWith("multiLiteralArgument(nodeName, literals)", "dev.jorel.commandapi.kotlindsl.*"), DeprecationLevel.WARNING)
inline fun CommandTree.multiLiteralArgument(nodeName: String, literals: List<String>, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): CommandTree= addArgument(MultiLiteralArgument(nodeName, literals), optional, block)

inline fun CommandTree.multiLiteralArgument(nodeName: String, vararg literals: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): CommandTree= addArgument(MultiLiteralArgument(nodeName, *literals), optional, block)


// ArgumentTree start
inline fun Argument<*>.argument(base: Argument<*>, block: Argument<*>.() -> Unit = {}): Argument<*> = then(base.apply(block))

inline fun Argument<*>.optionalArgument(base: Argument<*>, block: Argument<*>.() -> Unit = {}): Argument<*> = withOptionalArguments(base.apply(block))

inline fun Argument<*>.addArgument(base: Argument<*>, optional: Boolean, block: Argument<*>.() -> Unit): Argument<*> = if (optional) optionalArgument(base, block) else argument(base, block)

// Integer arguments
inline fun Argument<*>.integerArgument(nodeName: String, min: Int = Int.MIN_VALUE, max: Int = Int.MAX_VALUE, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): Argument<*> = addArgument(IntegerArgument(nodeName, min, max), optional, block)

// Float arguments
inline fun Argument<*>.floatArgument(nodeName: String, min: Float = -Float.MAX_VALUE, max: Float = Float.MAX_VALUE, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): Argument<*> = addArgument(FloatArgument(nodeName, min, max), optional, block)

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

// Literal arguments
inline fun Argument<*>.literalArgument(literal: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): Argument<*> = addArgument(LiteralArgument.of(literal, literal), optional, block)
inline fun Argument<*>.literalArgument(nodeName: String, literal: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): Argument<*> = addArgument(LiteralArgument.of(nodeName, literal), optional, block)

@Deprecated("This version has been deprecated since version 9.0.2", ReplaceWith("multiLiteralArgument(nodeName, listOf(literals))", "dev.jorel.commandapi.kotlindsl.*"), DeprecationLevel.WARNING)
inline fun Argument<*>.multiLiteralArgument(vararg literals: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): Argument<*> = addArgument(MultiLiteralArgument(literals), optional, block)
@Deprecated("This method has been deprecated since version 9.1.0", ReplaceWith("multiLiteralArgument(nodeName, literals)", "dev.jorel.commandapi.kotlindsl.*"), DeprecationLevel.WARNING)
inline fun Argument<*>.multiLiteralArgument(nodeName: String, literals: List<String>, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): Argument<*> = addArgument(MultiLiteralArgument(nodeName, literals), optional, block)

inline fun Argument<*>.multiLiteralArgument(nodeName: String, vararg literals: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): Argument<*> = addArgument(MultiLiteralArgument(nodeName, *literals), optional, block)

@Deprecated("This method has been deprecated since version 9.1.0 as it is not needed anymore. See the documentation for more information", ReplaceWith(""), DeprecationLevel.WARNING)
inline fun CommandTree.requirement(base: Argument<*>, predicate: Predicate<CommandSource>, block: Argument<*>.() -> Unit = {}): CommandTree = then(base.withRequirement(predicate).apply(block))
@Deprecated("This method has been deprecated since version 9.1.0 as it is not needed anymore. See the documentation for more information", ReplaceWith(""), DeprecationLevel.WARNING)
inline fun Argument<*>.requirement(base: Argument<*>, predicate: Predicate<CommandSource>, block: Argument<*>.() -> Unit = {}): Argument<*> = then(base.withRequirement(predicate).apply(block))
