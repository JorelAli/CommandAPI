# Command registration

## Command registration

| CommandRegistration method                                   | Outcome                                                   |
| ------------------------------------------------------------ | --------------------------------------------------------- |
| `CommandAPI.getInstance().register(String, LinkedHashMap, CommandExecutor)` | Basic command registration                                |
| `CommandAPI.getInstance().register(String, String[], LinkedHashMap, CommandExecutor)` | Register command with an array of aliases                 |
| `CommandAPI.getInstance().register(String, CommandPermission, LinkedHashMap, CommandExecutor)` | Register command which need certain permissions           |
| `CommandAPI.getInstance().register(String, CommandPermission, String[], LinkedHashMap, CommandExecutor)` | Register command with aliases and permission requirements |

To see more about `CommandPermission`, check out the link [here](https://github.com/JorelAli/1.13-Command-API/blob/master/v1.8%20Documentation.md#commandexecutor)

To see more about `CommandExecutor`, check out the link [here](https://github.com/JorelAli/1.13-Command-API/blob/master/v1.8%20Documentation.md#commandexecutor)

### Command loading order

**Commands must be registered before the server finishes loading**. The CommandAPI will prevent command registration after the server has loaded.

| When to load               | What to do                                                   |
| -------------------------- | ------------------------------------------------------------ |
| Plugin `onLoad()` method   | Register commands to be used in Minecraft functions (see Function section for more info) |
| Plugin `onEnable()` method | Register regular commands                                    |

### Command unregistration

The CommandAPI has support to unregister commands completely from Minecraft's command list. This includes Minecraft built in commands!

| Method                                                       | Result                                                       |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| `CommandAPI.getInstance().unregister(String cmd)`            | Unregisters a command from the game                          |
| `CommandAPI.getInstance().unregister(String cmd, boolean force)` | Unregisters a command from the game by force. This includes `/minecraft:cmd`, `/bukkit:cmd` and `/spigot:cmd` commands as well. |

**Example:**

| Method                                                  | Outcome                                                      |
| ------------------------------------------------------- | ------------------------------------------------------------ |
| `CommandAPI.getInstance().unregister("gamemode")`       | Unregisters the `/gamemode` command from the game            |
| `CommandAPI.getInstance().unregister("gamemode", true)` | Force unregisters all `/gamemode` commands from all plugins, Minecraft, Bukkit and Spigot. This includes `/minecraft:gamemode`, in addition to any other plugins which register `/gamemode` |
