# Argument suggestions

Sometimes, you want to override the list of suggestions that are provided by an argument. To handle this, CommandAPI arguments contain three methods to override suggestions:

```java
Argument overrideSuggestions(String... suggestions);
Argument overrideSuggestions(Function<CommandSender, String[]> suggestions);
Argument overrideSuggestions(BiFunction<CommandSender, Object[], String[]> suggestions);
```

We will describe these methods in detail in this section.

-----

## Suggestions with a String Array

The first method, `overrideSuggestions(String... suggestions)`, allows you to *replace* the suggestions normally associated with that argument with an array of strings.

<div class="example">


### Example - Teleport to worlds by overriding suggestions

Say we're creating a plugin with the ability to teleport to different worlds on the server. If we were to retrieve a list of worlds, we would be able to override the suggestions of a typical `StringArgument` to teleport to that world. Let's create a command with the following structure:

```
/tpworld <world>
```

We then implement our world teleporting command using `overrideSuggestions()` on the `StringArgument` to provide a list of worlds to teleport to:

```java
//Populate a String[] with the names of worlds on the server
String[] worlds = Bukkit.getWorlds().stream().map(World::getName).toArray(String[]::new);

//Override the suggestions of the StringArgument with the aforementioned String[]
LinkedHashMap<String, ArgumentType> arguments = new LinkedHashMap<>();
arguments.put("world", new StringArgument().overrideSuggestions(worlds));

new CommandAPICommand("tpworld")
    .withArguments(arguments)
    .executesPlayer((player, args) -> {
       	String world = (String) args[0];
		player.teleport(Bukkit.getWorld(world).getSpawnLocation());
    })
    .register();
```

</div>

-----

## Suggestions depending on a command sender

The `overrideSuggestions(Function<CommandSender, String[]> suggestions)` method allows you to replace the suggestions normally associated with that argument with an array of strings that are evaluated dynamically using information about the command sender.

<div class="example">


### Example - Friend list by overriding suggestions

Say you have a plugin which has a "friend list" for players. If you want to teleport to a friend in that list, you could use a `PlayerArgument`, which has the list of suggestions overridden with the list of friends that that player has. Since the list of friends *depends on the sender*, we can use the function to determine what our suggestions should be. Let's use the following command to teleport to a friend from our friend list:

```
/friendtp <friend>
```

Let's say we have a simple class to get the friends of a command sender:

```java
public class Friends {
    public static String[] getFriends(CommandSender sender) {
        if(sender instanceof Player) {
            return //Look up friends in a database or file
        } else {
            return new String[0];
        }
    }
}
```

We can then use this to generate our suggested list of friends:

```java
LinkedHashMap<String, ArgumentType> arguments = new LinkedHashMap<>();
arguments.put("friend", new PlayerArgument().overrideSuggestions((sender) -> {
    Friends.getFriends(sender);
}));

new CommandAPICommand("friendtp")
    .withArguments(arguments)
    .executesPlayer((player, args) -> {
       	Player target = (Player) args[0];
		player.teleport(target);
    })
    .register();
```

> **Developer's Note:**
>
> The syntax of inlining the `.overrideSuggestions()` method has been designed to work well with Java's lambdas. For example, we could write the above code more consisely, such as:
>
> ```java
> LinkedHashMap<String, ArgumentType> arguments = new LinkedHashMap<>();
> arguments.put("friend", new PlayerArgument().overrideSuggestions(Friends::getFriends));
> ```
>
> 

</div>

-----

## Suggestions depending on previous arguments

The `overrideSuggestions(BiFunction<CommandSender, Object[], String[]> suggestions)` method is the most powerful suggestion overriding function that the CommandAPI offers. It has the capability to suggest arguments based on the values of previously inputted arguments.

This method requires a function that takes in a command sender and the **list of previous arguments** and must return a `String[]` of suggestions. The arguments are parsed exactly like any regular `CommandAPI` command argument.


<div class="example">

### Example - Sending a message to a nearby player

Say we wanted to create a command that lets you send a message to a specific player in a given radius. _(This is a bit of a contrived example, but let's roll with it)_. To do this, we'll use the following command structure:

```
/localmsg <radius> <target> <message>
```

When run, this command will send a message to a target player within the provided radius. To help identify which players are within a radius, we can override the suggestions on the `<target>` argument to include a list of players within the provided radius. We do this with the following code:

```java
// Declare our arguments as normal
LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
arguments.put("radius", new IntegerArgument());

// Override the suggestions for the PlayerArgument, using (sender, args) as the parameters
// sender refers to the command sender that is running this command
// args refers to the Object[] of PREVIOUSLY DECLARED arguments (in this case, the IntegerArgument radius)
arguments.put("target", new PlayerArgument().overrideSuggestions((sender, args) -> {

    // Cast the first argument (radius, which is an IntegerArgument) to get its value
	int radius = (int) args[0];
	
    // Get nearby entities within the provided radius
	Player player = (Player) sender;
	Collection<Entity> entities = player.getWorld().getNearbyEntities(player.getLocation(), radius, radius, radius);
	
    // Get player names within that radius
	return entities.stream()
		.filter(e -> e.getType() == EntityType.PLAYER)
		.map(Entity::getName)
		.toArray(String[]::new);
}));
arguments.put("message", new GreedyStringArgument());

// Declare our command as normal
new CommandAPICommand("localmsg")
	.withArguments(arguments)
	.executesPlayer((player, args) -> {
		Player target = (Player) args[1];
		String message = (String) args[2];
		target.sendMessage(message);
	})
	.register();
```

As shown in this code, we use the `(sender, args) -> ...` lambda to override suggestions with previously declared arguments. In this example, our variable `args` will be `{ int }`, where this `int` refers to the radius. Note how this object array only has the previously declared arguments (and not for example `{ int, Player, String }`).

</div>

