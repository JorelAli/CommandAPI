package io.github.jorelali.commandapi;

import java.util.LinkedHashMap;
import java.util.logging.Logger;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.jorelali.commandapi.api.CommandAPI;
import io.github.jorelali.commandapi.api.CommandPermission;
import io.github.jorelali.commandapi.api.arguments.Argument;
import io.github.jorelali.commandapi.api.arguments.BooleanArgument;
import io.github.jorelali.commandapi.api.arguments.ChatColorArgument;

public class CommandAPIMain extends JavaPlugin {
	
	private static Logger logger;
	
	public static Logger getLog() {
		return logger;
	}
	
	/** 
	 * Configuration wrapper class.
	 * The config.yml file used by the CommandAPI is only ever read from,
	 * nothing is ever written to it. Due to that, that's why there's only
	 * ever getter methods.
	 */
	public class Config {
		
		private boolean verboseOutput;
		
		public Config(FileConfiguration fileConfig) {
			verboseOutput = fileConfig.getBoolean("verbose-outputs");
		}
		
		public boolean hasVerboseOutput() {
			return verboseOutput;
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
		
		
		
		
		
		
		
		
		//Nothing required here, just need
		//to state that this is a plugin so
		//other plugins can depend on it
		
		/////// EVERYTHING BELOW HERE IS COMMENTED AND DOESN'T AFFECT COMPILATION AT ALL>.
		
		LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
		CommandAPI.getInstance().register("trample", new CommandPermission("wikiop.general.trampling"), arguments, (sender, args) -> {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				player.sendMessage("Triggered the command");
			}
		});
		
		//arguments = new LinkedHashMap<>();
		arguments.put("thing", new BooleanArgument());
		CommandAPI.getInstance().register("trample", new CommandPermission("wikiop.general.trampling"), arguments, (sender, args) -> {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				player.sendMessage("Triggered the command again with boolean");
			}
		});
		
		arguments = new LinkedHashMap<>();
		arguments.put("a", new ChatColorArgument());
		CommandAPI.getInstance().register("mytestcmd", arguments, (sender, args) -> {
			System.out.println("woop");
		});

		CommandAPI.getInstance().unregister("gamemode");
		
		
		
		
//		LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
//		arguments.put("rawText", new ChatComponentArgument());
//		CommandAPI.getInstance().register("veryraw", arguments, (sender, args) -> {
//			if (sender instanceof Player) {
//				Player player = (Player) sender;
//				BaseComponent[] arr = (BaseComponent[]) args[0];
//				player.spigot().sendMessage(arr);
//			}
//		});
		
//		String packageName = null;
//		
//		try {
//			packageName = Bukkit.getServer().getClass().getDeclaredMethod("getServer").invoke(Bukkit.getServer()).getClass().getPackage().getName();
//		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
//				| SecurityException e) {
//			e.printStackTrace();
//		}
//		
//		try {
//			Class chatSerializer = Class.forName(packageName + "." + "IChatBaseComponent$ChatSerializer");//IChatBaseComponent.ChatSerializer
//			Method m = chatSerializer.getDeclaredMethod("a", Class.forName(packageName + "." + "IChatBaseComponent"));
//		//blah de blah	Object o = m.invoke(null, "{\"text\":\"Hi there!\",\"bold\":true}"); //returns String
//			Object yo = ComponentSerializer.parse((String) o);
//			
//			Bukkit.getPlayer("Skepter").spigot().sendMessage((BaseComponent) yo);
//			System.out.println("woop woop!");
//			//m.invoke(null, "h");
//		} catch (ClassNotFoundException | SecurityException | NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
//			e.printStackTrace();
//		}
		
//		Class<?> getNMS(String className) throws ClassNotFoundException {
//			return (Class.forName(packageName + "." + className));
//		}
//		
		//BookMeta m = null;
		//m.spigot().addPage(null);
		
