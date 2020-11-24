package dev.jorel.commandapi;

import java.io.File;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.EntitySelectorArgument.EntitySelector;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;

import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.CustomArgument;
import dev.jorel.commandapi.arguments.FloatArgument;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;

public class CommandAPIMain extends JavaPlugin implements Listener {
	
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
		
// Simple class replacement
class MoveType {}



new CommandAPICommand("speed")
	.withArguments(new PlayerArgument("pelaaja"), new FloatArgument("speed", 0, 10))
	.executes((sender, args) -> {
		Player player = (Player) args[0];
		float speed = (float) args[1] / 10;
		System.out.println(player + ", " + speed);
	})
	.register();
		
//new CommandAPICommand("speed")
//	.withArguments(new CustomArgument<MoveType>("mode", s -> new MoveType()), new FloatArgument("speed", 0, 10))
//	.executesPlayer((p, args) -> {
//		MoveType mode = (MoveType) args[0];
//		float speed = (float) args[1] / 10;
//		System.out.println(mode + ", " + speed);
//	})
//	.register();

//Register literal "randomchance"
LiteralCommandNode randomChance = Brigadier.fromLiteralArgument(new LiteralArgument("randomchance")).build();

//Declare arguments like normal
List<Argument> arguments = new ArrayList<>();
arguments.add(new IntegerArgument("numerator", 0));
arguments.add(new IntegerArgument("denominator", 1));

//Get brigadier argument objects
ArgumentBuilder numerator = Brigadier.fromArgument(arguments, "numerator");
ArgumentBuilder denominator = Brigadier.fromArgument(arguments, "denominator")
  //Fork redirecting to "execute" and state our predicate
  .fork(Brigadier.getRootNode().getChild("execute"), Brigadier.fromPredicate((sender, args) -> {
      //Parse arguments like normal
      int num = (int) args[0];
      int denom = (int) args[1];
      
      //Return boolean with a num/denom chance
      return Math.ceil(Math.random() * (double) denom) <= (double) num;
  }, arguments));

//Add <numerator> <denominator> as a child of randomchance
randomChance.addChild(numerator.then(denominator).build());

//Add (randomchance <numerator> <denominator>) as a child of (execute -> if)
Brigadier.getRootNode().getChild("execute").getChild("if").addChild(randomChance);


	}
	
	@Override
	public void onEnable() {
		CommandAPI.onEnable(this);
		getServer().getPluginManager().registerEvents(this, this);
	}
}
