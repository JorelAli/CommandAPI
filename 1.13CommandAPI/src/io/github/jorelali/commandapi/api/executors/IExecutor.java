package io.github.jorelali.commandapi.api.executors;

import org.bukkit.command.CommandSender;

public interface IExecutor {
	
	default Class<?> matchesInstance(CommandSender sender) {
		Class<?> type = this.getClass().getDeclaredMethods()[0].getParameterTypes()[0];
		System.out.println(type.getName());
		
		type.get
		return null;
		
	}
}
