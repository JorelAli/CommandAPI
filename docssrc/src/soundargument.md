# Sound arguments

The `SoundArgument` class allows a command sender to retrieve the Bukkit `Sound` object to represent in-game sound effects (such as mob sounds or ambient sound effects), as well as in-game music.

<div class="example">

### Example - Playing sound to yourself

Say we want a simple command that plays a specific sound at your location. To do this, we will make the following command:

```
/sound <sound>
```

This command simply plays the provided sound to the current player:

```java
LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
arguments.put("sound", new SoundArgument());

new CommandAPICommand("sound")
    .withArguments(arguments)
    .executesPlayer((player, args) -> {
        player.getWorld().playSound(player.getLocation(), (Sound) args[0], 100.0f, 1.0f);
    })
    .register();
```

</div>