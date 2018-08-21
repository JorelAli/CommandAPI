package io.github.jorelali.commandapi.api.arguments;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import net.minecraft.server.v1_13_R1.ArgumentParserItemStack;
import net.minecraft.server.v1_13_R1.ArgumentPredicateItemStack;

public class ArgumentItemStack implements ArgumentType<ArgumentPredicateItemStack> {
	private static final Collection<String> a = Arrays
			.asList(new String[]{"stick", "minecraft:stick", "stick{foo=bar}"});

	public static ArgumentItemStack a() {
		return new ArgumentItemStack();
	}

	public ArgumentPredicateItemStack parse(StringReader arg0) throws CommandSyntaxException {
		ArgumentParserItemStack arg1 = (new ArgumentParserItemStack(arg0, false)).h();
		return new ArgumentPredicateItemStack(arg1.b(), arg1.c());
	}

	public static <S> ArgumentPredicateItemStack a(CommandContext<S> arg, String arg0) {
		return (ArgumentPredicateItemStack) arg.getArgument(arg0, ArgumentPredicateItemStack.class);
	}

	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> arg0, SuggestionsBuilder arg1) {
		StringReader arg2 = new StringReader(arg1.getInput());
		arg2.setCursor(arg1.getStart());
		ArgumentParserItemStack arg3 = new ArgumentParserItemStack(arg2, false);

		try {
			arg3.h();
		} catch (CommandSyntaxException arg5) {
			;
		}

		return arg3.a(arg1);
	}

	public Collection<String> getExamples() {
		return a;
	}

}