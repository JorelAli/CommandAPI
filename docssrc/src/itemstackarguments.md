# Itemstack arguments

![](./images/arguments/itemstack.png)

The `ItemStackArgument` class represents in-game items. As expected, this should be casted to Bukkit's `ItemStack` object.

<div class="example">

### Example - Giving a player an itemstack

Say we want to create a command that gives you items. For this command, we will use the following structure:

```
/item <itemstack>
```

With this structure, we can easily create our command:

```java
// Declare our arguments
LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
arguments.put("itemstack", new ItemStackArgument());

new CommandAPICommand("item")
    .withArguments(arguments)
    .executesPlayer((player, args) -> {
        player.getInventory().addItem((ItemStack) args[0]);
    })
    .register();
```

</div>