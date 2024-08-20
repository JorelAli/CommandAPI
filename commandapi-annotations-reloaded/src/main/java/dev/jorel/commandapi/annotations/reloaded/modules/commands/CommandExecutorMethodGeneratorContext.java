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

import dev.jorel.commandapi.annotations.reloaded.generators.GeneratorContext;
import dev.jorel.commandapi.annotations.reloaded.modules.arguments.CommandExecutorMethodArgumentsGeneratorContext;
import dev.jorel.commandapi.annotations.reloaded.modules.permissions.CommandExecutorMethodPermissionsGeneratorContext;

/**
 * Context needed to generate the registration for a CommandAPI command
 *
 * @param commandClassName  The name of the command class
 * @param baseCommandName   The name of the command node
 * @param permissionContext Context needed to generate the command permissions option
 * @param argumentsContext  Context needed to generate the command arguments option
 * @param executorContext   Context needed to generate the command executor option
 */
public record CommandExecutorMethodGeneratorContext(
	String commandClassName,
	String baseCommandName,
	CommandExecutorMethodPermissionsGeneratorContext permissionContext,
	CommandExecutorMethodArgumentsGeneratorContext argumentsContext,
	CommandExecutorMethodExecutorGeneratorContext executorContext
) implements GeneratorContext {
}
