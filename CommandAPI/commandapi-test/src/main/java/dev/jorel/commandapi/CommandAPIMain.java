package dev.jorel.commandapi;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import dev.jorel.commandapi.tests.ArgumentTests;
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
		tests.add(new ArgumentTests());		
		
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
				try {
					test.test();
				} catch (Exception e) {
					e.printStackTrace();
					failure(test.getClass().getSimpleName());
				}
			}
			
			success();
			getLogger().info("Finished test suite");
		}, 5L);
	}
	
	/////////////
	// Testing //
	/////////////
	
	public static void success() {
		CommandAPI.logger.info("Success! All CommandAPI tests passed!");
	}
	
	public static void failure(String name) {
		CommandAPI.logger.info("Failure! A CommandAPI test failed in " + name);
	}
	
	public static List<String> getLog() {
		File file = new File(JavaPlugin.getPlugin(CommandAPIMain.class).getDataFolder().getParentFile().getParentFile(), "logs/latest.log");
		try {
			return Files.readAllLines(file.toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new ArrayList<>();
	}
}
