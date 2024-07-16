package dev.jorel.commandapi.arguments.parser;

import com.mojang.brigadier.exceptions.CommandSyntaxException;

//////////////////
// Return value //
//////////////////
public class Result<T> {
	protected final T value;
	protected final CommandSyntaxException exception;
	protected final int suggestionsStart;
	protected final SuggestionProvider suggestions;

	private Result(T value, CommandSyntaxException exception, int suggestionsStart, SuggestionProvider suggestions) {
		this.value = value;
		this.exception = exception;
		this.suggestionsStart = suggestionsStart;
		this.suggestions = suggestions;
	}

	public static <T> Result<T> withValue(T value) {
		return new Result<>(value, null, 0, null);
	}

	public static <T> Result<T> withException(CommandSyntaxException exception) {
		return new Result<>(null, exception, 0, null);
	}

	public static <T> Result<T> withValueAndSuggestions(T value, int suggestionsStart, SuggestionProvider suggestions) {
		return new Result<>(value, null, suggestionsStart, suggestions);
	}

	public static <T> Result<T> withExceptionAndSuggestions(CommandSyntaxException exception, int suggestionsStart, SuggestionProvider suggestions) {
		return new Result<>(null, exception, suggestionsStart, suggestions);
	}

	public Result<T> withSuggestions(int suggestionsStart, SuggestionProvider suggestions) {
		return new Result<>(this.value, this.exception, suggestionsStart, suggestions);
	}
}
