import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import com.mojang.brigadier.exceptions.CommandSyntaxException;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.WorldMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.AdvancementArgument;
import dev.jorel.commandapi.arguments.AdventureChatComponentArgument;
import dev.jorel.commandapi.arguments.BooleanArgument;
import dev.jorel.commandapi.arguments.ChatComponentArgument;
import dev.jorel.commandapi.arguments.EntitySelector;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import dev.jorel.commandapi.arguments.ListArgumentBuilder;
import dev.jorel.commandapi.arguments.Location2DArgument;
import dev.jorel.commandapi.arguments.LocationArgument;
import dev.jorel.commandapi.arguments.LocationType;
import dev.jorel.commandapi.arguments.PlayerArgument;
import dev.jorel.commandapi.arguments.PotionEffectArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import dev.jorel.commandapi.wrappers.Location2D;
import io.papermc.paper.text.PaperComponents;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.BaseComponentSerializer;

/**
 * Tests for the 40+ arguments in dev.jorel.commandapi.arguments
 */
public class ArgumentTests {
	
	private CustomServerMock server;
	private Main plugin;

	private String getDispatcherString() {
		try {
			return Files.readString(new File("command_registration.json").toPath());
		} catch (IOException e) {
			return "";
		}
	}

	@BeforeEach
	public void setUp() {
		server = MockBukkit.mock(new CustomServerMock());
		plugin = MockBukkit.load(Main.class);
	}

	@AfterEach
	public void tearDown() {
		Bukkit.getScheduler().cancelTasks(plugin);
		plugin.onDisable();
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
		assertThrows(CommandSyntaxException.class, () -> assertTrue(server.dispatchThrowableCommand(player, "test hello@email.com")));
		assertThrows(CommandSyntaxException.class, () -> assertTrue(server.dispatchThrowableCommand(player, "test yesn't")));
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
		assertThrows(CommandSyntaxException.class, () -> server.dispatchThrowableCommand(player, "test aaaaa"));
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

		server.dispatchCommand(sender, "list cat, wolf, axolotl");
		server.dispatchCommand(sender, "list cat, wolf, axolotl, chicken");
		server.dispatchCommand(sender, "list axolotl, wolf, chicken, axolotl");

		assertEquals(List.of("cat", "wolf", "axolotl"), type.get());
		assertEquals(List.of("cat", "wolf", "axolotl"), type.get());
		assertEquals(List.of("axolotl", "wolf"), type.get());
		
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

		server.dispatchCommand(sender, "listdup cat, wolf, axolotl, cat, wolf");
		server.dispatchCommand(sender, "listdup cat, wolf, axolotl, chicken, cat");
		server.dispatchCommand(sender, "listdup axolotl, wolf, chicken, axolotl, axolotl, axolotl, axolotl, axolotl, wolf");

		assertEquals(List.of("cat", "wolf", "axolotl", "cat", "wolf"), type.get());
		assertEquals(List.of("cat", "wolf", "axolotl", "cat"), type.get());
		assertEquals(List.of("axolotl", "wolf", "axolotl", "axolotl", "axolotl", "axolotl", "axolotl", "wolf"), type.get());

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

		server.dispatchCommand(sender, "listconst cat, wolf, axolotl");
		server.dispatchCommand(sender, "listconst cat, wolf, axolotl, chicken");
		server.dispatchCommand(sender, "listconst axolotl, wolf, chicken, axolotl");

		assertEquals(List.of("cat", "wolf", "axolotl"), type.get());
		assertEquals(List.of("cat", "wolf", "axolotl"), type.get());
		assertEquals(List.of("axolotl", "wolf"), type.get());
		
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
	
		server.dispatchCommand(sender, "listfunc cat, wolf, axolotl");
		server.dispatchCommand(sender, "listfunc cat, wolf, axolotl, chicken");
		server.dispatchCommand(sender, "listfunc axolotl, wolf, chicken, axolotl");
		server.dispatchCommand(sender, "listfunc axolotl, wolf, chicken, axolotl, " + sender.getName());
	
		assertEquals(List.of("cat", "wolf", "axolotl"), type.get());
		assertEquals(List.of("cat", "wolf", "axolotl"), type.get());
		assertEquals(List.of("axolotl", "wolf"), type.get());
		assertEquals(List.of("axolotl", "wolf", sender.getName()), type.get());
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
		assertThrows(CommandSyntaxException.class, () -> server.dispatchThrowableCommand(player, "test BPlayer"));
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

		PlayerMock player = server.addPlayer("Skepter");
		server.dispatchCommand(player, "spigot [\"[\\\"\\\",{\\\"text\\\":\\\"Once upon a time, there was a guy call \\\"},{\\\"text\\\":\\\"Skepter\\\",\\\"color\\\":\\\"light_purple\\\",\\\"hoverEvent\\\":{\\\"action\\\":\\\"show_entity\\\",\\\"value\\\":\\\"Skepter\\\"}},{\\\"text\\\":\\\" and he created the \\\"},{\\\"text\\\":\\\"CommandAPI\\\",\\\"underlined\\\":true,\\\"clickEvent\\\":{\\\"action\\\":\\\"open_url\\\",\\\"value\\\":\\\"https://github.com/JorelAli/CommandAPI\\\"}}]\"]");
		server.dispatchCommand(player, "adventure [\"[\\\"\\\",{\\\"text\\\":\\\"Once upon a time, there was a guy call \\\"},{\\\"text\\\":\\\"Skepter\\\",\\\"color\\\":\\\"light_purple\\\",\\\"hoverEvent\\\":{\\\"action\\\":\\\"show_entity\\\",\\\"value\\\":\\\"Skepter\\\"}},{\\\"text\\\":\\\" and he created the \\\"},{\\\"text\\\":\\\"CommandAPI\\\",\\\"underlined\\\":true,\\\"clickEvent\\\":{\\\"action\\\":\\\"open_url\\\",\\\"value\\\":\\\"https://github.com/JorelAli/CommandAPI\\\"}}]\"]");
		
		// We literally cannot test the result of Spigot's BaseComponent[], so
		// we only test Adventure's Component
		
		Component expectedAdventureComponent = GsonComponentSerializer.gson().deserialize("[\"[\\\"\\\",{\\\"text\\\":\\\"Once upon a time, there was a guy call \\\"},{\\\"text\\\":\\\"Skepter\\\",\\\"color\\\":\\\"light_purple\\\",\\\"hoverEvent\\\":{\\\"action\\\":\\\"show_entity\\\",\\\"value\\\":\\\"Skepter\\\"}},{\\\"text\\\":\\\" and he created the \\\"},{\\\"text\\\":\\\"CommandAPI\\\",\\\"underlined\\\":true,\\\"clickEvent\\\":{\\\"action\\\":\\\"open_url\\\",\\\"value\\\":\\\"https://github.com/JorelAli/CommandAPI\\\"}}]\"]");
		assertEquals(expectedAdventureComponent, adventure.get());
	}

}
