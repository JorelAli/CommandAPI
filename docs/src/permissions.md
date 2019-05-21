# Permissions

Permissions let you control which players are allowed to execute which commands. This is handled using the `CommandPermission` class, which has the following uses:

* Requires OP to execute a command:
  ```java
  CommandPermission.OP
  ```
* Anyone can execute a command:
  ```java
  CommandPermission.NONE
  ```
* Requires a specific permission node to exeucte a command:
  ```java
  CommandPermission.fromString("my.permission")
  ```

## Registering permissions to commands

To add a permission to a command, you can use any of the following constructors with a valid `CommandAPI` instance:

```java
register(String commandName, CommandPermission, LinkedHashMap<String, Argument>, CommandExecutor);
register(String commandName, CommandPermission, String[] aliases, LinkedHashMap<String, Argument>, CommandExecutor)
```

### Example - /god command with permissions

```java
LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();

//Register the /god command with the permission node "command.god"
CommandAPI.getInstance().register("god", CommandPermission.fromString("command.god"), arguments, (sender, args) -> {
    if(sender instanceof Player) {
		((Player) sender).setInvulnerable(true);
	}
});

//Add a target argument to allow /god <target>
arguments.put("target", new EntitySelectorArgument(EntitySelector.ONE_PLAYER));

//Require "command.god" permission node to execute /god <target>
CommandAPI.getInstance().register("god", CommandPermission.fromString("command.god"), arguments, (sender, args) -> {
	((Player) args[0]).setInvulnerable(true);
});
```

## Registering permissions to arguments

For further fine-tuning of permission management, the CommandAPI allows you to add permissions to individual arguments. This prevents the user from executing a command with a specific argument if they do not have a specific permission.

This is done by using the `.withPermission(CommandPermission)` method at the end of an argument.


If a player does not have the required permission:

* The argument hover text which suggests what the command is will not be shown
* The player will receive an error if they try to type something in for that argument
* Suggestions, such as a list of materials or players will not be shown

### Example - /kill command with argument permissions

```java
LinkedHashMap<String, ArgumentType> arguments = new LinkedHashMap<>();

//Register /kill command normally. Since no permissions are applied, anyone can run this command
CommandAPI.getInstance().register("kill", arguments, (sender, args) -> {
	((Player) sender).setHealth(0);
});

//Adds the OP permission to the "target" argument. The sender requires OP to execute /kill <target>
arguments.put("target", new PlayerArgument().withPermission(CommandPermission.OP));

CommandAPI.getInstance().register("kill", arguments, (sender, args) -> {
	((Player) args[0]).setHealth(0);
});
```

> **Developer's Note:**
>
> As you can see, there are multiple ways of applying permissions to commands with arguments. In the /god command shown above, the permission was applied to the whole command. In the /kill command shown above, the permission was applied to the argument. 
>
> There's not really much difference between the two methods, but I personally would use argument permissions with `.withPermission()` as it has greater control over arguments.
