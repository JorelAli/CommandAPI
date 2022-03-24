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
package dev.jorel.commandapi;

import java.io.File;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.context.StringRange;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;

import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.GreedyStringArgument;

public class CommandAPIMain extends JavaPlugin {
	
	@Override
	public void onLoad() {
		//Config loading
		saveDefaultConfig();
		CommandAPI.config = new Config(getConfig());
		CommandAPI.dispatcherFile = new File(getDataFolder(), "command_registration.json");
		CommandAPI.logger = getLogger();
		
		//Check dependencies for CommandAPI
		CommandAPIHandler.getInstance().checkDependencies();
		
		//Convert all plugins to be converted
		for(Entry<JavaPlugin, String[]> pluginToConvert : CommandAPI.config.getPluginsToConvert()) {
			if(pluginToConvert.getValue().length == 0) {
				Converter.convert(pluginToConvert.getKey());
			} else {
				for(String command : pluginToConvert.getValue()) {
					new AdvancedConverter(pluginToConvert.getKey(), command).convert();
				}
			}
		}
		
		// Convert all arbitrary commands		
		for(String commandName : CommandAPI.config.getCommandsToConvert()) {
			new AdvancedConverter(commandName).convertCommand();
		}
	}
	
	@Override
	public void onEnable() {
		CommandAPI.onEnable(this);
		
		ArgumentSuggestions commandSuggestions = (info, builder) -> {
		    // The current argument, which is a full command
		    String arg = info.currentArg();

		    // Identify the position of the current argument
		    int start;
		    if(arg.contains(" ")) {
		        // Current argument contains spaces - it starts after the last space and after the start of this argument.
		        start = builder.getStart() + arg.lastIndexOf(' ') + 1;
		    } else {
		        // Input starts at the start of this argument
		        start = builder.getStart();
		    }
		    
		    // Parse command using brigadier
		    ParseResults<?> parseResults = Brigadier.getCommandDispatcher()
		        .parse(info.currentArg(), Brigadier.getBrigadierSourceFromCommandSender(info.sender()));
		    
		    // Intercept any parsing errors indicating an invalid command
		    for(CommandSyntaxException exception : parseResults.getExceptions().values()) {
		        // Raise the error, with the cursor offset to line up with the argument
		        throw new CommandSyntaxException(exception.getType(), exception.getRawMessage(), exception.getInput(), exception.getCursor() + start);
		    }

		    return Brigadier
		        .getCommandDispatcher()
		        .getCompletionSuggestions(parseResults)
		        .thenApply((suggestionsObject) -> {
		            // Brigadier's suggestions
		            Suggestions suggestions = (Suggestions) suggestionsObject;

		            return new Suggestions(
		                // Offset the index range of the suggestions by the start of the current argument
		                new StringRange(start, start + suggestions.getRange().getLength()),
		                // Copy the suggestions
		                suggestions.getList()
		            );
		        });
		};
		/* ANCHOR_END: BrigadierSuggestions1 */

		/* ANCHOR: BrigadierSuggestions2 */
		new CommandAPICommand("commandargument")
		    .withArguments(new GreedyStringArgument("command").replaceSuggestions(commandSuggestions))
		    .executes((sender, args) -> {
		        // Run the command using Bukkit.dispatchCommand()
		        Bukkit.dispatchCommand(sender, (String) args[0]);
		    }).register();
	}
}
