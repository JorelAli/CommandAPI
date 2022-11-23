package dev.jorel.commandapi.test;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import dev.jorel.commandapi.*;
import dev.jorel.commandapi.arguments.*;
import dev.jorel.commandapi.executors.CommandExecutor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.advancement.Advancement;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.mojang.brigadier.exceptions.CommandSyntaxException;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.WorldMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import dev.jorel.commandapi.wrappers.Location2D;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;

/**
 * Tests for the 40+ arguments in dev.jorel.commandapi.arguments
 */
public class ArgumentTests {
	
	private CustomServerMock server;
	private Main plugin;

	private String getDispatcherString() {
		try {
			return Files.readString(CommandAPI.getConfiguration().getDispatcherFile().toPath());
		} catch (IOException e) {
			return "";
		}
	}

	public <T> void assertStoresResult(CommandSender sender, String command, Mut<T> queue, T expected) {
		assertDoesNotThrow(() -> assertTrue(
			server.dispatchThrowableCommand(sender, command),
			"Expected command dispatch to return true, but it gave false")
		);
		assertEquals(expected, queue.get());
	}

	public void assertInvalidSyntax(CommandSender sender, String command) {
		assertThrows(CommandSyntaxException.class, () -> assertTrue(server.dispatchThrowableCommand(sender,command)));
	}

	@BeforeEach
	public void setUp() {
		server = MockBukkit.mock(new CustomServerMock());
		plugin = MockBukkit.load(Main.class);
	}

