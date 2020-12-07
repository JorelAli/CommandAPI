package dev.jorel.commandapi;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import dev.jorel.commandapi.tests.Test;

public class CommandAPIMain extends JavaPlugin {
	
	List<Test> tests = new ArrayList<>();
	
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
		for(Entry<Plugin, String[]> pluginToConvert : CommandAPI.config.getPluginsToConvert()) {
			if(pluginToConvert.getValue().length == 0) {
				Converter.convert(pluginToConvert.getKey());
			} else {
				for(String command : pluginToConvert.getValue()) {
					new AdvancedConverter(pluginToConvert.getKey(), command).convert();
				}
			}
		}
		
		// Load tests
		
		
		
		// Run all of the Test.register() method here
		for(Test test : tests) {
			test.register();
		}

	}
	
	@Override
	public void onEnable() {
		CommandAPI.onEnable(this);
		
		// Run tests
		Bukkit.getScheduler().scheduleSyncDelayedTask(this, () -> {
			getLogger().info("Running " + tests.size() + " tests...");
			for(Test test : tests) {
				if(!test.test()) {
					failure();
				}
			}
			
			success();
			getLogger().info("Finished test suite");
		}, 5L);
	}
	
	/////////////
	// Testing //
	/////////////
	
	public void success() {
		getLogger().info("Success! All CommandAPI tests passed!");
	}
	
	public void failure() {
		getLogger().info("Failure! Some CommandAPI tests failed!");
	}
}
