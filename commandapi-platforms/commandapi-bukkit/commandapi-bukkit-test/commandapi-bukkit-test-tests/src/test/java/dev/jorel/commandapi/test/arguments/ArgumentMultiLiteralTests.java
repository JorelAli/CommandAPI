package dev.jorel.commandapi.test.arguments;

import be.seeseemelk.mockbukkit.entity.PlayerMock;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.ItemStackArgument;
import dev.jorel.commandapi.arguments.MultiLiteralArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import dev.jorel.commandapi.test.Mut;
import dev.jorel.commandapi.test.TestBase;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the {@link MultiLiteralArgument}
 */
public class ArgumentMultiLiteralTests extends TestBase {

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
	public void executionTestWithMultiLiteralArgument() {
		Mut<String> results = Mut.of();

		new CommandAPICommand("test")
			.withArguments(new MultiLiteralArgument("literals", new String[]{"literal", "literal1", "literal2"}))
			.executesPlayer((player, args) -> {
				results.set((String) args.get(0));
			})
			.register();

		PlayerMock player = server.addPlayer();

		// /test literal
		server.dispatchCommand(player, "test literal");
		assertEquals("literal", results.get());

		// /test literal1
		server.dispatchCommand(player, "test literal1");
		assertEquals("literal1", results.get());

		// /test literal3
		assertCommandFailsWith(player, "test literal3", "Incorrect argument for command at position 5: test <--[HERE]");

		assertNoMoreResults(results);
	}

	@Test
	public void executionTestWithMultiLiteralArgumentNodeName() {
		Mut<String> results = Mut.of();

		new CommandAPICommand("test")
			.withArguments(new MultiLiteralArgument("literals", new String[]{"literal", "literal1", "literal2"}))
			.executesPlayer((player, args) -> {
				results.set((String) args.get("literals"));
			})
			.register();

		PlayerMock player = server.addPlayer();

		// /test literal
		server.dispatchCommand(player, "test literal");
		assertEquals("literal", results.get());

		// /test literal1
		server.dispatchCommand(player, "test literal1");
		assertEquals("literal1", results.get());

		assertNoMoreResults(results);
	}

	@Test
	public void executionTestWithSubcommands() {
		// Doing these because subcommands are converted into MultiLiteralArguments
		Mut<Object> results = Mut.of();

		new CommandAPICommand("test")
			.withSubcommand(new CommandAPICommand("hello")
				.withArguments(new ItemStackArgument("hello"))
				.executesPlayer(info -> {
					results.set(info.args().get("hello"));
				})
			)
			.withSubcommand(new CommandAPICommand("bye")
				.withArguments(new IntegerArgument("bye"))
				.executesPlayer(info -> {
					results.set(info.args().get("bye"));
				})
			)
			.register();

		PlayerMock player = server.addPlayer();

		// /test hello minecraft:stick
		ItemStack item = new ItemStack(Material.STICK);
		server.dispatchCommand(player, "test hello minecraft:stick");
		assertEquals(item, results.get());

		// /test bye 5
		server.dispatchCommand(player, "test bye 5");
		assertEquals(5, results.get());
	}

	/********************
	 * Suggestion tests *
	 ********************/

	@Test
	public void suggestionTestWithMultiLiteralArgument() {
		new CommandAPICommand("test")
			.withArguments(new MultiLiteralArgument("literals", new String[]{"literal", "literal1", "literal2"}))
			.executesPlayer(P_EXEC)
			.register();

		PlayerMock player = server.addPlayer();

		assertEquals(List.of("literal", "literal1", "literal2"), server.getSuggestions(player, "test "));
	}

}
