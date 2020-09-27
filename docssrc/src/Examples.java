public class Examples {

/**
 * The list of all examples that are present in the CommandAPI's
 * documentation. The indentation SHOULD NOT be changed - any
 * indentation that appears in here will be reflected in the
 * documentation and that would look terrible!
 * 
 * To manage scope between each example, these should be encased
 * in curly braces {}.
 */
public static void examples() {

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
}

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
LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
arguments.put("block", new BlockStateArgument());

new CommandAPICommand("set")
    .withArguments(arguments)
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

}