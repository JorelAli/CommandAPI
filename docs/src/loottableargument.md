# LootTable argument

The `LootTableArgument` class can be used to get a Bukkit `LootTable` object.

## Example - ¯\\\_(ツ)_/¯

```java
LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
arguments.put("loottable", new LootTableArgument());

CommandAPI.getInstance().register("giveloottable", arguments, (sender, args) -> {
    LootTable lootTable = (LootTable) args[0];
    Player player = (Player) sender;
    
    LootContext context = /* Some generated LootContext relating to the lootTable*/
    lootTable.fillInventory(player.getInventory(), new Random(), context);
}); 
```

> **Developer's Note:**
>
> Honestly, I've not managed to get a successful example of using a `LootTable` in practice, due to being unable to generate a suitable `LootContext`. If you believe you can supply a suitable example for this page, feel free to send an example [on the CommandAPI issues page](https://github.com/JorelAli/1.13-Command-API/issues/new/choose).

