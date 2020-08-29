LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
//We want multiple players, so we use ScoreHolderType.MULTIPLE in the constructor
arguments.put("players", new ScoreHolderArgument(ScoreHolderType.MULTIPLE));

new CommandAPICommand("reward")
    .withArguments(arguments)
    .executes((sender, args) -> {
        //Get player names by casting to Collection<String>
        @SuppressWarnings("unchecked")
        Collection<String> players = (Collection<String>) args[0];
        
        for(String playerName : players) {
            Bukkit.getPlayer(playerName).getInventory().addItem(new ItemStack(Material.DIAMOND, 3));
        }
    })
    .register();