package dev.jorel.commandapi;

import java.util.Arrays;
import java.util.LinkedHashMap;

import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.StringArgument;

public class Testing {

	public static void registerTestCommands() {
		LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
		arguments.put("number", new IntegerArgument());
		arguments.put("extra", new StringArgument().overrideSuggestions("a", "b"));
		
		new CommandAPICommand("test").withArguments(arguments)
		.executes((sender, args) -> {
			System.out.println(Arrays.deepToString(args));
		}).register();
	}

}
