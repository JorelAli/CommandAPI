package io.github.jorelali;

import static org.junit.jupiter.api.Assertions.assertFalse;
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
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIConfig;
import dev.jorel.commandapi.nms.NMS_1_19_R1;
import dev.jorel.commandapi.test.CommandAPIServerMock;
import dev.jorel.commandapi.test.MockNMS;

public class TestSuite {

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
		server = MockBukkit.mock(new CommandAPIServerMock());
		
		// Pre-load the CommandAPI, using a specified NMS
		CommandAPI.onLoad(new CommandAPIConfig().setCustomNMS(new MockNMS(new NMS_1_19_R1())));

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
		PlayerMock player = server.addPlayer();
		assertFalse(server.isValidCommandAPICommand(player, "break ~ ~ ~ ~"));
		assertTrue(server.isValidCommandAPICommand(player, "break ~ ~ ~"));
	}

}
