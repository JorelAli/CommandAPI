package dev.jorel.commandapi.kotlindsl

import dev.jorel.commandapi.BukkitExecutable
import dev.jorel.commandapi.commandsenders.BukkitBlockCommandSender
import dev.jorel.commandapi.commandsenders.BukkitCommandSender
import dev.jorel.commandapi.commandsenders.BukkitConsoleCommandSender
import dev.jorel.commandapi.commandsenders.BukkitEntity
import dev.jorel.commandapi.commandsenders.BukkitFeedbackForwardingCommandSender
import dev.jorel.commandapi.commandsenders.BukkitNativeProxyCommandSender
import dev.jorel.commandapi.commandsenders.BukkitPlayer
import dev.jorel.commandapi.commandsenders.BukkitRemoteConsoleCommandSender
import dev.jorel.commandapi.executors.CommandArguments
import dev.jorel.commandapi.executors.CommandBlockCommandExecutor
import dev.jorel.commandapi.executors.CommandBlockExecutionInfo
import dev.jorel.commandapi.executors.CommandBlockResultingCommandExecutor
import dev.jorel.commandapi.executors.CommandBlockResultingExecutionInfo
import dev.jorel.commandapi.executors.CommandExecutionInfo
import dev.jorel.commandapi.executors.CommandExecutor
import dev.jorel.commandapi.executors.ConsoleCommandExecutor
import dev.jorel.commandapi.executors.ConsoleExecutionInfo
import dev.jorel.commandapi.executors.ConsoleResultingCommandExecutor
import dev.jorel.commandapi.executors.ConsoleResultingExecutionInfo
import dev.jorel.commandapi.executors.EntityCommandExecutor
import dev.jorel.commandapi.executors.EntityExecutionInfo
import dev.jorel.commandapi.executors.EntityResultingCommandExecutor
import dev.jorel.commandapi.executors.EntityResultingExecutionInfo
import dev.jorel.commandapi.executors.ExecutionInfo
import dev.jorel.commandapi.executors.FeedbackForwardingCommandExecutor
import dev.jorel.commandapi.executors.FeedbackForwardingExecutionInfo
import dev.jorel.commandapi.executors.FeedbackForwardingResultingCommandExecutor
import dev.jorel.commandapi.executors.FeedbackForwardingResultingExecutionInfo
import dev.jorel.commandapi.executors.NativeCommandExecutor
import dev.jorel.commandapi.executors.NativeExecutionInfo
import dev.jorel.commandapi.executors.NativeResultingCommandExecutor
import dev.jorel.commandapi.executors.NativeResultingExecutionInfo
import dev.jorel.commandapi.executors.PlayerCommandExecutor
import dev.jorel.commandapi.executors.PlayerExecutionInfo
import dev.jorel.commandapi.executors.PlayerResultingCommandExecutor
import dev.jorel.commandapi.executors.PlayerResultingExecutionInfo
import dev.jorel.commandapi.executors.ProxyCommandExecutor
import dev.jorel.commandapi.executors.ProxyExecutionInfo
import dev.jorel.commandapi.executors.ProxyResultingCommandExecutor
import dev.jorel.commandapi.executors.ProxyResultingExecutionInfo
import dev.jorel.commandapi.executors.RemoteConsoleCommandExecutor
import dev.jorel.commandapi.executors.RemoteConsoleExecutionInfo
import dev.jorel.commandapi.executors.RemoteConsoleResultingCommandExecutor
import dev.jorel.commandapi.executors.RemoteConsoleResultingExecutionInfo
import dev.jorel.commandapi.executors.ResultingCommandExecutionInfo
import dev.jorel.commandapi.executors.ResultingCommandExecutor
import dev.jorel.commandapi.wrappers.NativeProxyCommandSender
import org.bukkit.command.BlockCommandSender
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.command.ProxiedCommandSender
import org.bukkit.command.RemoteConsoleCommandSender
import org.bukkit.entity.Entity
import org.bukkit.entity.Player

// Executors for CommandAPICommand, CommandTree and ArgumentTree
inline fun BukkitExecutable<*>.feedbackForwardingExecutor(crossinline executor: (CommandSender, CommandArguments) -> Unit): BukkitExecutable<*> = executesFeedbackForwarding(FeedbackForwardingCommandExecutor { sender, args ->
	executor(sender, args)
})


// Resulting executors
inline fun BukkitExecutable<*>.feedbackForwardingResultingExecutor(crossinline executor: (CommandSender, CommandArguments) -> Int): BukkitExecutable<*> = executesFeedbackForwarding(FeedbackForwardingResultingCommandExecutor { sender, args ->
	executor(sender, args)
})

// ExecutionInfo normal executors
inline fun BukkitExecutable<*>.feedbackForwardingExecutionInfo(crossinline executor: (ExecutionInfo<CommandSender, BukkitFeedbackForwardingCommandSender<CommandSender>>) -> Unit): BukkitExecutable<*> = executesFeedbackForwarding(FeedbackForwardingExecutionInfo { info ->
	executor(info)
})

// ExecutionInfo resulting executors
inline fun BukkitExecutable<*>.feedbackForwardingResultingExecutionInfo(crossinline executor: (ExecutionInfo<CommandSender, BukkitFeedbackForwardingCommandSender<CommandSender>>) -> Int): BukkitExecutable<*> = executesFeedbackForwarding(FeedbackForwardingResultingExecutionInfo { info ->
	executor(info)
})