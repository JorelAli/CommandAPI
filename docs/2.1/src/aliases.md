# Aliases

Aliases let you create aliases for commands. They are simply added at command regisration time by using either of the following methods:

```java
CommandAPI.getInstance().register(String, String[], LinkedHashMap, CommandExecutor);
CommandAPI.getInstance().register(String, CommandPermission, String[], LinkedHashMap, CommandExecutor);
```

The `String[]` represents a list of aliases which can be used to execute a command.

## Example - Using aliases for /gamemode

```java
LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
//populate arguments here

CommandAPI.getInstance().register("gamemode", new String[] {"gm"}, arguments, (sender, args) -> {
	//Handle gamemode command here
});
```
