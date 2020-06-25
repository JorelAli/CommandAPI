package dev.jorel.commandapi;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.loot.LootContext;
import org.bukkit.loot.LootTable;
import org.bukkit.util.EulerAngle;

import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.ChatArgument;
import dev.jorel.commandapi.arguments.ChatComponentArgument;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.EntitySelectorArgument.EntitySelector;
import dev.jorel.commandapi.arguments.LootTableArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;
import dev.jorel.commandapi.arguments.RotationArgument;
import dev.jorel.commandapi.wrappers.Rotation;
import net.md_5.bungee.api.chat.BaseComponent;

public class Testing {

	public static void registerTestCommands() {
		
		{
			// Register the /god command with the permission node "command.god"
			new CommandAPICommand("god").withPermission(CommandPermission.fromString("command.god"))
					.executesPlayer((player, args) -> {
						player.setInvulnerable(true);
					}).register();
	
			// Register /kill command normally. Since no permissions are applied, anyone can run this command
			new CommandAPICommand("kill").executesPlayer((player, args) -> {
				player.setHealth(0);
			}).register();
		}

		{
			LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
			arguments.put("target", new PlayerArgument().withPermission(CommandPermission.OP));
	
			//Adds the OP permission to the "target" argument. The sender requires OP to execute /kill <target>
			new CommandAPICommand("kill").withArguments(arguments).executesPlayer((player, args) -> {
				((Player) args[0]).setHealth(0);
			}).register();
		}
		
		{
			LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
			arguments.put("loottable", new LootTableArgument());

			new CommandAPICommand("giveloottable")
				.withArguments(arguments)
				.executesPlayer((player, args) -> {
					LootTable lootTable = (LootTable) args[0];
				    
				    LootContext context = /* Some generated LootContext relating to the lootTable*/
				    		new LootContext.Builder(player.getLocation()).build();
				    lootTable.fillInventory(player.getInventory(), new Random(), context);
				})
				.register();
		}
		
		{
			LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
			arguments.put("rotation", new RotationArgument());
			arguments.put("target", new EntitySelectorArgument(EntitySelector.ONE_ENTITY));

			new CommandAPICommand("rotate")
			    .withArguments(arguments)
			    .executes((sender, args) -> {
			        Rotation rotation = (Rotation) args[0];
			        System.out.println(rotation.getPitch());
			        System.out.println(rotation.getYaw());
			        
			        System.out.println(rotation.getNormalizedPitch());
			        System.out.println(rotation.getNormalizedYaw());
			        Entity target = (Entity) args[1];
			        
			        if(target instanceof ArmorStand) {
			        	ArmorStand a = (ArmorStand) target;
			        	a.setHeadPose(new EulerAngle(Math.toRadians(rotation.getPitch()), Math.toRadians(rotation.getYaw() - 90), 0));
			        }
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
			    })
			    .register();
		}
		
		{
			LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
			arguments.put("message", new ChatArgument());

			new CommandAPICommand("pbroadcast")
			    .withArguments(arguments)
			    .executes((sender, args) -> {
			        BaseComponent[] message = (BaseComponent[]) args[0];
			    
			        //Broadcast the message to everyone on the server
			        Bukkit.getServer().spigot().broadcast(message);
			    })
			    .register();
		}
		
		{
			//LinkedHashMap to store arguments for the command
			LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();

			//Using a collective entity selector to select multiple entities
			arguments.put("entities", new EntitySelectorArgument(EntitySelector.MANY_ENTITIES));

			new CommandAPICommand("kill2")
			    .withArguments(arguments)
			    .executes((sender, args) -> {
			        //Parse the argument as a collection of entities (as stated above in the documentation)
			        Collection<Entity> entities = (Collection<Entity>) args[0];
			        sender.sendMessage("killed " + entities.size() + "entities");
			        for(Entity e : entities) {
			            e.remove();
			        }
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
