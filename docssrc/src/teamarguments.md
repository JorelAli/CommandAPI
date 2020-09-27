# Team arguments

The `TeamArgument` class interacts with the Minecraft scoreboard and represents a team. Similar to the `ObjectiveArgument` class, the `TeamArgument` class must be casted to a String.

<div class="example">

### Example - Toggling friendly fire in a team

Let's say we want to create a command to toggle the state of friendly fire in a team. We want a command of the following form

```
/togglepvp <team>
```

To do this, given a team we want to use the `setAllowFriendlyFire(boolean)` function. As with the `ObjectiveArgument`, we must convert the `String` into a `Team` object.

```java
new CommandAPICommand("togglepvp")
    .withArguments(new TeamArgument("team"))
    .executes((sender, args) -> {
        //The TeamArgument must be casted to a String
        String teamName = (String) args[0];
        
        //A team name can be turned into a Team using getTeam(String)
        Team team = Bukkit.getScoreboardManager().getMainScoreboard().getTeam(teamName);
        
        //Toggle pvp
        team.setAllowFriendlyFire(team.allowFriendlyFire());
    })
    .register();
```

</div>