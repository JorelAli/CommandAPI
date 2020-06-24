# Advancement arguments

The `AdvancementArgument` class represents in-game advancements. As expected, the `AdvancementArgument` can be casted to Bukkit's `Advancement` class.

<div class="example">

### Example - Awarding a player an advancement

Say we want to award a player an advancement. In this example, we will also update all preceding advancements that lead to a specific advancement. First, we need the structure of our command:

```
/award <player> <advancement>
```

Since we require a player, we will use the `PlayerArgument` for this example. Given a player, we can simply get the `AdvancementProgress` for that player, and then award the criteria required to fully complete the provided advancement.

```java
LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
arguments.put("player", new PlayerArgument());
arguments.put("advancement", new AdvancementArgument());

new CommandAPICommand("award")
    .withArguments(arguments)
    .executes((sender, args) -> {
        Player target = (Player) args[0];
        Advancement advancement = (Advancement) args[1];
        
        //Award all criteria for the advancement
        AdvancementProgress progress = target.getAdvancementProgress(advancement);
        for(String criteria : advancement.getCriteria()) {
            progress.awardCriteria(criteria);
        }
    })
    .register();
```

</div>