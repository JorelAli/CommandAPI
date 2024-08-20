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

import dev.jorel.commandapi.annotations.reloaded.annotations.Command;
import dev.jorel.commandapi.annotations.reloaded.annotations.Help;
import dev.jorel.commandapi.annotations.reloaded.parser.TypeElementParser;
import dev.jorel.commandapi.annotations.reloaded.parser.TypeElementParserContext;

import java.util.Optional;

/**
 * Parses command help information for generating the help option for CommandAPI command registration invocations
 */
public class CommandHelpParser implements TypeElementParser<CommandHelp> {

	@Override
	public Optional<CommandHelp> parse(TypeElementParserContext context) {
		var commandClass = context.element();
		var utils = context.utils();
		var logging = utils.logging();
		Help helpAnnotation = commandClass.getAnnotation(Help.class);
		if (helpAnnotation == null) {
			return Optional.of(new CommandHelp("", ""));
		}
		Command commandAnnotation = commandClass.getAnnotation(Command.class);
		if (commandAnnotation == null) {
			logging.complain(commandClass, "@%s can only go on @%s classes"
				.formatted(Help.class.getSimpleName(), Command.class.getSimpleName()));
			return Optional.empty();
		}
		logging.info(commandClass, "Help found %s".formatted(helpAnnotation));
		return Optional.of(
			new CommandHelp(
				helpAnnotation.value(),
				helpAnnotation.shortDescription()
			)
		);
	}
}
