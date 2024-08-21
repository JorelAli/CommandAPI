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

import dev.jorel.commandapi.annotations.reloaded.generators.GeneratorContext;
import dev.jorel.commandapi.annotations.reloaded.modules.commands.CommandRegisterMethodGeneratorContext;
import dev.jorel.commandapi.annotations.reloaded.parser.ImportsBuilder;

import javax.annotation.processing.ProcessingEnvironment;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * Context for generating the class for registering all CommandAPI commands from their annotations
 *
 * @param processingEnv     The annotations processing environment
 * @param imports           A builder for generating a set of imports for this class
 * @param packageName       The package name for this class
 * @param commandsClassName The class name for this class
 * @param generatorStarted  The time that the annotations processor started
 * @param registerMethods   The context needed to generate each of the register method invocations of each command
 */
public record CommandsClassGeneratorContext(
	ProcessingEnvironment processingEnv,
	ImportsBuilder imports,
	String packageName,
	String commandsClassName,
	ZonedDateTime generatorStarted,
	List<CommandRegisterMethodGeneratorContext> registerMethods
) implements GeneratorContext {
}
