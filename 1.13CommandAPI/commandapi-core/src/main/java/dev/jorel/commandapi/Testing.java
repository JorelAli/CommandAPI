package dev.jorel.commandapi;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Random;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.loot.LootContext;
import org.bukkit.loot.LootTable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.BiomeArgument;
import dev.jorel.commandapi.arguments.ChatArgument;
import dev.jorel.commandapi.arguments.ChatComponentArgument;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.arguments.LootTableArgument;
import dev.jorel.commandapi.arguments.ObjectiveArgument;
import dev.jorel.commandapi.arguments.ObjectiveCriteriaArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;
import dev.jorel.commandapi.arguments.ScoreHolderArgument;
import dev.jorel.commandapi.arguments.ScoreHolderArgument.ScoreHolderType;
import dev.jorel.commandapi.arguments.ScoreboardSlotArgument;
import dev.jorel.commandapi.arguments.TeamArgument;
import dev.jorel.commandapi.arguments.TextArgument;
import dev.jorel.commandapi.wrappers.ScoreboardSlot;
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
			        BaseComponent[] message = (BaseComponent[]) args[0];
			    
			        System.out.println(message);
			        //Broadcast the message to everyone on the server
			        Bukkit.getServer().spigot().broadcast(message);
			    })
			    .register();
		}
		
		{
			//LinkedHashMap to store arguments for the command
			LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();

			//Using a collective entity selector to select multiple entities
			arguments.put("biome", new BiomeArgument());

			new CommandAPICommand("sayBiome")
			    .withArguments(arguments)
			    .executes((sender, args) -> {
			    	Biome b = null;
			    	try {
			    		b = (Biome) args[0];
			    	} catch(Exception e) {
			    		System.out.println("Failed to cast to biome");
			    	}
			    	
			    	Bukkit.broadcastMessage(String.valueOf(b));
			    })
			    .register();
		}
		
		{
			LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
			arguments.put("literal", new LiteralArgument("hello"));
			arguments.put("text", new TextArgument());

			new CommandAPICommand("mycommand")
			    .withArguments(arguments)
			    .executes((sender, args) -> {
			        // This gives the variable "text" the contents of the TextArgument, and not the literal "hello"
			        String text = (String) args[0];
			        System.out.println(text);
			    })
			    .register();
		}
		
		{
			//Create a map of gamemode names to their respective objects
			HashMap<String, GameMode> gamemodes = new HashMap<>();
			gamemodes.put("adventure", GameMode.ADVENTURE);
			gamemodes.put("creative", GameMode.CREATIVE);
			gamemodes.put("spectator", GameMode.SPECTATOR);
			gamemodes.put("survival", GameMode.SURVIVAL);

			//Iterate over the map
			for(String key : gamemodes.keySet()) {
			    
			    //Create our arguments as usual, using the LiteralArgument for the name of the gamemode
			    LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
			    arguments.put(key, new LiteralArgument(key));
			    
			    //Register the command as usual
			    new CommandAPICommand("changegamemode")
			        .withArguments(arguments)
			        .executesPlayer((player, args) -> {
			            //Retrieve the object from the map via the key and NOT the args[]
			            player.setGameMode(gamemodes.get(key));
			        })
			        .register();
			}
		}
		
		{
			LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
			//We want multiple players, so we use ScoreHolderType.MULTIPLE in the constructor
			arguments.put("players", new ScoreHolderArgument(ScoreHolderType.MULTIPLE));

			new CommandAPICommand("reward")
			    .withArguments(arguments)
			    .executes((sender, args) -> {
			        //Get player names by casting to Collection<String>
			        Collection<String> players = (Collection<String>) args[0];
			        for(String playerName : players) {
			            Bukkit.getPlayer(playerName).getInventory().addItem(new ItemStack(Material.DIAMOND, 3));
			        }
			    })
			    .register();
		}
		
		{
			LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
			arguments.put("slot", new ScoreboardSlotArgument());

			new CommandAPICommand("clearobjectives")
			    .withArguments(arguments)
			    .executes((sender, args) -> {
			        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
			        DisplaySlot slot = ((ScoreboardSlot) args[0]).getDisplaySlot();
			        scoreboard.clearSlot(slot);
			    })
			    .register();
		}
		
		{
			LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
			arguments.put("objective", new ObjectiveArgument());

			new CommandAPICommand("sidebar")
			    .withArguments(arguments)
			    .executes((sender, args) -> {
			        //The ObjectArgument must be casted to a String
			        String objectiveName = (String) args[0];
			        
			        //An objective name can be turned into an Objective using getObjective(String)
			        Objective objective = Bukkit.getScoreboardManager().getMainScoreboard().getObjective(objectiveName);
			        
			        //Set display slot
			        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
			    })
			    .register();
		}
		
		{
			LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
			arguments.put("objective criteria", new ObjectiveCriteriaArgument());

			new CommandAPICommand("unregisterall")
			    .withArguments(arguments)
			    .executes((sender, args) -> {
			        String objectiveCriteria = (String) args[0];
			        Set<Objective> objectives = Bukkit.getScoreboardManager().getMainScoreboard().getObjectivesByCriteria(objectiveCriteria);
			        
			        //Unregister the objectives
			        for(Objective objective : objectives) {
			            objective.unregister();
			        }
			    })
			    .register();
		}
		
		{
			LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
			arguments.put("team", new TeamArgument());

			new CommandAPICommand("togglepvp")
			    .withArguments(arguments)
			    .executes((sender, args) -> {
			        //The TeamArgument must be casted to a String
			        String teamName = (String) args[0];
			        
			        //A team name can be turned into a Team using getTeam(String)
			        Team team = Bukkit.getScoreboardManager().getMainScoreboard().getTeam(teamName);
			        
			        //Toggle pvp
			        team.setAllowFriendlyFire(team.allowFriendlyFire());
			    })
			    .register();
		}
		
		{
			// Create our arguments
			LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
			arguments.put("message", new GreedyStringArgument());

			//Create our command
			new CommandAPICommand("broadcastmsg")
			    .withArguments(arguments)                     // The arguments
			    .withAliases("broadcast", "broadcastmessage") // Command aliases
			    .withPermission(CommandPermission.OP)         // Required permissions
			    .executes((sender, args) -> {
			        String message = (String) args[0];
			        Bukkit.getServer().broadcastMessage(message);
			    }).register();
		}
		
		{
			new CommandAPICommand("suicide")
		    .executesPlayer((player, args) -> {
		        player.setHealth(0);
		    }).register();
		}
		
		{
			new CommandAPICommand("killme")
		    .executesPlayer((player, args) -> {
		        player.setHealth(0);
		    })
		    .executesProxy((proxy, args) -> {
		        //Check if the callee is an Entity
		        if(proxy.getCallee() instanceof LivingEntity) {

		            //If so, kill the entity
		        	LivingEntity target = (LivingEntity) proxy.getCallee();
		            target.setHealth(0);
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
