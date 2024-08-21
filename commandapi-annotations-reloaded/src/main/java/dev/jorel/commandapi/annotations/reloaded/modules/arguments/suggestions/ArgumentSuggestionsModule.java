/*******************************************************************************
 * Copyright 2018, 2020 Jorel Ali (Skepter) - MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *******************************************************************************/
package dev.jorel.commandapi.annotations.reloaded.modules.arguments.suggestions;

import dev.jorel.commandapi.annotations.reloaded.arguments.utils.SuggestionsType;
import dev.jorel.commandapi.annotations.reloaded.generators.IndentedWriter;
import dev.jorel.commandapi.annotations.reloaded.semantics.SemanticAnalyzer;
import dev.jorel.commandapi.annotations.reloaded.semantics.SemanticRule;
import dev.jorel.commandapi.annotations.reloaded.modules.TypeElementAnalyzerParserGeneratorModule;
import dev.jorel.commandapi.annotations.reloaded.parser.TypeElementParserContext;

import java.util.List;
import java.util.Optional;

/**
 * Contains everything to do with generating the suggestions option for a CommandAPI argument
 */
public class ArgumentSuggestionsModule implements TypeElementAnalyzerParserGeneratorModule<
	ArgumentSuggestionsGeneratorContext
	> {

	private final SafeSuggestionsAppliedToSafeOverrideableArgument safeSuggestionsAppliedToSafeOverrideableArgument;
	private final SafeSuggestionsPrimitiveMatchesArgument safeSuggestionsPrimitiveMatchesArgument;

	public ArgumentSuggestionsModule(SafeSuggestionsAppliedToSafeOverrideableArgument safeSuggestionsAppliedToSafeOverrideableArgument, SafeSuggestionsPrimitiveMatchesArgument safeSuggestionsPrimitiveMatchesArgument) {
		this.safeSuggestionsAppliedToSafeOverrideableArgument = safeSuggestionsAppliedToSafeOverrideableArgument;
		this.safeSuggestionsPrimitiveMatchesArgument = safeSuggestionsPrimitiveMatchesArgument;
	}

	@Override
	public List<SemanticAnalyzer> subAnalyzers() {
		return List.of();
	}

	@Override
	public List<SemanticRule> rules() {
		return List.of(
			safeSuggestionsAppliedToSafeOverrideableArgument,
			safeSuggestionsPrimitiveMatchesArgument
		);
	}
	@Override
	public void generate(IndentedWriter out, ArgumentSuggestionsGeneratorContext context) {
		if (context.suggestionsType() == SuggestionsType.NONE) {
			return;
		}
		out.printOnNewLine(".%s(%s.get())".formatted(
			context.isSafe() ? "replaceSafeSuggestions" : "replaceSuggestions",
			null //TODO: constructorRenderer.render(context.typeStack())
		));
	}

	@Override
	public Optional<ArgumentSuggestionsGeneratorContext> parse(TypeElementParserContext context) {
		throw new UnsupportedOperationException("%s: Parsing %s is not yet implemented".formatted(context.element(), getClass().getSimpleName()));
	}
}