	@AfterEach
	public void tearDown() {
		Bukkit.getScheduler().cancelTasks(plugin);
		if(plugin != null) {
			plugin.onDisable();
		}
		MockBukkit.unmock();
	}

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
				String value = (String) args[0];
				player.sendMessage("success " + value);
			})
			.register();

		PlayerMock player = server.addPlayer();
		boolean commandResult = server.dispatchCommand(player, "test myvalue");
		assertTrue(commandResult);
		assertEquals("success myvalue", player.nextMessage());
		assertEquals(getDispatcherString(), """
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
				}""");
		
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

		assertEquals(getDispatcherString(), """
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
			}""");

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
	public void executionTestWithBooleanArgument() {
		new CommandAPICommand("test")
			.withArguments(new BooleanArgument("value"))
			.executesPlayer((player, args) -> {
				boolean value = (boolean) args[0];
				player.sendMessage("success " + value);
			})
			.register();

		PlayerMock player = server.addPlayer();
		server.dispatchCommand(player, "test true");
		server.dispatchCommand(player, "test false");
		assertEquals("success true", player.nextMessage());
		assertEquals("success false", player.nextMessage());
		assertInvalidSyntax(player, "test aaaaa");
	}
	
	@Test
	public void executionTestWithAdvancementArgument() {
		new CommandAPICommand("adv")
			.withArguments(new AdvancementArgument("value"))
			.executesPlayer((player, args) -> {
				Advancement advancement = (Advancement) args[0];
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
				Location value = (Location) args[0];
				player.sendMessage(value.getX() + ", " + value.getY() + ", " + value.getZ());
			})
			.register();
		
		new CommandAPICommand("loc3b")
			.withArguments(new LocationArgument("value", LocationType.BLOCK_POSITION))
			.executesPlayer((player, args) -> {
				Location value = (Location) args[0];
				player.sendMessage(value.getX() + ", " + value.getY() + ", " + value.getZ());
			})
			.register();
		
		new CommandAPICommand("loc2")
			.withArguments(new Location2DArgument("value", LocationType.PRECISE_POSITION))
			.executesPlayer((player, args) -> {
				Location2D value = (Location2D) args[0];
				player.sendMessage(value.getX() + ", " + value.getZ());
			})
			.register();
		
		new CommandAPICommand("loc2b")
			.withArguments(new Location2DArgument("value", LocationType.BLOCK_POSITION))
			.executesPlayer((player, args) -> {
				Location2D value = (Location2D) args[0];
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
			.withArguments(new EntitySelectorArgument<Player>("value", EntitySelector.ONE_PLAYER))
			.executesPlayer((player, args) -> {
				Player value = (Player) args[0];
				player.sendMessage(value.getName());
			})
			.register();

		new CommandAPICommand("testall")
			.withArguments(new EntitySelectorArgument<Collection<Player>>("value", EntitySelector.MANY_PLAYERS))
			.executesPlayer((player, args) -> {
				@SuppressWarnings("unchecked")
				Collection<Player> value = (Collection<Player>) args[0];
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
				String value = (String) args[0];
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
	public void executionTestWithPotionEffectArgument() {
		Mut<PotionEffectType> type = Mut.of();

		new CommandAPICommand("test")
			.withArguments(new PotionEffectArgument("potion"))
			.executesPlayer((player, args) -> {
				type.set((PotionEffectType) args[0]);
			})
			.register();

		PlayerMock player = server.addPlayer();
		server.dispatchCommand(player, "test speed");
		server.dispatchCommand(player, "test minecraft:speed");
		server.dispatchCommand(player, "test bukkit:speed");

		assertEquals(PotionEffectType.SPEED, type.get());
		assertEquals(PotionEffectType.SPEED, type.get());
		assertEquals(null, type.get());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void executionTestWithListArgument() {
		Mut<List<String>> type = Mut.of();

		PlayerMock sender = server.addPlayer("APlayer");
		
		// Typical usage of a list argument

		new CommandAPICommand("list")
			.withArguments(new ListArgumentBuilder<>("values", ", ")
				.withList(() -> List.of("cat", "wolf", "axolotl"))
				.withStringMapper()
				.build())
			.executesPlayer((player, args) -> {
				type.set((List<String>) args[0]);
			})
			.register();

		server.dispatchCommand(sender, "list cat, wolf, axolotl"); // normal list
		assertInvalidSyntax(sender, "list cat, wolf, axolotl, wolf"); // don't allow duplicates
		assertInvalidSyntax(sender, "list axolotl, wolf, chicken, cat"); // don't allow unknown items

		assertEquals(List.of("cat", "wolf", "axolotl"), type.get());
		
		// List argument, with duplicates

		new CommandAPICommand("listdup")
			.withArguments(new ListArgumentBuilder<>("values", ", ")
				.allowDuplicates(true)
				.withList(() -> List.of("cat", "wolf", "axolotl"))
				.withStringMapper()
				.build())
			.executesPlayer((player, args) -> {
				type.set((List<String>) args[0]);
			})
			.register();

		server.dispatchCommand(sender, "listdup cat, wolf, axolotl, cat, wolf"); // allow duplicates
		assertInvalidSyntax(sender, "listdup cat, wolf, axolotl, chicken, cat"); // don't allow unknown items

		assertEquals(List.of("cat", "wolf", "axolotl", "cat", "wolf"), type.get());

		// List argument, with a constant list (not using a supplier)
		
		new CommandAPICommand("listconst")
			.withArguments(new ListArgumentBuilder<>("values", ", ")
				.withList(List.of("cat", "wolf", "axolotl"))
				.withStringMapper()
				.build())
			.executesPlayer((player, args) -> {
				type.set((List<String>) args[0]);
			})
			.register();

		server.dispatchCommand(sender, "listconst cat, wolf, axolotl"); // normal list
		assertInvalidSyntax(sender, "listconst cat, wolf, axolotl, wolf"); // don't allow duplicates
		assertInvalidSyntax(sender, "listconst axolotl, wolf, chicken, cat"); // don't allow unknown items

		assertEquals(List.of("cat", "wolf", "axolotl"), type.get());
		
		// List argument using a function
		
		new CommandAPICommand("listfunc")
			.withArguments(new ListArgumentBuilder<>("values", ", ")
				.withList(player -> List.of("cat", "wolf", "axolotl", player.getName()))
				.withStringMapper()
				.build())
			.executesPlayer((player, args) -> {
				type.set((List<String>) args[0]);
			})
			.register();

		server.dispatchCommand(sender, "listfunc cat, wolf, axolotl"); // normal list
		assertInvalidSyntax(sender, "listfunc cat, wolf, axolotl, wolf"); // don't allow duplicates
		assertInvalidSyntax(sender, "listfunc axolotl, wolf, chicken, cat"); // don't allow unknown items
		server.dispatchCommand(sender, "listfunc axolotl, wolf, " + sender.getName()); // sender name

		assertEquals(List.of("cat", "wolf", "axolotl"), type.get());
		assertEquals(List.of("axolotl", "wolf", sender.getName()), type.get());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void executionTestWithListTextArgument() {
		Mut<List<String>> type = Mut.of();

		PlayerMock sender = server.addPlayer("APlayer");

		// Typical usage of a list argument

		new CommandAPICommand("list")
			.withArguments(new ListArgumentBuilder<>("values", ", ")
				.withList(() -> List.of("cat", "wolf", "axolotl"))
				.withStringMapper()
				.buildText())
			.executesPlayer((player, args) -> {
				type.set((List<String>) args[0]);
			})
			.register();

		server.dispatchCommand(sender, "list \"cat, wolf, axolotl\""); // normal list
		assertInvalidSyntax(sender, "list \"cat, wolf, axolotl, wolf\""); // don't allow duplicates
		assertInvalidSyntax(sender, "list \"axolotl, wolf, chicken, cat\""); // don't allow unknown items

		assertEquals(List.of("cat", "wolf", "axolotl"), type.get());

		// List argument, with duplicates

		new CommandAPICommand("listdup")
			.withArguments(new ListArgumentBuilder<>("values", ", ")
				.allowDuplicates(true)
				.withList(() -> List.of("cat", "wolf", "axolotl"))
				.withStringMapper()
				.buildText())
			.executesPlayer((player, args) -> {
				type.set((List<String>) args[0]);
			})
			.register();

		server.dispatchCommand(sender, "listdup \"cat, wolf, axolotl, cat, wolf\""); // allow duplicates
		assertInvalidSyntax(sender, "listdup \"cat, wolf, axolotl, chicken, cat\""); // don't allow unknown items

		assertEquals(List.of("cat", "wolf", "axolotl", "cat", "wolf"), type.get());

		// List argument, with a constant list (not using a supplier)

		new CommandAPICommand("listconst")
			.withArguments(new ListArgumentBuilder<>("values", ", ")
				.withList(List.of("cat", "wolf", "axolotl"))
				.withStringMapper()
				.buildText())
			.executesPlayer((player, args) -> {
				type.set((List<String>) args[0]);
			})
			.register();

		server.dispatchCommand(sender, "listconst \"cat, wolf, axolotl\""); // normal list
		assertInvalidSyntax(sender, "listconst \"cat, wolf, axolotl, wolf\""); // don't allow duplicates
		assertInvalidSyntax(sender, "listconst \"axolotl, wolf, chicken, cat\""); // don't allow unknown items

		assertEquals(List.of("cat", "wolf", "axolotl"), type.get());

		// List argument using a function

		new CommandAPICommand("listfunc")
			.withArguments(new ListArgumentBuilder<>("values", ", ")
				.withList(player -> List.of("cat", "wolf", "axolotl", player.getName()))
				.withStringMapper()
				.buildText())
			.executesPlayer((player, args) -> {
				type.set((List<String>) args[0]);
			})
			.register();

		server.dispatchCommand(sender, "listfunc \"cat, wolf, axolotl\""); // normal list
		assertInvalidSyntax(sender, "listfunc \"cat, wolf, axolotl, wolf\""); // don't allow duplicates
		assertInvalidSyntax(sender, "listfunc \"axolotl, wolf, chicken, cat\""); // don't allow unknown items
		server.dispatchCommand(sender, "listfunc \"axolotl, wolf, " + sender.getName() + "\""); // sender name

		assertEquals(List.of("cat", "wolf", "axolotl"), type.get());
		assertEquals(List.of("axolotl", "wolf", sender.getName()), type.get());

		// List argument followed by another list argument

		new CommandAPICommand("list2")
			.withArguments(new ListArgumentBuilder<>("values", ", ")
				.withList(player -> List.of("cat", "wolf", "axolotl"))
				.withStringMapper()
				.buildText())
			.withArguments(new ListArgumentBuilder<>("morevalues", ", ")
				.withList(player -> List.of("pumpkin", "melon", "cake"))
				.withStringMapper()
				.buildText())
			.executesPlayer((player, args) -> {
				type.set((List<String>) args[0]);
				type.set((List<String>) args[1]);
			})
			.register();

		server.dispatchCommand(sender, "list2 \"cat, wolf, axolotl\" \"pumpkin, melon\"");

		assertEquals(List.of("cat", "wolf", "axolotl"), type.get());
		assertEquals(List.of("pumpkin", "melon"), type.get());
	}

	@Test
	public void executionTestWithPlayerArgument() {
		Mut<Player> type = Mut.of();

		new CommandAPICommand("test")
			.withArguments(new PlayerArgument("target"))
			.executesPlayer((player, args) -> {
				type.set((Player) args[0]);
			})
			.register();

		PlayerMock player = server.addPlayer("APlayer");
		server.dispatchCommand(player, "test APlayer");
		assertInvalidSyntax(player, "test BPlayer");
		assertEquals(player, type.get());
		assertEquals(null, type.get());
	}
	
	@Test
	public void executionTestWithChatComponentArgument() {
		Mut<BaseComponent[]> spigot = Mut.of();
		Mut<Component> adventure = Mut.of();

		new CommandAPICommand("spigot")
			.withArguments(new ChatComponentArgument("text"))
			.executesPlayer((player, args) -> {
				spigot.set((BaseComponent[]) args[0]);
			})
			.register();
		
		new CommandAPICommand("adventure")
			.withArguments(new AdventureChatComponentArgument("text"))
			.executesPlayer((player, args) -> {
				adventure.set((Component) args[0]);
			})
			.register();
		
		//	["", {
		//	    "text": "Once upon a time, there was a guy call "
		//	}, {
		//	    "text": "Skepter",
		//	    "color": "light_purple",
		//	    "hoverEvent": {
		//	        "action": "show_entity",
		//	        "value": "Skepter"
		//	    }
		//	}, {
		//	    "text": " and he created the "
		//	}, {
		//	    "text": "CommandAPI",
		//	    "underlined": true,
		//	    "clickEvent": {
		//	        "action": "open_url",
		//	        "value": "https://github.com/JorelAli/CommandAPI"
		//	    }
		//	}]

		final String json = "[\"[\\\"\\\",{\\\"text\\\":\\\"Once upon a time, there was a guy call \\\"},{\\\"text\\\":\\\"Skepter\\\",\\\"color\\\":\\\"light_purple\\\",\\\"hoverEvent\\\":{\\\"action\\\":\\\"show_entity\\\",\\\"value\\\":\\\"Skepter\\\"}},{\\\"text\\\":\\\" and he created the \\\"},{\\\"text\\\":\\\"CommandAPI\\\",\\\"underlined\\\":true,\\\"clickEvent\\\":{\\\"action\\\":\\\"open_url\\\",\\\"value\\\":\\\"https://github.com/JorelAli/CommandAPI\\\"}}]\"]";
		
		PlayerMock player = server.addPlayer("Skepter");
		server.dispatchCommand(player, "spigot " + json);
		server.dispatchCommand(player, "adventure " + json);
		
		assertArrayEquals(ComponentSerializer.parse(json), spigot.get());
		assertEquals(GsonComponentSerializer.gson().deserialize(json), adventure.get());
	}

	@Test
	public void executionTestWithCommandArgument() {
		Mut<CommandResult> results = Mut.of();

		PlayerMock player = server.addPlayer("APlayer");
		CommandMap commandMap = CommandAPIBukkit.get().getSimpleCommandMap();

		// CommandArgument expects to find commands in the commandMap
		commandMap.registerAll("test", List.of(
			new Command("give") {
				@Override
				public boolean execute(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] strings) {
					return true;
				}
			},
			new Command("data") {
				@Override
				public boolean execute(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] strings) {
					return true;
				}
			},
			new Command("tp") {
				@Override
				public boolean execute(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] strings) {
					return true;
				}
			}
		));

		// Check command retrieval from CommandMap
		new CommandAPICommand("commandargument")
			.withArguments(new CommandArgument("command"))
			.executesPlayer((sender, args) -> {
				results.set((CommandResult) args[0]);
			}).register();

		assertStoresResult(player, "commandargument version",
			results, new CommandResult(commandMap.getCommand("version"), new String[]{}));

		// Check replaceSuggestions
		new CommandAPICommand("restrictedcommand")
			.withArguments(new CommandArgument("command")
				.replaceSuggestions(
					ArgumentSuggestions.strings("give"),
					ArgumentSuggestions.strings(info -> Bukkit.getOnlinePlayers().stream().map(Player::getName).toArray(String[]::new)),
					ArgumentSuggestions.strings("diamond", "minecraft:diamond"),
					ArgumentSuggestions.empty()
				)
			).executesPlayer((sender, args) -> {
				results.set((CommandResult) args[0]);
			}).register();

		server.addPlayer("BPlayer");

		// Valid commands
		assertStoresResult(player, "restrictedcommand give APlayer diamond",
			results, new CommandResult(commandMap.getCommand("give"), new String[]{"APlayer", "diamond"}));
		assertStoresResult(player, "restrictedcommand give BPlayer diamond",
			results, new CommandResult(commandMap.getCommand("give"), new String[]{"BPlayer", "diamond"}));
		assertStoresResult(player, "restrictedcommand give APlayer minecraft:diamond",
			results, new CommandResult(commandMap.getCommand("give"), new String[]{"APlayer", "minecraft:diamond"}));

		// Invalid commands
		assertInvalidSyntax(player, "restrictedcommand data APlayer diamond");
		assertInvalidSyntax(player, "restrictedcommand notacommand APlayer diamond");
		assertInvalidSyntax(player, "restrictedcommand give CPlayer diamond");
		assertInvalidSyntax(player, "restrictedcommand give APlayer dirt");
		assertInvalidSyntax(player, "restrictedcommand give APlayer diamond 64");
		assertInvalidSyntax(player, "restrictedcommand give APlayer");

		// Check branching suggestions
		new CommandAPICommand("multiplecommands")
			.withArguments(
				new CommandArgument("command")
					.branchSuggestions(
						SuggestionsBranch.<CommandSender>suggest(
							ArgumentSuggestions.strings("give"),
							ArgumentSuggestions.strings(info -> Bukkit.getOnlinePlayers().stream().map(Player::getName).toArray(String[]::new))
						).branch(
							SuggestionsBranch.suggest(
								ArgumentSuggestions.strings("diamond", "minecraft:diamond"),
								ArgumentSuggestions.empty()
							),
							SuggestionsBranch.suggest(
								ArgumentSuggestions.strings("dirt", "minecraft:dirt"),
								null,
								ArgumentSuggestions.empty()
							)
						),
						SuggestionsBranch.suggest(
							ArgumentSuggestions.strings("tp"),
							ArgumentSuggestions.strings(info -> Bukkit.getOnlinePlayers().stream().map(Player::getName).toArray(String[]::new)),
							ArgumentSuggestions.strings(info -> Bukkit.getOnlinePlayers().stream().map(Player::getName).toArray(String[]::new))
						)
					)
			).executes((sender, args) -> {
				results.set((CommandResult) args[0]);
			}).register();

		// Valid commands
		assertStoresResult(player, "multiplecommands give APlayer diamond",
			results, new CommandResult(commandMap.getCommand("give"), new String[]{"APlayer", "diamond"}));
		assertStoresResult(player, "multiplecommands give BPlayer diamond",
			results, new CommandResult(commandMap.getCommand("give"), new String[]{"BPlayer", "diamond"}));
		assertStoresResult(player, "multiplecommands give APlayer minecraft:diamond",
			results, new CommandResult(commandMap.getCommand("give"), new String[]{"APlayer", "minecraft:diamond"}));
		assertStoresResult(player, "multiplecommands give APlayer dirt",
			results, new CommandResult(commandMap.getCommand("give"), new String[]{"APlayer", "dirt"}));
		assertStoresResult(player, "multiplecommands give APlayer dirt 64",
			results, new CommandResult(commandMap.getCommand("give"), new String[]{"APlayer", "dirt", "64"}));

		assertStoresResult(player, "multiplecommands tp APlayer BPlayer",
			results, new CommandResult(commandMap.getCommand("tp"), new String[]{"APlayer", "BPlayer"}));
		assertStoresResult(player, "multiplecommands tp BPlayer APlayer",
			results, new CommandResult(commandMap.getCommand("tp"), new String[]{"BPlayer", "APlayer"}));

		// Invalid commands
		assertInvalidSyntax(player, "multiplecommands data get entity APlayer");
		assertInvalidSyntax(player, "multiplecommands notacommand APlayer diamond");
		assertInvalidSyntax(player, "multiplecommands give CPlayer diamond");
		assertInvalidSyntax(player, "multiplecommands give APlayer stone");
		assertInvalidSyntax(player, "multiplecommands give APlayer diamond 64");
		assertInvalidSyntax(player, "multiplecommands give APlayer");
		assertInvalidSyntax(player, "multiplecommands tp APlayer CPlayer");
		assertInvalidSyntax(player, "multiplecommands tp CPlayer APlayer");
		assertInvalidSyntax(player, "multiplecommands tp APlayer");


		assertNull(results.get(), "Expected there to be no results left, but at least one was found");
	}

	@Test // Pre-#321 
	public void executionTwoCommandsSameArgumentDifferentName() {
		Mut<String> str1 = Mut.of();
		Mut<String> str2 = Mut.of();

		new CommandAPICommand("test")
			.withArguments(new StringArgument("str_1"))
			.executesPlayer((player, args) -> {
				str1.set((String) args[0]);
			})
			.register();
		
		new CommandAPICommand("test")
			.withArguments(new StringArgument("str_2"))
			.executesPlayer((player, args) -> {
				str2.set((String) args[0]);
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
				int1.set((int) args[0]);
			})
			.register();
		
		new CommandAPICommand("test")
			.withArguments(new IntegerArgument("str_2", 50, 100))
			.executesPlayer((player, args) -> {
				int2.set((int) args[0]);
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
				int1.set((int) args[0]);
			})
			.register();
		
		new CommandAPICommand("test")
			.withArguments(new IntegerArgument("str_2", 50, 100))
			.executesPlayer((player, args) -> {
				int2.set((int) args[0]);
			})
			.register();

		PlayerMock player = server.addPlayer("APlayer");
		server.dispatchCommand(player, "test 5");
		server.dispatchCommand(player, "test 60");
		assertEquals(5, int1.get());
		assertEquals(60, int1.get());
		assertEquals(null, int2.get());
		
		
	}

}
