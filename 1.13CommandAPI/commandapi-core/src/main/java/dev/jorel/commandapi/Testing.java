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
		arguments.put("extra", new StringArgument().overrideSuggestions((sender, args) -> {
			int number = (int) args[0];
			
			switch(number) {
			case 1:
				return new String[] {"a"};
			case 2:
				return new String[] {"b"};
			case 3:
				return new String[] {"c"};
			}
			return new String[] {};
		}));
		
		new CommandAPICommand("test").withArguments(arguments)
		.executes((sender, args) -> {
			System.out.println(Arrays.deepToString(args));
		}).register();
	}

}
