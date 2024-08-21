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
package dev.jorel.commandapi.annotations.reloaded.modules.commands;

import dev.jorel.commandapi.annotations.reloaded.annotations.Command;
import dev.jorel.commandapi.annotations.reloaded.semantics.SemanticRule;
import dev.jorel.commandapi.annotations.reloaded.semantics.SemanticRuleContext;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.NestingKind;
import javax.lang.model.element.TypeElement;

/**
 * A semantic rule that checks to make sure that all command annotations are on top-level classes
 */
public class RuleCommandCanOnlyGoOnTopLevelClasses implements SemanticRule {
	@Override
	public boolean passes(SemanticRuleContext context) {
        boolean passing = true;
		for (var element : context.roundEnv().getElementsAnnotatedWith(Command.class)) {
			if (element instanceof TypeElement typeElement) {
				if (typeElement.getKind() == ElementKind.CLASS &&
					typeElement.getNestingKind() == NestingKind.TOP_LEVEL) {
					continue;
				}
			}
			context.logging().complain(element, "@%s can only go on a top level class"
				.formatted(Command.class.getSimpleName()));
			passing = false;
		}
		return passing;
	}
}
