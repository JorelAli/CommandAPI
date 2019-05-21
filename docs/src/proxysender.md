# Proxied commandsenders

The CommandAPI has extra support for vanilla Minecraft's `/execute` command, by allowing the CommandSender to be an instance of the `ProxiedCommandSender` class. This allows the CommandSender to contain two extra pieces of information: The "proxied sender" and the original sender.

## Example - Running a command as a chicken

Say we have a command which kills the sender of a command. This is easily implemented as follows:

```java
CommandAPI.getInstance().register("killme", new LinkedHashMap<String, Argument>(), (sender, args) -> {
	if(sender instanceof Player) {
		Player player = (Player) sender;
		player.setHealth(0);
	}
});
```

But what if the sender of the command is _not_ a player? By using Minecraft's `/execute` command, we could execute the command as _any_ arbitrary entity, as shown with the command below:

```
/execute as @e[type=chicken] run killme
```

To handle this case, we can check if the sender is an instance of a `ProxiedCommandSender`, and kill the `callee` _(the entity which is being 'forced' to run the command /killme)_

```java
CommandAPI.getInstance().register("killme", new LinkedHashMap<String, Argument>(), (sender, args) -> {
	if(sender instanceof Player) {
		Player player = (Player) sender;
		player.setHealth(0);
	} else if(sender instanceof ProxiedCommandSender) {

		//Get the proxy CommandSender object from the CommandSender
		ProxiedCommandSender proxy = (ProxiedCommandSender) sender;

		//Check if the callee is an Entity
		if(proxy.getCallee() instanceof Entity) {

			//If so, kill the entity
			Entity target = (Entity) proxy.getCallee();
			entity.setHealth(0);
		}
	}
});

```

This allows the command above to run successfully, killing all chickens it can find.
