package dev.jorel.commandapi;

import java.util.LinkedHashMap;
import java.util.Random;

import org.bukkit.entity.Player;
import org.bukkit.loot.LootContext;
import org.bukkit.loot.LootTable;

import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.LootTableArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;

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
				.executes((sender, args) -> {
					LootTable lootTable = (LootTable) args[0];
				    Player player = (Player) sender;
				    
				    LootContext context = /* Some generated LootContext relating to the lootTable*/
				    		new LootContext.Builder(player.getLocation()).build();
				    lootTable.fillInventory(player.getInventory(), new Random(), context);
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
