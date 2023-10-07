package dev.jorel.commandapi.test.dsltests

import dev.jorel.commandapi.kotlindsl.commandAPICommand
import dev.jorel.commandapi.kotlindsl.playerExecutionInfo
import dev.jorel.commandapi.kotlindsl.playerExecutor
import dev.jorel.commandapi.kotlindsl.playerResultingExecutionInfo
import dev.jorel.commandapi.kotlindsl.playerResultingExecutor
import dev.jorel.commandapi.kotlindsl.stringArgument
import dev.jorel.commandapi.test.Mut
import dev.jorel.commandapi.test.TestBase
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.scoreboard.Criteria
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ExecutorTests : TestBase() {

	@BeforeEach
	override fun setUp() {
		super.setUp()
	}

	@AfterEach
	override fun tearDown() {
		super.tearDown()
	}

	@Test
	fun testNormalExecutors() {
		// If one of them works, all of them work because they are structured the same
		// Also, I am not sure if MockBukkit has implementations for CommandSenders other than Players
		val results: Mut<String> = Mut.of()

		commandAPICommand("test") {
			stringArgument("string")
			playerExecutor { _, args ->
				results.set(args["string"] as String)
			}
		}

		val player = server.addPlayer()

		assertDoesNotThrow { server.dispatchCommand(player, "test hello") }

		assertEquals("hello", results.get())

		assertNoMoreResults(results)
	}

	@Test
	fun testResultingExecutors() {
		val results: Mut<String> = Mut.of()

		commandAPICommand("test") {
			stringArgument("string")
			playerResultingExecutor { _, args ->
				results.set(args["string"] as String)
				return@playerResultingExecutor 5
			}
		}

		val player = server.addPlayer()

		val result = server.dispatchBrigadierCommand(player, "test hello")
		assertEquals(5, result)
		assertEquals("hello", results.get())

		assertNoMoreResults(results)
	}

	@Test
	fun testNormalExecutionInfo() {
		val results: Mut<String> = Mut.of()

		commandAPICommand("test") {
			stringArgument("string")
			playerExecutionInfo { info ->
				results.set(info.args()["string"] as String)
			}
		}

		val player = server.addPlayer()

		server.dispatchCommand(player, "test hello")
		assertEquals("hello", results.get())

		assertNoMoreResults(results)
	}

	@Test
	fun testResultingExecutionInfo() {
		val results: Mut<String> = Mut.of()

		commandAPICommand("test") {
			stringArgument("string")
			playerResultingExecutionInfo { info ->
				results.set(info.args()["string"] as String)
				return@playerResultingExecutionInfo 5
			}
		}

		val player = server.addPlayer()

		val result = server.dispatchBrigadierCommand(player, "test hello")
		assertEquals(5, result)
		assertEquals("hello", results.get())

		assertNoMoreResults(results)
	}

}