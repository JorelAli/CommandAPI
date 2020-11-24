import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.Container;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ComplexRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.loot.LootContext;
import org.bukkit.loot.LootTable;
import org.bukkit.loot.Lootable;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.EulerAngle;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;

import de.tr7zw.changeme.nbtapi.NBTContainer;
import dev.jorel.commandapi.Brigadier;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandPermission;
import dev.jorel.commandapi.Converter;
import dev.jorel.commandapi.IStringTooltip;
import dev.jorel.commandapi.StringTooltip;
import dev.jorel.commandapi.Tooltip;
import dev.jorel.commandapi.arguments.AdvancementArgument;
import dev.jorel.commandapi.arguments.AngleArgument;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.BiomeArgument;
import dev.jorel.commandapi.arguments.BlockPredicateArgument;
import dev.jorel.commandapi.arguments.BlockStateArgument;
import dev.jorel.commandapi.arguments.BooleanArgument;
import dev.jorel.commandapi.arguments.ChatArgument;
import dev.jorel.commandapi.arguments.ChatColorArgument;
import dev.jorel.commandapi.arguments.ChatComponentArgument;
import dev.jorel.commandapi.arguments.CustomArgument;
import dev.jorel.commandapi.arguments.CustomArgument.CustomArgumentException;
import dev.jorel.commandapi.arguments.CustomArgument.MessageBuilder;
import dev.jorel.commandapi.arguments.EnchantmentArgument;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.EntitySelectorArgument.EntitySelector;
import dev.jorel.commandapi.arguments.EntityTypeArgument;
import dev.jorel.commandapi.arguments.EnvironmentArgument;
import dev.jorel.commandapi.arguments.FunctionArgument;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.IntegerRangeArgument;
import dev.jorel.commandapi.arguments.ItemStackArgument;
import dev.jorel.commandapi.arguments.ItemStackPredicateArgument;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.arguments.LocationArgument;
import dev.jorel.commandapi.arguments.LocationType;
import dev.jorel.commandapi.arguments.LootTableArgument;
import dev.jorel.commandapi.arguments.MathOperationArgument;
import dev.jorel.commandapi.arguments.MultiLiteralArgument;
import dev.jorel.commandapi.arguments.NBTCompoundArgument;
import dev.jorel.commandapi.arguments.ObjectiveArgument;
import dev.jorel.commandapi.arguments.ObjectiveCriteriaArgument;
import dev.jorel.commandapi.arguments.ParticleArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;
import dev.jorel.commandapi.arguments.PotionEffectArgument;
import dev.jorel.commandapi.arguments.RecipeArgument;
import dev.jorel.commandapi.arguments.RotationArgument;
import dev.jorel.commandapi.arguments.ScoreHolderArgument;
import dev.jorel.commandapi.arguments.ScoreHolderArgument.ScoreHolderType;
import dev.jorel.commandapi.arguments.ScoreboardSlotArgument;
import dev.jorel.commandapi.arguments.SoundArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import dev.jorel.commandapi.arguments.TeamArgument;
import dev.jorel.commandapi.arguments.TextArgument;
import dev.jorel.commandapi.arguments.TimeArgument;
import dev.jorel.commandapi.wrappers.FunctionWrapper;
import dev.jorel.commandapi.wrappers.IntegerRange;
import dev.jorel.commandapi.wrappers.MathOperation;
import dev.jorel.commandapi.wrappers.Rotation;
import dev.jorel.commandapi.wrappers.ScoreboardSlot;
import net.md_5.bungee.api.chat.BaseComponent;

public class Examples extends JavaPlugin {

/**
 * The list of all examples that are present in the CommandAPI's
 * documentation. The indentation SHOULD NOT be changed - any
 * indentation that appears in here will be reflected in the
 * documentation and that would look terrible!
 * 
 * To manage scope between each example, these should be encased
 * in curly braces {}.
 */

{
/* ANCHOR: booleanargs */
// Load keys from config file
String[] configKeys = getConfig().getKeys(true).toArray(new String[0]);

// Create arguments with the config key and a boolean value to set it to
List<Argument> arguments = new ArrayList<>();
arguments.add(new TextArgument("config-key").overrideSuggestions(configKeys));
arguments.add(new BooleanArgument("value"));

// Register our command
new CommandAPICommand("editconfig")
    .withArguments(arguments)
    .executes((sender, args) -> {
        // Update the config with the boolean argument
        getConfig().set((String) args[0], (boolean) args[1]);
    })
    .register();
}
/* ANCHOR_END: booleanargs */

{
/* ANCHOR: rangedarguments */
// Declare our arguments for /searchrange <IntegerRange> <ItemStack>
List<Argument> arguments = new ArrayList<>();
arguments.add(new IntegerRangeArgument("range"));
arguments.add(new ItemStackArgument("item"));

new CommandAPICommand("searchrange")
    .withArguments(arguments)
    .executesPlayer((player, args) -> {
        // Retrieve the range from the arguments
        IntegerRange range = (IntegerRange) args[0];
        ItemStack itemStack = (ItemStack) args[1];

        // Store the locations of chests with certain items
        List<Location> locations = new ArrayList<>();

        // Iterate through all chunks, and then all tile entities within each chunk
        for(Chunk chunk : player.getWorld().getLoadedChunks()) {
            for(BlockState blockState : chunk.getTileEntities()) {

                // The distance between the block and the player
                int distance = (int) blockState.getLocation().distance(player.getLocation());

                // Check if the distance is within the specified range 
                if(range.isInRange(distance)) {

                    // Check if the tile entity is a chest
                    if(blockState instanceof Chest) {
                        Chest chest = (Chest) blockState;

                        // Check if the chest contains the item specified by the player
                        if(chest.getInventory().contains(itemStack.getType())) {
                            locations.add(chest.getLocation());
                        }
                    }
                }

            }
        }

        // Output the locations of the chests, or whether no chests were found
        if(locations.isEmpty()) {
            player.sendMessage("No chests were found");
        } else {
            player.sendMessage("Found " + locations.size() + " chests:");
            locations.forEach(location -> {
                player.sendMessage("  Found at: " 
                        + location.getX() + ", " 
                        + location.getY() + ", " 
                        + location.getZ());
            });
        }
    })
    .register();
/* ANCHOR_END: rangedarguments */
}

{
/* ANCHOR: greedystringarguments */
List<Argument> arguments = new ArrayList<>();
arguments.add(new PlayerArgument("target"));
arguments.add(new GreedyStringArgument("message"));

new CommandAPICommand("message")
    .withArguments(arguments)
    .executes((sender, args) -> {
        ((Player) args[0]).sendMessage((String) args[1]);
    })
    .register();
/* ANCHOR_END: greedystringarguments */
}

{
/* ANCHOR: locationarguments */
new CommandAPICommand("break")
    //We want to target blocks in particular, so use BLOCK_POSITION
    .withArguments(new LocationArgument("block", LocationType.BLOCK_POSITION))
    .executesPlayer((player, args) -> {
        ((Location) args[0]).getBlock().setType(Material.AIR);
    })
    .register();
/* ANCHOR_END: locationarguments */
}

{
/* ANCHOR: rotationarguments */
new CommandAPICommand("rotate")
    .withArguments(new RotationArgument("rotation"))
    .withArguments(new EntitySelectorArgument("target", EntitySelector.ONE_ENTITY))
    .executes((sender, args) -> {
        Rotation rotation = (Rotation) args[0];
        Entity target = (Entity) args[1];

        if(target instanceof ArmorStand) {
            ArmorStand a = (ArmorStand) target;
            a.setHeadPose(new EulerAngle(Math.toRadians(rotation.getPitch()), Math.toRadians(rotation.getYaw() - 90), 0));
        }
    })
    .register();
/* ANCHOR_END: rotationarguments */
}

{
/* ANCHOR: chatcolorarguments */
new CommandAPICommand("namecolor")
    .withArguments(new ChatColorArgument("chatcolor"))
    .executesPlayer((player, args) -> {
        ChatColor color = (ChatColor) args[0];
        player.setDisplayName(color + player.getName());
    })
    .register();
/* ANCHOR_END: chatcolorarguments */
}

{
/* ANCHOR: chatcomponentarguments */
new CommandAPICommand("makebook")
    .withArguments(new PlayerArgument("player"))
    .withArguments(new ChatComponentArgument("contents"))
    .executes((sender, args) -> {
        Player player = (Player) args[0];
        BaseComponent[] arr = (BaseComponent[]) args[1];
        
        //Create book
        ItemStack is = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) is.getItemMeta(); 
        meta.setTitle("Custom Book");
        meta.setAuthor(player.getName());
        meta.spigot().setPages(arr);
        is.setItemMeta(meta);
        
        //Give player the book
        player.getInventory().addItem(is);
    })
    .register();
/* ANCHOR_END: chatcomponentarguments */
}

