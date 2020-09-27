# Argument suggestions with tooltips

![](./images/warps.gif)

The CommandAPI can also display tooltips for specific argument suggestions. These are shown to the user when they hover over a given suggestion and can be used to provide more context to a user about the suggestions that are shown to them. In this section, we'll outline the two ways of creating suggestions with tooltips: 

- Normal (String) suggestions with tooltips
- Safe suggestions with tooltips

-----

## Tooltips with normal (String) suggestions

To use these features, the CommandAPI includes the `overrideSuggestionsT` methods for arguments, that accept `StringTooltip` objects instead of `String` objects:

```java
Argument overrideSuggestionsT(StringTooltip... suggestions);
Argument overrideSuggestionsT(Function<CommandSender, StringTooltip[]> suggestions);
Argument overrideSuggestionsT(BiFunction<CommandSender, Object[], StringTooltip[]> suggestions);
```

The `StringTooltip` object has two static methods to construct it easily:

```java
StringTooltip none(String suggestion);
StringTooltip of(String suggestion, String tooltip);
```

The first method, `StringTooltip.none(String)` creates a normal suggestion entry with no tooltip, whereas the `StringTooltip.of(String, String)` method creates a suggestion with the provided tooltip text.

Lastly, the `StringTooltip` object also has a static method `arrayOf(StringTooltip...)` to easily construct a `StringTooltip[]`.

<div class="example">

### Example - An emotes command with string suggestion tooltips

Say we want to create a simple command to provide ingame emotes between players. For example, if you did `/emote wave Bob`, you'll "wave" to the player _Bob_. For this example, we'll use the following command structure:

```
/emote <emote> <target>
```

First, we'll declare our arguments. Here, we'll use the `overrideSuggestionsT` method, along with the `StringTooltip.of(String, String)` method to create emote suggestions and include suitable descriptions. In this example, we also make use of `arrayOf(StringTooltip...)` to easily create a `StringTooltip[]`:

```java
List<Argument> arguments = new ArrayList<>();
arguments.add(new StringArgument("emote")
	.overrideSuggestionsT( 
		StringTooltip.arrayOf(
			StringTooltip.of("wave", "Waves at a player"),
			StringTooltip.of("hug", "Gives a player a hug"),
			StringTooltip.of("glare", "Gives a player the death glare")
		)
	));
arguments.add(new PlayerArgument("target"));
```

Finally, we declare our command as normal:

```java
new CommandAPICommand("emote")
	.withArguments(arguments)
	.executesPlayer((player, args) -> {
		String emote = (String) args[0];
		Player target = (Player) args[1];
		
		switch(emote) {
		case "wave":
			target.sendMessage(player.getName() + " waves at you!");
			break;
		case "hug":
			target.sendMessage(player.getName() + " hugs you!");
			break;
		case "glare":
			target.sendMessage(player.getName() + " gives you the death glare...");
			break;
		}
	})
	.register();
```


</div>

-----

## Tooltips with safe suggestions

Using tooltips with safe suggestions is almost identical to the method described above for normal suggestions, except for two things. Firstly, you must use `safeOverrideSuggestionsT` method instead of the `overrideSuggestionsT` method and secondly, instead of using `StringTooltip`, you must use `Tooltip<S>`. Let's look at these differences in more detail.

The `safeOverrideSuggestionsT` methods are fairly similar to the `overrideSuggestionsT` methods, except instead of using `StringTooltip`, it simply uses `Tooltip<S>`.

```java
Argument safeOverrideSuggestionsT(Tooltip<S>... suggestions);
Argument safeOverrideSuggestionsT(Function<CommandSender, Tooltip<S>[]> suggestions);
Argument safeOverrideSuggestionsT(BiFunction<CommandSender, Object[], Tooltip<S>[]> suggestions);
```

The `Tooltip<S>` class represents a tooltip for a given object `S`. For example, a tooltip that is for a `LocationArgument` would be a `Tooltip<Location>` and a tooltip for an `EnchantmentArgument` would be a `Tooltip<Enchantment>`.

Just like the `StringTooltip` class, the `Tooltip<S>` class provides the following static methods, which operate exactly the same as the ones in the `StringTooltip` class:

```java
Tooltip<S> none(S object);
Tooltip<S> of(S object, String tooltip);
Tooltip<S>[] arrayOf(Tooltip<S>... tooltips);
```

The use of `arrayOf` is heavily recommended as it provides the necessary type safety for Java code to ensure that the correct types are being passed to the `safeOverrideSuggestionsT` method.

<div class="example">

### Example - Teleportation command with suggestion descriptions

Say we wanted to create a custom teleport command which suggestions a few key locations. In this example, we'll use the following command structure:

```
/warp <location>
```

First, we'll declare our arguments. Here, we use a `LocationArgument()` and use the `safeOverrideSuggestionsT` method, with a parameter for the command sender, so we can get information about the world. We populate the suggestions with tooltips using `Tooltip.of(Location, String)` and collate them together with `Tooltip.arrayOf(Tooltip<Location>...)`:

```java
List<Argument> arguments = new ArrayList<>();
arguments.add(new LocationArgument("location")
	.safeOverrideSuggestionsT((sender) -> {
		return Tooltip.arrayOf(
			Tooltip.of(((Player) sender).getWorld().getSpawnLocation(), "World spawn"),
			Tooltip.of(((Player) sender).getBedSpawnLocation(), "Your bed"),
			Tooltip.of(((Player) sender).getTargetBlockExact(256).getLocation(), "Target block")
		);
	}));
```

In the arguments declaration, we've casted the command sender to a player. To ensure that the command sender is definitely a player, we'll use the `executesPlayer` command execution method in our command declaration:

```java
new CommandAPICommand("warp")
	.withArguments(arguments)
	.executesPlayer((player, args) -> {
		player.teleport((Location) args[0]);
	})
	.register();
```

</div>

