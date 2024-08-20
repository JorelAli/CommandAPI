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

import dev.jorel.commandapi.annotations.reloaded.annotations.Executes;
import dev.jorel.commandapi.annotations.reloaded.generators.IndentedWriter;
import dev.jorel.commandapi.annotations.reloaded.semantics.SemanticAnalyzer;
import dev.jorel.commandapi.annotations.reloaded.semantics.SemanticRule;
import dev.jorel.commandapi.annotations.reloaded.modules.TypeElementAnalyzerParserGeneratorModule;
import dev.jorel.commandapi.annotations.reloaded.parser.ExecutableElementParserContext;
import dev.jorel.commandapi.annotations.reloaded.parser.TypeElementParserContext;

import java.util.List;
import java.util.Optional;

/**
 * Contains everything to do with generating the list of CommandAPI command registrations
 */
public class CommandExecutorsModule implements TypeElementAnalyzerParserGeneratorModule<CommandExecutorsGeneratorContext> {
	private final CommandExecutorMethodModule commandExecutorMethodModule;

	public CommandExecutorsModule(CommandExecutorMethodModule commandExecutorMethodModule) {
		this.commandExecutorMethodModule = commandExecutorMethodModule;
	}

	@Override
	public void generate(IndentedWriter out, CommandExecutorsGeneratorContext context) {
		for (var executorContext : context.list()) {
			commandExecutorMethodModule.generate(out, executorContext);
		}
	}

	@Override
	public Optional<CommandExecutorsGeneratorContext> parse(TypeElementParserContext context) {
		var utils = context.utils();
		var annotationUtils = utils.annotationUtils();
		var maybeExecutors = annotationUtils.getEnclosedMethodsWithAnnotation(context.element(), Executes.class).stream()
			.map(executorMethod -> new ExecutableElementParserContext(utils, executorMethod))
			.map(commandExecutorMethodModule::parse)
			.toList();
		if (maybeExecutors.stream().anyMatch(Optional::isEmpty)) {
			return Optional.empty();
		}
		return Optional.of(new CommandExecutorsGeneratorContext(
			maybeExecutors.stream().flatMap(Optional::stream).toList()
		));
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
