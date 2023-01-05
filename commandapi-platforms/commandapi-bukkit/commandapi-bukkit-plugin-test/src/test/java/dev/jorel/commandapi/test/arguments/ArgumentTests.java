package dev.jorel.commandapi.test.arguments;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import org.bukkit.Location;
import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.WorldMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.AdvancementArgument;
import dev.jorel.commandapi.arguments.AdventureChatComponentArgument;
import dev.jorel.commandapi.arguments.ChatComponentArgument;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.ListArgumentBuilder;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.arguments.Location2DArgument;
import dev.jorel.commandapi.arguments.LocationArgument;
import dev.jorel.commandapi.arguments.LocationType;
import dev.jorel.commandapi.arguments.PlayerArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import dev.jorel.commandapi.executors.CommandExecutor;
import dev.jorel.commandapi.test.Mut;
import dev.jorel.commandapi.test.TestBase;
import dev.jorel.commandapi.wrappers.Location2D;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;

/**
 * Tests for the 40+ arguments in dev.jorel.commandapi.arguments
 */
@SuppressWarnings("null")
public class ArgumentTests extends TestBase {
	
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
	public void executionTest() {
		new CommandAPICommand("test")
			.executesPlayer((player, args) -> {
				player.sendMessage("success");
			})
			.register();

		PlayerMock player = server.addPlayer();
		boolean commandResult = server.dispatchCommand(player, "test");
		assertTrue(commandResult);
		assertEquals("success", player.nextMessage());
	}

	@Test
	public void executionTestWithStringArgument() {
		new CommandAPICommand("test")
			.withArguments(new StringArgument("value"))
			.executesPlayer((player, args) -> {
				String value = (String) args.get(0);
				player.sendMessage("success " + value);
			})
			.register();

		PlayerMock player = server.addPlayer();
		boolean commandResult = server.dispatchCommand(player, "test myvalue");
		assertTrue(commandResult);
		assertEquals("success myvalue", player.nextMessage());
		assertEquals("""
				{
				  "type": "root",
				  "children": {
				    "test": {
				      "type": "literal",
				      "children": {
				        "value": {
				          "type": "argument",
				          "parser": "brigadier:string",
				          "properties": {
				            "type": "word"
				          },
				          "executable": true
				        }
				      }
				    }
				  }
				}""", getDispatcherString());
		
		// Negative test
		server.dispatchCommand(player, "test myvalue");
		assertNotEquals("success blah", player.nextMessage());
		
		// Tests from the documentation
		assertDoesNotThrow(() -> assertTrue(server.dispatchThrowableCommand(player, "test Hello")));
		assertDoesNotThrow(() -> assertTrue(server.dispatchThrowableCommand(player, "test 123")));
		assertDoesNotThrow(() -> assertTrue(server.dispatchThrowableCommand(player, "test hello123")));
		assertDoesNotThrow(() -> assertTrue(server.dispatchThrowableCommand(player, "test Hello_world")));
		assertEquals("success Hello", player.nextMessage());
		assertEquals("success 123", player.nextMessage());
		assertEquals("success hello123", player.nextMessage());
		assertEquals("success Hello_world", player.nextMessage());

		// Negative tests from the documentation
		assertInvalidSyntax(player, "test hello@email.com");
		assertInvalidSyntax(player, "test yesn't");
	}

