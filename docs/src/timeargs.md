# Time argument

The `TimeArgument` class represents in-game time, _in the number of in-game ticks_. This allows command senders to specify a certain number of ticks in a simpler way, by including the characters `d` to specify the numbers of days, `s` to specify the number of seconds or `t` to specify a number of ticks.

The CommandAPI converts the inputs provided by the command sender into a number of ticks as an integer.

> **Developer's Note:**
>
> The `TimeArgument` provides inputs such as `2d` (2 in-game days), `10s` (10 seconds) and `20t` (20 ticks), but does **not** let you combine them, such as `2d10s`.

## Example - Displaying a server-wide announcement

Say we have a command `bigmsg` that displays a title message to all players for a certain duration:

```
/bigmsg <duration> <message>
```

```java
// Declare our arguments
LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
arguments.put("duration", new TimeArgument());
arguments.put("message", new GreedyStringArgument());

CommandAPI.getInstance().register("bigmsg", arguments, (sender, args) -> {
	int duration = (int) args[0];
	String message = (String) args[1];
    
	for(Player player : Bukkit.getOnlinePlayers()) {
        // Display the message to all players, with the default fade in/out times.
		player.sendTitle(message, "", 10, duration, 20);
	}
});
```

