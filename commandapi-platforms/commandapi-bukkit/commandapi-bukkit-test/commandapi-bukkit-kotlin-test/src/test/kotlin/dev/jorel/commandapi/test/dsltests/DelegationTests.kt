package dev.jorel.commandapi.test.dsltests

import be.seeseemelk.mockbukkit.entity.PlayerMock
import dev.jorel.commandapi.executors.CommandArguments
import dev.jorel.commandapi.kotlindsl.*
import dev.jorel.commandapi.test.Mut
import dev.jorel.commandapi.test.TestBase
import org.bukkit.entity.Player
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.lang.NullPointerException

class DelegationTests : TestBase() {

	/*********
	 * Setup *
	 *********/

	@BeforeEach
	override fun setUp() {
		super.setUp()
	}

	@AfterEach
	override fun tearDown() {
		super.tearDown()
	}

	/*********
	 * Tests *
	 *********/

	@Test
	fun failingDelegationTestWithKotlinDSL() {
		val results: Mut<Any> = Mut.of()

		commandAPICommand("test") {
			stringArgument("string")
			playerArgument("target")
			playerExecutor { _, args ->
				val string: String by args
				val player: Player by args
				results.set(string)
				results.set(player)
			}
		}

		val player: PlayerMock = server.addPlayer("Player1")

		// This should throw an exception because the 'Player1' is assigned
		// to a variable that does not match the node name
		assertThrows<NullPointerException> {
			server.dispatchCommand(player, "test testString Player1")
		}
	}

	@Test
	fun passingDelegationTestWithKotlinDSL() {
		commandAPICommand("test") {
			stringArgument("string")
			playerArgument("target")
			playerExecutor { _, args ->
				val string: String by args
				val target: Player by args
			}
		}

		val player: PlayerMock = server.addPlayer("Player1")

		assertDoesNotThrow {
			server.dispatchCommand(player, "test testString Player1")
		}
	}

}