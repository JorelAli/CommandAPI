package io.github.jorelali;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mojang.brigadier.exceptions.CommandSyntaxException;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import dev.jorel.commandapi.CommandAPIVersionHandler;
import dev.jorel.commandapi.test.CommandAPIServerMock;
import dev.jorel.commandapi.test.MockPlatform;
import dev.jorel.commandapi.test.Version;

public class MyTests {

	private CommandAPIServerMock server;
	private Main plugin;

	private String getDispatcherString() {
		try {
			return Files.readString(new File("command_registration.json").toPath());
		} catch (IOException e) {
			return "";
		}
	}

	public void assertInvalidSyntax(CommandSender sender, String command) {
		assertThrows(CommandSyntaxException.class, () -> assertTrue(server.dispatchThrowableCommand(sender,command)));
	}

	@BeforeEach
	public void setUp() {
		CommandAPIVersionHandler.setVersion(Version.MINECRAFT_1_20);
		server = MockBukkit.mock(new CommandAPIServerMock());
		plugin = MockBukkit.load(Main.class);
	}
		
	@AfterEach
	public void tearDown() {
		if(server != null) {
			Bukkit.getScheduler().cancelTasks(plugin);
			if (plugin != null) {
				plugin.onDisable();
			}
			MockBukkit.unmock();
		}
		server = null;
		plugin = null;
		MockPlatform.unload();
	}

	@Test
	public void executionTest() {
		PlayerMock player = server.addPlayer("myname");
		
		server.dispatchCommand(player, "myeffect myname speed");
		
		assertEquals(1, player.getActivePotionEffects().size());
		
//		assertThrows(CommandSyntaxException.class, () -> server.dispatchThrowableCommand(player, "break ~ ~ ~ ~"));
//		assertDoesNotThrow(() -> server.dispatchThrowableCommand(player, "break ~ ~ ~"));
	}

}
