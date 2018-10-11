package io.github.jorelali.commandapi;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

	//final static private boolean TEST = false;
	
	@Override
	public void onEnable() {
		//Nothing required here, just need
		//to state that this is a plugin so
		//other plugins can depend on it
		
//		if(TEST) {
//		
//			LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
//			arguments.put("player", new EntitySelectorArgument(EntitySelector.ONE_PLAYER));
//			CommandAPI.getInstance().register("apioneplayer", arguments, (sender, args) -> {
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
