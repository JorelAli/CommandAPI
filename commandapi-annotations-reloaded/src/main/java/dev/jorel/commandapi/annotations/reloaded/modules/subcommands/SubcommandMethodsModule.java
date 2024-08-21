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
import dev.jorel.commandapi.annotations.reloaded.generators.IndentedWriter;
import dev.jorel.commandapi.annotations.reloaded.modules.commands.CommandExecutorMethodGeneratorContext;
import dev.jorel.commandapi.annotations.reloaded.parser.ParserUtils;
import dev.jorel.commandapi.annotations.reloaded.semantics.SemanticAnalyzer;
import dev.jorel.commandapi.annotations.reloaded.semantics.SemanticRule;
import dev.jorel.commandapi.annotations.reloaded.annotations.Subcommand;
import dev.jorel.commandapi.annotations.reloaded.modules.TypeElementAnalyzerParserGeneratorModule;
import dev.jorel.commandapi.annotations.reloaded.modules.commands.CommandExecutorMethodModule;
import dev.jorel.commandapi.annotations.reloaded.parser.ExecutableElementParserContext;
import dev.jorel.commandapi.annotations.reloaded.parser.TypeElementParserContext;

import java.util.List;
import java.util.Optional;

/**
 * Contains everything to do with generating the CommandAPI command registrations for sub-command method annotations
 */
public class SubcommandMethodsModule implements TypeElementAnalyzerParserGeneratorModule<SubcommandMethodsGeneratorContext> {
	private final CommandExecutorMethodModule commandExecutorMethodModule;

	public SubcommandMethodsModule(CommandExecutorMethodModule commandExecutorMethodModule) {
		this.commandExecutorMethodModule = commandExecutorMethodModule;
	}

	@Override
	public Optional<SubcommandMethodsGeneratorContext> parse(TypeElementParserContext context) {
        ParserUtils utils = context.utils();
        AnnotationUtils annotationUtils = utils.annotationUtils();
        List<Optional<CommandExecutorMethodGeneratorContext>> maybeSubcommands = annotationUtils
			.getEnclosedMethodsWithAnnotation(context.element(), Subcommand.class).stream()
			.map(element -> new ExecutableElementParserContext(utils, element))
			.map(commandExecutorMethodModule::parse)
			.toList();
		if (maybeSubcommands.stream().anyMatch(Optional::isEmpty)) {
			return Optional.empty();
		}
		return Optional.of(
			new SubcommandMethodsGeneratorContext(
				maybeSubcommands.stream().flatMap(Optional::stream).toList()
			)
		);
	}

	@Override
	public void generate(IndentedWriter out, SubcommandMethodsGeneratorContext context) {
		context.list().forEach(subcontext -> commandExecutorMethodModule.generate(out, subcontext));
	}

	@Override
	public List<SemanticAnalyzer> subAnalyzers() {
		return List.of(
			commandExecutorMethodModule
		);
	}

	@Override
	public List<SemanticRule> rules() {
		return List.of();
	}
}
