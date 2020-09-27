# Scoreboard arguments

The scoreboard arguments that the CommandAPI provides allows you to interact with various scoreboard elements, such as objectives, teams and score holders. Since commands are registered in the `onEnable()` or `onLoad()` method of a plugin, this means that these are registered before Bukkit's main scoreboard is loaded.

This means that calling `Bukkit.getScoreboardManager().getMainScoreboard()` will *always* result in a `NullPointerException`. To avoid this scenario, try using a lambda which delays the call that gets Bukkit's main scoreboard to the moment when the command is executed, which typically occurs after the server has initialized it. For example, if you wanted to populate a `TeamArgument` with a list of all registered teams on the server, you should use the following:

```java
List<Argument> arguments = new ArrayList<>();
	        	
arguments.add(new TeamArgument("team").safeOverrideSuggestions(s ->
    Bukkit.getScoreboardManager().getMainScoreboard().getTeams().toArray(new Team[0]))
);
```
