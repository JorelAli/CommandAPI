# Dynamically suggested arguments

Dynamically suggested arguments lets you provide a function to generate arguments dynamically. This is achieved using the `DynamicSuggestedStringArgument` class, which has two constuctor formats:

```java
//Constructor requires a function which returns a String[]
new DynamicSuggestedStringArgument(() -> return new String[]);

//Constructor requires a function which returns a String[] and receives a sender input
new DynamicSuggestedStringArgument((sender) -> return new String[]);
```

The first one simply lets you return a list of strings as suggestions. Whenever a player begins typing a command, a packet is sent to the server and it will return the list of suggestions _(similar to tab completion)_. The second constructor allows you to also use the command sender as an input _(of type `CommandSender`)_, which can be used for more intricate suggestions.

## Example - Friend command with dynamic suggestions

Say we want a plugin which is a friend system. The friend system allows you to add friends and when you've made a friend, you can message them.

```
/friend <player>       - Adds a player as a friend
/msgf   <player> <msg> - Message a friend
```

```java
//Global mapping of a player to a list of their friends
final Map<String, List<String>> friends = new ArrayList<>();

//Register /friend command
LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
arguments.put("friend", new PlayerArgument());

CommandAPI.getInstance().register("friend", arguments, (sender, args) -> {
	
	//Get list of friends from the sender
	List<String> senderFriends;

	if(friends.containsKey(sender.getName())) {
		senderFriends = friends.get(sender.getName());
	} else {
		senderFriends = new ArrayList<>(); 
	}

	//Add friend to the global map
	senderFriends.put(((Player) args[0]).getName());
	friends.put(sender.getName(), senderFriends);
});

//Register /msgf command
arguments.clear();

/* Use a DynamicSuggestedStringArgument which consists of the names of
 * friends for that player. Don't forget that this returns a String */
arguments.put("friend", new DynamicSuggestedStringArgument((sender) -> {
	List<String> senderFriends = friends.get(sender.getName());
	return senderFriends.toArray(new String[senderFriends.size()]);
}));

arguments.put("message", new GreedyStringArgument());

CommandAPI.getInstance().register("msgf", arguments, (sender, args) -> {
	//Parse the target player from the String, failing if it can't find that player
	Player target = Bukkit.getPlayer((String) args[0]);
	if(target == null) {
		CommandAPI.fail("Couldn't find that player!");
	} else {
		target.sendMessage((String) args[1]);
	}
});
```
A thing to note from the code above: The DynamicSuggestedStringArgument is a _String_ argument, meaning that the resulting type is a _String_. Despite the fact that we want a player object, the CommandAPI only lets you make dynamic suggestions for Strings. Therefore, it _could_ fail in this situation (if the target player isn't found), and we have to handle that case on our own. If we had used a `PlayerArgument`, that argument would automatically fail due to the implementation of the player argument.
