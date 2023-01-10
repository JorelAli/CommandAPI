package dev.jorel.commandapi.test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import com.mojang.brigadier.exceptions.CommandSyntaxException;

import be.seeseemelk.mockbukkit.MockBukkit;
import dev.jorel.commandapi.CommandAPIVersionHandler;
import dev.jorel.commandapi.nms.NMS;

public abstract class TestBase {

	public CommandAPIServerMock server;
	public Main plugin;
	
	public void setUp(Supplier<NMS<?>> nmsImplementation) {
		CommandAPIVersionHandler.setPlatform(nmsImplementation);
		server = MockBukkit.mock(new CommandAPIServerMock());
		plugin = MockBukkit.load(Main.class);
	}

	public void setUp() {
		CommandAPIVersionHandler.setPlatform(CommandAPIVersionHandler.DEFAULT_NMS_IMPLEMENTATION);
		server = MockBukkit.mock(new CommandAPIServerMock());
		plugin = MockBukkit.load(Main.class);
	}

	public void tearDown() {
		Bukkit.getScheduler().cancelTasks(plugin);
		if (plugin != null) {
			plugin.onDisable();
		}
		MockBukkit.unmock();
		CommandAPIVersionHandler.resetPlatform();
	}

	public <T> void assertStoresResult(CommandSender sender, String command, Mut<T> queue, T expected) {
		assertDoesNotThrow(() -> assertTrue(
			server.dispatchThrowableCommand(sender, command),
			"Expected command dispatch to return true, but it gave false"));
		assertEquals(expected, queue.get());
	}

	@Deprecated
	public void assertInvalidSyntax(CommandSender sender, String command) {
		// XXX: Bogus code, do not use. Use assertCommandFailsWith instead.
		assertThrows(CommandSyntaxException.class, () -> assertTrue(server.dispatchThrowableCommand(sender, command)));
	}
	
	public void assertCommandFailsWith(CommandSender sender, String command, String message) {
		CommandSyntaxException exception = assertThrows(CommandSyntaxException.class, () -> server.dispatchThrowableCommand(sender, command));
		assertEquals(message, exception.getMessage());
	}
	
	public void assertNotCommandFailsWith(CommandSender sender, String command, String message) {
		CommandSyntaxException exception = assertThrows(CommandSyntaxException.class, () -> server.dispatchThrowableCommand(sender, command));
		assertNotEquals(message, exception.getMessage());
	}
	
	public void assertNoMoreResults(Mut<?> mut) {
		assertThrows(NoSuchElementException.class, () -> mut.get(), "Expected there to be no results left, but at least one was found");
	}

	public String getDispatcherString() {
		try {
			return Files.readString(new File(plugin.getDataFolder(), "command_registration.json").toPath());
		} catch (IOException e) {
			e.printStackTrace(System.out);
			return "";
		}
	}
	
	public void registerDummyCommands(CommandMap commandMap, String... commandName) {
		commandMap.registerAll("minecraft", Arrays.stream(commandName).map(name -> 
			new Command(name) {
				@Override
				public boolean execute(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] strings) {
					return true;
				}
			}
		).collect(Collectors.toList()));
	}

}
