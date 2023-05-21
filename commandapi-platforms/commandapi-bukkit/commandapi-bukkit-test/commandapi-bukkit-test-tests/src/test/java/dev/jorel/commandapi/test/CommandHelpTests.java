package dev.jorel.commandapi.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.StringArgument;

/**
 * Tests to do with command help
 */
class CommandHelpTests extends TestBase {

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
	void testRegisterCommandWithHelp() {
		new CommandAPICommand("test")
			.withHelp("short description", "full description")
			.executes((sender, args) -> {
			})
			.register();

		disablePaperImplementations();
		server.getScheduler().performOneTick();
		Player player = server.addPlayer("APlayer");

		// Check the help topic was added
		assertNotNull(server.getHelpMap().getHelpTopic("/test"));

		// Check the short description
		assertEquals("short description", server.getHelpMap().getHelpTopic("/test").getShortText());
		
		// Check the full description
		assertEquals(ChatColor.translateAlternateColorCodes('&', """
			short description
			&6Description: &ffull description
			&6Usage: &f/test"""), server.getHelpMap().getHelpTopic("/test").getFullText(player));
	}
	
	@Test
	void testRegisterCommandWithShortDescription() {
		new CommandAPICommand("test")
			.withShortDescription("short description")
			.executes((sender, args) -> {
			})
			.register();

		disablePaperImplementations();
		server.getScheduler().performOneTick();
		Player player = server.addPlayer("APlayer");

		// Check the help topic was added
		assertNotNull(server.getHelpMap().getHelpTopic("/test"));

		// Check the short description
		assertEquals("short description", server.getHelpMap().getHelpTopic("/test").getShortText());
		
		// Check the full description
		assertEquals(ChatColor.translateAlternateColorCodes('&', """
			short description
			&6Usage: &f/test"""), server.getHelpMap().getHelpTopic("/test").getFullText(player));
	}

	@Test
	void testRegisterCommandFullDescription() {
		new CommandAPICommand("test")
			.withFullDescription("full description")
			.executes((sender, args) -> {
			})
			.register();

		disablePaperImplementations();
		server.getScheduler().performOneTick();
		Player player = server.addPlayer("APlayer");

		// Check the help topic was added
		assertNotNull(server.getHelpMap().getHelpTopic("/test"));

		// Check the short description
		assertEquals("full description", server.getHelpMap().getHelpTopic("/test").getShortText());
		
		// Check the full description
		assertEquals(ChatColor.translateAlternateColorCodes('&', """
			full description
			&6Description: &ffull description
			&6Usage: &f/test"""), server.getHelpMap().getHelpTopic("/test").getFullText(player));
	}

	@Test
	void testRegisterCommandNoDescription() {
		new CommandAPICommand("test")
			.executes((sender, args) -> {
			})
			.register();

		disablePaperImplementations();
		server.getScheduler().performOneTick();
		Player player = server.addPlayer("APlayer");

		// Check the help topic was added
		assertNotNull(server.getHelpMap().getHelpTopic("/test"));

		// Check the short description
		assertEquals("A command by the CommandAPITest plugin.", server.getHelpMap().getHelpTopic("/test").getShortText());
		
		// Check the full description
		assertEquals(ChatColor.translateAlternateColorCodes('&', """
			A command by the CommandAPITest plugin.
			&6Usage: &f/test"""), server.getHelpMap().getHelpTopic("/test").getFullText(player));
	}

	@Test
	void testRegisterCommandWithHelpWithAliases() {
		new CommandAPICommand("test")
			.withHelp("short description", "full description")
			.withAliases("othertest", "othercommand")
			.executes((sender, args) -> {
			})
			.register();

		disablePaperImplementations();
		server.getScheduler().performOneTick();
		Player player = server.addPlayer("APlayer");

		// Check the help topic was added
		assertNotNull(server.getHelpMap().getHelpTopic("/test"));

		// Check the short description
		assertEquals("short description", server.getHelpMap().getHelpTopic("/test").getShortText());
		
		// Check the full description
		
		// TODO: This test only passes if there is a trailing space character after
		// the usage section. The CommandAPI should trim this when usage help is
		// generated!
		assertEquals(ChatColor.translateAlternateColorCodes('&', """
			short description
			&6Description: &ffull description
			&6Usage: &f/test
			&6Aliases: &fothertest, othercommand"""), server.getHelpMap().getHelpTopic("/test").getFullText(player));
	}
	
	@Test
	void testRegisterCommandWithMultipleArguments() {
		new CommandAPICommand("test")
			.withHelp("short description", "full description")
			.withArguments(new StringArgument("arg1"))
			.withArguments(new IntegerArgument("arg2"))
			.executes((sender, args) -> {
			})
			.register();

		disablePaperImplementations();
		server.getScheduler().performOneTick();
		Player player = server.addPlayer("APlayer");

		// Check the help topic was added
		assertNotNull(server.getHelpMap().getHelpTopic("/test"));

		// Check the short description
		assertEquals("short description", server.getHelpMap().getHelpTopic("/test").getShortText());
		
		// Check the full description
		assertEquals(ChatColor.translateAlternateColorCodes('&', """
			short description
			&6Description: &ffull description
			&6Usage: &f/test <arg1> <arg2>"""), server.getHelpMap().getHelpTopic("/test").getFullText(player));
	}

	@Test
	void testRegisterMultipleCommands() {
		new CommandAPICommand("test")
			.withHelp("short description", "full description")
			.withArguments(new StringArgument("arg1"))
			.executes((sender, args) -> {
			})
			.register();

		new CommandAPICommand("test")
			.withHelp("short description", "full description")
			.withArguments(new StringArgument("arg1"))
			.withArguments(new IntegerArgument("arg2"))
			.executes((sender, args) -> {
			})
			.register();

		disablePaperImplementations();
		server.getScheduler().performOneTick();
		Player player = server.addPlayer("APlayer");

		// Check the help topic was added
		assertNotNull(server.getHelpMap().getHelpTopic("/test"));

		// Check the short description
		assertEquals("short description", server.getHelpMap().getHelpTopic("/test").getShortText());
		
		// Check the full description

		// TODO: This test only passes if there is a trailing space character after
		// the first command in the usage section. The CommandAPI should trim this
		// when usage help is generated!
		assertEquals(ChatColor.translateAlternateColorCodes('&', """
			short description
			&6Description: &ffull description
			&6Usage: &f
			- /test <arg1>
			- /test <arg1> <arg2>"""), server.getHelpMap().getHelpTopic("/test").getFullText(player));
	}

}
