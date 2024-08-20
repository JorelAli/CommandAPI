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

import dev.jorel.commandapi.annotations.reloaded.modules.commands.CommandNames;
import dev.jorel.commandapi.annotations.reloaded.parser.TypeElementParser;
import dev.jorel.commandapi.annotations.reloaded.parser.TypeElementParserContext;

import java.util.Deque;
import java.util.Optional;

/**
 * Parses the list of command and sub-command names in the context of a given sub-command class
 */
public class SubcommandClassNamesStackParser implements TypeElementParser<Deque<CommandNames>> {
	@Override
	public Optional<Deque<CommandNames>> parse(TypeElementParserContext context) {
		throw new UnsupportedOperationException("Not yet implemented"); //TODO
	}
}
