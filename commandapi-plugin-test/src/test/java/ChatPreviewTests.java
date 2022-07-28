import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.AdventureChatArgument;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;

/**
 * Tests for the 40+ arguments in dev.jorel.commandapi.arguments
 */
public class ChatPreviewTests {
	
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
	public void chatPreviewTest() {
		new CommandAPICommand("chatarg")
			.withArguments(new AdventureChatArgument("str").withPreview(info -> {
				if(info.input().contains("hello")) {
					throw CommandAPI.fail(ChatColor.RED + "Input cannot contain the word 'hello'");
					// return Component.text("Input cannot contain the word 'hello'").color(NamedTextColor.RED);
				} else {
					return Component.text(info.input()).decorate(TextDecoration.BOLD);
				}
			}))
			.executesPlayer((sender, args) -> {
				Component component = (Component) args[0];
				sender.sendMessage(component.decorate(TextDecoration.BOLD));
			})
			.register();

		PlayerMock player = server.addPlayer();
		boolean commandResult = server.dispatchCommand(player, "chatarg blah");
		assertTrue(commandResult);
		assertEquals(ChatColor.BOLD + "blah", player.nextMessage());
	}

}