{
/* ANCHOR: chatarguments */
new CommandAPICommand("pbroadcast")
    .withArguments(new ChatArgument("message"))
    .executes((sender, args) -> {
        BaseComponent[] message = (BaseComponent[]) args[0];
    
        //Broadcast the message to everyone on the server
        Bukkit.getServer().spigot().broadcast(message);
    })
    .register();
/* ANCHOR_END: chatarguments */
}

{
/* ANCHOR: entityselectorarguments */
new CommandAPICommand("remove")
    //Using a collective entity selector to select multiple entities
    .withArguments(new EntitySelectorArgument("entities", EntitySelector.MANY_ENTITIES))
    .executes((sender, args) -> {
        //Parse the argument as a collection of entities (as stated above in the documentation)
        @SuppressWarnings("unchecked")
        Collection<Entity> entities = (Collection<Entity>) args[0];
        
        sender.sendMessage("Removed " + entities.size() + " entities");
        for(Entity e : entities) {
            e.remove();
        }
    })
    .register();
/* ANCHOR_END: entityselectorarguments */
}

{
/* ANCHOR: entitytypearguments */
new CommandAPICommand("spawnmob")
    .withArguments(new EntityTypeArgument("entity"))
    .withArguments(new IntegerArgument("amount", 1, 100)) //Prevent spawning too many entities
    .executesPlayer((Player player, Object[] args) -> {
        for(int i = 0; i < (int) args[1]; i++) {
            player.getWorld().spawnEntity(player.getLocation(), (EntityType) args[0]);
        }
    })
    .register();
/* ANCHOR_END: entitytypearguments */
}

{
/* ANCHOR: scoreholderargument */
new CommandAPICommand("reward")
    //We want multiple players, so we use ScoreHolderType.MULTIPLE in the constructor
    .withArguments(new ScoreHolderArgument("players", ScoreHolderType.MULTIPLE))
    .executes((sender, args) -> {
        //Get player names by casting to Collection<String>
        @SuppressWarnings("unchecked")
        Collection<String> players = (Collection<String>) args[0];
        
        for(String playerName : players) {
            Bukkit.getPlayer(playerName).getInventory().addItem(new ItemStack(Material.DIAMOND, 3));
        }
    })
    .register();
/* ANCHOR_END: scoreholderargument */
}

{
/* ANCHOR: scoreboardslotargument */
new CommandAPICommand("clearobjectives")
    .withArguments(new ScoreboardSlotArgument("slot"))
    .executes((sender, args) -> {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        DisplaySlot slot = ((ScoreboardSlot) args[0]).getDisplaySlot();
        scoreboard.clearSlot(slot);
    })
    .register();
/* ANCHOR_END: scoreboardslotargument */
}

{
/* ANCHOR: objectiveargument */
new CommandAPICommand("sidebar")
    .withArguments(new ObjectiveArgument("objective"))
    .executes((sender, args) -> {
        //The ObjectArgument must be casted to a String
        String objectiveName = (String) args[0];
        
        //An objective name can be turned into an Objective using getObjective(String)
        Objective objective = Bukkit.getScoreboardManager().getMainScoreboard().getObjective(objectiveName);
        
        //Set display slot
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    })
    .register();
/* ANCHOR_END: objectiveargument */
}

{
/* ANCHOR: objectivecriteriaarguments */
new CommandAPICommand("unregisterall")
    .withArguments(new ObjectiveCriteriaArgument("objective criteria"))
    .executes((sender, args) -> {
        String objectiveCriteria = (String) args[0];
        Set<Objective> objectives = Bukkit.getScoreboardManager().getMainScoreboard().getObjectivesByCriteria(objectiveCriteria);
        
        //Unregister the objectives
        for(Objective objective : objectives) {
            objective.unregister();
        }
    })
    .register();
/* ANCHOR_END: objectivecriteriaarguments */
}

