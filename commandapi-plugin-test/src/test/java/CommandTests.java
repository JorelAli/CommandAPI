import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collection;
import java.util.stream.Collectors;

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
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import dev.jorel.commandapi.arguments.Location2DArgument;
import dev.jorel.commandapi.arguments.LocationArgument;
import dev.jorel.commandapi.arguments.LocationType;
import dev.jorel.commandapi.arguments.StringArgument;
import dev.jorel.commandapi.exceptions.GreedyArgumentException;
import dev.jorel.commandapi.exceptions.InvalidCommandNameException;
import dev.jorel.commandapi.wrappers.Location2D;

/**
 * Tests for command semantics
 */
public class CommandTests {

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
	public void testGreedyStringArgumentNotAtEnd() {
		assertDoesNotThrow(() -> {
			new CommandAPICommand("test")
				.withArguments(new StringArgument("arg1"))
				.withArguments(new GreedyStringArgument("arg2"))
				.executes((sender, args) -> {
					String arg1 = (String) args[0];
					String arg2 = (String) args[0];
				})
				.register();
		});

		assertThrows(GreedyArgumentException.class, () -> {
			new CommandAPICommand("test")
				.withArguments(new GreedyStringArgument("arg1"))
				.withArguments(new StringArgument("arg2"))
				.executes((sender, args) -> {
					String arg1 = (String) args[0];
					String arg2 = (String) args[0];
				})
				.register();
		});
	}

	@Test
	public void testInvalidCommandName() {
		assertThrows(InvalidCommandNameException.class, () -> {
			new CommandAPICommand(null)
				.withArguments(new StringArgument("arg1"))
				.executes((sender, args) -> {
					String arg1 = (String) args[0];
				})
				.register();
		});
		
		assertThrows(InvalidCommandNameException.class, () -> {
			new CommandAPICommand("")
				.withArguments(new StringArgument("arg1"))
				.executes((sender, args) -> {
					String arg1 = (String) args[0];
				})
				.register();
		});
		
		assertThrows(InvalidCommandNameException.class, () -> {
			new CommandAPICommand("my command")
				.withArguments(new StringArgument("arg1"))
				.executes((sender, args) -> {
					String arg1 = (String) args[0];
				})
				.register();
		});
	}
	
	@Test
	public void testNoExecutor() {
		// TODO: Catch this case. Need to check if has no executor AND has no
		// subcommand or otherwise when .register() called
		new CommandAPICommand("test")
			.withArguments(new StringArgument("arg1"))
			.register();
	}

}
