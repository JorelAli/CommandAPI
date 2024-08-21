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

import dev.jorel.commandapi.annotations.reloaded.AnnotationUtils;
import dev.jorel.commandapi.annotations.reloaded.parser.ExecutableElementParser;
import dev.jorel.commandapi.annotations.reloaded.parser.ExecutableElementParserContext;
import dev.jorel.commandapi.annotations.reloaded.parser.ParserUtils;
import dev.jorel.commandapi.annotations.reloaded.parser.TypeElementParserContext;

import javax.lang.model.element.TypeElement;
import java.util.Optional;

/**
 * Parses the base command name for generating the parameter for a CommandAPI command constructor invocation
 */
public class CommandExecutorMethodBaseCommandNameParser implements ExecutableElementParser<String> {
	private final CommandNamesParser commandNamesParser;

	public CommandExecutorMethodBaseCommandNameParser(CommandNamesParser commandNamesParser) {
		this.commandNamesParser = commandNamesParser;
	}

	@Override
	public Optional<String> parse(ExecutableElementParserContext context) {
        ParserUtils utils = context.utils();
        AnnotationUtils annotationUtils = utils.annotationUtils();
        TypeElement topLevelClass = annotationUtils.getTopLevelClass(context.element());
		return commandNamesParser
			.parse(new TypeElementParserContext(utils, topLevelClass))
			.map(CommandNames::name);
	}
}
