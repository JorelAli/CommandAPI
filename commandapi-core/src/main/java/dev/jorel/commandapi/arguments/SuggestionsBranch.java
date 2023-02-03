package dev.jorel.commandapi.arguments;

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.jorel.commandapi.SuggestionInfo;
import dev.jorel.commandapi.executors.CommandArguments;

import java.util.*;

/**
 * This class represents a branch in the suggestions of an argument. Use {@link SuggestionsBranch#suggest(ArgumentSuggestions...)}
 * to add suggestions, then {@link SuggestionsBranch#branch(SuggestionsBranch...)} to add more branches.
 */
public class SuggestionsBranch<CommandSender> {
	private final List<ArgumentSuggestions<CommandSender>> suggestions;
	private final List<SuggestionsBranch<CommandSender>> branches = new ArrayList<>();

	private SuggestionsBranch(List<ArgumentSuggestions<CommandSender>> suggestions) {
		this.suggestions = suggestions;
	}

	/**
	 * Creates a {@link SuggestionsBranch} starting with the given suggestions. If a suggestions is null, the suggestions
	 * at that position will not be overridden.
	 *
	 * @param suggestions An array of {@link ArgumentSuggestions} representing the suggestions. Use the static methods in
	 *                    ArgumentSuggestions to create these.
	 * @return a new {@link SuggestionsBranch} starting with the given suggestions
	 */
	@SafeVarargs
	public static <CommandSender> SuggestionsBranch<CommandSender> suggest(ArgumentSuggestions<CommandSender>... suggestions) {
		// Arrays#asList allows null elements
		return new SuggestionsBranch<>(Arrays.asList(suggestions));
	}

	/**
	 * Adds further branches to this {@link SuggestionsBranch}. After going through the suggestions provided by
	 * {@link SuggestionsBranch#suggest(ArgumentSuggestions...)} the suggestions of these branches will be used.
	 *
	 * @param branches An array of {@link SuggestionsBranch} representing the branching suggestions. Use
	 *                 {@link SuggestionsBranch#suggest(ArgumentSuggestions...)} to start creating these.
	 * @return the current {@link SuggestionsBranch}
	 */
	@SafeVarargs
	public final SuggestionsBranch<CommandSender> branch(SuggestionsBranch<CommandSender>... branches) {
		// List#of does not allow null elements
		this.branches.addAll(List.of(branches));
		return this;
	}

	/**
	 * Gets the next {@link ArgumentSuggestions} based on the previous arguments.
	 *
	 * @param sender            The {@link CommandSender} the suggestions are being built for
	 * @param previousArguments An array of previously given arguments that is used to find the next {@link ArgumentSuggestions}
	 * @return The next {@link ArgumentSuggestions} given by this {@link SuggestionsBranch} or null if the suggestions should not be overridden
	 * @throws CommandSyntaxException if the given previous arguments don't lead to a valid path for this {@link SuggestionsBranch}
	 */
	public ArgumentSuggestions<CommandSender> getNextSuggestion(CommandSender sender, String... previousArguments) throws CommandSyntaxException {
		return getNextSuggestion(sender, previousArguments, new StringReader(String.join(" ", previousArguments)), new ArrayList<>(), new StringBuilder());
	}

	@SuppressWarnings("unchecked")
	private ArgumentSuggestions<CommandSender> getNextSuggestion(CommandSender sender, String[] previousArguments, StringReader errorContext, List<String> processedArguments, StringBuilder currentInput) throws CommandSyntaxException {
		if (branches.isEmpty() && suggestions.isEmpty()) {
			return null;
		}
		for (ArgumentSuggestions<CommandSender> currentSuggestion : suggestions) {
			// If all the arguments were processed, this suggestion is next
			if (processedArguments.size() == previousArguments.length) {
				return currentSuggestion;
			}
			String currentArgument = previousArguments[processedArguments.size()];
			errorContext.setCursor(currentInput.length());

			if (currentSuggestion != null) {
				// Validate argument on the path
				SuggestionInfo<CommandSender> info = new SuggestionInfo<>(sender, new CommandArguments(processedArguments.toArray(), new HashMap<>()), currentInput.toString(), "");
				SuggestionsBuilder builder = new SuggestionsBuilder(currentInput.toString(), currentInput.length());
				currentSuggestion.suggest(info, builder);
				if (builder.build().getList().stream().map(Suggestion::getText).noneMatch(currentArgument::equals)) {
					if (processedArguments.isEmpty()) {
						throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownCommand().createWithContext(errorContext);
					} else {
						throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownArgument().createWithContext(errorContext);
					}
				}
			}
			currentInput.append(currentArgument).append(" ");
			processedArguments.add(currentArgument);
		}

		List<ArgumentSuggestions<CommandSender>> mergedBranches = new ArrayList<>();
		for (SuggestionsBranch<CommandSender> branch : branches) {
			try {
				mergedBranches.add(branch.getNextSuggestion(
					sender, previousArguments, errorContext, new ArrayList<>(processedArguments), new StringBuilder(currentInput)
				));
			} catch (CommandSyntaxException ignored) {
				assert true;
			}
		}

		if (mergedBranches.isEmpty()) {
			// If all branches had errors there were no valid paths
			if (processedArguments.isEmpty()) {
				throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownCommand().createWithContext(errorContext);
			} else {
				throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownArgument().createWithContext(errorContext);
			}
		}

		if (mergedBranches.size() == 1) {
			return mergedBranches.get(0);
		} else {
			if (mergedBranches.contains(null)) {
				if (mergedBranches.stream().allMatch(Objects::isNull)) {
					return null;
				} else {
					throw new SimpleCommandExceptionType(new LiteralMessage("Multiple paths for a SuggestionsBranch must either be all null or all non-null")).createWithContext(errorContext);
				}
			} else {
				return ArgumentSuggestions.merge(mergedBranches.toArray(ArgumentSuggestions[]::new));
			}
		}
	}

