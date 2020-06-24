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