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

import dev.jorel.commandapi.annotations.reloaded.annotations.Subcommand;
import dev.jorel.commandapi.annotations.reloaded.modules.commands.CommandNames;
import dev.jorel.commandapi.annotations.reloaded.parser.ExecutableElementParser;
import dev.jorel.commandapi.annotations.reloaded.parser.ExecutableElementParserContext;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.Optional;

/**
 * Parses the list of command and sub-command names in the context of a sub-command method
 */
public class SubcommandMethodNamesStackParser implements ExecutableElementParser<Deque<CommandNames>> {
	@Override
	public Optional<Deque<CommandNames>> parse(ExecutableElementParserContext context) {
		var utils = context.utils();
		var logging = utils.logging();
		var subcommands = context.element().getAnnotationsByType(Subcommand.class);
		if (subcommands.length == 0) {
			logging.complain(context.element(), "@%s annotation is missing from subcommand method"
				.formatted(Subcommand.class.getSimpleName()));
			return Optional.empty();
		}
		var valid = true;
		var stack = new ArrayDeque<CommandNames>();
		for (Subcommand subcommand : subcommands) {
			var values = subcommand.value();
			if (values.length == 0) {
				logging.complain(context.element(), "@%s annotation on method is missing a value"
					.formatted(Subcommand.class.getSimpleName()));
				valid = false;
				continue;
			}
			var aliases = values.length > 1 ? Arrays.copyOfRange(values, 1, values.length) : new String[0];
			stack.add(new CommandNames(values[0], aliases));
		}
		if (!valid) {
			return Optional.empty();
		}
		return Optional.of(stack);
	}
}
