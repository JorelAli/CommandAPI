# Sound argument

The `SoundArgument` class allows a command sender to retrieve the Bukkit `Sound` object to represent in-game sound effects (such as mob sounds or ambient sound effects), as well as in-game music.

## Example - Playing sound to yourself

```java
LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
arguments.put("sound", new SoundArgument());

CommandAPI.getInstance().register("sound", arguments, (sender, args) -> {
    Sound sound = (Sound) args[0];
    Player player = (Player) sender;
    player.getWorld().playSound(player.getLocation(), sound, 100.0f, 1.0f);
});
```

