# Advancement argument

The `AdvancementArgument` class is used to represent Bukkit `Advancement` objects. This can be used to get the advancement progress for a player and a specific advancement, as well as awarding advancements programmatically.

## Example - Awarding an advancement to a player

```java
LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
arguments.put("advancement", new AdvancementArgument());

CommandAPI.getInstance().register("giveadvancement", arguments, (sender, args) -> {
    Advancement advancement = (Advancement) args[0];
    Player player = (Player) sender;
    
    advancement.getCriteria().forEach(criteria -> {
        player.getAdvancementProgress(advancement).awardCriteria(criteria);
    });
});
```

