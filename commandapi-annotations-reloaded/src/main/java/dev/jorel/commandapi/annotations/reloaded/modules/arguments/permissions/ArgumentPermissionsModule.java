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
package dev.jorel.commandapi.annotations.reloaded.modules.arguments.permissions;

import dev.jorel.commandapi.CommandPermission;
import dev.jorel.commandapi.annotations.reloaded.generators.IndentedWriter;
import dev.jorel.commandapi.annotations.reloaded.semantics.SemanticAnalyzer;
import dev.jorel.commandapi.annotations.reloaded.semantics.SemanticRule;
import dev.jorel.commandapi.annotations.reloaded.modules.VariableElementAnalyzerParserGeneratorModule;
import dev.jorel.commandapi.annotations.reloaded.parser.VariableElementParserContext;

import java.util.List;
import java.util.Optional;

/**
 * A module containing everything to do with generating permissions for a CommandAPI argument
 */
public class ArgumentPermissionsModule implements VariableElementAnalyzerParserGeneratorModule<
	ArgumentPermissionGeneratorContext
	> {

	@Override
	public void generate(IndentedWriter out, ArgumentPermissionGeneratorContext context) {
		CommandPermission permission = context.permission();
		if (permission.equals(CommandPermission.NONE)) {
			// Do nothing
			return;
		}
		if (permission.equals(CommandPermission.OP)) {
			out.printOnNewLine(".withPermission(CommandPermission.OP)");
			return;
		}
		permission.getPermission().ifPresent(permissionValue ->
			out.printOnNewLine(".%s(\"%s\")".formatted(
				permission.isNegated() ? "withoutPermission" : "withPermission",
				permissionValue
			))
		);
	}

	@Override
	public Optional<ArgumentPermissionGeneratorContext> parse(VariableElementParserContext context) {
		throw new UnsupportedOperationException("%s: Parsing %s is not yet implemented".formatted(context.element(), getClass().getSimpleName())); //TODO
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