{
/* ANCHOR: teamarguments */
new CommandAPICommand("togglepvp")
    .withArguments(new TeamArgument("team"))
    .executes((sender, args) -> {
        //The TeamArgument must be casted to a String
        String teamName = (String) args[0];
        
        //A team name can be turned into a Team using getTeam(String)
        Team team = Bukkit.getScoreboardManager().getMainScoreboard().getTeam(teamName);
        
        //Toggle pvp
        team.setAllowFriendlyFire(team.allowFriendlyFire());
    })
    .register();
/* ANCHOR_END: teamarguments */
}

{
/* ANCHOR: advancementarguments */
new CommandAPICommand("award")
    .withArguments(new PlayerArgument("player"))
    .withArguments(new AdvancementArgument("advancement"))
    .executes((sender, args) -> {
        Player target = (Player) args[0];
        Advancement advancement = (Advancement) args[1];
        
        //Award all criteria for the advancement
        AdvancementProgress progress = target.getAdvancementProgress(advancement);
        for(String criteria : advancement.getCriteria()) {
            progress.awardCriteria(criteria);
        }
    })
    .register();
/* ANCHOR_END: advancementarguments */
}

{
/* ANCHOR: biomearguments */
new CommandAPICommand("setbiome")
	.withArguments(new BiomeArgument("biome"))
	.executesPlayer((player, args) -> {
		Biome biome = (Biome) args[0];

		Chunk chunk = player.getLocation().getChunk();
		player.getWorld().setBiome(chunk.getX(), player.getLocation().getBlockY(), chunk.getZ(), biome);
	})
	.register();
/* ANCHOR_END: biomearguments */
}

{
/* ANCHOR: blockstateargument */
new CommandAPICommand("set")
    .withArguments(new BlockStateArgument("block"))
    .executesPlayer((player, args) -> {
        BlockData blockdata = (BlockData) args[0];
        Block targetBlock = player.getTargetBlockExact(256);
        
        // Set the block, along with its data
        targetBlock.setType(blockdata.getMaterial());
        targetBlock.getState().setBlockData(blockdata);
    })
    .register();
/* ANCHOR_END: blockstateargument */
}

{
/* ANCHOR: enchantmentarguments */
new CommandAPICommand("enchantitem")
    .withArguments(new EnchantmentArgument("enchantment"))
    .withArguments(new IntegerArgument("level", 1, 5))
    .executesPlayer((player, args) -> {
        Enchantment enchantment = (Enchantment) args[0];
        int level = (int) args[1];
        
        //Add the enchantment
        player.getInventory().getItemInMainHand().addEnchantment(enchantment, level);
    })
    .register();
/* ANCHOR_END: enchantmentarguments */
}

{
/* ANCHOR: environmentarguments */
new CommandAPICommand("createworld")
    .withArguments(new StringArgument("worldname"))
    .withArguments(new EnvironmentArgument("type"))
    .executes((sender, args) -> {
        String worldName = (String) args[0];
        Environment environment = (Environment) args[1];

        // Create a new world with the specific world name and environment
        Bukkit.getServer().createWorld(new WorldCreator(worldName).environment(environment));
        sender.sendMessage("World created!");
    })
    .register();
/* ANCHOR_END: environmentarguments */
}

{
/* ANCHOR: itemstackarguments */
new CommandAPICommand("item")
    .withArguments(new ItemStackArgument("itemstack"))
    .executesPlayer((player, args) -> {
        player.getInventory().addItem((ItemStack) args[0]);
    })
    .register();
/* ANCHOR_END: itemstackarguments */
}

{
/* ANCHOR: loottablearguments */
new CommandAPICommand("giveloottable")
    .withArguments(new LootTableArgument("loottable"))
    .withArguments(new LocationArgument("location", LocationType.BLOCK_POSITION))
    .executes((sender, args) -> {
		LootTable lootTable = (LootTable) args[0];
		Location location = (Location) args[1];
		
		BlockState state = location.getBlock().getState();
		
		// Check if the input block is a container (e.g. chest)
		if(state instanceof Container && state instanceof Lootable) {
			Container container = (Container) state;
			Lootable lootable = (Lootable) container;
			
			// Apply the loot table to the chest
			lootable.setLootTable(lootTable);
			container.update();
		}
	})
    .register();
/* ANCHOR_END: loottablearguments */
}

{
/* ANCHOR: mathoperationarguments */
new CommandAPICommand("changelevel")
    .withArguments(new PlayerArgument("player"))
    .withArguments(new MathOperationArgument("operation"))
    .withArguments(new IntegerArgument("value"))
    .executes((sender, args) -> {
        Player target = (Player) args[0];
        MathOperation op = (MathOperation) args[1];
        int value = (int) args[2];

        target.setLevel(op.apply(target.getLevel(), value));
    })
    .register();
/* ANCHOR_END: mathoperationarguments */
}

{
/* ANCHOR: particlearguments */
new CommandAPICommand("showparticle")
    .withArguments(new ParticleArgument("particle"))
    .executesPlayer((player, args) -> {
        player.getWorld().spawnParticle((Particle) args[0], player.getLocation(), 1);
    })
    .register();
/* ANCHOR_END: particlearguments */
}

{
/* ANCHOR: potioneffectarguments */
new CommandAPICommand("potion")
    .withArguments(new PlayerArgument("target"))
    .withArguments(new PotionEffectArgument("potion"))
    .withArguments(new TimeArgument("duration"))
    .withArguments(new IntegerArgument("strength"))
    .executes((sender, args) -> {
        Player target = (Player) args[0];
        PotionEffectType potion = (PotionEffectType) args[1];
        int duration = (int) args[2];
        int strength = (int) args[3];
        
        //Add the potion effect to the target player
        target.addPotionEffect(new PotionEffect(potion, duration, strength));
    })
    .register();
/* ANCHOR_END: potioneffectarguments */
}

{
/* ANCHOR: recipearguments */
new CommandAPICommand("giverecipe")
    .withArguments(new RecipeArgument("recipe"))
    .executesPlayer((player, args) -> {
        Recipe recipe = (Recipe) args[0];
    	player.getInventory().addItem(recipe.getResult());
    })
    .register();
/* ANCHOR_END: recipearguments */
}

{
/* ANCHOR: recipearguments2 */
new CommandAPICommand("unlockrecipe")
    .withArguments(new PlayerArgument("player"))
    .withArguments(new RecipeArgument("recipe"))
    .executes((sender, args) -> {
        Player target = (Player) args[0];
        Recipe recipe = (Recipe) args[1];
		
        //Check if we're running 1.15+
        if(recipe instanceof ComplexRecipe) {
            ComplexRecipe complexRecipe = (ComplexRecipe) recipe;
            target.discoverRecipe(complexRecipe.getKey());
        } else {
            //Error here, can't unlock recipe for player
            CommandAPI.fail("Cannot unlock recipe for player (Are you using version 1.15 or above?)");
        }
    })
    .register();
/* ANCHOR_END: recipearguments2 */
}

