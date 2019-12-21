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