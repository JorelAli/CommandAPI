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
    
</div>



