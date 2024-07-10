package dev.jorel.commandapi;

import be.seeseemelk.mockbukkit.entity.PlayerMock;
import com.mojang.brigadier.suggestion.Suggestion;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.StringArgument;
import org.bukkit.command.CommandSender;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static dev.jorel.commandapi.StringTooltip.ofString;

class AssertSuggestionUtilitiesTest extends CommandTestBase {
	// Setup
	private PlayerMock player;

	@BeforeEach
	public void setUp() {
		super.setUp();

		new CommandAPICommand("test")
			.withArguments(new StringArgument("string").replaceSuggestions(ArgumentSuggestions.stringsWithTooltips(
				ofString("A", "First letter"),
				ofString("B", "Second letter"),
				ofString("C", "Third letter")
			)))
			.executes(DEFAULT_EXECUTOR)
			.register();

		player = server.addPlayer();
	}

	@AfterEach
	public void tearDown() {
		super.tearDown();
	}

	private void useList(boolean useList, CommandSender sender, String command, String... suggestions) {
		if (useList) {
			assertCommandSuggests(sender, command, List.of(suggestions));
		} else {
			assertCommandSuggests(sender, command, suggestions);
		}
	}

	private void useListTooltips(boolean useList, CommandSender sender, String command, Suggestion... suggestions) {
		if (useList) {
			assertCommandSuggestsTooltips(sender, command, List.of(suggestions));
		} else {
			assertCommandSuggestsTooltips(sender, command, suggestions);
		}
	}

	// Tests
	@ParameterizedTest
	@ValueSource(booleans = {false, true})
	void testSuccessfulAssertCommandSuggests(boolean useList) {
		useList(useList, player, "test ", "A", "B", "C");
	}

	@ParameterizedTest
	@ValueSource(booleans = {false, true})
	void testSuccessfulAssertCommandSuggestsTooltips(boolean useList) {
		useListTooltips(
			useList, player, "test ",
			makeTooltip("A", "First letter"),
			makeTooltip("B", "Second letter"),
			makeTooltip("C", "Third letter")
		);
	}

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	void testUnsuccessfulAssertCommandSuggests(boolean useList) {
		// Invalid command
		assertAssertionFails(
			() -> useList(useList, player, "invalid ", "A", "B", "C"),
			"Suggestions did not match ==> expected: <[A, B, C]> but was: <[]>"
		);

		// Wrong number of suggestions
		assertAssertionFails(
			() -> useList(useList, player, "test ", "A", "B"),
			"Suggestions did not match ==> expected: <[A, B]> but was: <[A, B, C]>"
		);
		assertAssertionFails(
			() -> useList(useList, player, "test ", "A", "B", "C", "D"),
			"Suggestions did not match ==> expected: <[A, B, C, D]> but was: <[A, B, C]>"
		);

		// Wrong suggestion order
		assertAssertionFails(
			() -> useList(useList, player, "test ", "B", "C", "A"),
			"Suggestions did not match ==> expected: <[B, C, A]> but was: <[A, B, C]>"
		);

		// Wrong text
		assertAssertionFails(
			() -> useList(useList, player, "test ", "a", "B", "C"),
			"Suggestions did not match ==> expected: <[a, B, C]> but was: <[A, B, C]>"
		);
	}

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	void testUnsuccessfulAssertCommandSuggestsTooltips(boolean useList) {
		// Invalid command
		assertAssertionFails(
			() -> useListTooltips(
				useList, player, "invalid ",
				makeTooltip("A", "First letter"),
				makeTooltip("B", "Second letter"),
				makeTooltip("C", "Third letter")
			),
			"Suggestions did not match ==> expected: <[A, B, C]> but was: <[]>"
		);

		// Wrong number of suggestions
		assertAssertionFails(
			() -> useListTooltips(
				useList, player, "test ",
				makeTooltip("A", "First letter"),
				makeTooltip("B", "Second letter")
			),
			"Suggestions did not match ==> expected: <[A, B]> but was: <[A, B, C]>"
		);
		assertAssertionFails(
			() -> useListTooltips(
				useList, player, "test ",
				makeTooltip("A", "First letter"),
				makeTooltip("B", "Second letter"),
				makeTooltip("C", "Third letter"),
				makeTooltip("D", "Fourth letter")
			),
			"Suggestions did not match ==> expected: <[A, B, C, D]> but was: <[A, B, C]>"
		);

		// Wrong suggestion order
		assertAssertionFails(
			() -> useListTooltips(
				useList, player, "test ",
				makeTooltip("B", "Second letter"),
				makeTooltip("C", "Third letter"),
				makeTooltip("A", "First letter")
			),
			"Suggestions did not match ==> expected: <[B, C, A]> but was: <[A, B, C]>"
		);

		// Wrong text
		assertAssertionFails(
			() -> useListTooltips(
				useList, player, "test ",
				makeTooltip("a", "First letter"),
				makeTooltip("B", "Second letter"),
				makeTooltip("C", "Third letter")
			),
			"Suggestions did not match ==> expected: <[a, B, C]> but was: <[A, B, C]>"
		);

		// Wrong tooltip
		assertAssertionFails(
			() -> useListTooltips(
				useList, player, "test ",
				makeTooltip("A", "First"),
				makeTooltip("B", "Second letter"),
				makeTooltip("C", "Third letter")
			),
			"Tooltips did not match " +
				"==> expected: <[First, Second letter, Third letter]> but was: <[First letter, Second letter, Third letter]>"
		);
	}
}
