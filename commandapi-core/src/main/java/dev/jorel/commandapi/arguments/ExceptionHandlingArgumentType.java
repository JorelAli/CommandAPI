package dev.jorel.commandapi.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.jorel.commandapi.CommandAPI;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class ExceptionHandlingArgumentType<T> implements ArgumentType<T> {

	private final ArgumentType<T> baseType;

	public ExceptionHandlingArgumentType(ArgumentType<T> baseType) {
		this.baseType = baseType;
	}

	@Override
	public T parse(StringReader stringReader) throws CommandSyntaxException {
		try {
			return baseType.parse(stringReader);
		} catch (CommandSyntaxException original) {
			// TODO: It would be cool if this part could return a substitute value if the parse failed
			//  Unfortunately, exceptionHandler cannot do this since T dose not necessarily equal RawClass
			//  To do this, I think an additional exceptionHandler for RawClass would need to be added
			//  This means Argument would need to be parameterized over RawClass, giving it 2 type
			//  parameters and ruining backwards compatibility :(
			CommandAPI.logNormal("Intercepted exception with message: " + original.getMessage());
			throw CommandAPI.failWithString("Haha! Custom Error has intercepted " + original.getMessage()).getException();
		}
	}

	@Override
	public Collection<String> getExamples() {
		return baseType.getExamples();
	}

	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
		return baseType.listSuggestions(context, builder);
	}

	public ArgumentType<T> getBaseType() {
		return baseType;
	}
}