{
/* ANCHOR: soundarguments */
new CommandAPICommand("sound")
    .withArguments(new SoundArgument("sound"))
    .executesPlayer((player, args) -> {
        player.getWorld().playSound(player.getLocation(), (Sound) args[0], 100.0f, 1.0f);
    })
    .register();
/* ANCHOR_END: soundarguments */
}

{
/* ANCHOR: timearguments */
new CommandAPICommand("bigmsg")
    .withArguments(new TimeArgument("duration"))
    .withArguments(new GreedyStringArgument("message"))
    .executes((sender, args) -> {
        //Duration in ticks
        int duration = (int) args[0];
        String message = (String) args[1];

        for(Player player : Bukkit.getOnlinePlayers()) {
            //Display the message to all players, with the default fade in/out times (10 and 20).
            player.sendTitle(message, "", 10, duration, 20);
        }
    })
    .register();
/* ANCHOR_END: timearguments */
}

{
/* ANCHOR: blockpredicatearguments */
Argument[] arguments = new Argument[] {
	new IntegerArgument("radius"),
	new BlockPredicateArgument("fromBlock"),
	new BlockStateArgument("toBlock"),
};
/* ANCHOR_END: blockpredicatearguments */

/* ANCHOR: blockpredicatearguments2 */
new CommandAPICommand("replace")
	.withArguments(arguments)
	.executesPlayer((player, args) -> {
	    
	    // Parse the arguments
	    int radius = (int) args[0];
	    @SuppressWarnings("unchecked")
	    Predicate<Block> predicate = (Predicate<Block>) args[1];
	    BlockData blockData = (BlockData) args[2];
	    
	    // Find a (solid) sphere of blocks around the player with a given radius
	    Location center = player.getLocation();
	    for (int Y = -radius; Y < radius; Y++) {
	        for (int X = -radius; X < radius; X++) {
	            for (int Z = -radius; Z < radius; Z++) {
	                if (Math.sqrt((X * X) + (Y * Y) + (Z * Z)) <= radius) {
	                    Block block = center.getWorld().getBlockAt(X + center.getBlockX(), Y + center.getBlockY(), Z + center.getBlockZ());
	                    
	                    // If that block matches a block from the predicate, set it
	                    if(predicate.test(block)) {
	                        block.setType(blockData.getMaterial());
	                        block.setBlockData(blockData);
	                    }
	                }
	            }
	        }
	    }
	    return;
	})
	.register();
/* ANCHOR_END: blockpredicatearguments2 */
}

{
/* ANCHOR: itemstackpredicatearguments */
// Register our command
new CommandAPICommand("rem")
	.withArguments(new ItemStackPredicateArgument("items"))
	.executesPlayer((player, args) -> {
	    
	    // Get our predicate
		@SuppressWarnings("unchecked")
	    Predicate<ItemStack> predicate = (Predicate<ItemStack>) args[0];
	    
	    for(ItemStack item : player.getInventory()) {
	        if(predicate.test(item)) {
	            player.getInventory().remove(item);
	        }
	    }
	})
	.register();
/* ANCHOR_END: itemstackpredicatearguments */
}

@SuppressWarnings("unused")
void b(){
/* ANCHOR: nbtcompoundarguments */
new CommandAPICommand("award")
    .withArguments(new NBTCompoundArgument("nbt"))
    .executes((sender, args) -> {
        NBTContainer nbt = (NBTContainer) args[0];
        
        //Do something with "nbt" here...
    })
    .register();
/* ANCHOR_END: nbtcompoundarguments */
}

@SuppressWarnings("unused")
void c(){
/* ANCHOR: literalarguments */
new CommandAPICommand("mycommand")
    .withArguments(new LiteralArgument("hello"))
    .withArguments(new TextArgument("text"))
    .executes((sender, args) -> {
        // This gives the variable "text" the contents of the TextArgument, and not the literal "hello"
        String text = (String) args[0];
    })
    .register();
/* ANCHOR_END: literalarguments */
}

{
/* ANCHOR: literalarguments2 */
//Create a map of gamemode names to their respective objects
HashMap<String, GameMode> gamemodes = new HashMap<>();
gamemodes.put("adventure", GameMode.ADVENTURE);
gamemodes.put("creative", GameMode.CREATIVE);
gamemodes.put("spectator", GameMode.SPECTATOR);
gamemodes.put("survival", GameMode.SURVIVAL);

//Iterate over the map
for(String key : gamemodes.keySet()) {
    
    //Register the command as usual
    new CommandAPICommand("changegamemode")
        .withArguments(new LiteralArgument(key))
        .executesPlayer((player, args) -> {
            //Retrieve the object from the map via the key and NOT the args[]
            player.setGameMode(gamemodes.get(key));
        })
        .register();
}	
/* ANCHOR_END: literalarguments2 */
}

{
/* ANCHOR: multiliteralarguments */
new CommandAPICommand("gamemode")
    .withArguments(new MultiLiteralArgument("adventure", "creative", "spectator", "survival"))
    .executesPlayer((player, args) -> {
        // The literal string that the player enters IS available in the args[]
        switch((String) args[0]) {
            case "adventure":
                player.setGameMode(GameMode.ADVENTURE);
                break;
            case "creative":
                player.setGameMode(GameMode.CREATIVE);
                break;
            case "spectator":
                player.setGameMode(GameMode.SPECTATOR);
                break;
            case "survival":
                player.setGameMode(GameMode.SURVIVAL);
                break;
        }
    }) 
    .register();
/* ANCHOR_END: multiliteralarguments */
}

{
/* ANCHOR: customarguments */
new CommandAPICommand("tpworld")
    .withArguments(worldArgument("world"))
    .executesPlayer((player, args) -> {
        player.teleport(((World) args[0]).getSpawnLocation());
    })
    .register();
/* ANCHOR_END: customarguments */
}

