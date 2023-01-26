package dev.jorel.commandapi.test

import be.seeseemelk.mockbukkit.ServerMock
import com.mojang.brigadier.context.StringRange
import com.mojang.brigadier.exceptions.CommandSyntaxException
import com.mojang.brigadier.suggestion.Suggestions
import dev.jorel.commandapi.Brigadier
import org.bukkit.Keyed
import org.bukkit.Registry
import org.bukkit.command.CommandSender
import org.bukkit.scoreboard.Criteria
import org.junit.jupiter.api.Assertions
import java.util.concurrent.ExecutionException

class CommandAPIServerMock : ServerMock() {
	@Throws(CommandSyntaxException::class)
	fun dispatchThrowableCommand(sender: CommandSender, commandLine: String): Boolean {
		val commands = commandLine.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
		val commandLabel = commands[0]
		val command = commandMap.getCommand(commandLabel)
		return if (command != null) {
			super.dispatchCommand(sender, commandLine)
		} else {
			val dispatcher = Brigadier.getCommandDispatcher()
			val css = Brigadier.getBrigadierSourceFromCommandSender(sender)
			dispatcher.execute(commandLine, css) != 0
		}
	}



	override fun dispatchCommand(sender: CommandSender, commandLine: String): Boolean {
		return try {
			dispatchThrowableCommand(sender, commandLine)
		} catch (e: CommandSyntaxException) {
			Assertions.fail<Any>("Command '/$commandLine' failed. If you expected this to fail, use dispatchThrowableCommand() instead.", e)
			false
		}
	}

	fun getSuggestions(sender: CommandSender, commandLine: String?): List<String> {
		val dispatcher = Brigadier.getCommandDispatcher()
		val css = Brigadier.getBrigadierSourceFromCommandSender(sender)
		val parseResults = dispatcher.parse(commandLine, css)
		var suggestions: Suggestions? = null
		suggestions = try {
			dispatcher.getCompletionSuggestions(parseResults).get() as Suggestions
		} catch (e: InterruptedException) {
			e.printStackTrace()
			Suggestions(StringRange.at(0), ArrayList()) // Empty suggestions
		} catch (e: ExecutionException) {
			e.printStackTrace()
			Suggestions(StringRange.at(0), ArrayList())
		}
		val suggestionsAsStrings: MutableList<String> = ArrayList()
		for (suggestion in suggestions!!.list) {
			suggestionsAsStrings.add(suggestion.text)
		}
		return suggestionsAsStrings
	}

	override fun isTickingWorlds(): Boolean {
		TODO("Not yet implemented")
	}

	override fun shouldSendChatPreviews(): Boolean {
		return true
	}

	override fun isEnforcingSecureProfiles(): Boolean {
		TODO("Not yet implemented")
	}

	override fun getMaxChainedNeighborUpdates(): Int {
		TODO("Not yet implemented")
	}

	override fun getScoreboardCriteria(name: String): Criteria {
		TODO("Not yet implemented")
	}

	override fun <T : Keyed?> getRegistry(tClass: Class<T>): Registry<T>? {
		TODO("Not yet implemented")
	}

}