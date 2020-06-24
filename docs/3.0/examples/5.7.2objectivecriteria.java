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