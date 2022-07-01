import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mojang.brigadier.exceptions.CommandSyntaxException;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.WorldMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.AdvancementArgument;
import dev.jorel.commandapi.arguments.BooleanArgument;
import dev.jorel.commandapi.arguments.EntitySelector;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.Location2DArgument;
import dev.jorel.commandapi.arguments.LocationArgument;
import dev.jorel.commandapi.arguments.LocationType;
import dev.jorel.commandapi.arguments.StringArgument;
import dev.jorel.commandapi.wrappers.Location2D;

public class TestFile {

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
	public void executionTestWithStringArg() {
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
	}

	@Test
	public void executionTestWithStringArgNegative() {
		new CommandAPICommand("test").withArguments(new StringArgument("value")).executesPlayer((player, args) -> {
			String value = (String) args[0];
			player.sendMessage("success " + value);
		}).register();

		PlayerMock player = server.addPlayer();
		server.dispatchCommand(player, "test myvalue");
		// Test "the thing", then test "not the thing"
		assertNotEquals("success blah", player.nextMessage());
	}

	@Test
	public void executionTestWithBoolean() {
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
	public void executionTestWithLocationArguments() {
		new CommandAPICommand("loc3")
			.withArguments(new LocationArgument("value", LocationType.PRECISE_POSITION))
			.executesPlayer((player, args) -> {
				Location value = (Location) args[0];
				player.sendMessage(value.getX() + ", " + value.getY() + ", " + value.getZ());
			})
			.register();
		
//		new CommandAPICommand("loc3b")
//			.withArguments(new LocationArgument("value", LocationType.BLOCK_POSITION))
//			.executesPlayer((player, args) -> {
//				Location value = (Location) args[0];
//				player.sendMessage(value.getX() + ", " + value.getY() + ", " + value.getZ());
//			})
//			.register();
		
		new CommandAPICommand("loc2")
			.withArguments(new Location2DArgument("value", LocationType.PRECISE_POSITION))
			.executesPlayer((player, args) -> {
				Location2D value = (Location2D) args[0];
				player.sendMessage(value.getX() + ", " + value.getZ());
			})
			.register();
		
//		new CommandAPICommand("loc2b")
//			.withArguments(new Location2DArgument("value", LocationType.BLOCK_POSITION))
//			.executesPlayer((player, args) -> {
//				Location2D value = (Location2D) args[0];
//				player.sendMessage(value.getX() + ", " + value.getZ());
//			})
//			.register();

		PlayerMock player = server.addPlayer();
		
		server.dispatchCommand(player, "loc3 1 10 15");
//		server.dispatchCommand(player, "loc3b 1 10 15");
		server.dispatchCommand(player, "loc2 1 15");
//		server.dispatchCommand(player, "loc2b 1 15");
		assertEquals("1.5, 10.0, 15.5", player.nextMessage());
//		assertEquals("1, 10, 15", player.nextMessage());
		assertEquals("1.5, 15.5", player.nextMessage());
//		assertEquals("1, 15", player.nextMessage());
		
		player.setLocation(new Location(new WorldMock(), 2, 2, 2));
		server.dispatchCommand(player, "loc3 ~ ~5 ~");
//		server.dispatchCommand(player, "loc3b ~ ~5 ~");
		server.dispatchCommand(player, "loc2 ~ ~5");
//		server.dispatchCommand(player, "loc2b ~ ~5");
		assertEquals("2.0, 7.0, 2.0", player.nextMessage());
//		assertEquals("2, 7, 2", player.nextMessage());
		assertEquals("2.0, 7.0", player.nextMessage());
//		assertEquals("2, 7", player.nextMessage());
	}
	
	@Test
	public void executionTestWithEntitySelectorArgument() {
		new CommandAPICommand("test")
			.withArguments(new EntitySelectorArgument<Player>("value", EntitySelector.ONE_PLAYER))
			.executesPlayer((player, args) -> {
				Player value = (Player) args[0];
				player.sendMessage("success " + value.getName());
			})
			.register();

		PlayerMock player = server.addPlayer("APlayer");
		server.dispatchCommand(player, "test APlayer");

		assertEquals("success APlayer", player.nextMessage());
	}

}
