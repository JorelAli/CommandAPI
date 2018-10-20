package io.github.jorelali.commandapi;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.logging.Logger;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.jorelali.commandapi.api.CommandAPI;
import io.github.jorelali.commandapi.api.arguments.Argument;
import io.github.jorelali.commandapi.api.arguments.ChatComponentArgument;
import io.github.jorelali.commandapi.api.arguments.IntegerArgument;
import io.github.jorelali.commandapi.api.arguments.LiteralArgument;
import io.github.jorelali.commandapi.api.arguments.SuggestedStringArg;
import net.md_5.bungee.api.chat.BaseComponent;

public class CommandAPIMain extends JavaPlugin {
	
	private static Logger logger;
	
	public static Logger getLog() {
		return logger;
	}
	
	/** 
	 * Configuration wrapper class.
	 * The config.yml file used by the CommandAPI is only ever read from,
	 * nothing is ever written to it. That's why there's only getter methods.
	 */
	public class Config {
		
		//Output registering and unregistering of commands
		private final boolean verboseOutput;
		
		//Create a command_registration.json file
		private final boolean createDispatcherFile;
		
		//Run test code in the onEnable() method
		private final boolean runTestCode;
		
		public Config(FileConfiguration fileConfig) {
			verboseOutput = fileConfig.getBoolean("verbose-outputs");
			createDispatcherFile = fileConfig.getBoolean("create-dispatcher-json");
			runTestCode = fileConfig.getBoolean("test-code");
		}
		
		public boolean hasVerboseOutput() {
			return verboseOutput;
		}
		
		public boolean willCreateDispatcherFile() {
			return createDispatcherFile;
		}
		
		public boolean runTestCode() {
			return runTestCode;
		}
		
	}

	private static Config config;

	//Gets the instance of Config
	public static Config getConfiguration() {
		return config;
	}
	
	
	@Override
	public void onLoad() {
		saveDefaultConfig();
		CommandAPIMain.config = new Config(getConfig());
		logger = getLogger();
	}
	
	@Override
	public void onEnable() {
		if(config.runTestCode()) {

			//Test command unregistration
			CommandAPI.getInstance().unregister("gamemode");

			//Test ChatComponentArgument
			LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
			arguments.put("rawText", new ChatComponentArgument());
			CommandAPI.getInstance().register("veryraw", arguments, (sender, args) -> {
				if (sender instanceof Player) {
					Player player = (Player) sender;
					BaseComponent[] arr = (BaseComponent[]) args[0];
					player.spigot().sendMessage(arr);
				}
			});
			
			//Tests ChatComponentArgument compatibility with books
			arguments.clear();
			arguments.put("contents", new ChatComponentArgument());
			CommandAPI.getInstance().register("tobook", arguments, (sender, args) -> {
				if (sender instanceof Player) {
					Player player = (Player) sender;
					BaseComponent[] arr = (BaseComponent[]) args[0];
					
					ItemStack is = new ItemStack(Material.WRITTEN_BOOK);
					BookMeta meta = (BookMeta) is.getItemMeta(); 
					meta.spigot().addPage(arr);
					is.setItemMeta(meta);
					
					player.getInventory().addItem(is);
				}
			});

			//Test gamemode command literals
			HashMap<String, GameMode> gamemodes = new HashMap<>();
			gamemodes.put("adventure", GameMode.ADVENTURE);
			gamemodes.put("creative", GameMode.CREATIVE);
			gamemodes.put("spectator", GameMode.SPECTATOR);
			gamemodes.put("survival", GameMode.SURVIVAL);

			for (String key : gamemodes.keySet()) {
				LinkedHashMap<String, Argument> myArgs = new LinkedHashMap<>();
				myArgs.put(key, new LiteralArgument(key));
				CommandAPI.getInstance().register("gamemode", new String[] {"gm"}, myArgs, (sender, args) -> {
					if (sender instanceof Player) {
						Player player = (Player) sender;
						player.setGameMode(gamemodes.get(key));
					}
				});
			}
			
			arguments.clear();
			arguments.put("id", new IntegerArgument(0, 3));
			CommandAPI.getInstance().register("gamemode", new String[] {"gm"}, arguments, (sender, args) -> {
				if (sender instanceof Player) {
					Player player = (Player) sender;
					GameMode targetGM = null;
					switch((int) args[0]) {
						default:
						case 0:
							targetGM = GameMode.SURVIVAL;
							break;
						case 1:
							targetGM = GameMode.CREATIVE;
							break;
						case 2:
							targetGM = GameMode.ADVENTURE;
							break;
						case 3:
							targetGM = GameMode.SPECTATOR;
							break;
					}
					player.setGameMode(targetGM);
				}
			});
			
			//Tests SuggestedStringArguments
			arguments.clear();
			arguments.put("test", new SuggestedStringArg());
			CommandAPI.getInstance().register("suggest", arguments, (sender, args) -> {
				if (sender instanceof Player) {
					Player player = (Player) sender;
					player.sendMessage((String) args[0]);
				}
			});
		}
	}
	
}
