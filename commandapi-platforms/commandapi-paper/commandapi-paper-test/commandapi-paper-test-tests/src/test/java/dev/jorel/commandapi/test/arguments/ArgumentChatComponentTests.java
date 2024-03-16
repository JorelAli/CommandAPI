package dev.jorel.commandapi.test.arguments;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.entity.PlayerMock;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.MCVersion;
import dev.jorel.commandapi.arguments.ChatComponentArgument;
import dev.jorel.commandapi.test.Mut;
import dev.jorel.commandapi.test.TestBase;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;

// TODO: Split into multiple modules
/**
 * Tests for the {@link ChatComponentArgument}
 */
class ArgumentChatComponentTests extends TestBase {

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
	
	final String json = "[\"%s\"]".formatted("""
		["", {
		    "text": "Once upon a time, there was a guy "
		}, {
		    "text": "Skepter",
		    "color": "light_purple",
		    "hoverEvent": {
		        "action": "show_entity",
		        "value": "Skepter"
		    }
		}, {
		    "text": " and he created the "
		}, {
		    "text": "CommandAPI",
		    "underlined": true,
		    "clickEvent": {
		        "action": "open_url",
		        "value": "https://github.com/JorelAli/CommandAPI"
		    }
		}]
		""".stripIndent().replace("\n", "").replace("\r", "").replace("\"", "\\\""));
	
	// The above, in normal human-readable JSON gets turned into this for command purposes:
	// [\"[\\\"\\\",{\\\"text\\\":\\\"Once upon a time, there was a guy call \\\"},{\\\"text\\\":\\\"Skepter\\\",\\\"color\\\":\\\"light_purple\\\",\\\"hoverEvent\\\":{\\\"action\\\":\\\"show_entity\\\",\\\"value\\\":\\\"Skepter\\\"}},{\\\"text\\\":\\\" and he created the \\\"},{\\\"text\\\":\\\"CommandAPI\\\",\\\"underlined\\\":true,\\\"clickEvent\\\":{\\\"action\\\":\\\"open_url\\\",\\\"value\\\":\\\"https://github.com/JorelAli/CommandAPI\\\"}}]\"]

	/*********
	 * Tests *
	 *********/

	@Disabled
	@Test
	void executionTestWithSpigotChatComponentArgument() {
		Mut<BaseComponent[]> results = Mut.of();

		new CommandAPICommand("test")
			.withArguments(new ChatComponentArgument("text"))
			.executesPlayer((player, args) -> {
				results.set((BaseComponent[]) args.get(0));
			})
			.register();

		PlayerMock player = server.addPlayer("Skepter");

		// /test [\"[\\\"\\\",{\\\"text ... /CommandAPI\\\"}}]\"]
		// Dev note: See comment at top of file
		server.dispatchCommand(player, "test " + json);
		assertArrayEquals(ComponentSerializer.parse(json), results.get());
		
		// /test []
		// Fails due to invalid JSON for a chat component
		if (version.lessThan(MCVersion.V1_18)) {
			assertCommandFailsWith(player, "test []", "Invalid chat component: empty at position 8: test []<--[HERE]");
		} else if(version.greaterThanOrEqualTo(MCVersion.V1_20_3)) {
			assertCommandFailsWith(player, "test []", "Invalid chat component: Not a JSON object: [] at position 8: test []<--[HERE]");
		} else {
			assertCommandFailsWith(player, "test []", "Invalid chat component: Invalid chat component: empty at position 8: test []<--[HERE] at position 8: test []<--[HERE]");	
		}
		
		// /test [\"[\"\",{\"text\":\"Some text with bad quote escaping\"}\"]
		// Fails due to inner quotes not being escaped with a \ character
		if (version.greaterThanOrEqualTo(MCVersion.V1_20_3)) {
			assertCommandFailsWith(player, "test [\"[\"\",{\"text\":\"Some text with bad quote escaping\"}\"]", "Invalid chat component: Unterminated array at line 1 column 6 path $[1] at position 11: ...est [\"[\"\",<--[HERE]");
		} else {
			assertCommandFailsWith(player, "test [\"[\"\",{\"text\":\"Some text with bad quote escaping\"}\"]", "Invalid chat component: Unterminated array at line 1 column 6 path $[1] at position 5: test <--[HERE]");
		}
		
		assertNoMoreResults(results);
	}

	@Test
	void executionTestWithAdventureChatComponentArgument() {
		Mut<Component> results = Mut.of();

		new CommandAPICommand("test")
			.withArguments(new ChatComponentArgument("text"))
			.executesPlayer((player, args) -> {
				results.set((Component) args.get(0));
			})
			.register();

		PlayerMock player = server.addPlayer("Skepter");

		// /test [\"[\\\"\\\",{\\\"text ... /CommandAPI\\\"}}]\"]
		// Dev note: See comment at top of file
		server.dispatchCommand(player, "test " + json);
		assertEquals(GsonComponentSerializer.gson().deserialize(json), results.get());
		
		// /test []
		// Fails due to invalid JSON for a chat component
		if (version.lessThan(MCVersion.V1_18)) {
			assertCommandFailsWith(player, "test []", "Invalid chat component: empty at position 8: test []<--[HERE]");
		} else if(version.greaterThanOrEqualTo(MCVersion.V1_20_3)) {
			assertCommandFailsWith(player, "test []", "Invalid chat component: Not a JSON object: [] at position 8: test []<--[HERE]");
		} else {
			assertCommandFailsWith(player, "test []", "Invalid chat component: Invalid chat component: empty at position 8: test []<--[HERE] at position 8: test []<--[HERE]");	
		}
		
		// /test [\"[\"\",{\"text\":\"Some text with bad quote escaping\"}\"]
		// Fails due to inner quotes not being escaped with a \ character
		if (version.greaterThanOrEqualTo(MCVersion.V1_20_3)) {
			assertCommandFailsWith(player, "test [\"[\"\",{\"text\":\"Some text with bad quote escaping\"}\"]", "Invalid chat component: Unterminated array at line 1 column 6 path $[1] at position 11: ...est [\"[\"\",<--[HERE]");
		} else {
			assertCommandFailsWith(player, "test [\"[\"\",{\"text\":\"Some text with bad quote escaping\"}\"]", "Invalid chat component: Unterminated array at line 1 column 6 path $[1] at position 5: test <--[HERE]");
		}

		assertNoMoreResults(results);
	}

	/********************
	 * Suggestion tests *
	 ********************/

	@Disabled
	@Test
	void suggestionTestWithSpigotChatComponentArgument() {
		new CommandAPICommand("test")
			.withArguments(new ChatComponentArgument("text"))
			.executesPlayer(P_EXEC)
			.register();

		PlayerMock player = server.addPlayer();

		// /test
		// The ChatComponentArgument doesn't have any suggestions
		assertEquals(List.of(), server.getSuggestions(player, "test "));

		// /test [
		// The ChatComponentArgument doesn't have any suggestions
		assertEquals(List.of(), server.getSuggestions(player, "test ["));
	}

	@Test
	void suggestionTestWithAdventureChatComponentArgument() {
		new CommandAPICommand("test")
			.withArguments(new ChatComponentArgument("text"))
			.executesPlayer(P_EXEC)
			.register();

		PlayerMock player = server.addPlayer();

		// /test
		// The ChatComponentArgument doesn't have any suggestions
		assertEquals(List.of(), server.getSuggestions(player, "test "));

		// /test [
		// The ChatComponentArgument doesn't have any suggestions
		assertEquals(List.of(), server.getSuggestions(player, "test ["));
	}

}