/* ANCHOR: customarguments2 */
//Function that returns our custom argument
public Argument worldArgument(String nodeName) {
	
	//Construct our CustomArgument that takes in a String input and returns a World object
	return new CustomArgument<World>(nodeName, (input) -> {
	    //Parse the world from our input
	    World world = Bukkit.getWorld(input);
	
	    if(world == null) {
	        throw new CustomArgumentException(new MessageBuilder("Unknown world: ").appendArgInput());
	    } else {
	        return world;
	    }
	}).overrideSuggestions(sender -> {
		//List of worlds on the server, as Strings. We use overrideSuggestions(sender -> ...)
		//since this evaluates the list of worlds when the player types the command as opposed
		//to when the plugin starts up
		return Bukkit.getWorlds().stream().map(World::getName).toArray(String[]::new);
	});
}
/* ANCHOR_END: customarguments2 */

{
/* ANCHOR: functionarguments */
new CommandAPICommand("runfunc")
    .withArguments(new FunctionArgument("function"))
	.executes((sender, args) -> {
        FunctionWrapper[] functions = (FunctionWrapper[]) args[0];
        for(FunctionWrapper function : functions) {
            function.run(); // The command executor in this case is 'sender'
        }
    })
    .register();
/* ANCHOR_END: functionarguments */
}

{
/* ANCHOR: functionarguments2 */
new CommandAPICommand("runfunction")
    .withArguments(new FunctionArgument("function"))
    .executes((sender, args) -> {
        FunctionWrapper[] functions = (FunctionWrapper[]) args[0];

        //Run all functions in our FunctionWrapper[]
        for(FunctionWrapper function : functions) {
            function.run();
        }
    })
    .register();
/* ANCHOR_END: functionarguments2 */
}

{
/* ANCHOR: permissions */
// Register the /god command with the permission node "command.god"
new CommandAPICommand("god")
    .withPermission(CommandPermission.fromString("command.god"))
    .executesPlayer((player, args) -> {
        player.setInvulnerable(true);
    })
    .register();
/* ANCHOR_END: permissions */

/* ANCHOR: permissions2 */
//Register the /god command with the permission node "command.god", without creating a CommandPermission
new CommandAPICommand("god")
    .withPermission("command.god")
    .executesPlayer((player, args) -> {
        player.setInvulnerable(true);
    })
    .register();
/* ANCHOR_END: permissions2 */
}

{
/* ANCHOR: permissions2 */
// Register /kill command normally. Since no permissions are applied, anyone can run this command
new CommandAPICommand("kill")
    .executesPlayer((player, args) -> {
        player.setHealth(0);
    })
    .register();
/* ANCHOR_END: permissions2 */
}

{
/* ANCHOR: permissions3 */
// Adds the OP permission to the "target" argument. The sender requires OP to execute /kill <target>
new CommandAPICommand("kill")
    .withArguments(new PlayerArgument("target").withPermission(CommandPermission.OP))
    .executesPlayer((player, args) -> {
        ((Player) args[0]).setHealth(0);
    })
    .register();
/* ANCHOR_END: permissions3 */
}

{
/* ANCHOR: aliases */
new CommandAPICommand("getpos")
	// Declare your aliases
	.withAliases("getposition", "getloc", "getlocation", "whereami")
	  
	    //Declare your implementation
	.executesEntity((entity, args) -> {
	    entity.sendMessage(String.format("You are at %d, %d, %d", 
	        entity.getLocation().getBlockX(), 
	        entity.getLocation().getBlockY(), 
	        entity.getLocation().getBlockZ())
	    );
	})
	.executesCommandBlock((block, args) -> {
	    block.sendMessage(String.format("You are at %d, %d, %d", 
	            block.getBlock().getLocation().getBlockX(), 
	            block.getBlock().getLocation().getBlockY(), 
	            block.getBlock().getLocation().getBlockZ())
	        );
	    })
	  
	    //Register the command
	.register();
/* ANCHOR_END: aliases */
}

{
/* ANCHOR: normalcommandexecutors */
new CommandAPICommand("suicide")
	.executesPlayer((player, args) -> {
	    player.setHealth(0);
	})
	.register();
/* ANCHOR_END: normalcommandexecutors */
}

{
/* ANCHOR: normalcommandexecutors2 */
new CommandAPICommand("suicide")
    .executesPlayer((player, args) -> {
        player.setHealth(0);
    })
    .executesEntity((entity, args) -> {
        entity.getWorld().createExplosion(entity.getLocation(), 4);
        entity.remove();
    })
    .register();
/* ANCHOR_END: normalcommandexecutors2 */
}

{
/* ANCHOR: normalcommandexecutors3 */
//Create our command
new CommandAPICommand("broadcastmsg")
    .withArguments(new GreedyStringArgument("message")) // The arguments
    .withAliases("broadcast", "broadcastmessage")       // Command aliases
    .withPermission(CommandPermission.OP)               // Required permissions
    .executes((sender, args) -> {
        String message = (String) args[0];
        Bukkit.getServer().broadcastMessage(message);
    })
    .register();
/* ANCHOR_END: normalcommandexecutors3 */
}

{
/* ANCHOR: proxysender */
new CommandAPICommand("killme")
    .executesPlayer((player, args) -> {
        player.setHealth(0);
    })
    .register();
/* ANCHOR_END: proxysender */
}

{
/* ANCHOR: proxysender2 */
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
/* ANCHOR_END: proxysender2 */
}

{
/* ANCHOR: nativesender */
new CommandAPICommand("break")
    .executesNative((sender, args) -> {
        Location location = (Location) sender.getLocation();
        if(location != null) {
            location.getBlock().breakNaturally();
        }
    })
    .register();
/* ANCHOR_END: nativesender */
}

{
/* ANCHOR: resultingcommandexecutor */
new CommandAPICommand("randnum")
    .executes((sender, args) -> {
        return new Random().nextInt();
    })
    .register();
/* ANCHOR_END: resultingcommandexecutor */
}

{
/* ANCHOR: resultingcommandexecutor2 */
//Register random number generator command from 1 to 99 (inclusive)
new CommandAPICommand("randomnumber")
    .executes((sender, args) -> {
        return ThreadLocalRandom.current().nextInt(1, 100); //Returns random number from 1 <= x < 100
    })
    .register();
/* ANCHOR_END: resultingcommandexecutor2 */
}

{
/* ANCHOR: resultingcommandexecutor3 */
//Register reward giving system for a target player
new CommandAPICommand("givereward")
    .withArguments(new EntitySelectorArgument("target", EntitySelector.ONE_PLAYER))
    .executes((sender, args) -> {
        Player player = (Player) args[0];
        player.getInventory().addItem(new ItemStack(Material.DIAMOND, 64));
        Bukkit.broadcastMessage(player.getName() + " won a rare 64 diamonds from a loot box!");
    })
    .register();
/* ANCHOR_END: resultingcommandexecutor3 */
}

