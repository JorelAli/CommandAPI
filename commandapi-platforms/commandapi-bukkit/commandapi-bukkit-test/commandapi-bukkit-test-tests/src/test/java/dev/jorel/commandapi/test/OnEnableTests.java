package dev.jorel.commandapi.test;

import com.mojang.brigadier.tree.RootCommandNode;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkit;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.StringArgument;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.help.HelpTopic;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Run CommandAPI.onEnable()'s scheduler
 */
class OnEnableTests extends TestBase {

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
	void testOnEnableExecution() {
		disablePaperImplementations();
		assertDoesNotThrow(() -> server.getScheduler().performOneTick());
	}

	@Test
	void testOnEnableRegisterCommand() {
		disablePaperImplementations();
		assertDoesNotThrow(() -> server.getScheduler().performOneTick());

		assertFalse(CommandAPI.canRegister());

		Mut<String> results = Mut.of();
		new CommandAPICommand("command")
			.withArguments(new StringArgument("argument"))
			.withAliases("alias1", "alias2")
			.withPermission("permission")
			.withHelp("short description", "full description")
			.executes((sender, args) -> {
				results.set(args.getUnchecked(0));
			})
			.register();


		// Make sure command tree built correctly
		assertEquals("""
			{
			  "type": "root",
			  "children": {
			    "alias1": {
			      "type": "literal",
			      "children": {
			        "argument": {
			          "type": "argument",
			          "parser": "brigadier:string",
			          "properties": {
			            "type": "word"
			          },
			          "executable": true
			        }
			      }
			    },
			    "alias2": {
			      "type": "literal",
			      "children": {
			        "argument": {
			          "type": "argument",
			          "parser": "brigadier:string",
			          "properties": {
			            "type": "word"
			          },
			          "executable": true
			        }
			      }
			    },
			    "command": {
			      "type": "literal",
			      "children": {
			        "argument": {
			          "type": "argument",
			          "parser": "brigadier:string",
			          "properties": {
			            "type": "word"
			          },
			          "executable": true
			        }
			      }
			    }
			  }
			}""", getDispatcherString());

		// Make sure command and its aliases exist in the Bukkit CommandMap as VanillaCommandWrappers
		SimpleCommandMap commandMap = CommandAPIBukkit.get().getSimpleCommandMap();

		Command mainCommand = commandMap.getCommand("command");
		assertNotNull(mainCommand);
		assertTrue(CommandAPIBukkit.get().isVanillaCommandWrapper(mainCommand));

		Command alias1Command = commandMap.getCommand("alias1");
		assertNotNull(alias1Command);
		assertTrue(CommandAPIBukkit.get().isVanillaCommandWrapper(alias1Command));

		Command alias2Command = commandMap.getCommand("alias2");
		assertNotNull(alias2Command);
		assertTrue(CommandAPIBukkit.get().isVanillaCommandWrapper(alias2Command));

		// Make sure namespaces were set up as well
		assertEquals(mainCommand, commandMap.getCommand("minecraft:command"));
		assertEquals(alias1Command, commandMap.getCommand("minecraft:alias1"));
		assertEquals(alias2Command, commandMap.getCommand("minecraft:alias2"));

		// Make sure permissions were added to Bukkit commands
		assertEquals("permission", mainCommand.getPermission());
		assertEquals("permission", alias1Command.getPermission());
		assertEquals("permission", alias2Command.getPermission());


		// Make sure commands were added to 'resources' dispatcher
		RootCommandNode<?> root = CommandAPIBukkit.get().getResourcesDispatcher().getRoot();

		assertNotNull(root.getChild("command"));
		assertNotNull(root.getChild("alias1"));
		assertNotNull(root.getChild("alias2"));


		// Check the help topic was added
		HelpTopic topic = server.getHelpMap().getHelpTopic("/command");
		assertNotNull(topic);

		// Check the short description
		assertEquals("short description", topic.getShortText());

		// Check the full description
		assertEquals(ChatColor.translateAlternateColorCodes('&', """
			short description
			&6Description: &ffull description
			&6Usage: &f/command <argument>
			&6Aliases: &falias1, alias2"""), topic.getFullText(null));
	}
}
