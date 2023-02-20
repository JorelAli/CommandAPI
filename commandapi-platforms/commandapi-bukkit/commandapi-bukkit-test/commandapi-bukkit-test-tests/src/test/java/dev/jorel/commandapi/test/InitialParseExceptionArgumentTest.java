package dev.jorel.commandapi.test;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import dev.jorel.commandapi.arguments.IntegerArgument;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InitialParseExceptionArgumentTest extends TestBase {

	/*********
	 * Setup *
	 *********/

	@BeforeEach
	public void setUp() {
		super.setUp();
	}

	@AfterEach
	public void tearDown() {
		super.tearDown();
	}

	/*********
	 * Tests *
	 *********/

	@Test
	void test() {
		Mut<Integer> ints = Mut.of();
		Mut<String> messages = Mut.of();

		new CommandAPICommand("rate")
			.withArguments(
				new IntegerArgument("rating", 0, 10)
					.withInitialParseExceptionHandler(context -> {
						String message = context.exception().getMessage();
						if(message.startsWith("Integer must not be less than")) {
							context.stringReader().readInt();
							return 0; // Integer too low, move to 0
						} else if(message.startsWith("Integer must not be more than")) {
							context.stringReader().readInt();
							return 10; // Integer too high, cap to 10
						} else {
							throw context.exception(); // Integer wasn't entered, use original exception
						}
					}),
				new GreedyStringArgument("message")
			).executes((sender, args) -> {
				ints.set(args.getUnchecked(0));
				messages.set(args.getUnchecked(1));
			})
			.register();

		assertEquals("""
			{
			  "type": "root",
			  "children": {
			    "rate": {
			      "type": "literal",
			      "children": {
			        "rating": {
			          "type": "argument",
			          "parser": "commandapi:exception_handler",
			          "properties": {
			            "baseType": "brigadier:integer",
			            "baseProperties": {
			              "min": 0,
			              "max": 10
			            }
			          },
			          "children": {
			            "message": {
			              "type": "argument",
			              "parser": "brigadier:string",
			              "properties": {
			                "type": "greedy"
			              },
			              "executable": true
			            }
			          }
			        }
			      }
			    }
			  }
			}""", getDispatcherString());

		Player player = server.addPlayer("APlayer");

		// /rate 5 Final message
		server.dispatchCommand(player, "rate 5 Final message");
		assertEquals(5, ints.get());
		assertEquals("Final message", messages.get());

		// /rate -10 Final message
		server.dispatchCommand(player, "rate -10 Final message");
		assertEquals(0, ints.get());
		assertEquals("Final message", messages.get());

		// /rate 20 Final message
		server.dispatchCommand(player, "rate 20 Final message");
		assertEquals(10, ints.get());
		assertEquals("Final message", messages.get());

		// /rate not an integer
		assertCommandFailsWith(player, "rate not an integer", "Expected integer at position 5: rate <--[HERE]");


		assertNoMoreResults(ints);
		assertNoMoreResults(messages);
	}
}
