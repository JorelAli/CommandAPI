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