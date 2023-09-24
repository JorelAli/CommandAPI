package dev.jorel.commandapi.kotlindsl.adventure

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.CommandTree
import dev.jorel.commandapi.arguments.Argument
import dev.jorel.commandapi.arguments.adventure.ChatColorArgument
import dev.jorel.commandapi.arguments.adventure.ChatComponentArgument
import dev.jorel.commandapi.arguments.adventure.ChatArgument

inline fun CommandAPICommand.chatColorArgument(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): CommandAPICommand = withArguments(ChatColorArgument(nodeName).setOptional(optional).apply(block))
inline fun CommandAPICommand.chatComponentArgument(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): CommandAPICommand = withArguments(ChatComponentArgument(nodeName).setOptional(optional).apply(block))
inline fun CommandAPICommand.chatArgument(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): CommandAPICommand = withArguments(ChatArgument(nodeName).setOptional(optional).apply(block))

inline fun CommandTree.chatColorArgument(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): CommandTree = then(ChatColorArgument(nodeName).setOptional(optional).apply(block))
inline fun CommandTree.chatComponentArgument(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): CommandTree = then(ChatComponentArgument(nodeName).setOptional(optional).apply(block))
inline fun CommandTree.chatArgument(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): CommandTree = then(ChatArgument(nodeName).setOptional(optional).apply(block))

inline fun Argument<*>.chatColorArgument(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): Argument<*> = then(ChatColorArgument(nodeName).setOptional(optional).apply(block))
inline fun Argument<*>.chatComponentArgument(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): Argument<*> = then(ChatComponentArgument(nodeName).setOptional(optional).apply(block))
inline fun Argument<*>.chatArgument(nodeName: String, optional: Boolean = false, block: Argument<*>.() -> Unit = {}): Argument<*> = then(ChatArgument(nodeName).setOptional(optional).apply(block))
