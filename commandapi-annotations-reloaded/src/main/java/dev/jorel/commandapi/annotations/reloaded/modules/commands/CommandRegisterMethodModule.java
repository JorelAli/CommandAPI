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

import dev.jorel.commandapi.annotations.reloaded.Logging;
import dev.jorel.commandapi.annotations.reloaded.generators.IndentedWriter;
import dev.jorel.commandapi.annotations.reloaded.modules.TypeElementAnalyzerParserGeneratorModule;
import dev.jorel.commandapi.annotations.reloaded.modules.subcommands.SubcommandsGeneratorContext;
import dev.jorel.commandapi.annotations.reloaded.modules.subcommands.SubcommandsModule;
import dev.jorel.commandapi.annotations.reloaded.parser.ParserUtils;
import dev.jorel.commandapi.annotations.reloaded.parser.TypeElementParserContext;
import dev.jorel.commandapi.annotations.reloaded.semantics.SemanticAnalyzer;
import dev.jorel.commandapi.annotations.reloaded.semantics.SemanticRule;

import javax.lang.model.element.TypeElement;
import java.util.List;
import java.util.Optional;

/**
 * Contains everything to do with generating the registration of a CommandAPI command via annotations
 */
public class CommandRegisterMethodModule implements TypeElementAnalyzerParserGeneratorModule<CommandRegisterMethodGeneratorContext> {
	private final RuleCommandCanOnlyGoOnTopLevelClasses ruleCommandCanOnlyGoOnTopLevelClasses;
	private final CommandRegisterMethodJavadocGenerator javadocGenerator;
	private final SubcommandsModule subcommandsModule;

	public CommandRegisterMethodModule(
		RuleCommandCanOnlyGoOnTopLevelClasses ruleCommandCanOnlyGoOnTopLevelClasses,
		CommandRegisterMethodJavadocGenerator javadocGenerator,
		SubcommandsModule subcommandsModule
	) {
		this.ruleCommandCanOnlyGoOnTopLevelClasses = ruleCommandCanOnlyGoOnTopLevelClasses;
		this.javadocGenerator = javadocGenerator;
		this.subcommandsModule = subcommandsModule;
	}

	@Override
	public Optional<CommandRegisterMethodGeneratorContext> parse(TypeElementParserContext context) {
		TypeElement element = context.element();
		ParserUtils utils = context.utils();
		Logging logging = utils.logging();
		logging.info(element, "Parsing context");
		Optional<SubcommandsGeneratorContext> maybeSubcommandsContext = subcommandsModule.parse(context);
		if (maybeSubcommandsContext.isEmpty()) {
			logging.info(element, "Failed to parse context");
			return Optional.empty();
		}
		logging.info(element, "Successfully parsed context");
		return Optional.of(
			new CommandRegisterMethodGeneratorContext(
				utils.imports().withImport(element),
				element.getQualifiedName().toString(),
				maybeSubcommandsContext.orElseThrow()
			)
		);
	}

	@Override
	public void generate(IndentedWriter out, CommandRegisterMethodGeneratorContext context) {
		javadocGenerator.generate(out, context);
		out.printOnNewLine("public static void register(%s command) {"
			.formatted(context.commandClassName()));
		out.indent(indented -> subcommandsModule.generate(indented, context.subcommands()));
		out.printOnNewLine("}");
	}

	@Override
	public List<SemanticAnalyzer> subAnalyzers() {
		return List.of(
			subcommandsModule
		);
	}

	@Override
	public List<SemanticRule> rules() {
		return List.of(
			ruleCommandCanOnlyGoOnTopLevelClasses
		);
	}
}