	@Test
	public void executionTestWithCommandTree() {
		Mut<String> result = Mut.of();
		new CommandTree("test").executes(givePosition("", result))
			.then(new LiteralArgument("1").executes(givePosition("1", result))
				.then(new LiteralArgument("1").executes(givePosition("11", result))
					.then(new LiteralArgument("1").executes(givePosition("111", result)))
					.then(new LiteralArgument("2").executes(givePosition("112", result)))
				)
				.then(new LiteralArgument("2").executes(givePosition("12", result))
					.then(new LiteralArgument("1").executes(givePosition("121", result)))
					.then(new LiteralArgument("2").executes(givePosition("122", result)))
				)
			)
			.then(new LiteralArgument("2").executes(givePosition("2", result))
				.then(new LiteralArgument("1").executes(givePosition("21", result))
					.then(new LiteralArgument("1").executes(givePosition("211", result)))
					.then(new LiteralArgument("2").executes(givePosition("212", result)))
				)
				.then(new LiteralArgument("2").executes(givePosition("22", result))
					.then(new LiteralArgument("1").executes(givePosition("221", result)))
					.then(new LiteralArgument("2").executes(givePosition("222", result)))
				)
			).register();

		assertEquals("""
			{
			  "type": "root",
			  "children": {
			    "test": {
			      "type": "literal",
			      "children": {
			        "1": {
			          "type": "literal",
			          "children": {
			            "1": {
			              "type": "literal",
			              "children": {
			                "1": {
			                  "type": "literal",
			                  "executable": true
			                },
			                "2": {
			                  "type": "literal",
			                  "executable": true
			                }
			              },
			              "executable": true
			            },
			            "2": {
			              "type": "literal",
			              "children": {
			                "1": {
			                  "type": "literal",
			                  "executable": true
			                },
			                "2": {
			                  "type": "literal",
			                  "executable": true
			                }
			              },
			              "executable": true
			            }
			          },
			          "executable": true
			        },
			        "2": {
			          "type": "literal",
			          "children": {
			            "1": {
			              "type": "literal",
			              "children": {
			                "1": {
			                  "type": "literal",
			                  "executable": true
			                },
			                "2": {
			                  "type": "literal",
			                  "executable": true
			                }
			              },
			              "executable": true
			            },
			            "2": {
			              "type": "literal",
			              "children": {
			                "1": {
			                  "type": "literal",
			                  "executable": true
			                },
			                "2": {
			                  "type": "literal",
			                  "executable": true
			                }
			              },
			              "executable": true
			            }
			          },
			          "executable": true
			        }
			      },
			      "executable": true
			    }
			  }
			}""", getDispatcherString());

		PlayerMock sender = server.addPlayer("APlayer");

		server.dispatchCommand(sender, "test");
		server.dispatchCommand(sender, "test 1");
		server.dispatchCommand(sender, "test 1 1");
		server.dispatchCommand(sender, "test 1 1 1");
		server.dispatchCommand(sender, "test 1 1 2");
		server.dispatchCommand(sender, "test 1 2");
		server.dispatchCommand(sender, "test 1 2 1");
		server.dispatchCommand(sender, "test 1 2 2");
		server.dispatchCommand(sender, "test 2");
		server.dispatchCommand(sender, "test 2 1");
		server.dispatchCommand(sender, "test 2 1 1");
		server.dispatchCommand(sender, "test 2 1 2");
		server.dispatchCommand(sender, "test 2 2");
		server.dispatchCommand(sender, "test 2 2 1");
		server.dispatchCommand(sender, "test 2 2 2");

		assertEquals("", result.get());
		assertEquals("1", result.get());
		assertEquals("11", result.get());
		assertEquals("111", result.get());
		assertEquals("112", result.get());
		assertEquals("12", result.get());
		assertEquals("121", result.get());
		assertEquals("122", result.get());
		assertEquals("2", result.get());
		assertEquals("21", result.get());
		assertEquals("211", result.get());
		assertEquals("212", result.get());
		assertEquals("22", result.get());
		assertEquals("221", result.get());
		assertEquals("222", result.get());
	}

	private CommandExecutor givePosition(String pos, Mut<String> result) {
		return (sender, args) -> result.set(pos);
	}
	
	@Test
	public void executionTestWithAdvancementArgument() {
		new CommandAPICommand("adv")
			.withArguments(new AdvancementArgument("value"))
			.executesPlayer((player, args) -> {
				Advancement advancement = (Advancement) args.get(0);
				player.sendMessage(advancement.getKey().asString());
			})
			.register();
		
		PlayerMock player = server.addPlayer();
		
		/** Add advancements in {@link MockNMS#mockAdvancementDataWorld} */
		server.dispatchCommand(player, "adv my:advancement");
		server.dispatchCommand(player, "adv my:advancement2");
		server.dispatchCommand(player, "adv my:advancement3");
		assertEquals("my:advancement", player.nextMessage());
		assertEquals("my:advancement2", player.nextMessage());
		assertEquals(null, player.nextMessage());
	}
	
