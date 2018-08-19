package io.github.jorelali.commandapi.api;

import java.util.LinkedHashMap;


public class CommandAPI {
	
	private SemiReflector reflector;
	
	public CommandAPI() {
		try {
			this.reflector = new SemiReflector();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void register(String commandName, final LinkedHashMap<String, ArgumentType> args, CommandExecutor executor) {
		
		try {
			reflector.register(commandName, args, executor);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
