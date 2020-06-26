# Custom arguments

Custom arguments are arguably the most powerful argument that the CommandAPI offers. This argument is used to represent any String, or Minecraft key _(Something of the form `String:String`, such as `minecraft:diamond`)_

In order to specify which type of custom argument is being used, the additional flag `keyed` can be added by, instead of using the default constructor which requires a lambda, you use the alternate constructor which requires a lambda, followed by `true`, which represents a Minecraft key input.

```java
new CustomArgument<T>((input) -> { 
	/* Code which handles input */ 
	return //Some object of type T;
}, true);
```

The custom argument requires the type of the target object that the custom argument will return when parsing the arguments for a command. For instance, if you have a `CustomArgument<Player>`, then when parsing the arguments for the command, you would cast to a Player object: `(Player) args[n]`.

## Example - Scoreboard objectives as a custom argument

Here, we aim to create a custom argument that represents the [Objective](https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/scoreboard/Objective.html) object for a scoreboard.

```java
//Function that returns a CustomArgument<Object>
private CustomArgument<Objective> objectiveArgument() {
	Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

	/* Create our CustomArgument instance. This takes an input of a 
	 * lambda that takes in a String and returns an Objective object. */
	return new CustomArgument<Objective>((input) -> {

		//Parse the objective
		if(scoreboard.getObjective(input) == null) {

			//Throw a custom error to the user if it fails to get the objective
			CustomArgument.throwError(new MessageBuilder("Unknown objective: ").appendArgInput());
			return null;
		} else {
			return scoreboard.getObjective(input);
		}
	
	//Use .overrideSuggestions to suggest the list of names of objectives
	}).overrideSuggestions(scoreboard.getObjectives().stream().map(o -> o.getName()).toArray(String[]::new));
}
```

From the code above, it uses the `CustomArgument.throwError` function which throws an error to the command sender if the command that they input is invalid. The `throwError` function can accept one of two parameters:

```javaHookedUpHentai
CustomArgument.throwError(String message)
CustomArgument.throwError(MessageBuilder message)
```
 
## Message Builders

The `MessageBuilder` class is a class to easily create messages to describe errors when a sender sends a command which does not meet the expected syntax for an argument. It acts in a similar way to a `StringBuilder`, where you can append content to the end of a String.

The following methods are as follows:

| Method | Description |
| ------ | ----------- |
| `appendArgInput()` | Appends the argument that failed that the sender submitted to the end of the builder. E.g. `/foo bar` will append `bar` |
| `appendFullInput()` | Appends the full command that a sender submitted to the end of the builder. E.g. `/foo bar` will append `foo bar` |
| `appendHere()` | Appends the text `<--[HERE]` to the end of the builder | 
| `append(Object)`| Appends the object to the end of the builder | 

### Example - Message builder for invalid objective argument

See the code above, which uses the following code snippet:

```java
//Creates a MessageBuilder object that handles an invalid objective. 
new MessageBuilder("Unknown objective: ").appendArgInput();
```

## Defined custom arguments

The CommandAPI has a few custom arguments which have been predefined, in the `DefinedCustomArguments` class. The methods are as follows:

```java
DefinedCustomArguments.objectiveArgument(); //CustomArgument<Objective>
DefinedCustomArguments.teamArgument();      //CustomArgument<Team>
```

These are included to help reduce the amount of code required if you were to implement custom arguments for the types stated above.
