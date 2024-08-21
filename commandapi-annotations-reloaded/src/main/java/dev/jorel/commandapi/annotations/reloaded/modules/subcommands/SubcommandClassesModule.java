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

import dev.jorel.commandapi.annotations.reloaded.parser.ParserUtils;
import dev.jorel.commandapi.annotations.reloaded.semantics.SemanticAnalyzer;
import dev.jorel.commandapi.annotations.reloaded.semantics.SemanticRule;
import dev.jorel.commandapi.annotations.reloaded.BackReference;
import dev.jorel.commandapi.annotations.reloaded.annotations.Subcommand;
import dev.jorel.commandapi.annotations.reloaded.generators.IndentedWriter;
import dev.jorel.commandapi.annotations.reloaded.modules.TypeElementAnalyzerParserGeneratorModule;
import dev.jorel.commandapi.annotations.reloaded.parser.TypeElementParserContext;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Contains everything to do with generating the list of sub-command registration invocations for a CommandAPI command
 * class
 */
public class SubcommandClassesModule implements TypeElementAnalyzerParserGeneratorModule<SubcommandClassesGeneratorContext> {

	// Back-referencing so that we can get subcommand classes at any nesting depth
	private final BackReference<SubcommandClassModule> subcommandClassModuleRef;

	public SubcommandClassesModule(
		BackReference<SubcommandClassModule> subcommandClassModuleRef) {
		this.subcommandClassModuleRef = subcommandClassModuleRef;
	}

	@Override
	public Optional<SubcommandClassesGeneratorContext> parse(TypeElementParserContext context) {
        ParserUtils utils = context.utils();
        List<TypeElement> subcommandClasses = context.element()
			.getEnclosedElements().stream()
			.filter(element -> element.getAnnotation(Subcommand.class) != null)
			.filter(element -> element.getKind() == ElementKind.CLASS)
			.map(TypeElement.class::cast)
			.toList();
		utils.logging().info(context.element(), "Found %d enclosed subcommand classes"
				.formatted(subcommandClasses.size()));
		if (subcommandClasses.isEmpty()) {
			return Optional.of(new SubcommandClassesGeneratorContext(Collections.emptyList()));
		}
        SubcommandClassModule subcommandClassModule = subcommandClassModuleRef.get();
        List<Optional<SubcommandClassGeneratorContext>> maybeSubcommands = subcommandClasses.stream()
			.map(element -> new TypeElementParserContext(utils, element))
			.map(subcommandClassModule::parse)
			.toList();
		if (maybeSubcommands.stream().anyMatch(Optional::isEmpty)) {
			return Optional.empty();
		}
		return Optional.of(
			new SubcommandClassesGeneratorContext(
				maybeSubcommands.stream().flatMap(Optional::stream).toList()
			)
		);
	}

	@Override
	public void generate(IndentedWriter out, SubcommandClassesGeneratorContext context) {
		if (context.list().isEmpty()) {
			return;
		}
        SubcommandClassModule subcommandClassModule = subcommandClassModuleRef.get();
		context.list().forEach(subcontext -> subcommandClassModule.generate(out, subcontext));
	}

	@Override
	public List<SemanticAnalyzer> subAnalyzers() {
		return List.of();
	}

	@Override
	public List<SemanticRule> rules() {
		return List.of();
	}
}
