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

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.annotations.reloaded.generators.IndentedWriter;
import dev.jorel.commandapi.annotations.reloaded.modules.arguments.CommandExecutorMethodArgumentsGeneratorContext;
import dev.jorel.commandapi.annotations.reloaded.modules.arguments.CommandExecutorMethodArgumentsModule;
import dev.jorel.commandapi.annotations.reloaded.modules.permissions.CommandExecutorMethodPermissionsGeneratorContext;
import dev.jorel.commandapi.annotations.reloaded.modules.permissions.CommandExecutorMethodPermissionsModule;
import dev.jorel.commandapi.annotations.reloaded.parser.ParserUtils;
import dev.jorel.commandapi.annotations.reloaded.semantics.SemanticAnalyzer;
import dev.jorel.commandapi.annotations.reloaded.semantics.SemanticRule;
import dev.jorel.commandapi.annotations.reloaded.modules.ExecutableElementAnalyzerParserGeneratorModule;
import dev.jorel.commandapi.annotations.reloaded.parser.ExecutableElementParserContext;

import java.util.List;
import java.util.Optional;

/**
 * Contains everything to do with generating the registration for a CommandAPI command
 */
public class CommandExecutorMethodModule implements ExecutableElementAnalyzerParserGeneratorModule<CommandExecutorMethodGeneratorContext> {

	private final CommandExecutorMethodBaseCommandNameParser commandNameParser;
	private final CommandExecutorMethodPermissionsModule permissionsModule;
	private final CommandExecutorMethodArgumentsModule argumentsModule;
	private final CommandExecutorMethodExecutorModule executorModule;

	public CommandExecutorMethodModule(
		CommandExecutorMethodBaseCommandNameParser commandNameParser,
		CommandExecutorMethodPermissionsModule permissionsModule,
		CommandExecutorMethodArgumentsModule argumentsModule,
		CommandExecutorMethodExecutorModule executorModule
	) {
		this.commandNameParser = commandNameParser;
		this.permissionsModule = permissionsModule;
		this.argumentsModule = argumentsModule;
		this.executorModule = executorModule;
	}

	@Override
	public void generate(IndentedWriter out, CommandExecutorMethodGeneratorContext context) {
		out.printOnNewLine("new %s(\"%s\")".formatted(
			context.commandClassName(),
			context.baseCommandName()
		));
		out.indent(indented -> {
			permissionsModule.generate(indented, context.permissionContext());
			argumentsModule.generate(indented, context.argumentsContext());
			executorModule.generate(indented, context.executorContext());
		});
		out.appendInLine(";");
		out.printEmptyLine();
	}

	@Override
	public Optional<CommandExecutorMethodGeneratorContext> parse(ExecutableElementParserContext context) {
        ParserUtils utils = context.utils();
        Optional<String> maybeBaseCommandName = commandNameParser.parse(context);
        Optional<CommandExecutorMethodPermissionsGeneratorContext> maybePermissions = permissionsModule.parse(context);
        Optional<CommandExecutorMethodArgumentsGeneratorContext> maybeArguments = argumentsModule.parse(context);
        Optional<CommandExecutorMethodExecutorGeneratorContext> maybeExecutor = executorModule.parse(context);
		if (
			maybeBaseCommandName.isEmpty() ||
			maybePermissions.isEmpty() ||
			maybeArguments.isEmpty() ||
			maybeExecutor.isEmpty()) {
			return Optional.empty();
		}
		return Optional.of(
			new CommandExecutorMethodGeneratorContext(
				utils.imports().withImport(CommandAPICommand.class),
				maybeBaseCommandName.orElseThrow(),
				maybePermissions.orElseThrow(),
				maybeArguments.orElseThrow(),
				maybeExecutor.orElseThrow()
			)
		);
	}

	@Override
	public List<SemanticAnalyzer> subAnalyzers() {
		return List.of(
			permissionsModule,
			argumentsModule,
			executorModule
		);
	}

	@Override
	public List<SemanticRule> rules() {
		return List.of();
	}
}
