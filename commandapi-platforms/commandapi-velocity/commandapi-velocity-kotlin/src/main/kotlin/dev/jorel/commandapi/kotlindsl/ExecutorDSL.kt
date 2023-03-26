package dev.jorel.commandapi.kotlindsl

import com.velocitypowered.api.command.CommandSource
import com.velocitypowered.api.proxy.ConsoleCommandSource
import com.velocitypowered.api.proxy.Player
import dev.jorel.commandapi.VelocityExecutable
import dev.jorel.commandapi.executors.*

// Executors for CommandAPICommand, CommandTree and ArgumentTree
inline fun VelocityExecutable<*>.anyExecutor(crossinline executor: (CommandSource, CommandArguments) -> Unit) = executes(CommandExecutor { sender, args ->
	executor(sender, args)
})
inline fun VelocityExecutable<*>.playerExecutor(crossinline executor: (Player, CommandArguments) -> Unit) = executesPlayer(PlayerCommandExecutor { sender, args ->
	executor(sender, args)
})
inline fun VelocityExecutable<*>.consoleExecutor(crossinline executor: (ConsoleCommandSource, CommandArguments) -> Unit) = executesConsole(ConsoleCommandExecutor { sender, args ->
	executor(sender, args)
})

inline fun VelocityExecutable<*>.anyResultingExecutor(crossinline executor: (CommandSource, CommandArguments) -> Int) = executes(ResultingCommandExecutor { sender, args ->
	executor(sender, args)
})
inline fun VelocityExecutable<*>.playerResultingExecutor(crossinline executor: (Player, CommandArguments) -> Int) = executesPlayer(PlayerResultingCommandExecutor { sender, args ->
	executor(sender, args)
})
inline fun VelocityExecutable<*>.consoleResultingExecutor(crossinline executor: (ConsoleCommandSource, CommandArguments) -> Int) = executesConsole(ConsoleResultingCommandExecutor { sender, args ->
	executor(sender, args)
})