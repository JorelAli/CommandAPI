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
import dev.jorel.commandapi.annotations.reloaded.generators.InvocationParametersRenderer;
import dev.jorel.commandapi.annotations.reloaded.semantics.SemanticAnalyzer;
import dev.jorel.commandapi.annotations.reloaded.semantics.SemanticRule;
import dev.jorel.commandapi.annotations.reloaded.generators.ConstructorClassNameRenderer;
import dev.jorel.commandapi.annotations.reloaded.generators.IndentedWriter;
import dev.jorel.commandapi.annotations.reloaded.modules.VariableElementAnalyzerParserGeneratorModule;
import dev.jorel.commandapi.annotations.reloaded.modules.arguments.listed.ArgumentListedOptionGenerator;
import dev.jorel.commandapi.annotations.reloaded.modules.arguments.permissions.ArgumentPermissionsModule;
import dev.jorel.commandapi.annotations.reloaded.modules.arguments.requirements.ArgumentRequirementsModule;
import dev.jorel.commandapi.annotations.reloaded.modules.arguments.suggestions.ArgumentSuggestionsModule;
import dev.jorel.commandapi.annotations.reloaded.parser.VariableElementParserContext;

import java.util.List;
import java.util.Optional;

/**
 * Contains everything to do with generating CommandAPI arguments
 */
public class ArgumentModule implements VariableElementAnalyzerParserGeneratorModule<
	ArgumentGeneratorContext
	> {

	private final ConstructorClassNameRenderer constructorClassNameRenderer;
	private final InvocationParametersRenderer invocationParametersRenderer;
	private final ArgumentListedOptionGenerator listedOptionGenerator;
	private final ArgumentPermissionsModule permissionsModule;
	private final ArgumentRequirementsModule requirementsModule;
	private final ArgumentSuggestionsModule suggestionsModule;

	public ArgumentModule(
		ConstructorClassNameRenderer constructorClassNameRenderer,
		InvocationParametersRenderer invocationParametersRenderer,
		ArgumentListedOptionGenerator listedOptionGenerator,
		ArgumentPermissionsModule permissionsModule,
		ArgumentRequirementsModule requirementsModule,
		ArgumentSuggestionsModule suggestionsModule
	) {
		this.constructorClassNameRenderer = constructorClassNameRenderer;
		this.invocationParametersRenderer = invocationParametersRenderer;
		this.listedOptionGenerator = listedOptionGenerator;
		this.permissionsModule = permissionsModule;
		this.requirementsModule = requirementsModule;
		this.suggestionsModule = suggestionsModule;
	}

	@Override
	public void generate(IndentedWriter out, ArgumentGeneratorContext context) {
        ArgumentAnnotation argumentAnnotation = context.argumentAnnotation();
        String options = out.indentToBuffer(buffer -> {
			suggestionsModule.generate(buffer, context.argumentSuggestionsGeneratorContext());
			permissionsModule.generate(buffer, context.argumentPermissionGeneratorContext());
			requirementsModule.generate(buffer, context.argumentRequirementsGeneratorContext());
			listedOptionGenerator.generate(buffer, context.argumentListedOptionGeneratorContext());
		});
		out.printOnNewLine(".%s(new %s(%s)%s".formatted(
			argumentAnnotation.optional() ? "withOptionalArguments" : "withArguments",
			constructorClassNameRenderer.render(argumentAnnotation.argumentType()),
			invocationParametersRenderer.render(argumentAnnotation.constructorParameters()),
			options
		));
		if (options.isEmpty()) {
			out.appendInLine(")");
		} else {
			out.printOnNewLine(")");
		}
	}

	@Override
	public Optional<ArgumentGeneratorContext> parse(VariableElementParserContext context) {
		throw new UnsupportedOperationException("%s: Parsing not yet implemented".formatted(context.element()));
	}

	@Override
	public List<SemanticAnalyzer> subAnalyzers() {
		return List.of(
			permissionsModule,
			requirementsModule,
			suggestionsModule
		);
	}

	@Override
	public List<SemanticRule> rules() {
		return List.of();
	}
}
