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
package dev.jorel.commandapi.annotations.reloaded.modules.arguments;

import dev.jorel.commandapi.annotations.reloaded.arguments.utils.ArgumentAnnotation;
import dev.jorel.commandapi.annotations.reloaded.generators.GeneratorContext;
import dev.jorel.commandapi.annotations.reloaded.modules.arguments.listed.ArgumentListedOptionGeneratorContext;
import dev.jorel.commandapi.annotations.reloaded.modules.arguments.permissions.ArgumentPermissionGeneratorContext;
import dev.jorel.commandapi.annotations.reloaded.modules.arguments.requirements.ArgumentRequirementsGeneratorContext;
import dev.jorel.commandapi.annotations.reloaded.modules.arguments.suggestions.ArgumentSuggestionsGeneratorContext;

import javax.lang.model.element.Element;

/**
 * Context needed to generate a CommandAPI argument
 *
 * @param element                              The argument field or parameter
 * @param argumentAnnotation                   The CommandAPI argument annotation properties assigned to this argument
 * @param argumentPermissionGeneratorContext   Context needed to generate the permissions option
 * @param argumentRequirementsGeneratorContext Context needed to generate the requirements option
 * @param argumentSuggestionsGeneratorContext  Context needed to generate the suggestions option
 * @param argumentListedOptionGeneratorContext Context needed to generate the listed option
 */
public record ArgumentGeneratorContext(
	Element element,
	ArgumentAnnotation argumentAnnotation,
	ArgumentPermissionGeneratorContext argumentPermissionGeneratorContext,
	ArgumentRequirementsGeneratorContext argumentRequirementsGeneratorContext,
	ArgumentSuggestionsGeneratorContext argumentSuggestionsGeneratorContext,
	ArgumentListedOptionGeneratorContext argumentListedOptionGeneratorContext
) implements GeneratorContext {
}
