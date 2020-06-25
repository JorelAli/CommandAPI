package dev.jorel.commandapi;

import java.util.LinkedHashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.loot.LootContext;
import org.bukkit.loot.LootTable;

import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.ChatArgument;
import dev.jorel.commandapi.arguments.ChatComponentArgument;
import dev.jorel.commandapi.arguments.LootTableArgument;
import net.md_5.bungee.api.chat.BaseComponent;

public class Testing {

	public static void registerTestCommands() {
		
		{
			LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
			arguments.put("loottable", new LootTableArgument());

			new CommandAPICommand("giveloottable")
				.withArguments(arguments)
				.executesPlayer((player, args) -> {
					LootTable lootTable = (LootTable) args[0];
				    
				    LootContext context = new LootContext.Builder(player.getLocation()).lootedEntity(player).build();
				    lootTable.fillInventory(player.getInventory(), new Random(), context);
				})
				.register();
		}
		
		{
			LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
			arguments.put("contents", new ChatComponentArgument());

			new CommandAPICommand("makebook")
			    .withArguments(arguments)
			    .executesPlayer((player, args) -> {
			        BaseComponent[] arr = (BaseComponent[]) args[0];
			        
			        //Create book
			        ItemStack is = new ItemStack(Material.WRITTEN_BOOK);
			        BookMeta meta = (BookMeta) is.getItemMeta(); 
			        meta.spigot().addPage(arr);
			        is.setItemMeta(meta);
			        
			        //Give player the book
			        player.getInventory().addItem(is);
			        player.spigot().sendMessage(arr);
			    })
			    .register();
		}
		
		{
			LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
			arguments.put("message", new ChatArgument());

			new CommandAPICommand("pbroadcast")
			    .withArguments(arguments)
			    .executes((sender, args) -> {
			    	try {
			    		System.out.println("init2");
				    	Object arg = args[0];
				    	System.out.println(arg);
				        BaseComponent[] message = (BaseComponent[]) args[0];
				        System.out.println(message);
				        System.out.println(BaseComponent.toPlainText(message));
				        Bukkit.getServer().spigot().broadcast(message);
			    	} catch(Exception e) {
			    		System.out.println("AAAA");
			    	}
			    	
			        
			        //Broadcast the message to everyone on the server
			    })
			    .register();
		}
		
		
		
//		LootTable lt = ((Lootable) mob).getLootTable();
//        LootContext.Builder bd = new LootContext.Builder(mob.getLocation());
//        Builder b = bd;
//        if(e.getDamager() instanceof Player) {
//            b = b.killer((HumanEntity) e.getDamager());
//        }
//        b = b.lootedEntity(mob);
//        LootContext lc = b.build();
//       
//        for(ItemStack i : lt.populateLoot(randor, lc)){
		
	}
}
