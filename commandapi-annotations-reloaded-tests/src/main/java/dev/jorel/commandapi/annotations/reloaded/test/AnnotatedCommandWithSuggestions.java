/*******************************************************************************
 * Copyright 2018, 2021 Jorel Ali (Skepter) - MIT License
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
package dev.jorel.commandapi.annotations.reloaded.test;

import java.util.function.Supplier;

import dev.jorel.commandapi.annotations.reloaded.annotations.Executes;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.jorel.commandapi.annotations.reloaded.annotations.Command;
import dev.jorel.commandapi.annotations.reloaded.annotations.Subcommand;
import dev.jorel.commandapi.annotations.reloaded.annotations.Suggestion;
import dev.jorel.commandapi.annotations.reloaded.annotations.Suggests;
import dev.jorel.commandapi.annotations.reloaded.arguments.AStringArgument;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;

/**
 * Annotated command that uses suggestion classes
 */
@Command("mycommand")
public class AnnotatedCommandWithSuggestions {

	// /mycommand <name>
	
	@AStringArgument
	@Suggests(ListOfNames.class)
	String name;

	@Executes
	public void myExecutor(Player player) {
		// ...
	}

	@Suggestion
	class ListOfNames implements Supplier<ArgumentSuggestions<CommandSender>> {

		@Override
		public ArgumentSuggestions<CommandSender> get() {
			return ArgumentSuggestions.strings("Player1", "Player2", "Player3");
		}

	}

}