{
/* ANCHOR: commandfailures */
//Array of fruit
String[] fruit = new String[] {"banana", "apple", "orange"};

//Argument accepting a String, suggested with the list of fruit
List<Argument> arguments = new ArrayList<>();
arguments.add(new StringArgument("item").overrideSuggestions(fruit));

//Register the command
new CommandAPICommand("getfruit")
    .withArguments(arguments)
    .executes((sender, args) -> {
        String inputFruit = (String) args[0];
        
        if(Arrays.stream(fruit).anyMatch(inputFruit::equals)) {
            //Do something with inputFruit
        } else {
            //The player's input is not in the list of fruit
            CommandAPI.fail("That fruit doesn't exist!");
        }
    })
    .register();
/* ANCHOR_END: commandfailures */
}

{
/* ANCHOR: requirements */
new CommandAPICommand("repair")
    .withRequirement(sender -> ((Player) sender).getLevel() >= 30)
    .executesPlayer((player, args) -> {
        
        //Repair the item back to full durability
        ItemStack is = player.getInventory().getItemInMainHand();
        ItemMeta itemMeta = is.getItemMeta();
        if(itemMeta instanceof Damageable) {
            ((Damageable) itemMeta).setDamage(0);
            is.setItemMeta(itemMeta);
        }
        
        //Subtract 30 levels
        player.setLevel(player.getLevel() - 30);
    })
    .register();
/* ANCHOR_END: requirements */
}

{
/* ANCHOR: requirementsmap */
Map<UUID, String> partyMembers = new HashMap<>();
/* ANCHOR_END: requirementsmap */

/* ANCHOR: requirements2 */
List<Argument> arguments = new ArrayList<>();

// The "create" literal, with a requirement that a player must have a party
arguments.add(new LiteralArgument("create")
	.withRequirement(sender -> {
		
		return !partyMembers.containsKey(((Player) sender).getUniqueId());
		
	}));

arguments.add(new StringArgument("partyName"));
/* ANCHOR_END: requirements2 */

/* ANCHOR: requirements3 */
new CommandAPICommand("party")
	.withArguments(arguments)
	.executesPlayer((player, args) -> {
		
		//Get the name of the party to create
		String partyName = (String) args[0];
		
		partyMembers.put(player.getUniqueId(), partyName);
	})
	.register();
/* ANCHOR_END: requirements3 */

/* ANCHOR: requirementstp */
/* ANCHOR: requirements4 */
arguments = new ArrayList<>();
arguments.add(new LiteralArgument("tp")
	.withRequirement(sender -> {
		
		return partyMembers.containsKey(((Player) sender).getUniqueId());
        
	}));
/* ANCHOR_END: requirementstp */

arguments.add(new PlayerArgument("player")
	.safeOverrideSuggestions((sender) -> {
		
		//Store the list of party members to teleport to
		List<Player> playersToTeleportTo = new ArrayList<>();
		
		String partyName = partyMembers.get(((Player) sender).getUniqueId());
		
		//Find the party members
		for(UUID uuid : partyMembers.keySet()) {
			
			//Ignore yourself
			if(uuid.equals(((Player) sender).getUniqueId())) {
				continue;
			} else {
				//If the party member is in the same party as you
				if(partyMembers.get(uuid).equals(partyName)) {
					Player target = Bukkit.getPlayer(uuid);
					if(target.isOnline()) {
						//Add them if they are online
						playersToTeleportTo.add(target);
					}
				}
			}
		}
		
		return playersToTeleportTo.toArray(new Player[0]);
	}));
/* ANCHOR_END: requirements4 */

/* ANCHOR: requirements5 */
new CommandAPICommand("party")
	.withArguments(arguments)
	.executesPlayer((player, args) -> {
		Player target = (Player) args[0];
		player.teleport(target);
	})
	.register();
/* ANCHOR_END: requirements5 */

/* ANCHOR: updatingrequirements */
new CommandAPICommand("party")
	.withArguments(arguments)
	.executesPlayer((player, args) -> {
		
		//Get the name of the party to create
		String partyName = (String) args[0];
		
		partyMembers.put(player.getUniqueId(), partyName);
	    
	    CommandAPI.updateRequirements(player);
	})
	.register();
/* ANCHOR_END: updatingrequirements */
}

{
/* ANCHOR: multiplerequirements */
new CommandAPICommand("someCommand")
	.withRequirement(sender -> ((Player) sender).getLevel() >= 30)
	.withRequirement(sender -> ((Player) sender).getInventory().contains(Material.DIAMOND_PICKAXE))
	.withRequirement(sender -> ((Player) sender).isInvulnerable())
	.executesPlayer((player, args) -> {
		//Code goes here
	})
	.register();
/* ANCHOR_END: multiplerequirements */
}

{
Map<UUID, String> partyMembers = new HashMap<>();
/* ANCHOR: predicatetips */
Predicate<CommandSender> testIfPlayerHasParty = sender -> {
    return partyMembers.containsKey(((Player) sender).getUniqueId());
};
/* ANCHOR_END: predicatetips */

/* ANCHOR: predicatetips2 */
List<Argument> arguments = new ArrayList<>();
arguments.add(new LiteralArgument("create").withRequirement(testIfPlayerHasParty.negate()));
arguments.add(new StringArgument("partyName"));
/* ANCHOR_END: predicatetips2 */

/* ANCHOR: predicatetips3 */
arguments = new ArrayList<>();
arguments.add(new LiteralArgument("tp").withRequirement(testIfPlayerHasParty));
/* ANCHOR_END: predicatetips3 */
}

{
/* ANCHOR: converter2 */
Plugin essentials = Bukkit.getPluginManager().getPlugin("Essentials");

// /speed <speed>
Converter.convert(essentials, "speed", new IntegerArgument("speed", 0, 10));

// /speed <target>
Converter.convert(essentials, "speed", new PlayerArgument("target"));

// /speed <walk/fly> <speed>
Converter.convert(essentials, "speed", 
	new MultiLiteralArgument("walk", "fly"), 
	new IntegerArgument("speed", 0, 10)
	);

// /speed <walk/fly> <speed> <target>
Converter.convert(essentials, "speed", 
	new MultiLiteralArgument("walk", "fly"), 
	new IntegerArgument("speed", 0, 10), 
	new PlayerArgument("target")
	);
/* ANCHOR_END: converter2 */
}

