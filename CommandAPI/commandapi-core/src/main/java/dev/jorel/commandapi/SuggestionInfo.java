package dev.jorel.commandapi;

import org.bukkit.command.CommandSender;

/**
 * A class that represents information which you can use to generate suggestions.
 * This information is the following:
 * 
 *   - sender()       - the CommandSender typing this command
 *   - previousArgs() - the list of previously declared (and parsed) arguments
 *   - currentInput() - a string representing the full current input (including /)
 *   - currentArg()   - the current partially typed argument. For example "/mycmd tes" will return "tes" 
 */
public record SuggestionInfo(CommandSender sender, Object[] previousArgs, String currentInput, String currentArg) {
}
