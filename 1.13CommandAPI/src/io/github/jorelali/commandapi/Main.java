package io.github.jorelali.commandapi;

import org.bukkit.plugin.java.JavaPlugin;

import io.github.jorelali.commandapi.api.CommandAPI;

public class Main extends JavaPlugin {

	@Override
	public void onEnable() {
		//Instantiate the CommandAPI
		new CommandAPI();
		//Nothing required here, just need
		//to state that this is a plugin so
		//other plugins can depend on it
	}
	
}