		//Convert from ICHAT TO BASECOMPONENT[]
		//ComponentSerializer.parse(IChatBaseComponent.ChatSerializer.a(pages.get(page - 1)));
		
//		BaseComponent b;
//		//b.
//		BookMeta m = null;
//		m.spigot().addPage(null);
//		
//LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
//arguments.put("target", new PlayerArgument());
//arguments.put("message", new GreedyStringArgument());
//CommandAPI.getInstance().register("custmsg", arguments, (sender, args) -> {
//	((Player) args[0]).sendMessage((String) args[1]);
//});
//		
//		arguments = new LinkedHashMap<>();
//		arguments.put("target", new PlayerArgument());
//		arguments.put("message", new StringArgument());
//		CommandAPI.getInstance().register("custmsg2", arguments, (sender, args) -> {
//			((Player) args[0]).sendMessage((String) args[1]);
//		});
//		
//		arguments = new LinkedHashMap<>();
//		arguments.put("target", new PlayerArgument());
//		arguments.put("message", new TextArgument());
//		CommandAPI.getInstance().register("custmsg3", arguments, (sender, args) -> {
//			((Player) args[0]).sendMessage((String) args[1]);
//		});
//		
//		arguments = new LinkedHashMap<>();
//		arguments.put("literal", new LiteralArgument("group"));
//		arguments.put("group", new StringArgument());
//		arguments.put("users", new LiteralArgument("users"));
//		CommandAPI.getInstance().register("pex", arguments, (sender, args) -> {
//			sender.sendMessage("groups");
//		});
//		
//		arguments = new LinkedHashMap<>();
//		arguments.put("literal", new LiteralArgument("group"));
//		arguments.put("group", new StringArgument());
//		arguments.put("user", new LiteralArgument("user"));
//		arguments.put("add", new LiteralArgument("add"));
//		CommandAPI.getInstance().register("pex", arguments, (sender, args) -> {
//			sender.sendMessage("add user");
//		});
//		
//		arguments = new LinkedHashMap<>();
//		arguments.put("literal", new LiteralArgument("group"));
//		arguments.put("group", new StringArgument());
//		arguments.put("user", new LiteralArgument("user"));
//		arguments.put("rem", new LiteralArgument("rem"));
//		CommandAPI.getInstance().register("pex", arguments, (sender, args) -> {
//			sender.sendMessage("rem user");
//		});
		
		
//HashMap<String, GameMode> gamemodes = new HashMap<>();
//gamemodes.put("adventure", GameMode.ADVENTURE);
//gamemodes.put("creative", GameMode.CREATIVE);
//gamemodes.put("spectator", GameMode.SPECTATOR);
//gamemodes.put("survival", GameMode.SURVIVAL);
//
//for(String key : gamemodes.keySet()) {
//	LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
//	arguments.put(key, new LiteralArgument(key));
//	CommandAPI.getInstance().register("gamemode2", arguments, (sender, args) -> {
//	    if(sender instanceof Player) {
//	        Player player = (Player) sender;
//	        player.setGameMode(gamemodes.get(key));
//	    }
//	});
//}
		
//		if(TEST) {
//		
//			LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
//			arguments.put("player", new EntitySelectorArgument(EntitySelector.ONE_PLAYER));
//			CommandAPI.)getInstance().register("apioneplayer", arguments, (sender, args) -> {
//				((Player) args[0]).sendMessage("hello");
//			});
//			
//			arguments = new LinkedHashMap<>();
//			arguments.put("players", new EntitySelectorArgument(EntitySelector.MANY_PLAYERS));
//			CommandAPI.getInstance().register("apimanyplayers", arguments, (sender, args) -> {
//				((Collection<Player>) args[0]).forEach(p -> p.sendMessage("hi"));
//			});
//			
//			arguments = new LinkedHashMap<>();
//			arguments.put("entity", new EntitySelectorArgument(EntitySelector.ONE_ENTITY));
//			CommandAPI.getInstance().register("apioneentity", arguments, (sender, args) -> {
//				((Entity) args[0]).remove();
//			});
//			
//			arguments = new LinkedHashMap<>();
//			arguments.put("entities", new EntitySelectorArgument(EntitySelector.MANY_ENTITIES));
//			CommandAPI.getInstance().register("apimanyentities", arguments, (sender, args) -> {
//				((Collection<Entity>) args[0]).forEach(e -> e.remove());
//			});
//		
//			arguments = new LinkedHashMap<>();
//			arguments.put("location", new LocationArgument());
//			CommandAPI.getInstance().register("setcustloc", arguments, (sender, args) -> {
//				Location loc = (Location) args[0];
//				loc.getBlock().setType(Material.GOLD_BLOCK);
//			});
//			
//			arguments = new LinkedHashMap<>();
//			CommandAPI.getInstance().register("noarg", arguments, (sender, args) -> {
//				Bukkit.broadcastMessage("woop");
//			});
//			
//			CommandAPI.getInstance().register("noarg2", null, (sender, args) -> {
//				Bukkit.broadcastMessage("woop2");
//			});
//			
//			CommandAPI.getInstance().register("custexec", null, (sender, args) -> {
//				System.out.println(sender.getClass());
//			});
//			
//			CommandAPI.getInstance().register("custkill", null, (sender, args) -> {
//				if(sender instanceof ProxiedCommandSender) {
//					CommandSender callee = ((ProxiedCommandSender) sender).getCallee();
//					if(callee instanceof LivingEntity) {
//						((LivingEntity) callee).setHealth(0);
//					}
//				}
//			});
//			
//			arguments = new LinkedHashMap<>();
//			arguments.put("gamemode", new LiteralArgument("survival"));
//			CommandAPI.getInstance().register("custgm", arguments, (sender, args) -> {
//				sender.sendMessage("set gamemode to " + args[0]);
//			});
//			
//			//For some arbitrary reason, this command takes all precedence over the first one...
//			arguments = new LinkedHashMap<>();
//			arguments.put("gamemode2", new LiteralArgument("creative"));
//			CommandAPI.getInstance().register("custgm", arguments, (sender, args) -> {
//				sender.sendMessage("set gamemode to " + args[0]);
//			});
//			
//			//derpy /item command
//			
//		
//			arguments = new LinkedHashMap<>();
//			arguments.put("item", new LiteralArgument("silver"));
//			arguments.put("amount", new IntegerArgument(0, 64));
//			CommandAPI.getInstance().register("custitem", arguments, (sender, args) -> {
//				sender.sendMessage("Gave you a silver");
//			});
//			
//			arguments = new LinkedHashMap<>();
//			arguments.put("amount", new IntegerArgument(0, 64));
//			arguments.put("item", new LiteralArgument("bronze"));
//			CommandAPI.getInstance().register("custitem", arguments, (sender, args) -> {
//				sender.sendMessage("Gave you a bronze");
//			});
//				
//			arguments = new LinkedHashMap<>();
//			arguments.put("item", new LiteralArgument("gold"));
//			CommandAPI.getInstance().register("custitem", arguments, (sender, args) -> {
//				sender.sendMessage("Gave you a gold");
//			});
//			
////			arguments = new LinkedHashMap<>();
////			arguments.put("item", new LiteralArgument("gold"));
////			arguments.put("item2", new LiteralArgument("silver"));
////			CommandAPI.getInstance().register("custitem2", arguments, (sender, args) -> {
////				sender.sendMessage("Executed successfully");
////			});
////			
////			arguments = new LinkedHashMap<>();
////			arguments.put("item", new LiteralArgument("gold"));
////			CommandAPI.getInstance().register("custitem2", arguments, (sender, args) -> {
////				sender.sendMessage("Executed successfully");
////			});
//			
//			arguments = new LinkedHashMap<>();
//			arguments.put("item", new LiteralArgument("silver"));
//			CommandAPI.getInstance().register("custitem2", arguments, (sender, args) -> {
//				sender.sendMessage("Executed successfully");
//			});
//			
//			arguments = new LinkedHashMap<>();
//			arguments.put("item", new LiteralArgument("myitem"));
//			arguments.put("amount", new IntegerArgument());
//			CommandAPI.getInstance().register("custitem3", arguments, (sender, args) -> {
//				sender.sendMessage("Executed successfully, with amount " + args[0]);
//			});
//			
//			arguments = new LinkedHashMap<>();
//			arguments.put("item", new LiteralArgument("a"));
//			arguments.put("item2", new LiteralArgument("b"));
//			CommandAPI.getInstance().register("custitem4", arguments, (sender, args) -> {
//				sender.sendMessage("Executed successfully");
//			});
//			
//			arguments = new LinkedHashMap<>();
//			arguments.put("item", new LiteralArgument("myitem"));
//			arguments.put("itemAmount", new IntegerArgument());
//			CommandAPI.getInstance().register("custitem5", arguments, (sender, args) -> {
//				System.out.println(args);
//				sender.sendMessage("Executed successfully, with amount " + args.toString());
//			});
//		}
	}
	
}
