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
import dev.jorel.commandapi.annotations.reloaded.annotations.Command;
import dev.jorel.commandapi.annotations.reloaded.parser.ParserUtils;
import dev.jorel.commandapi.annotations.reloaded.parser.TypeElementParser;
import dev.jorel.commandapi.annotations.reloaded.parser.TypeElementParserContext;

import javax.lang.model.element.TypeElement;
import java.util.Arrays;
import java.util.Optional;

/**
 * Parses command names and aliases for use in generating CommandAPI command registration invocations
 */
public class CommandNamesParser implements TypeElementParser<CommandNames> {
	@Override
	public Optional<CommandNames> parse(TypeElementParserContext context) {
        TypeElement commandClass = context.element();
        ParserUtils utils = context.utils();
        Logging logging = utils.logging();
		logging.info(commandClass, "Parsing command names");
		Command command = commandClass.getAnnotation(Command.class);
		if (command == null) {
			logging.complain(commandClass, "@%s annotation missing on command class"
				.formatted(Command.class.getSimpleName()));
			return Optional.empty();
		}
        String[] names = command.value();
		if (names.length == 0) {
			logging.complain(commandClass, "@%s annotation must have at least one value"
				.formatted(Command.class.getSimpleName()));
			return Optional.empty();
		}
        String[] aliases = names.length > 1 ? Arrays.copyOfRange(names, 1, names.length) : new String[0];
		logging.info(commandClass, "Parsed command names %s and aliases %s"
			.formatted(names[0], String.join(", ", aliases)));
		return Optional.of(new CommandNames(names[0], aliases));
	}
}
