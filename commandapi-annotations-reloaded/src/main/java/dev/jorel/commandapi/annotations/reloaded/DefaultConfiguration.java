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
package dev.jorel.commandapi.annotations.reloaded;

import dev.jorel.commandapi.annotations.reloaded.annotations.Command;
import dev.jorel.commandapi.annotations.reloaded.annotations.Executes;
import dev.jorel.commandapi.annotations.reloaded.annotations.Help;
import dev.jorel.commandapi.annotations.reloaded.annotations.NeedsOp;
import dev.jorel.commandapi.annotations.reloaded.annotations.Permission;
import dev.jorel.commandapi.annotations.reloaded.annotations.Subcommand;
import dev.jorel.commandapi.annotations.reloaded.annotations.Subcommands;
import dev.jorel.commandapi.annotations.reloaded.annotations.Suggestion;
import dev.jorel.commandapi.annotations.reloaded.annotations.Suggests;
import dev.jorel.commandapi.annotations.reloaded.arguments.ArgumentAnnotations;
import dev.jorel.commandapi.annotations.reloaded.modules.arguments.CommandExecutorMethodArgumentsModule;
import dev.jorel.commandapi.annotations.reloaded.modules.base.CommandsClassImportsGenerator;
import dev.jorel.commandapi.annotations.reloaded.modules.base.CommandsClassJavadocGenerator;
import dev.jorel.commandapi.annotations.reloaded.modules.base.CommandsClassModule;
import dev.jorel.commandapi.annotations.reloaded.modules.base.CommandsClassPackageGenerator;
import dev.jorel.commandapi.annotations.reloaded.modules.commands.CommandExecutorMethodBaseCommandNameParser;
import dev.jorel.commandapi.annotations.reloaded.modules.commands.CommandExecutorMethodExecutorModule;
import dev.jorel.commandapi.annotations.reloaded.modules.commands.CommandExecutorMethodModule;
import dev.jorel.commandapi.annotations.reloaded.modules.commands.CommandExecutorsModule;
import dev.jorel.commandapi.annotations.reloaded.modules.commands.CommandNamesParser;
import dev.jorel.commandapi.annotations.reloaded.modules.commands.CommandRegisterMethodJavadocGenerator;
import dev.jorel.commandapi.annotations.reloaded.modules.commands.CommandRegisterMethodModule;
import dev.jorel.commandapi.annotations.reloaded.modules.commands.RuleCommandCanOnlyGoOnTopLevelClasses;
import dev.jorel.commandapi.annotations.reloaded.modules.permissions.CommandExecutorMethodPermissionsModule;
import dev.jorel.commandapi.annotations.reloaded.modules.subcommands.RuleNonTopLevelTypeSubCommandsMustGoInsideCommandOrSubcommandClasses;
import dev.jorel.commandapi.annotations.reloaded.modules.subcommands.RuleNonTopLevelTypeSubcommandsCanOnlyGoInsideClasses;
import dev.jorel.commandapi.annotations.reloaded.modules.subcommands.RuleTopLevelSubCommandClassesMustExtendCommandOrSubcommandClass;
import dev.jorel.commandapi.annotations.reloaded.modules.subcommands.RuleTopLevelSubCommandClassesMustExtendParentClassWithExternalSubcommand;
import dev.jorel.commandapi.annotations.reloaded.modules.subcommands.RuleTopLevelSubCommandClassesMustHaveASuperClass;
import dev.jorel.commandapi.annotations.reloaded.modules.subcommands.RuleTypeSubCommandsCanOnlyGoOnNormalClasses;
import dev.jorel.commandapi.annotations.reloaded.modules.subcommands.SubcommandClassModule;
import dev.jorel.commandapi.annotations.reloaded.modules.subcommands.SubcommandClassesModule;
import dev.jorel.commandapi.annotations.reloaded.modules.subcommands.SubcommandMethodsModule;
import dev.jorel.commandapi.annotations.reloaded.modules.subcommands.SubcommandsModule;
import dev.jorel.commandapi.annotations.reloaded.parser.ImportsBuilder;
import dev.jorel.commandapi.annotations.reloaded.parser.ImportsBuilderImpl;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The default configuration for the CommandAPI annotation system
 */
public class DefaultConfiguration implements Configuration {
	private static final String COMMANDS_CLASS_NAME = "Commands";

	private static final Set<Class<? extends Annotation>> OTHER_ANNOTATIONS = Set.of(
		Command.class,
		NeedsOp.class,
		Permission.class,
		Help.class,
		Suggestion.class,
		Subcommand.class,
		Subcommands.class,
		Suggests.class,
		Executes.class
	);

	@Override
	public String getCommandsClassName() {
		return COMMANDS_CLASS_NAME;
	}

	@Override
	public Set<Class<? extends Annotation>> getArgumentAnnotations() {
		return ArgumentAnnotations.ALL;
	}

	@Override
	public Set<Class<? extends Annotation>> getOtherAnnotations() {
		return OTHER_ANNOTATIONS;
	}

	@Override
	public Set<String> getSupportedAnnotationTypes() {
		return Stream.of(
				getArgumentAnnotations(),
				getOtherAnnotations()
			)
			.flatMap(Collection::stream)
			.map(Class::getCanonicalName)
			.collect(Collectors.toSet());
	}

	private SubcommandClassModule getSubcommandClassModule() {
		BackReference<SubcommandClassModule> subcommandClassModuleRef = new BackReference<>();
		SubcommandClassModule subcommandClassModule = new SubcommandClassModule(
			new SubcommandClassesModule(
				subcommandClassModuleRef
			), new SubcommandMethodsModule(
			new CommandExecutorMethodModule(
				new CommandExecutorMethodBaseCommandNameParser(
					new CommandNamesParser()
				),
				new CommandExecutorMethodPermissionsModule(),
				new CommandExecutorMethodArgumentsModule(),
				new CommandExecutorMethodExecutorModule()
			)
		),
			new CommandExecutorsModule(
				new CommandExecutorMethodModule(
					new CommandExecutorMethodBaseCommandNameParser(
						new CommandNamesParser()
					),
					new CommandExecutorMethodPermissionsModule(),
					new CommandExecutorMethodArgumentsModule(),
					new CommandExecutorMethodExecutorModule()
				)
			)
		);
		subcommandClassModuleRef.initialise(subcommandClassModule);
		return subcommandClassModule;
	}

	@Override
	public CommandsClassModule getBaseModule() {
		return new CommandsClassModule(
			new CommandsClassPackageGenerator(),
			new CommandsClassImportsGenerator(),
			new CommandsClassJavadocGenerator(),
			new CommandRegisterMethodModule(
				new RuleCommandCanOnlyGoOnTopLevelClasses(),
				new CommandRegisterMethodJavadocGenerator(),
				new SubcommandsModule(
					new RuleNonTopLevelTypeSubCommandsMustGoInsideCommandOrSubcommandClasses(),
					new RuleNonTopLevelTypeSubcommandsCanOnlyGoInsideClasses(),
					new RuleTypeSubCommandsCanOnlyGoOnNormalClasses(),
					new RuleTopLevelSubCommandClassesMustExtendParentClassWithExternalSubcommand(),
					new RuleTopLevelSubCommandClassesMustHaveASuperClass(),
					new RuleTopLevelSubCommandClassesMustExtendCommandOrSubcommandClass(),
					getSubcommandClassModule()
				)
			)
		);
	}

	@Override
	public ImportsBuilder getImportsBuilder() {
		return new ImportsBuilderImpl();
	}

	@Override
	public AnnotationUtils getAnnotationUtils() {
		return new AnnotationUtils();
	}
}