	@Test
	public void executionTestWithLocationArgument() {
		new CommandAPICommand("loc3")
			.withArguments(new LocationArgument("value", LocationType.PRECISE_POSITION))
			.executesPlayer((player, args) -> {
				Location value = (Location) args.get(0);
				player.sendMessage(value.getX() + ", " + value.getY() + ", " + value.getZ());
			})
			.register();
		
		new CommandAPICommand("loc3b")
			.withArguments(new LocationArgument("value", LocationType.BLOCK_POSITION))
			.executesPlayer((player, args) -> {
				Location value = (Location) args.get(0);
				player.sendMessage(value.getX() + ", " + value.getY() + ", " + value.getZ());
			})
			.register();
		
		new CommandAPICommand("loc2")
			.withArguments(new Location2DArgument("value", LocationType.PRECISE_POSITION))
			.executesPlayer((player, args) -> {
				Location2D value = (Location2D) args.get(0);
				player.sendMessage(value.getX() + ", " + value.getZ());
			})
			.register();
		
		new CommandAPICommand("loc2b")
			.withArguments(new Location2DArgument("value", LocationType.BLOCK_POSITION))
			.executesPlayer((player, args) -> {
				Location2D value = (Location2D) args.get(0);
				player.sendMessage(value.getX() + ", " + value.getZ());
			})
			.register();

		PlayerMock player = server.addPlayer();
		
		server.dispatchCommand(player, "loc3 1 10 15");
		server.dispatchCommand(player, "loc3b 1 10 15");
		server.dispatchCommand(player, "loc2 1 15");
		server.dispatchCommand(player, "loc2b 1 15");
		assertEquals("1.5, 10.0, 15.5", player.nextMessage());
		assertEquals("1.0, 10.0, 15.0", player.nextMessage());
		assertEquals("1.5, 15.5", player.nextMessage());
		assertEquals("1.0, 15.0", player.nextMessage());
		
		player.setLocation(new Location(new WorldMock(), 2, 2, 2));
		server.dispatchCommand(player, "loc3 ~ ~5 ~");
		server.dispatchCommand(player, "loc3b ~ ~5 ~");
		server.dispatchCommand(player, "loc2 ~ ~5");
		server.dispatchCommand(player, "loc2b ~ ~5");
		assertEquals("2.0, 7.0, 2.0", player.nextMessage());
		assertEquals("2.0, 7.0, 2.0", player.nextMessage());
		assertEquals("2.0, 7.0", player.nextMessage());
		assertEquals("2.0, 7.0", player.nextMessage());
	}
	
	@Test
	public void executionTestWithEntitySelectorArgument() {
		new CommandAPICommand("test")
			.withArguments(new EntitySelectorArgument.OnePlayer("value"))
			.executesPlayer((player, args) -> {
				Player value = (Player) args.get(0);
				player.sendMessage(value.getName());
			})
			.register();

		new CommandAPICommand("testall")
			.withArguments(new EntitySelectorArgument.ManyPlayers("value"))
			.executesPlayer((player, args) -> {
				@SuppressWarnings("unchecked")
				Collection<Player> value = (Collection<Player>) args.get(0);
				player.sendMessage(value.stream().map(Player::getName).collect(Collectors.joining(", ")));
			})
			.register();

		PlayerMock player = server.addPlayer("APlayer");
		server.dispatchCommand(player, "test APlayer");
		
		server.addPlayer("APlayer1");
		server.addPlayer("APlayer2");
		server.addPlayer("APlayer3");
		server.addPlayer("APlayer4");
		server.dispatchCommand(player, "testall @a");

		assertEquals("APlayer", player.nextMessage());
		// TODO: Why do we have APlayer here twice?
		assertEquals("APlayer, APlayer, APlayer1, APlayer2, APlayer3, APlayer4", player.nextMessage());
	}
	
	@RepeatedTest(10)
	public void executionTestWithGreedyStringArgument() {
		new CommandAPICommand("test")
			.withArguments(new GreedyStringArgument("value"))
			.executesPlayer((player, args) -> {
				String value = (String) args.get(0);
				player.sendMessage(value);
			})
			.register();
		
		StringBuilder builder = new StringBuilder();
		for(int i = 0; i < 100; i++) {
			builder.append((char) ThreadLocalRandom.current().nextInt(32, 127));
		}
		String stringArgValue = builder.toString();
		
		PlayerMock player = server.addPlayer("APlayer");
		server.dispatchCommand(player, "test " + stringArgValue);
		assertEquals(stringArgValue, player.nextMessage());
	}

	@Test
	public void executionTestWithPlayerArgument() {
		Mut<Player> results = Mut.of();

		new CommandAPICommand("test")
			.withArguments(new PlayerArgument("target"))
			.executesPlayer((player, args) -> {
				results.set((Player) args.get(0));
			})
			.register();

		PlayerMock player = server.addPlayer("APlayer");
		server.dispatchCommand(player, "test APlayer");
		assertEquals(player, results.get());
		
		assertInvalidSyntax(player, "test BPlayer");
		
		assertNoMoreResults(results);
	}
	
