package dev.jorel.commandapi.arguments;

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.jorel.commandapi.SuggestionInfo;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class SuggestionsBranch {
	private final List<ArgumentSuggestions> suggestions;
	private final List<SuggestionsBranch> branches = new ArrayList<>();

	private SuggestionsBranch(List<ArgumentSuggestions> suggestions) {
		this.suggestions = suggestions;
	}

	public static SuggestionsBranch suggest(ArgumentSuggestions... suggestions) {
		// Arrays#asList allows null elements
		return new SuggestionsBranch(Arrays.asList(suggestions));
	}

	public SuggestionsBranch branch(SuggestionsBranch... branches) {
		// List#of does not allow null elements
		this.branches.addAll(List.of(branches));
		return this;
	}

	public ArgumentSuggestions getNextSuggestion(CommandSender sender, StringReader errorContext, String... previousArguments) throws CommandSyntaxException {
		return getNextSuggestion(sender, errorContext, previousArguments, new ArrayList<>(), new StringBuilder());
	}

	private ArgumentSuggestions getNextSuggestion(CommandSender sender, StringReader errorContext, String[] previousArguments, List<String> processedArguments, StringBuilder currentInput) throws CommandSyntaxException {
		if (branches.size() == 0 && suggestions.size() == 0) return null;
		for (ArgumentSuggestions currentSuggestion : suggestions) {
			// If all the arguments were processed, this suggestion is next
			if (processedArguments.size() == previousArguments.length) return currentSuggestion;
			String currentArgument = previousArguments[processedArguments.size()];
			errorContext.setCursor(currentInput.length());

			if (currentSuggestion != null) {
				// Validate argument on the path
				SuggestionInfo info = new SuggestionInfo(sender, processedArguments.toArray(), currentInput.toString(), "");
				SuggestionsBuilder builder = new SuggestionsBuilder(currentInput.toString(), currentInput.length());
				currentSuggestion.suggest(info, builder);
				if (builder.build().getList().stream().map(Suggestion::getText).noneMatch(currentArgument::equals)) {
					if (processedArguments.size() == 0)
						throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownCommand().createWithContext(errorContext);
					else
						throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownArgument().createWithContext(errorContext);
				}
			}
			currentInput.append(currentArgument).append(" ");
			processedArguments.add(currentArgument);
		}

		List<ArgumentSuggestions> mergedBranches = new ArrayList<>();
		for (SuggestionsBranch branch : branches) {
			try {
				mergedBranches.add(branch.getNextSuggestion(
					sender, errorContext, previousArguments, new ArrayList<>(processedArguments), new StringBuilder(currentInput)
				));
			} catch (CommandSyntaxException ignored) {
			}
		}

		if (mergedBranches.size() == 0) {
			// If all branches had errors there were no valid paths
			if (processedArguments.size() == 0)
				throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownCommand().createWithContext(errorContext);
			else
				throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownArgument().createWithContext(errorContext);
		}

		if (mergedBranches.size() == 1) {
			return mergedBranches.get(0);
		} else {
			if (mergedBranches.contains(null)) {
				if (mergedBranches.stream().allMatch(Objects::isNull)) return null;
				else
					throw new SimpleCommandExceptionType(new LiteralMessage("Multiple paths for a SuggestionsBranch must either be all null or all non-null")).createWithContext(errorContext);
			} else {
				return ArgumentSuggestions.merge(mergedBranches.toArray(ArgumentSuggestions[]::new));
			}
		}
	}

	public void enforceReplacements(CommandSender sender, StringReader errorContext, String... arguments) throws CommandSyntaxException {
		EnforceReplacementsResult result = enforceReplacements(sender, errorContext, arguments, new ArrayList<>(), new StringBuilder());
		if(result.type != ExceptionType.NO_ERROR) throw result.exception;
	}

	private record EnforceReplacementsResult(ExceptionType type, CommandSyntaxException exception) {
		public static EnforceReplacementsResult withContext(ExceptionType type, StringReader errorContext) {
			return new EnforceReplacementsResult(type, switch (type) {
				case NO_ERROR -> null;
				case NOT_ENOUGH_ARGUMENTS -> new SimpleCommandExceptionType(new LiteralMessage("Expected more arguments")).createWithContext(errorContext);
				case UNKNOWN_ARGUMENT -> CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownArgument().createWithContext(errorContext);
				case UNKNOWN_COMMAND -> CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownCommand().createWithContext(errorContext);
				case UNKNOWN -> new SimpleCommandExceptionType(new LiteralMessage("Unknown syntax exception while parsing argument")).createWithContext(errorContext);
			});
		}

		public boolean isHigherPriority(EnforceReplacementsResult other) {
			// priority determined by the order of the elements in the enum
			return this.type.ordinal() < other.type.ordinal();
		}
	}

	private enum ExceptionType {
		NO_ERROR,
		NOT_ENOUGH_ARGUMENTS,
		UNKNOWN_ARGUMENT,
		UNKNOWN_COMMAND,
		UNKNOWN
	}

	private EnforceReplacementsResult enforceReplacements(CommandSender sender, StringReader errorContext, String[] arguments, List<String> processedArguments, StringBuilder currentInput) {
		if (branches.size() == 0 && suggestions.size() == 0)
			return new EnforceReplacementsResult(ExceptionType.NO_ERROR, null);

		for (ArgumentSuggestions currentSuggestion : suggestions) {
			String currentArgument;
			if (processedArguments.size() >= arguments.length)
				currentArgument = "";
			else
				currentArgument = arguments[processedArguments.size()];
			errorContext.setCursor(currentInput.length());

			if (currentSuggestion != null) {
				// Validate argument on the path
				SuggestionInfo info = new SuggestionInfo(sender, processedArguments.toArray(), currentInput.toString(), "");
				SuggestionsBuilder builder = new SuggestionsBuilder(currentInput.toString(), currentInput.length());
				try {
					currentSuggestion.suggest(info, builder);
				} catch (CommandSyntaxException exception) {
					return new EnforceReplacementsResult(ExceptionType.UNKNOWN, exception);
				}
				List<String> results = builder.build().getList().stream().map(Suggestion::getText).toList();
				if (currentArgument.isEmpty()) {
					if (results.size() == 0)
						// arguments ended at same time as suggestions
						return EnforceReplacementsResult.withContext(ExceptionType.NO_ERROR, errorContext);
					else
						return EnforceReplacementsResult.withContext(ExceptionType.NOT_ENOUGH_ARGUMENTS, errorContext);
				} else if (!results.contains(currentArgument)) {
					if (processedArguments.size() == 0)
						return EnforceReplacementsResult.withContext(ExceptionType.UNKNOWN_COMMAND, errorContext);
					else
						return EnforceReplacementsResult.withContext(ExceptionType.UNKNOWN_ARGUMENT, errorContext);
				}
			}
			currentInput.append(currentArgument).append(" ");
			processedArguments.add(currentArgument);
		}

		if(branches.size() == 0) return new EnforceReplacementsResult(ExceptionType.NO_ERROR, null);

		// Check the branches to see if the arguments fit and try to choose an appropriate response
		EnforceReplacementsResult finalResult = EnforceReplacementsResult.withContext(ExceptionType.UNKNOWN, errorContext);
		for (SuggestionsBranch branch : branches) {
			EnforceReplacementsResult result = branch.enforceReplacements(sender, errorContext, arguments, new ArrayList<>(processedArguments), new StringBuilder(currentInput));
			if (result.isHigherPriority(finalResult)) {
				finalResult = result;
			}
		}

		return finalResult;
	}
}
