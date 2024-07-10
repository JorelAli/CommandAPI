package dev.jorel.commandapi;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.context.StringRange;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.Suggestions;
import dev.jorel.commandapi.commandsenders.AbstractCommandSender;
import dev.jorel.commandapi.executors.ExecutionInfo;
import dev.jorel.commandapi.spying.ExecutionQueue;
import org.bukkit.command.CommandSender;
import org.opentest4j.AssertionFailedError;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class CommandAPITestUtilities {
	public static MockCommandAPIBukkit getCommandAPIPlatform() {
		return MockCommandAPIBukkit.getInstance();
	}

	// Running commands
	public static void dispatchCommand(CommandSender sender, String command) throws CommandSyntaxException {
		getCommandAPIPlatform().getBrigadierDispatcher().execute(command, new MockCommandSource(sender));
	}

	public static void assertCommandSucceeds(CommandSender sender, String command) {
		assertDoesNotThrow(
			() -> dispatchCommand(sender, command),
			"Expected command dispatch to succeed"
		);
	}

	@CanIgnoreReturnValue
	public static CommandSyntaxException assertCommandFails(CommandSender sender, String command, String expectedMessage) {
		CommandSyntaxException exception = assertThrows(
			CommandSyntaxException.class,
			() -> dispatchCommand(sender, command),
			"Expected command dispatch to fail"
		);

		String actualMessage = exception.getMessage();
		if (!Objects.equals(expectedMessage, actualMessage)) {
			throw new AssertionFailedError(
				"Expected command dispatch to fail with message <" + expectedMessage + ">, but got <" + actualMessage + ">"
			);
		}
		return exception;
	}

	// Verifying arguments
	public static ExecutionInfo<CommandSender, AbstractCommandSender<? extends CommandSender>>
	getExecutionInfo(Runnable executeCommand) {
		ExecutionQueue executions = getCommandAPIPlatform().getCommandAPIHandlerSpy().getExecutionQueue();
		executions.clear();

		executeCommand.run();

		ExecutionInfo<CommandSender, AbstractCommandSender<? extends CommandSender>> execution = executions.poll();
		assertNotNull(execution, "No command executor was not called");
		executions.assertNoMoreCommandsWereRun();

		return execution;
	}

	public static ExecutionInfo<CommandSender, AbstractCommandSender<? extends CommandSender>>
	getExecutionInfoOfSuccessfulCommand(CommandSender sender, String command) {
		return getExecutionInfo(() -> assertCommandSucceeds(sender, command));
	}

	public static void assertCommandSucceedsWithArguments(CommandSender sender, String command, Object... argumentsArray) {
		assertArrayEquals(argumentsArray, getExecutionInfoOfSuccessfulCommand(sender, command).args().args(),
			"Argument arrays are not equal"
		);
	}

	public static void assertCommandSucceedsWithArguments(CommandSender sender, String command, Map<String, Object> argumentsMap) {
		assertEquals(argumentsMap, getExecutionInfoOfSuccessfulCommand(sender, command).args().argsMap(),
			"Argument maps are not equal"
		);
	}

	public static ExecutionInfo<CommandSender, AbstractCommandSender<? extends CommandSender>>
	getExecutionInfoOfFailingCommand(CommandSender sender, String command, String expectedFailureMessage) {
		return getExecutionInfo(() -> assertCommandFails(sender, command, expectedFailureMessage));
	}

	public static void assertCommandFailsWithArguments(
		CommandSender sender, String command, String expectedFailureMessage,
		Object... argumentsArray
	) {
		assertArrayEquals(argumentsArray, getExecutionInfoOfFailingCommand(sender, command, expectedFailureMessage).args().args(),
			"Argument arrays are not equal"
		);
	}

	public static void assertCommandFailsWithArguments(
		CommandSender sender, String command, String expectedFailureMessage,
		Map<String, Object> argumentsMap
	) {
		assertEquals(argumentsMap, getExecutionInfoOfFailingCommand(sender, command, expectedFailureMessage).args().argsMap(),
			"Argument maps are not equal"
		);
	}

	// Verifying suggestions
	public static Suggestions getSuggestions(CommandSender sender, String command) {
		CommandDispatcher<MockCommandSource> dispatcher = getCommandAPIPlatform().getBrigadierDispatcher();
		ParseResults<MockCommandSource> parse = dispatcher.parse(command, new MockCommandSource(sender));
		return dispatcher.getCompletionSuggestions(parse).join();
	}

	public static void assertCommandSuggests(CommandSender sender, String command, String... expectedSuggestions) {
		assertCommandSuggests(sender, command, Arrays.asList(expectedSuggestions));
	}

	public static void assertCommandSuggests(CommandSender sender, String command, List<String> expectedSuggestions){
		List<Suggestion> actualSuggestions = getSuggestions(sender, command).getList();
		List<String> actualSuggestionStrings = new ArrayList<>(actualSuggestions.size());
		actualSuggestions.forEach(suggestion -> actualSuggestionStrings.add(suggestion.getText()));

		assertEquals(expectedSuggestions, actualSuggestionStrings, "Suggestions did not match");
	}

	public static Suggestion makeTooltip(String text, String tooltip) {
		return new Suggestion(StringRange.at(0), text, () -> tooltip);
	}

	public static void assertCommandSuggestsTooltips(CommandSender sender, String command, Suggestion... expectedSuggestions) {
		assertCommandSuggestsTooltips(sender, command, Arrays.asList(expectedSuggestions));
	}

	public static void assertCommandSuggestsTooltips(CommandSender sender, String command, List<Suggestion> expectedSuggestions) {
		List<Suggestion> actualSuggestions = getSuggestions(sender, command).getList();
		List<String> actualSuggestionStrings = new ArrayList<>(actualSuggestions.size());
		List<String> actualSuggestionTooltips = new ArrayList<>(actualSuggestions.size());
		actualSuggestions.forEach(suggestion -> {
			actualSuggestionStrings.add(suggestion.getText());
			actualSuggestionTooltips.add(suggestion.getTooltip().getString());
		});

		List<String> expectedSuggestionStrings = new ArrayList<>(expectedSuggestions.size());
		List<String> expectedSuggestionTooltips = new ArrayList<>(expectedSuggestions.size());
		expectedSuggestions.forEach(suggestion -> {
			expectedSuggestionStrings.add(suggestion.getText());
			expectedSuggestionTooltips.add(suggestion.getTooltip().getString());
		});

		assertEquals(expectedSuggestionStrings, actualSuggestionStrings, "Suggestions did not match");
		assertEquals(expectedSuggestionTooltips, actualSuggestionTooltips, "Tooltips did not match");
	}
}
