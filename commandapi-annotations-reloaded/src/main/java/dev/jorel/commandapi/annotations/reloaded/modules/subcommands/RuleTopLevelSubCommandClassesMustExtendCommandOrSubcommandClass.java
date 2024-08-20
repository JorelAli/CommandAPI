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
package dev.jorel.commandapi.annotations.reloaded.modules.subcommands;

import dev.jorel.commandapi.annotations.reloaded.annotations.Command;
import dev.jorel.commandapi.annotations.reloaded.annotations.ExternalSubcommand;
import dev.jorel.commandapi.annotations.reloaded.annotations.Subcommand;
import dev.jorel.commandapi.annotations.reloaded.semantics.SemanticRule;
import dev.jorel.commandapi.annotations.reloaded.semantics.SemanticRuleContext;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;

/**
 * A semantic rule that checks that top-level sub-commands extend a command or sub-command class
 */
public class RuleTopLevelSubCommandClassesMustExtendCommandOrSubcommandClass implements SemanticRule {

	@Override
	public boolean passes(SemanticRuleContext context) {
		var utils = context.annotationUtils();
		boolean passes = true;
		for (var element : context.roundEnv().getElementsAnnotatedWith(Subcommand.class)) {
			if (element instanceof TypeElement subcommandElement) {
				var enclosingElement = subcommandElement.getEnclosingElement();
				if (enclosingElement.getKind() == ElementKind.PACKAGE) {
					var parentClass = subcommandElement.getSuperclass();
					if (parentClass instanceof TypeElement parentElement) {
						if (!utils.hasAnyAnnotation(parentElement, Command.class, Subcommand.class)) {
							context.logging().complain(subcommandElement,
								"Top-level " + Subcommand.class.getSimpleName() +
									" classes must extend a class with " + Command.class.getSimpleName() +
									" or " + Subcommand.class.getSimpleName() +
									" that has a " + ExternalSubcommand.class.getSimpleName() +
									" pointing to it, but " + parentElement.getQualifiedName() +
									" is not a " + Command.class.getSimpleName() + "" +
									" or " + Subcommand.class.getSimpleName()
							);
							passes = false;
						}
					}
				}
			}
		}
		return passes;
	}
}
