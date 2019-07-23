# TODO

## Trailer design - things to showcase:

* Custom error messages?
* Integer boundaries
* Proxied senders
  * Say we have some command that makes an entity say something in chat, then blow up or something

```
/// Numbers ///

LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
arguments.put("number", new IntegerArgument(1, 10));

CommandAPI.getInstance().register("printnumber", arguments, (sender, args) -> {
Bukkit.broadcastMessage("Your number is: " + (int) args[0]);
});

/// Literals ///

arguments.clear();
arguments.put("choice", new LiteralArgument("yes"));
CommandAPI.getInstance().register("choice", arguments, (sender, args) -> {
Bukkit.broadcastMessage(ChatColor.GREEN + "YES!");
});

arguments.clear();
arguments.put("choice", new LiteralArgument("no"));
CommandAPI.getInstance().register("choice", arguments, (sender, args) -> {
Bukkit.broadcastMessage(ChatColor.RED + "NO!");
});

/// Execute as ///

arguments.clear();
CommandAPI.getInstance().register("selfdestruct", arguments, (sender, args) -> {
ProxiedCommandSender s = (ProxiedCommandSender) sender;
Entity e = (Entity) s.getCallee();
e.getWorld().createExplosion(e.getLocation(), 5.0f);
});

```

