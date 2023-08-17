package dev.jorel.commandapi.test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

public class CommandConvertedTestsPlugin extends JavaPlugin {

	@Override
	public void onEnable() {
	}

	// Additional constructors required for MockBukkit
	public CommandConvertedTestsPlugin() {
		super();
	}

	public CommandConvertedTestsPlugin(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
		super(loader, description, dataFolder, file);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (label.equalsIgnoreCase("mycommand")) {
			sender.sendMessage("hello");
			return true;
		}
		return false;
	}

	public static InputStream pluginYaml() {
		return new ByteArrayInputStream("""
			name: MyPlugin
			main: dev.jorel.commandapi.test.CommandConvertedTestsPlugin
			version: 0.0.1
			description: A mock Bukkit plugin for CommandAPI testing
			author: Skepter
			website: https://www.jorel.dev/CommandAPI/
			api-version: 1.13
			commands:
			  mycommand:
			""".getBytes());
	}
}