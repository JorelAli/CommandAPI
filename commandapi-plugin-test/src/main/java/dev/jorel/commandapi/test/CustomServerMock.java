package dev.jorel.commandapi.test;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.Suggestions;

import be.seeseemelk.mockbukkit.AsyncCatcher;
import be.seeseemelk.mockbukkit.ServerMock;
import dev.jorel.commandapi.Brigadier;

public class CustomServerMock extends ServerMock {

	@SuppressWarnings("unchecked")
	public boolean dispatchThrowableCommand(CommandSender sender, String commandLine) throws CommandSyntaxException{
		String[] commands = commandLine.split(" ");
		String commandLabel = commands[0];
		Command command = getCommandMap().getCommand(commandLabel);
		
		if(command != null) {
			return super.dispatchCommand(sender, commandLine);
		} else {
			AsyncCatcher.catchOp("command dispatch");
			@SuppressWarnings("rawtypes")
			CommandDispatcher dispatcher = Brigadier.getCommandDispatcher();
			Object css = Brigadier.getBrigadierSourceFromCommandSender(sender);
			return dispatcher.execute(commandLine, css) != 0;
		}
	}
	
	@Override
	public boolean dispatchCommand(CommandSender sender, String commandLine) {
		try {
			return dispatchThrowableCommand(sender, commandLine);
		} catch (CommandSyntaxException e1) {
			return false;
		}
	}
	
	public boolean isValidCommandAPICommand(CommandSender sender, String commandLine) {
		String[] commands = commandLine.split(" ");
		String commandLabel = commands[0];
		Command command = getCommandMap().getCommand(commandLabel);
		
		if(command != null) {
			return false;
		} else {
			AsyncCatcher.catchOp("command dispatch");
			@SuppressWarnings("rawtypes")
			CommandDispatcher dispatcher = Brigadier.getCommandDispatcher();
			Object css = Brigadier.getBrigadierSourceFromCommandSender(sender);
			ParseResults results = dispatcher.parse(commandLine, css);
			return results.getExceptions().size() == 0;
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<String> getSuggestions(CommandSender sender, String commandLine) {
		AsyncCatcher.catchOp("command tabcomplete");
		CommandDispatcher dispatcher = Brigadier.getCommandDispatcher();
		Object css = Brigadier.getBrigadierSourceFromCommandSender(sender);
		ParseResults parseResults = dispatcher.parse(commandLine, css);
		Suggestions suggestions = null;
		try {
			suggestions = (Suggestions) dispatcher.getCompletionSuggestions(parseResults).get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		
		List<String> suggestionsAsStrings = new ArrayList<>();
		for(Suggestion suggestion : suggestions.getList()) {
			suggestionsAsStrings.add(suggestion.getText());
		}
		
		return suggestionsAsStrings;
	}
	
	@Override
	public boolean shouldSendChatPreviews() {
		return true;
	}
}
