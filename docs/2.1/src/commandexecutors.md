# Command executors

> **Developer's Note:**
>
> This section can be a little bit difficult to follow. If you only want the bare basic features _(executes a command)_, read the section below on _Normal command executors_ - this behaves very similar to the `onCommand` method in Bukkit.

-----

The CommandAPI provides two separate command executors which are lambdas which execute the code you want when a command is called. These are the classes `CommandExecutor` _(Not to be confused with Bukkit's `CommandExecutor` class)_, which just runs the contents of a command, and `ResultingCommandExecutor` that returns an integral _(whole number)_ result.

> **Developer's Note:**
> 
> In general, you need not focus too much on what type of command executor to implement. If you know for certain that you're going to be using your command with command blocks, just ensure you return an integer at the end of your declared command executor. Java will infer the type _(whether it's a CommandExecutor or ResultingCommandExecutor)_ automatically, so feel free to return an integer or not. 

## Normal command executors

Command executors are of the following format, where `sender` is a [`CommandSender`](https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/command/CommandSender.html), and `args` is an `Object[]`, which represents arguments which are parsed by the CommandAPI.

```java
(sender, args) -> {
  //Code here  
};
```

With normal command executors, these do not need to return anything. By default, this will return a _success value_ of 1 if it runs successfully, and a _success value_ of 0 if it runs unsuccessfully, either by throwing an exception _(RuntimeException)_ or by forcing the command to fail.

## Forcing commands to fail

Sometimes, you want your command to fail on purpose. This is basically the way to "gracefully" handle errors in your command execution. This is performed using the following method:

```java
CommandAPI.fail("Error message goes here");
```

When the CommandAPI calls the fail method, it will cause the command to return a _success value_ of 0, to indicate failure.

### Example - Command failing for element not in a list

Say we have some list, `List<String>` containing a bunch of fruit and the player can choose from it. In order to do that, we can use a `StringArgument` and suggest it to the player using `.overrideSuggestions(String[])`. However, because this only lists _suggestions_ to the player, it does **not** stop the player from entering an option that isn't on the list of suggestions.

Therefore, to gracefully handle this with a proper error message, we use `CommandAPI.fail(String)` with a meaningful error message which is displayed to the user.

```java
//Array of fruit
List<String> fruit = new ArrayList<>();
fruit.add("banana");
fruit.add("apple");
fruit.add("orange");

//Argument accepting a String, suggested with the list of fruit
LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
arguments.put("item", new StringArgument().overrideSuggestions(fruit.toArray(new String[fruit.size()])));

//Register the command
CommandAPI.getInstance().register("getfruit", arguments, (sender, args) -> {
    String inputFruit = (String) args[0];
    if(fruit.contains(inputFruit)) {
        //Do something with inputFruit
    } else {
        //The player's input is not in the list of fruit
        CommandAPI.fail("That fruit doesn't exist!");
    }
});
```

> **Developer's Note:**
>
> In general, it's a good idea to handle unexpected cases with the `CommandAPI.fail()` method. Most arguments used by the CommandAPI will have their own builtin failsafe system _(e.g. the `EntitySelectorArgument` will not execute the command executor if it fails to find an entity)_, so this feature is for those extra cases.