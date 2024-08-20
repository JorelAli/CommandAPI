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
package dev.jorel.commandapi.annotations.reloaded.modules.base;

import dev.jorel.commandapi.annotations.reloaded.generators.CodeGenerator;
import dev.jorel.commandapi.annotations.reloaded.generators.IndentedWriter;
import dev.jorel.commandapi.annotations.reloaded.modules.commands.CommandRegisterMethodModule;
import dev.jorel.commandapi.annotations.reloaded.parser.ParserUtils;
import dev.jorel.commandapi.annotations.reloaded.parser.TypeElementParserContext;
import dev.jorel.commandapi.annotations.reloaded.semantics.SemanticAnalyzer;
import dev.jorel.commandapi.annotations.reloaded.semantics.SemanticRule;

import javax.lang.model.element.TypeElement;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Contains everything to do with generating the commands class for registering CommandAPI commands from annotations
 */
public class CommandsClassModule implements SemanticAnalyzer, CodeGenerator<CommandsClassGeneratorContext> {
	private final CommandsClassPackageGenerator commandsClassPackageGenerator;
	private final CommandsClassImportsGenerator commandsClassImportsGenerator;
	private final CommandsClassJavadocGenerator commandsClassJavadocGenerator;
	private final CommandRegisterMethodModule commandRegisterMethodModule;

	public CommandsClassModule(
		CommandsClassPackageGenerator commandsClassPackageGenerator,
		CommandsClassImportsGenerator commandsClassImportsGenerator,
		CommandsClassJavadocGenerator commandsClassJavadocGenerator,
		CommandRegisterMethodModule commandRegisterMethodModule
	) {
		this.commandsClassPackageGenerator = commandsClassPackageGenerator;
		this.commandsClassImportsGenerator = commandsClassImportsGenerator;
		this.commandsClassJavadocGenerator = commandsClassJavadocGenerator;
		this.commandRegisterMethodModule = commandRegisterMethodModule;
	}

	public Optional<CommandsClassGeneratorContext> parseAllContexts(
		ParserUtils parserUtils,
		String commandsClassName,
		ZonedDateTime generatorStarted,
		Set<TypeElement> commandClasses
	) {
		var maybeAllContexts = commandClasses.stream()
			.map(element -> commandRegisterMethodModule.parse(new TypeElementParserContext(
				parserUtils,
				element
			)))
			.toList();
		if (!maybeAllContexts.stream().allMatch(Optional::isPresent)) {
			return Optional.empty();
		}
		return Optional.of(new CommandsClassGeneratorContext(
			parserUtils.processingEnv(),
			parserUtils.imports(),
			"packageName", //TODO How do we want to handle packages?
			commandsClassName,
			generatorStarted,
			maybeAllContexts.stream().map(Optional::orElseThrow).toList()
		));
	}

	@Override
	public void generate(IndentedWriter out, CommandsClassGeneratorContext context) {
		commandsClassPackageGenerator.generate(out, context);
		commandsClassImportsGenerator.generate(out, context);
		commandsClassJavadocGenerator.generate(out, context);
		out.printOnNewLine("public class %s {".formatted(context.commandsClassName()));
		out.indent(indented -> context.registerMethods().forEach(registerMethodContext ->
			commandRegisterMethodModule.generate(indented, registerMethodContext)
		));
		out.printOnNewLine("}");
	}

	@Override
	public List<SemanticAnalyzer> subAnalyzers() {
		return List.of(
			commandRegisterMethodModule
		);
	}

	@Override
	public List<SemanticRule> rules() {
		return List.of();
	}
}
