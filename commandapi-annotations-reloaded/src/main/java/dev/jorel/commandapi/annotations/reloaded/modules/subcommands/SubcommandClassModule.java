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
import dev.jorel.commandapi.annotations.reloaded.generators.IndentedWriter;
import dev.jorel.commandapi.annotations.reloaded.semantics.SemanticAnalyzer;
import dev.jorel.commandapi.annotations.reloaded.semantics.SemanticRule;
import dev.jorel.commandapi.annotations.reloaded.modules.TypeElementAnalyzerParserGeneratorModule;
import dev.jorel.commandapi.annotations.reloaded.modules.commands.CommandExecutorsModule;
import dev.jorel.commandapi.annotations.reloaded.parser.TypeElementParserContext;

import java.util.List;
import java.util.Optional;

/**
 * Contains everything to do with generating all the CommandAPI command registration invocations for a command class
 */
public class SubcommandClassModule implements TypeElementAnalyzerParserGeneratorModule<SubcommandClassGeneratorContext> {

	private final SubcommandClassesModule subcommandClassesModule;
	private final SubcommandMethodsModule subcommandMethodsModule;
	private final CommandExecutorsModule commandExecutorsModule;

	public SubcommandClassModule(
		SubcommandClassesModule subcommandClassesModule,
		SubcommandMethodsModule subcommandMethodsModule,
		CommandExecutorsModule commandExecutorsModule) {
		this.subcommandClassesModule = subcommandClassesModule;
		this.subcommandMethodsModule = subcommandMethodsModule;
		this.commandExecutorsModule = commandExecutorsModule;
	}

	@Override
	public Optional<SubcommandClassGeneratorContext> parse(TypeElementParserContext context) {
		var utils = context.utils();
		var logging = utils.logging();
		logging.info(context.element(), "Parsing subcommand class");
		var maybeSubcommandClasses = subcommandClassesModule.parse(context);
		var maybeSubcommandMethods = subcommandMethodsModule.parse(context);
		var maybeCommandExecutors = commandExecutorsModule.parse(context);
		if (maybeSubcommandMethods.isEmpty() ||
			maybeSubcommandClasses.isEmpty() ||
			maybeCommandExecutors.isEmpty()) {
			return Optional.empty();
		}
		var subcommandClasses = maybeSubcommandClasses.orElseThrow();
		var subcommandMethods = maybeSubcommandMethods.orElseThrow();
		var commandExecutors = maybeCommandExecutors.orElseThrow();
		if (subcommandClasses.list().isEmpty() &&
			subcommandMethods.list().isEmpty() &&
			commandExecutors.list().isEmpty()) {
			logging.warn(context.element(), "@%s class has no executors"
				.formatted(Command.class.getSimpleName()));
		}
		logging.info(context.element(), "Parsed %d subcommand classes, %d subcommand methods, and %d command executors".formatted(
			subcommandClasses.list().size(),
			subcommandMethods.list().size(),
			commandExecutors.list().size()
		));
		return Optional.of(
			new SubcommandClassGeneratorContext(
				subcommandMethods,
				subcommandClasses,
				commandExecutors
			)
		);
	}

	@Override
	public void generate(IndentedWriter out, SubcommandClassGeneratorContext context) {
		subcommandMethodsModule.generate(out, context.subcommandMethods());
		subcommandClassesModule.generate(out, context.subcommandClasses());
		commandExecutorsModule.generate(out, context.commandExecutors());
	}

	@Override
	public List<SemanticAnalyzer> subAnalyzers() {
		return List.of(
			subcommandClassesModule,
			subcommandMethodsModule,
			commandExecutorsModule
		);
	}

	@Override
	public List<SemanticRule> rules() {
		return List.of();
	}
}
