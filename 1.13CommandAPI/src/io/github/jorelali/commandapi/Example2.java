package io.github.jorelali.commandapi;

import java.util.LinkedHashMap;

import org.bukkit.plugin.java.JavaPlugin;

import io.github.jorelali.commandapi.api.arguments.Argument;
import io.github.jorelali.commandapi.api.arguments.LiteralArgument;

/**
 * ~
 */
public class Example2 extends JavaPlugin {
	
	@Override
	public void onEnable() {

		//say we had one command to do literally everything
		
		LinkedHashMap<String, Argument> a = new LinkedHashMap<String, Argument>();
		a.put("", new LiteralArgument(""));
	}
	
	
}
