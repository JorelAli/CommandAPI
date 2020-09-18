package dev.jorel.commandapi;

import java.util.LinkedHashMap;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.MultiLiteralArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;

public class CommandAPIMain extends JavaPlugin {
	
	@Override
	public void onLoad() {
		CommandAPI.onLoad(this);
		
		Plugin essentials = Bukkit.getPluginManager().getPlugin("Essentials");

		LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();

		// /speed <speed>
		arguments.put("speed", new IntegerArgument(0, 10));
		Converter.convert(essentials, "speed", arguments);

		// /speed <target>
		arguments.put("target", new PlayerArgument());
		Converter.convert(essentials, "speed", arguments);

		arguments.clear();

		// /speed <walk/fly> <speed>
		arguments.put("type", new MultiLiteralArgument("walk", "fly"));
		arguments.put("speed", new IntegerArgument(0, 10));
		Converter.convert(essentials, "speed", arguments);

		// /speed <walk/fly> <speed> <target>
		arguments.put("target", new PlayerArgument());
		Converter.convert(essentials, "speed", arguments);
	}
	
	@Override
	public void onEnable() {
		CommandAPI.onEnable(this);
	}
}
