package dev.jorel.commandapi.test.dsltests

import be.seeseemelk.mockbukkit.entity.PlayerMock
import dev.jorel.commandapi.arguments.StringArgument
import dev.jorel.commandapi.exceptions.OptionalArgumentException
import dev.jorel.commandapi.kotlindsl.*
import dev.jorel.commandapi.test.Mut
import dev.jorel.commandapi.test.TestBase
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class OptionalArgumentTests: TestBase() {

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
	fun executionTestWithCommandAPICommandAndOptionalArgumentMethod() {
		val results: Mut<String> = Mut.of()

		commandAPICommand("test") {
			optionalArgument(StringArgument("value"))
			playerExecutor { player, args ->
				results.set(args.getOptional("value").orElse("DefaultValue") as String)
			}
		}

		val player: PlayerMock = server.addPlayer()

		// /test
		server.dispatchCommand(player, "test")
		assertEquals("DefaultValue", results.get())

		// /test hello
		server.dispatchCommand(player, "test hello")
		assertEquals("hello", results.get())

		assertNoMoreResults(results)
	}

	@Test
	fun exceptionTestWithCommandAPICommandAndOptionalArgumentMethod() {
		// This throws an OptionalArgumentException because
		// a required argument is declared after an optional argument
		assertThrows<OptionalArgumentException> {
			commandAPICommand("test") {
				optionalArgument(StringArgument("value"))
				literalArgument("test")
				playerExecutor { player, args ->

				}
			}
		}
	}

	// TODO: This test currently fails - The optionality of an argument in a CommandTree is currently ignored
	//  Note: I'm not sure if optional arguments in command trees make sense
	//  For example, consider this CommandTree (excuse my Java)
	//   new CommandTree("command")
	//   	.then(new StringArgument("string").setOptional(true)
	//   		.then(new LiteralArgument("a").executes((sender, args) -> {
	//   			sender.sendMessage("You ran the A path");
	//   		}))
	//   		.then(new LiteralArgument("b").executes((sender, args) -> {
	//   			sender.sendMessage("You ran the B path");
	//   		}))
	//   	)
	//   	.register();
	//  The `string` argument is optional, which theoretically defines the following commands:
	//    /command <string> a
	//    /command <string> b
	//    /command
	//  The problem is: What is `/command` supposed to execute? `A path`, `B path`, something else? This works fine for
	//  a CommandAPICommand since there can only be one executor defined, but I don't think anything makes sense for a
	//  CommandTree.
	@Disabled
	@Test
	fun executionTestWithCommandTreeAndOptionalArgumentMethod() {
		val results: Mut<String> = Mut.of()

		// TODO: Come up with a more complicated test for optional arguments in a CommandTree
		// I was unsure about the optionalArgument() method for the CommandTree DSL anyway
		// because everything can be achieved by using the normal CommandTree DSL syntax and
		// the argument() method
		commandTree("test") {
			stringArgument("value", optional = true) {
				playerExecutor { player, args ->
					results.set(args.getOptional("value").orElse("DefaultValue") as String)
				}
			}
		}

		val player: PlayerMock = server.addPlayer()

		// /test
		server.dispatchCommand(player, "test")
		assertEquals("DefaultValue", results.get())

		// /test hello
		server.dispatchCommand(player, "test hello")
		assertEquals("hello", results.get())

		assertNoMoreResults(results)
	}

	@Test
	fun executionTestWithOptionalValue() {
		val results: Mut<String> = Mut.of()

		commandAPICommand("test") {
			stringArgument("value", optional = true)
			playerExecutor { player, args ->
				results.set(args.getOptional("value").orElse("DefaultValue") as String)
			}
		}

		val player: PlayerMock = server.addPlayer()

		// /test
		server.dispatchCommand(player, "test")
		assertEquals("DefaultValue", results.get())

		// /test hello
		server.dispatchCommand(player, "test hello")
		assertEquals("hello", results.get())

		assertNoMoreResults(results)
	}

}