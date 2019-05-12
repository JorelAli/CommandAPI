# Dynamically suggested arguments

Dynamically suggested arguments lets you provide a function to generate arguments dynamically. This is achieved using the `DynamicSuggestedStringArgument` class, which has two constuctor formats:

```java
//Constructor requires a function which returns a String[]
new DynamicSuggestedStringArgument(() -> return new String[]);

//Constructor requires a function which returns a String[] and receives a sender input
new DynamicSuggestedStringArgument((sender) -> return new String[]);
```

## Example - Friend command with dynamic suggestions

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
	Bukkit.getPlayer((String) args[0]).sendMessage((String) args[1]);
});
```
