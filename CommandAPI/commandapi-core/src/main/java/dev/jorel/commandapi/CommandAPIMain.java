package dev.jorel.commandapi;

import java.util.Arrays;

import org.bukkit.plugin.java.JavaPlugin;

import dev.jorel.commandapi.arguments.PlayerArgument;
import dev.jorel.commandapi.arguments.StringArgument;

public class CommandAPIMain extends JavaPlugin {
	
	@Override
	public void onLoad() {
		CommandAPI.onLoad(this);
	}
	
	@Override
	public void onEnable() {
		CommandAPI.onEnable(this);
		
		CommandAPICommand create = new CommandAPICommand("create")
			.withArgument(new StringArgument("guildname"))
			.executes((sender, args) -> {
				System.out.println(Arrays.deepToString(args));
			});

		CommandAPICommand add = new CommandAPICommand("add")
			.withArgument(new StringArgument("guildname"))
			.withArgument(new PlayerArgument("member"))
			.executes((sender, args) -> {
				System.out.println(Arrays.deepToString(args));
			});

		new CommandAPICommand("guild")
		  .withSubcommand(create)
		  .withSubcommand(add)
		  .register();
	}
}
