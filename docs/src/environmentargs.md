# Environment arguments

The `EnvironmentArgument` class allows a command sender to refer to a specific world environment, declared in Bukkit's `World.Environment` class. This includes the following three environments: `NORMAL`, `NETHER` and `THE_END`.

> **Note:**
>
> The `EnvironmentArgument` is only supported for Minecraft versions 1.13.1 and later, meaning it _will not work_ on Minecraft 1.13. This is due to fact that Minecraft added the environment argument in 1.13.1. Attempting to use the `EnvironmentArgument` on Minecraft 1.13 will throw an `EnvironmentException`.

## Example - Creating a new world

```java
// Declare our arguments
LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
arguments.put("worldname", new StringArgument());
arguments.put("type", new EnvironmentArgument());

CommandAPI.getInstance().register("createworld", arguments, (sender, args) -> {
	String worldName = (String) args[0];
	Environment environment = (Environment) args[1];
    
    // Create a new world with the specific world name and environment
    Bukkit.getServer().createWorld(new WorldCreator(worldName).environment(environment));
	sender.sendMessage("World created!");
});
```