@SuppressWarnings({ "rawtypes", "unchecked" })
void a(){
/* ANCHOR: brigadier */
/* ANCHOR: declareliteral */
//Register literal "randomchance"
LiteralCommandNode randomChance = Brigadier.fromLiteralArgument(new LiteralArgument("randomchance")).build();
/* ANCHOR_END: declareliteral */

/* ANCHOR: declarearguments */
//Declare arguments like normal
List<Argument> arguments = new ArrayList<>();
arguments.add(new IntegerArgument("numerator", 0));
arguments.add(new IntegerArgument("denominator", 1));
/* ANCHOR_END: declarearguments */

//Get brigadier argument objects
/* ANCHOR: declareargumentbuilders */
ArgumentBuilder numerator = Brigadier.fromArgument(arguments, "numerator");
/* ANCHOR: declarefork */
ArgumentBuilder denominator = Brigadier.fromArgument(arguments, "denominator")
/* ANCHOR_END: declareargumentbuilders */
    //Fork redirecting to "execute" and state our predicate
    .fork(Brigadier.getRootNode().getChild("execute"), Brigadier.fromPredicate((sender, args) -> {
        //Parse arguments like normal
        int num = (int) args[0];
        int denom = (int) args[1];
        
        //Return boolean with a num/denom chance
        return Math.ceil(Math.random() * (double) denom) <= (double) num;
    }, arguments));
/* ANCHOR_END: declarefork */

/* ANCHOR: declarerandomchance */
//Add <numerator> <denominator> as a child of randomchance
randomChance.addChild(numerator.then(denominator).build());
/* ANCHOR_END: declarerandomchance */

/* ANCHOR: injectintoroot */
//Add (randomchance <numerator> <denominator>) as a child of (execute -> if)
Brigadier.getRootNode().getChild("execute").getChild("if").addChild(randomChance);
/* ANCHOR_END: injectintoroot */
/* ANCHOR_END: brigadier */
}

{

/* ANCHOR: subcommandspart */
CommandAPICommand groupAdd = new CommandAPICommand("add")
	.withArguments(new StringArgument("permission"))
	.withArguments(new StringArgument("groupName"))
	.executes((sender, args) -> {
	    //perm group add code
	});
/* ANCHOR_END: subcommandspart */
/* ANCHOR: subcommands */
CommandAPICommand groupRemove = new CommandAPICommand("remove")
	.withArguments(new StringArgument("permission"))
	.withArguments(new StringArgument("groupName"))
	.executes((sender, args) -> {
	    //perm group remove code
	});

CommandAPICommand group = new CommandAPICommand("group")
	.withSubcommand(groupAdd)
	.withSubcommand(groupRemove);
/* ANCHOR_END: subcommands */
/* ANCHOR: subcommandsend */
new CommandAPICommand("perm")
    .withSubcommand(group)
    .register();
/* ANCHOR_END: subcommandsend */
/* ANCHOR: subcommands1 */
new CommandAPICommand("perm")
    .withSubcommand(new CommandAPICommand("group")
        .withSubcommand(new CommandAPICommand("add")
            .withArguments(new StringArgument("permission"))
            .withArguments(new StringArgument("groupName"))
            .executes((sender, args) -> {
                //perm group add code
            })
        )
        .withSubcommand(new CommandAPICommand("remove")
            .withArguments(new StringArgument("permission"))
            .withArguments(new StringArgument("groupName"))
            .executes((sender, args) -> {
                //perm group remove code
            })
        )
    )
    .withSubcommand(new CommandAPICommand("user")
        .withSubcommand(new CommandAPICommand("add")
            .withArguments(new StringArgument("permission"))
            .withArguments(new StringArgument("userName"))
            .executes((sender, args) -> {
                //perm user add code
            })
        )
        .withSubcommand(new CommandAPICommand("remove")
            .withArguments(new StringArgument("permission"))
            .withArguments(new StringArgument("userName"))
            .executes((sender, args) -> {
                //perm user remove code
            })
        )
    )
    .register();
/* ANCHOR_END: subcommands1 */
}

{
	//NOTE: This example isn't used!
/* ANCHOR: anglearguments */
new CommandAPICommand("yaw")
	.withArguments(new AngleArgument("amount"))
	.executesPlayer((player, args) -> {
		Location newLocation = player.getLocation();
		newLocation.setYaw((float) args[0]);
		player.teleport(newLocation);
	})
	.register();
/* ANCHOR_END: anglearguments */
}

{
/* ANCHOR: listed */
new CommandAPICommand("mycommand")
    .withArguments(new PlayerArgument("player"))
    .withArguments(new IntegerArgument("value").setListed(false))
    .withArguments(new GreedyStringArgument("message"))
    .executes((sender, args) -> {
    	// args == [player, message]
    	Player player = (Player) args[0];
    	String message = (String) args[1]; //Note that this is args[1] and NOT args[2]
        player.sendMessage(message);
    })
    .register();
/* ANCHOR_END: listed */
}

{
/* ANCHOR: Tooltips1 */
List<Argument> arguments = new ArrayList<>();
arguments.add(new StringArgument("emote")
	.overrideSuggestionsT( 
        StringTooltip.of("wave", "Waves at a player"),
        StringTooltip.of("hug", "Gives a player a hug"),
        StringTooltip.of("glare", "Gives a player the death glare")
	)
);
arguments.add(new PlayerArgument("target"));
/* ANCHOR_END: Tooltips1 */
/* ANCHOR: Tooltips2 */
new CommandAPICommand("emote")
	.withArguments(arguments)
	.executesPlayer((player, args) -> {
		String emote = (String) args[0];
		Player target = (Player) args[1];
		
		switch(emote) {
		case "wave":
			target.sendMessage(player.getName() + " waves at you!");
			break;
		case "hug":
			target.sendMessage(player.getName() + " hugs you!");
			break;
		case "glare":
			target.sendMessage(player.getName() + " gives you the death glare...");
			break;
		}
	})
	.register();
/* ANCHOR_END: Tooltips2 */
}

