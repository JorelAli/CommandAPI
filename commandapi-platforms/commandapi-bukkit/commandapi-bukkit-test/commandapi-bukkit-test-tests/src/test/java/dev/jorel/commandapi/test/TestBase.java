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
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.opentest4j.AssertionFailedError;

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.context.StringRange;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestion;

import be.seeseemelk.mockbukkit.MockBukkit;
import dev.jorel.commandapi.CommandAPIVersionHandler;
import dev.jorel.commandapi.MCVersion;
import dev.jorel.commandapi.PaperImplementations;
import dev.jorel.commandapi.executors.PlayerCommandExecutor;

public abstract class TestBase {

	public CommandAPIServerMock server;
	public JavaPlugin plugin;
	public MCVersion version;
	
	public TestBase() {
		this.version = CommandAPIVersionHandler.getVersion();
	}

	public void setUp() {
		setUp(Main.class);
	}

	public <T extends JavaPlugin> void setUp(Class<T> pluginClass) {
		// resetAllPotions();
		server = MockBukkit.mock(new CommandAPIServerMock());
		plugin = MockBukkit.load(pluginClass);
	}

	public void tearDown() {
		if(server != null) {
			Bukkit.getScheduler().cancelTasks(plugin);
			if (plugin != null) {
				plugin.onDisable();
			}
			MockBukkit.unmock();
		}
	}

	public static final PlayerCommandExecutor P_EXEC = (player, args) -> {};
	
	private void resetAllPotions() {
		PotionEffectType[] arr = MockPlatform.getFieldAs(PotionEffectType.class, "byId", null, PotionEffectType[].class);
		for(int i = 0; i < arr.length; i++) {
			arr[i] = null;
		}
		
		MockPlatform.getFieldAs(PotionEffectType.class, "byName", null, Map.class).clear();
		
		@SuppressWarnings("unchecked")
		Map<String, PotionEffectType> byKey = MockPlatform.getFieldAs(PotionEffectType.class, "byName", null, Map.class);
		if(byKey != null) {
			byKey.clear();
		}
		
		MockPlatform.setField(PotionEffectType.class, "acceptingNew", null, true);
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
	
	public <T> void compareLists(Collection<T> list1, Collection<T> list2) {
		Set<T> s1 = new LinkedHashSet<>(list1);
		Set<T> s2 = new LinkedHashSet<>(list2);
		
		Set<T> s1_2 = new LinkedHashSet<>(list1);
		Set<T> s2_2 = new LinkedHashSet<>(list2);
		
		s1.removeAll(s2);
		s2_2.removeAll(s1_2);
		System.out.println("List 1 has the following extra items: " + s1);
		System.out.println("List 2 has the following extra items: " + s2_2);
	}
	
	public static void disablePaperImplementations() {
		MockPlatform.setField(PaperImplementations.class, "isPaperPresent", MockPlatform.get().getPaper(), false);
	}
	
	/***************
	 * Suggestions *
	 ***************/
	
	public Suggestion mkSuggestion(String text, String tooltip) {
		return new Suggestion(StringRange.at(0), text, new LiteralMessage(tooltip));
	}
	
	// We only care about checking the text and tooltip message, nothing else
	public boolean suggestionEquals(Suggestion suggestion1, Suggestion suggestion2) {
		return suggestion1.getText().equals(suggestion2.getText())
			&& suggestion1.getTooltip().getString().equals(suggestion2.getTooltip().getString());
	}
	
	public void assertSuggestionListEquals(List<Suggestion> list1, List<Suggestion> list2) {
		if (list1.size() != list2.size()) {
			throw new AssertionFailedError("List " + list1 + " and " + list2 + " have differing lengths");
		}
		for (int i = 0; i < list1.size(); i++) {
			if(!suggestionEquals(list1.get(i), list2.get(i))) {
				throw new AssertionFailedError("Expected: <" + list1 + "> but was: <" + list2 + ">");
			}
		}
	}

}
