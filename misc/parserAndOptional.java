Argument worldsArgument = new StringArgument("world")
	.overrideSuggestions(sender -> {
		return Bukkit.getWorlds().stream().map(World::getName).toArray(String[]::new);
	})
	.withParser((fullInput, range) -> {
		int start = fullInput.indexOf(" ") + 1;
		String input = fullInput.substring(start);
		List<World> worlds = Bukkit.getWorlds();
		boolean found = false;
		for(World world : worlds) {
			String worldName = world.getName().toLowerCase();
			if(worldName.startsWith(input)) {
				found = true;
			}
		}
		if(!found) {
			throw new ParseException(ChatColor.RED + "Invalid world at position " + start + ": " + fullInput.substring(0, start) + " <--[HERE]");
		}
	});

new CommandAPICommand("mycommand")
	.withArguments(worldsArgument)
	.executes((sender, args) -> {
		String input = (String) args[0];
		Bukkit.broadcastMessage(input);
	})
	.register();

new CommandAPICommand("blah")
  .withArguments(new IntegerArgument("int"))
  .withOptionalArgument(new IntegerArgument("optional"), 10)
  .executes((sender, args) -> {
    int arg1 = (int) args[0];
    int arg2 = (int) args[1]; // This is 10 if not existant
    System.out.println("This is whatever:  " + arg1);
    System.out.println("This should be 10: " + arg2);
  })
  .register();