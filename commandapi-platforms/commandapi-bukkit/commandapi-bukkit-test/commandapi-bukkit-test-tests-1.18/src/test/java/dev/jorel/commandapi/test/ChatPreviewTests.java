package dev.jorel.commandapi.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.bukkit.ChatColor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.entity.PlayerMock;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.AdventureChatArgument;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;

/**
 * Tests for chat preview (1.19 - 1.19.2)
 */
@SuppressWarnings("null")
public class ChatPreviewTests extends TestBase {

	/*********
	 * Setup *
	 *********/

	@BeforeEach
	public void setUp() {
		super.setUp();
	}

	@AfterEach
	public void tearDown() {
		super.tearDown();
	}

	/*********
	 * Tests *
	 *********/

	@Test
	public void chatPreviewTest() {
		new CommandAPICommand("chatarg")
			.withArguments(new AdventureChatArgument("str").withPreview(info -> {
				if (info.input().contains("hello")) {
					throw CommandAPI.failWithString(ChatColor.RED + "Input cannot contain the word 'hello'");
					// return Component.text("Input cannot contain the word
					// 'hello'").color(NamedTextColor.RED);
				} else {
					return Component.text(info.input()).decorate(TextDecoration.BOLD);
				}
			}))
			.executesPlayer((sender, args) -> {
				Component component = (Component) args.get("str");
				sender.sendMessage(component.decorate(TextDecoration.BOLD));
			})
			.register();

		PlayerMock player = server.addPlayer();
		boolean commandResult = server.dispatchCommand(player, "chatarg blah");
		// TODO: This doesn't actually test previewing!
		assertTrue(commandResult);
		assertEquals(ChatColor.BOLD + "blah", player.nextMessage());
	}

}