	/**
	 * Makes sure the given arguments correspond to the suggestions of this {@link SuggestionsBranch}
	 *
	 * @param sender    The {@link CommandSender} the suggestions are being built for
	 * @param arguments An array of arguments to check against the suggestions of this {@link SuggestionsBranch}
	 * @throws CommandSyntaxException if the given arguments don't lead to a valid path for this {@link SuggestionsBranch}
	 */
	public void enforceReplacements(CommandSender sender, String... arguments) throws CommandSyntaxException {
		EnforceReplacementsResult result = enforceReplacements(sender, arguments, new StringReader(String.join(" ", arguments)), new ArrayList<>(), new StringBuilder());
		if (result.type != ExceptionType.NO_ERROR) {
			throw result.exception;
		}
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
			// Priority is determined by the order of the elements in the enum
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

	private EnforceReplacementsResult enforceReplacements(CommandSender sender, String[] arguments, StringReader errorContext, List<String> processedArguments, StringBuilder currentInput) {
		if (branches.isEmpty() && suggestions.isEmpty()) {
			return new EnforceReplacementsResult(ExceptionType.NO_ERROR, null);
		}

		for (ArgumentSuggestions<CommandSender> currentSuggestion : suggestions) {
			String currentArgument;
			if (processedArguments.size() >= arguments.length) {
				currentArgument = "";
			} else {
				currentArgument = arguments[processedArguments.size()];
			}
			errorContext.setCursor(currentInput.length());

			if (currentSuggestion != null) {
				// Validate argument on the path
				SuggestionInfo<CommandSender> info = new SuggestionInfo<>(sender, new CommandArguments(processedArguments.toArray(), new HashMap<>()), currentInput.toString(), "");
				SuggestionsBuilder builder = new SuggestionsBuilder(currentInput.toString(), currentInput.length());
				try {
					currentSuggestion.suggest(info, builder);
				} catch (CommandSyntaxException exception) {
					return new EnforceReplacementsResult(ExceptionType.UNKNOWN, exception);
				}
				List<String> results = builder.build().getList().stream().map(Suggestion::getText).toList();
				if (currentArgument.isEmpty()) {
					if (results.isEmpty()) {
						// Arguments ended at same time as suggestions
						return EnforceReplacementsResult.withContext(ExceptionType.NO_ERROR, errorContext);
					} else {
						return EnforceReplacementsResult.withContext(ExceptionType.NOT_ENOUGH_ARGUMENTS, errorContext);
					}
				} else if (!results.contains(currentArgument)) {
					if (processedArguments.isEmpty()) {
						return EnforceReplacementsResult.withContext(ExceptionType.UNKNOWN_COMMAND, errorContext);
					} else {
						return EnforceReplacementsResult.withContext(ExceptionType.UNKNOWN_ARGUMENT, errorContext);
					}
				}
			}
			currentInput.append(currentArgument).append(" ");
			processedArguments.add(currentArgument);
		}

		if (branches.isEmpty()) {
			return new EnforceReplacementsResult(ExceptionType.NO_ERROR, null);
		}

		// Check the branches to see if the arguments fit and try to choose an appropriate response
		EnforceReplacementsResult finalResult = EnforceReplacementsResult.withContext(ExceptionType.UNKNOWN, errorContext);
		for (SuggestionsBranch<CommandSender> branch : branches) {
			EnforceReplacementsResult result = branch.enforceReplacements(sender, arguments, errorContext, new ArrayList<>(processedArguments), new StringBuilder(currentInput));
			if (result.isHigherPriority(finalResult)) {
				finalResult = result;
			}
		}

		return finalResult;
	}
}