	@Test
	public void executionTestWithChatComponentArgument() {
		Mut<BaseComponent[]> spigot = Mut.of();
		Mut<Component> adventure = Mut.of();

		new CommandAPICommand("spigot")
			.withArguments(new ChatComponentArgument("text"))
			.executesPlayer((player, args) -> {
				spigot.set((BaseComponent[]) args.get(0));
			})
			.register();
		
		new CommandAPICommand("adventure")
			.withArguments(new AdventureChatComponentArgument("text"))
			.executesPlayer((player, args) -> {
				adventure.set((Component) args.get(0));
			})
			.register();
		
		final String json = "[\"%s\"]".formatted("""
			["", {
			    "text": "Once upon a time, there was a guy "
			}, {
			    "text": "Skepter",
			    "color": "light_purple",
			    "hoverEvent": {
			        "action": "show_entity",
			        "value": "Skepter"
			    }
			}, {
			    "text": " and he created the "
			}, {
			    "text": "CommandAPI",
			    "underlined": true,
			    "clickEvent": {
			        "action": "open_url",
			        "value": "https://github.com/JorelAli/CommandAPI"
			    }
			}]
			""".stripIndent().replace("\n", "").replace("\r", "").replace("\"", "\\\""));
		
		// The above, in normal human-readable JSON gets turned into this for command purposes:
		// [\"[\\\"\\\",{\\\"text\\\":\\\"Once upon a time, there was a guy call \\\"},{\\\"text\\\":\\\"Skepter\\\",\\\"color\\\":\\\"light_purple\\\",\\\"hoverEvent\\\":{\\\"action\\\":\\\"show_entity\\\",\\\"value\\\":\\\"Skepter\\\"}},{\\\"text\\\":\\\" and he created the \\\"},{\\\"text\\\":\\\"CommandAPI\\\",\\\"underlined\\\":true,\\\"clickEvent\\\":{\\\"action\\\":\\\"open_url\\\",\\\"value\\\":\\\"https://github.com/JorelAli/CommandAPI\\\"}}]\"]

		PlayerMock player = server.addPlayer("Skepter");
		server.dispatchCommand(player, "spigot " + json);
		server.dispatchCommand(player, "adventure " + json);
		
		assertArrayEquals(ComponentSerializer.parse(json), spigot.get());
		assertEquals(GsonComponentSerializer.gson().deserialize(json), adventure.get());
	}

	@Test // Pre-#321 
	public void executionTwoCommandsSameArgumentDifferentName() {
		Mut<String> str1 = Mut.of();
		Mut<String> str2 = Mut.of();

		new CommandAPICommand("test")
			.withArguments(new StringArgument("str_1"))
			.executesPlayer((player, args) -> {
				str1.set((String) args.get(0));
			})
			.register();
		
		new CommandAPICommand("test")
			.withArguments(new StringArgument("str_2"))
			.executesPlayer((player, args) -> {
				str2.set((String) args.get(0));
			})
			.register();

		PlayerMock player = server.addPlayer("APlayer");
		server.dispatchCommand(player, "test hello");
		server.dispatchCommand(player, "test world");
		assertEquals("hello", str1.get());
		assertEquals("world", str1.get());
	}
	
	@Test // Pre-#321
	public void executionTwoCommandsSameArgumentDifferentNameDifferentImplementation() {
		Mut<Integer> int1 = Mut.of();
		Mut<Integer> int2 = Mut.of();

		new CommandAPICommand("test")
			.withArguments(new IntegerArgument("int_1", 1, 10))
			.executesPlayer((player, args) -> {
				int1.set((int) args.get(0));
			})
			.register();
		
		new CommandAPICommand("test")
			.withArguments(new IntegerArgument("str_2", 50, 100))
			.executesPlayer((player, args) -> {
				int2.set((int) args.get(0));
			})
			.register();

		PlayerMock player = server.addPlayer("APlayer");
		server.dispatchCommand(player, "test 5");
		server.dispatchCommand(player, "test 60");
		assertEquals(5, int1.get());
		assertEquals(60, int2.get());
	}
	
	@Test // Pre-#321
	public void executionTwoCommandsSameArgumentDifferentNameDifferentImplementation2() {
		Mut<Integer> int1 = Mut.of();
		Mut<Integer> int2 = Mut.of();

		new CommandAPICommand("test")
			.withArguments(new IntegerArgument("int_1", 1, 100))
			.executesPlayer((player, args) -> {
				int1.set((int) args.get(0));
			})
			.register();
		
		new CommandAPICommand("test")
			.withArguments(new IntegerArgument("str_2", 50, 100))
			.executesPlayer((player, args) -> {
				int2.set((int) args.get(0));
			})
			.register();

		PlayerMock player = server.addPlayer("APlayer");
		server.dispatchCommand(player, "test 5");
		server.dispatchCommand(player, "test 60");
		assertEquals(5, int1.get());
		assertEquals(60, int1.get());
		
		assertNoMoreResults(int1);
		assertNoMoreResults(int2);
	}
	
}
