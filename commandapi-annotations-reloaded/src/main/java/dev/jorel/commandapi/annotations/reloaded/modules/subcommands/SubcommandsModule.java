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

import dev.jorel.commandapi.annotations.reloaded.generators.IndentedWriter;
import dev.jorel.commandapi.annotations.reloaded.modules.TypeElementAnalyzerParserGeneratorModule;
import dev.jorel.commandapi.annotations.reloaded.parser.TypeElementParserContext;
import dev.jorel.commandapi.annotations.reloaded.semantics.SemanticAnalyzer;
import dev.jorel.commandapi.annotations.reloaded.semantics.SemanticRule;

import java.util.List;
import java.util.Optional;

/**
 * Contains everything to do with generating the CommandAPI command registrations for sub-commands
 */
public class SubcommandsModule implements TypeElementAnalyzerParserGeneratorModule<SubcommandsGeneratorContext> {

	private final RuleNonTopLevelTypeSubCommandsMustGoInsideCommandOrSubcommandClasses ruleNonTopLevelTypeSubCommandsMustGoInsideCommandOrSubcommandClasses;
	private final RuleNonTopLevelTypeSubcommandsCanOnlyGoInsideClasses ruleNonTopLevelTypeSubcommandsCanOnlyGoInsideClasses;
	private final RuleTypeSubCommandsCanOnlyGoOnNormalClasses ruleTypeSubCommandsCanOnlyGoOnNormalClasses;
	private final RuleTopLevelSubCommandClassesMustExtendParentClassWithExternalSubcommand ruleTopLevelSubCommandClassesMustExtendParentClassWithExternalSubcommand;
	private final RuleTopLevelSubCommandClassesMustHaveASuperClass ruleTopLevelSubCommandClassesMustHaveASuperClass;
	private final RuleTopLevelSubCommandClassesMustExtendCommandOrSubcommandClass ruleTopLevelSubCommandClassesMustExtendCommandOrSubcommandClass;
	private final SubcommandClassModule subcommandClassModule;

	public SubcommandsModule(
		RuleNonTopLevelTypeSubCommandsMustGoInsideCommandOrSubcommandClasses ruleNonTopLevelTypeSubCommandsMustGoInsideCommandOrSubcommandClasses,
		RuleNonTopLevelTypeSubcommandsCanOnlyGoInsideClasses ruleNonTopLevelTypeSubcommandsCanOnlyGoInsideClasses,
		RuleTypeSubCommandsCanOnlyGoOnNormalClasses ruleTypeSubCommandsCanOnlyGoOnNormalClasses,
		RuleTopLevelSubCommandClassesMustExtendParentClassWithExternalSubcommand ruleTopLevelSubCommandClassesMustExtendParentClassWithExternalSubcommand,
		RuleTopLevelSubCommandClassesMustHaveASuperClass ruleTopLevelSubCommandClassesMustHaveASuperClass,
		RuleTopLevelSubCommandClassesMustExtendCommandOrSubcommandClass ruleTopLevelSubCommandClassesMustExtendCommandOrSubcommandClass,
		SubcommandClassModule subcommandClassModule
	) {
		this.ruleNonTopLevelTypeSubCommandsMustGoInsideCommandOrSubcommandClasses = ruleNonTopLevelTypeSubCommandsMustGoInsideCommandOrSubcommandClasses;
		this.ruleNonTopLevelTypeSubcommandsCanOnlyGoInsideClasses = ruleNonTopLevelTypeSubcommandsCanOnlyGoInsideClasses;
		this.ruleTypeSubCommandsCanOnlyGoOnNormalClasses = ruleTypeSubCommandsCanOnlyGoOnNormalClasses;
		this.ruleTopLevelSubCommandClassesMustExtendParentClassWithExternalSubcommand = ruleTopLevelSubCommandClassesMustExtendParentClassWithExternalSubcommand;
		this.ruleTopLevelSubCommandClassesMustHaveASuperClass = ruleTopLevelSubCommandClassesMustHaveASuperClass;
		this.ruleTopLevelSubCommandClassesMustExtendCommandOrSubcommandClass = ruleTopLevelSubCommandClassesMustExtendCommandOrSubcommandClass;
		this.subcommandClassModule = subcommandClassModule;
	}

	@Override
	public Optional<SubcommandsGeneratorContext> parse(TypeElementParserContext context) {
        Optional<SubcommandClassGeneratorContext> maybeSubcommandClass = subcommandClassModule.parse(context);
		if (maybeSubcommandClass.isEmpty()) {
			return Optional.empty();
		}
		return Optional.of(
			new SubcommandsGeneratorContext(
				maybeSubcommandClass.orElseThrow()
			)
		);
	}

	@Override
	public void generate(IndentedWriter out, SubcommandsGeneratorContext context) {
		subcommandClassModule.generate(out, context.subcommandClass());
	}

	@Override
	public List<SemanticAnalyzer> subAnalyzers() {
		return List.of(
			subcommandClassModule
		);
	}

	@Override
	public List<SemanticRule> rules() {
		return List.of(
			ruleNonTopLevelTypeSubCommandsMustGoInsideCommandOrSubcommandClasses,
			ruleNonTopLevelTypeSubcommandsCanOnlyGoInsideClasses,
			ruleTypeSubCommandsCanOnlyGoOnNormalClasses,
			ruleTopLevelSubCommandClassesMustHaveASuperClass,
			ruleTopLevelSubCommandClassesMustExtendCommandOrSubcommandClass,
			ruleTopLevelSubCommandClassesMustExtendParentClassWithExternalSubcommand
		);
	}
}
