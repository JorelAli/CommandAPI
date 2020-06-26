# Handling command failures

Sometimes, you want your command to fail on purpose. This is basically the way to "gracefully" handle errors in your command execution. This is performed using the following method:

```java
CommandAPI.fail("Error message goes here");
```

When the CommandAPI calls the fail method, it will cause the command to return a _success value_ of 0, to indicate failure.

<div class="example">

### Example - Command failing for element not in a list

Say we have some list containing fruit and the player can choose from it. In order to do that, we can use a `StringArgument` and suggest it to the player using `.overrideSuggestions(String[])`. However, because this only lists _suggestions_ to the player, it does **not** stop the player from entering an option that isn't on the list of suggestions.

Therefore, to gracefully handle this with a proper error message, we use `CommandAPI.fail(String)` with a meaningful error message which is displayed to the user.

```java
//Array of fruit
String[] fruit = new String[] {"banana", "apple", "orange"};

//Argument accepting a String, suggested with the list of fruit
LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
arguments.put("item", new StringArgument().overrideSuggestions(fruit));

//Register the command
new CommandAPICommand("getfruit")
    .withArguments(arguments)
    .executes((sender, args) -> {
        String inputFruit = (String) args[0];
        if(fruit.contains(inputFruit)) {
            //Do something with inputFruit
        } else {
            //The player's input is not in the list of fruit
            CommandAPI.fail("That fruit doesn't exist!");
        }
	})
    .register();
```

</div>

> **Developer's Note:**
>
> In general, it's a good idea to handle unexpected cases with the `CommandAPI.fail()` method. Most arguments used by the CommandAPI will have their own built-in failsafe system _(e.g. the `EntitySelectorArgument` will not execute the command executor if it fails to find an entity)_, so this feature is for those extra cases.