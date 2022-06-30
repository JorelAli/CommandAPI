import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.bukkit.Bukkit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.StringArgument;

public class TestFile {

	private ServerMock server;
	private Main plugin;

	@BeforeEach
	public void setUp() {
		server = MockBukkit.mock(new CustomServerMock());
		plugin = MockBukkit.load(Main.class);
	}

	@AfterEach
	public void tearDown() {
		// This completely falls apart when the CommandAPI's post-server-loading
		// runnable runs, so we'll simply just not call MockBukkit.unmock();
		
		Bukkit.getScheduler().cancelTasks(plugin);
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
	}

}