{
/* ANCHOR: Tooltips4 */
CustomItem[] customItems = new CustomItem[] {
	new CustomItem(new ItemStack(Material.DIAMOND_SWORD), "God sword", "A sword from the heavens"),
	new CustomItem(new ItemStack(Material.PUMPKIN_PIE), "Sweet pie", "Just like grandma used to make")
};  //
	
new CommandAPICommand("giveitem")
	.withArguments(new StringArgument("item").overrideSuggestionsT(customItems)) // We use customItems[] as the input for our suggestions with tooltips
	.executesPlayer((player, args) -> {
		String itemName = (String) args[0];
		
		//Give them the item
		for(CustomItem item : customItems) {
			if(item.getName().equals(itemName)) {
				player.getInventory().addItem(item.getItem());
			}
		}
	})
	.register();
/* ANCHOR_END: Tooltips4 */
}

{
/* ANCHOR: SafeTooltips */
List<Argument> arguments = new ArrayList<>();
arguments.add(new LocationArgument("location")
    .safeOverrideSuggestionsT((sender) -> {
        return Tooltip.arrayOf(
            Tooltip.of(((Player) sender).getWorld().getSpawnLocation(), "World spawn"),
            Tooltip.of(((Player) sender).getBedSpawnLocation(), "Your bed"),
            Tooltip.of(((Player) sender).getTargetBlockExact(256).getLocation(), "Target block")
        );
    }));
/* ANCHOR_END: SafeTooltips */
/* ANCHOR: SafeTooltips2 */
new CommandAPICommand("warp")
	.withArguments(arguments)
	.executesPlayer((player, args) -> {
	    player.teleport((Location) args[0]);
	})
	.register();
/* ANCHOR_END: SafeTooltips2 */
}

{
/* ANCHOR: ArgumentSuggestionsPrevious */
// Declare our arguments as normal
List<Argument> arguments = new ArrayList<>();
arguments.add(new IntegerArgument("radius"));

// Override the suggestions for the PlayerArgument, using (sender, args) as the parameters
// sender refers to the command sender that is running this command
// args refers to the Object[] of PREVIOUSLY DECLARED arguments (in this case, the IntegerArgument radius)
arguments.add(new PlayerArgument("target").overrideSuggestions((sender, args) -> {

    // Cast the first argument (radius, which is an IntegerArgument) to get its value
	int radius = (int) args[0];
	
    // Get nearby entities within the provided radius
	Player player = (Player) sender;
	Collection<Entity> entities = player.getWorld().getNearbyEntities(player.getLocation(), radius, radius, radius);
	
    // Get player names within that radius
	return entities.stream()
		.filter(e -> e.getType() == EntityType.PLAYER)
		.map(Entity::getName)
		.toArray(String[]::new);
}));
arguments.add(new GreedyStringArgument("message"));

// Declare our command as normal
new CommandAPICommand("localmsg")
	.withArguments(arguments)
	.executesPlayer((player, args) -> {
		Player target = (Player) args[1];
		String message = (String) args[2];
		target.sendMessage(message);
	})
	.register();
/* ANCHOR_END: ArgumentSuggestionsPrevious */
}

{
/* ANCHOR: ArgumentSuggestions2_2 */
List<Argument> arguments = new ArrayList<>();
arguments.add(new PlayerArgument("friend").overrideSuggestions((sender) -> {
    return Friends.getFriends(sender);
}));

new CommandAPICommand("friendtp")
    .withArguments(arguments)
    .executesPlayer((player, args) -> {
       	Player target = (Player) args[0];
		player.teleport(target);
    })
    .register();
/* ANCHOR_END: ArgumentSuggestions2_2 */
}

{
Map<String, Location> warps = new HashMap<>();
/* ANCHOR: ArgumentSuggestions1 */
List<Argument> arguments = new ArrayList<>();
arguments.add(new StringArgument("world").overrideSuggestions("northland", "eastland", "southland", "westland"));

new CommandAPICommand("warp")
    .withArguments(arguments)
    .executesPlayer((player, args) -> {
       	String warp = (String) args[0];
		player.teleport(warps.get(warp)); // Look up the warp in a map, for example
    })
    .register();
/* ANCHOR_END: ArgumentSuggestions1 */
}



} // Examples class end ////////////////////////////////////////////////////////////////////



/* ANCHOR: ArgumentSuggestions2_1 */
class Friends {
	
	static Map<UUID, String[]> friends /* = ... */;
	
    public static String[] getFriends(CommandSender sender) {
        if(sender instanceof Player) {
        	//Look up friends in a database or file
            return friends.get(((Player) sender).getUniqueId());
        } else {
            return new String[0];
        }
    }
}
/* ANCHOR_END: ArgumentSuggestions2_1 */


/* ANCHOR: Tooltips3 */
class CustomItem implements IStringTooltip {

	private ItemStack itemstack;
	private String name;
	
	public CustomItem(ItemStack itemstack, String name, String lore) {
		ItemMeta meta = itemstack.getItemMeta();
		meta.setDisplayName(name);
		meta.setLore(Arrays.asList(lore));
		itemstack.setItemMeta(meta);
		this.itemstack = itemstack;
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public ItemStack getItem() {
		return this.itemstack;
	}
	
	@Override
	public String getSuggestion() {
		return this.itemstack.getItemMeta().getDisplayName();
	}

	@Override
	public String getTooltip() {
		return this.itemstack.getItemMeta().getLore().get(0);
	}
	
}
/* ANCHOR_END: Tooltips3 */

/* ANCHOR: functionregistration */
class Main extends JavaPlugin {

    @Override
    public void onLoad() {
        //Commands which will be used in Minecraft functions are registered here

        new CommandAPICommand("killall")
            .executes((sender, args) -> {
                //Kills all enemies in all worlds
                Bukkit.getWorlds().forEach(w -> w.getLivingEntities().forEach(e -> e.setHealth(0)));
        	})
            .register();
    }
    
    @Override
    public void onEnable() {
        //Register all other commands here
    } 
}
/* ANCHOR_END: functionregistration */

/* ANCHOR: shading */
class MyPlugin extends JavaPlugin {

    @Override
    public void onLoad() {
        CommandAPI.onLoad(true); //Load with verbose output
        
        new CommandAPICommand("ping")
            .executes((sender, args) -> {
                sender.sendMessage("pong!");
            })
            .register();
    }
    
    @Override
    public void onEnable() {
        CommandAPI.onEnable(this);
        
        //Register commands, listeners etc.
    }

}
/* ANCHOR_END: shading */

/* ANCHOR: converter */
class YourPlugin extends JavaPlugin {
    
    @Override
    public void onEnable() {
        Converter.convert(Bukkit.getPluginManager().getPlugin("TargetPlugin"));
        //Other code goes here...
    }
    
}
/* ANCHOR_END: converter */