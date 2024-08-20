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
import dev.jorel.commandapi.annotations.reloaded.generators.GeneratorContext;

import java.util.Queue;

/**
 * Context needed to generate the suggestions option of a CommandAPI argument
 *
 * @param suggestionsType Whether a suggestion should be set, and whether it is safe or normal
 * @param typeStack A set of types for generating the constructor invocation for the suggestions provider
 */
public record ArgumentSuggestionsGeneratorContext(
	SuggestionsType suggestionsType,
	Queue<String> typeStack
) implements GeneratorContext {

	public boolean isSafe() {
		return suggestionsType == SuggestionsType.SAFE;
	}
}
