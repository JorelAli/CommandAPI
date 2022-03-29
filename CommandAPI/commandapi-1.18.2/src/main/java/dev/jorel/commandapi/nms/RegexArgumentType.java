package dev.jorel.commandapi.nms;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Pattern;

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

public class RegexArgumentType implements ArgumentType<String> {

	public static final SimpleCommandExceptionType DOES_NOT_MATCH_REGEX_EXCEPTION = new SimpleCommandExceptionType(new LiteralMessage("This doesn't match the regex!"));
	
	public Pattern pattern;
	
	public RegexArgumentType(String pattern) {
		this.pattern = Pattern.compile(pattern);
	}
	
	public static String getString(final CommandContext<?> context, final String name) {
		return context.getArgument(name, String.class);
	}

	@Override
	public String parse(StringReader reader) throws CommandSyntaxException {
		String input = "";
		while(reader.canRead() && !pattern.matcher(input).matches()) {
			input = input + reader.read();
		}
		if(!pattern.matcher(input).matches()) {
			throw DOES_NOT_MATCH_REGEX_EXCEPTION.createWithContext(reader);
		}
		return input;
	}

	@Override
	public Collection<String> getExamples() {
		// TODO Auto-generated method stub
		return ArgumentType.super.getExamples();
	}

	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
		// TODO Auto-generated method stub
		return ArgumentType.super.listSuggestions(context, builder);
	}

	@Override
	public String toString() {
		return "regex(" + pattern.pattern() + ")";
	}
	
	
}