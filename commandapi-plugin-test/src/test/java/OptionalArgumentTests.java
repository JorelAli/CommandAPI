import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.bukkit.Bukkit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.StringArgument;

/**
 * Tests for optional arguments
 */
public class OptionalArgumentTests {
	
	private CustomServerMock server;
	private Main plugin;

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
	public void optionalArgument1Argument() {
		new CommandAPICommand("test")
			.withOptionalArgument(new StringArgument("stringarg"), "hello")
			.executesPlayer((player, args) -> {
				player.sendMessage("value: " + args[0]);
			})
			.register();

		PlayerMock player = server.addPlayer();
		assertTrue(server.dispatchCommand(player, "test"));
		assertTrue(server.dispatchCommand(player, "test bye"));
		assertEquals("value: hello", player.nextMessage());
		assertEquals("value: bye", player.nextMessage());
	}

}
