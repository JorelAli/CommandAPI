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

import dev.jorel.commandapi.annotations.reloaded.AnnotationUtils;
import dev.jorel.commandapi.annotations.reloaded.annotations.Command;
import dev.jorel.commandapi.annotations.reloaded.annotations.ExternalSubcommand;
import dev.jorel.commandapi.annotations.reloaded.annotations.Subcommand;
import dev.jorel.commandapi.annotations.reloaded.semantics.SemanticRule;
import dev.jorel.commandapi.annotations.reloaded.semantics.SemanticRuleContext;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

/**
 * A semantic rule that checks that top-level sub-commands extend a class that points to it as an external sub-command
 */
public class RuleTopLevelSubCommandClassesMustExtendParentClassWithExternalSubcommand implements SemanticRule {

	private boolean isLinkedExternalSubcommand(SemanticRuleContext context, TypeElement subcommandElement, TypeElement parentElement) {
        AnnotationUtils utils = context.annotationUtils();
        Elements elementUtils = context.processingEnv().getElementUtils();
        Types typeUtils = context.processingEnv().getTypeUtils();
        String className = elementUtils.getBinaryName(subcommandElement).toString();
		return utils.getEnclosedElementsWithAnnotation(parentElement, ExternalSubcommand.class)
			.stream()
			.flatMap(it -> utils.getAnnotationClassValue(it, ExternalSubcommand.class).stream())
			.map(typeUtils::asElement)
			.map(TypeElement.class::cast)
			.map(TypeElement::getQualifiedName)
			.map(Object::toString)
			.anyMatch(className::equals);
	}

	@Override
	public boolean passes(SemanticRuleContext context) {
		boolean passes = true;
		for (var element : context.roundEnv().getElementsAnnotatedWith(Subcommand.class)) {
			if (element instanceof TypeElement subcommandElement) {
                Element enclosingElement = subcommandElement.getEnclosingElement();
				if (enclosingElement.getKind() == ElementKind.PACKAGE) {
                    TypeMirror parentClass = subcommandElement.getSuperclass();
					if (parentClass instanceof TypeElement parentElement) {
						if (!isLinkedExternalSubcommand(context, subcommandElement, parentElement)) {
							context.logging().complain(subcommandElement,
								"Top-level " + Subcommand.class.getSimpleName() +
									" classes must extend a class with " + Command.class.getSimpleName() +
									" or " + Subcommand.class.getSimpleName() +
									" that has a " + ExternalSubcommand.class.getSimpleName() +
									" pointing to it, but " + parentElement.getQualifiedName() +
									" does not have a " + ExternalSubcommand.class.getSimpleName() +
									" pointing back to this"
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
