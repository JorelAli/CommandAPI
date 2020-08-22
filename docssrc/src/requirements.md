# Requirements

Requirements is a feature that allows you to put a constraint on commands and arguments. Similar to permissions, a requirement is something that must be fulfilled in order to use a given command or argument.

This section is broken up into three parts:

- Adding requirements to commands
- Adding requirements to arguments
- Updating requirements
- Multiple requirements

**Please don't skip the section on updating requirements** - the last section is necessary to get requirements to work as you'd want!

-----

## Adding requirements to commands

To add a requirement to a command, similar to adding permissions to commands, use the `withRequirement` method:

```java
withRequirement(Predicate<CommandSender> sender);
```

The `withRequirement` method requires a predicate that determines if the sender is able to run the command - if the predicate is satisfied, then the command sender will be able to execute that command.

<div class="example">

### Example - Perks based on a player's level

Say we have a perks-based command system that depends on a player's level. For example, if a player has over 30 levels of experience, they would then be able to run a command that lets them repair the item in their hand in exchange for 30 levels. As such, we'll use the following command structure:

```
/repair
```

We want to put a requirement on this command that the player needs to have at least 30 levels of experience in order to run the command - if the player has less than 30 levels, the player should not be able to run the command. The easiest way to make the player not able to run the command is to literally tell the user that the command doesn't exist. That's what requirements do in the CommandAPI:

```java
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
```

It's important to note that in this example, we case the `sender` to a `player` for the requirement method. We know that the sender is definitely a player because we use `executesPlayer()`, which ensures that this is the case. Now that we've got this, **we need to make sure we update the player's requirements _when their exp changes_**. This is covered in more detail in the section about updating requirements below.

</div>

-----

## Adding requirements to arguments

In a similar way that you can restrict certain arguments by adding permissions to them, you can restrict them by using arbitrary predicates by using the `withRequirement` method on the arguments themselves.

<div class="example">

### Example - A party creation and teleportation system

Let's say that we're working on a plugin that has a system to form groups of players called "parties". If you are not already in a party, you can create one of your own and if you are in a party, you can teleport to any other member in your party.

For this example, we'll use the following command structure:

```
/party create <partyName>
/party tp <player>
```

To represent our party in code, we'll use a simple `Map` called `partyMembers` which maps the player's UUID to the name of their registered party:

```java
Map<UUID, String> partyMembers = new HashMap<>();
```

To begin with, let's create the `/party create <partyName>` command. First, we must declare our arguments:

```java
LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();

// The "create" literal, with a requirement that a player must have a party
arguments.put("createParty", new LiteralArgument("create")
	.withRequirement(sender -> {
		
		return !partyMembers.containsKey(((Player) sender).getUniqueId());
		
	}));
arguments.put("partyName", new StringArgument());
```

In this argument declaration, we put a requirement on the literal `create`, where the player does not have a party. In other words, if the player does not have a party, they are allowed to run `/party create <partyName>`. If a player already has a party, then they won't be allowed to run this command. 

Now that we've declared our arguments, we can now declare our main command `/party create <partyName>`. We populate it with the arguments, and we create an entry in our `partyMembers` with the player's UUID and the name of the party that they created. Since this updates the requirements of the player, we'll have to make sure we update it (which is covered in more detail in the section about updating requirements below) - until then, I'll omit this from the code:

```java
new CommandAPICommand("party")
	.withArguments(arguments)
	.executesPlayer((player, args) -> {
		
		//Get the name of the party to create
		String partyName = (String) args[0];
		
		partyMembers.put(player.getUniqueId(), partyName);
	})
	.register();
```

-----

So now we've added the ability to create a party if we're not already in it. Now we need to implement our `party tp <player>` command. Again, we must start by declaring our arguments:

```java
arguments = new LinkedHashMap<>();
arguments.put("teleport", new LiteralArgument("tp")
	.withRequirement(sender -> {
		
		return partyMembers.containsKey(((Player) sender).getUniqueId());
        
	}));
arguments.put("player", new PlayerArgument()
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
```

Notice something here? There's some code repetition for the `withRequirement` method - this is the same predicate that we used earlier, except we remove the negation. If you are interested, you can view the section [Predicate tips](./predicatetips.md) for a method to improve code reuse.

Once the arguments have been declared, we can now implement our party teleportation command:

```java
new CommandAPICommand("party")
	.withArguments(arguments)
	.executesPlayer((player, args) -> {
		Player target = (Player) args[0];
		player.teleport(target);
	})
	.register();
```

-----

What's important to note in this example is that if you spend the time to set up the arguments properly, it severely decreases the amount of code required to write your command. This makes the commands you declare easier to understand and follow and you don't end up having to put all of these checks in the body of your command executor.

</div>



