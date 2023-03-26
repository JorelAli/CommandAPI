package dev.jorel.commandapi.kotlindsl

import dev.jorel.commandapi.BukkitExecutable
import dev.jorel.commandapi.executors.*
import dev.jorel.commandapi.wrappers.NativeProxyCommandSender
import org.bukkit.command.BlockCommandSender
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.command.ProxiedCommandSender
import org.bukkit.entity.Entity
import org.bukkit.entity.Player

// Executors for CommandAPICommand, CommandTree and ArgumentTree
inline fun BukkitExecutable<*>.anyExecutor(crossinline executor: (CommandSender, CommandArguments) -> Unit) = executes(CommandExecutor { sender, args ->
	executor(sender, args)
})
inline fun BukkitExecutable<*>.playerExecutor(crossinline executor: (Player, CommandArguments) -> Unit) = executesPlayer(PlayerCommandExecutor { sender, args ->
	executor(sender, args)
})
inline fun BukkitExecutable<*>.entityExecutor(crossinline executor: (Entity, CommandArguments) -> Unit) = executesEntity(EntityCommandExecutor { sender, args ->
	executor(sender, args)
})
inline fun BukkitExecutable<*>.consoleExecutor(crossinline executor: (ConsoleCommandSender, CommandArguments) -> Unit) = executesConsole(ConsoleCommandExecutor { sender, args ->
	executor(sender, args)
})
inline fun BukkitExecutable<*>.commandBlockExecutor(crossinline executor: (BlockCommandSender, CommandArguments) -> Unit) = executesCommandBlock(CommandBlockCommandExecutor { sender, args ->
	executor(sender, args)
})
inline fun BukkitExecutable<*>.proxyExecutor(crossinline executor: (ProxiedCommandSender, CommandArguments) -> Unit) = executesProxy(ProxyCommandExecutor { sender, args ->
	executor(sender, args)
})
inline fun BukkitExecutable<*>.nativeExecutor(crossinline executor: (NativeProxyCommandSender, CommandArguments) -> Unit) = executesNative(NativeCommandExecutor { sender, args ->
	executor(sender, args)
})

inline fun BukkitExecutable<*>.anyResultingExecutor(crossinline executor: (CommandSender, CommandArguments) -> Int) = executes(ResultingCommandExecutor { sender, args ->
	executor(sender, args)
})
inline fun BukkitExecutable<*>.playerResultingExecutor(crossinline executor: (Player, CommandArguments) -> Int) = executesPlayer(PlayerResultingCommandExecutor { sender, args ->
	executor(sender, args)
})
inline fun BukkitExecutable<*>.entityResultingExecutor(crossinline executor: (Entity, CommandArguments) -> Int) = executesEntity(EntityResultingCommandExecutor { sender, args ->
	executor(sender, args)
})
inline fun BukkitExecutable<*>.consoleResultingExecutor(crossinline executor: (ConsoleCommandSender, CommandArguments) -> Int) = executesConsole(ConsoleResultingCommandExecutor { sender, args ->
	executor(sender, args)
})
inline fun BukkitExecutable<*>.commandBlockResultingExecutor(crossinline executor: (BlockCommandSender, CommandArguments) -> Int) = executesCommandBlock(CommandBlockResultingCommandExecutor { sender, args ->
	executor(sender, args)
})
inline fun BukkitExecutable<*>.proxyResultingExecutor(crossinline executor: (ProxiedCommandSender, CommandArguments) -> Int) = executesProxy(ProxyResultingCommandExecutor { sender, args ->
	executor(sender, args)
})
inline fun BukkitExecutable<*>.nativeResultingExecutor(crossinline executor: (NativeProxyCommandSender, CommandArguments) -> Int) = executesNative(NativeResultingCommandExecutor { sender, args ->
	executor(sender, args)
})