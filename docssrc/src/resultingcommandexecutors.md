# Resulting command executors

Resulting command executors are very similar to normal command executors, the only difference is that they can return an integer _result value_.

```java
(sender, args) -> {
	//Code here
	return /*some integer here*/ ;
};
```

## Example - Random number result command

Say we want a command that returns a random number as a result. This can then be used by vanilla Minecraft's `/execute store result ...` command, which can be used for other command block chains.

```java
CommandAPI.getInstance().register("randnum", new LinkedHashMap<String, Argument>(), (sender, args) -> {
	return new Random().nextInt();
});
```

This returns a success value of 1 _(Because no errors or `CommandAPI.fail(String)` was thrown)_ and a return value of a random number.

## Example - Lootbox system with `/execute` command

We can store state using `/execute store` and we can perform conditional checks using `/execute if`. By combining these, we can create a system which can be used with commandblocks to say, give players random lootboxes and redeem them.

```java
LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();

//Register random number generator command from 1 to 99 (inclusive)
CommandAPI.getInstance().register("randomnumber", arguments, (sender, args) -> {
	return ThreadLocalRandom.current().nextInt(1, 100); //Returns random number from 1 <= x < 100
});

//Register reward giving system for a target player
arguments.put("target", new EntitySelectorArgument(EntitySelector.ONE_PLAYER));

CommandAPI.getInstance().register("givereward", arguments, (sender, args) -> {
	Player player = (Player) args[0];
	player.getInventory().addItem(new ItemStack(Material.DIAMOND, 64));
	Bukkit.broadcastMessage(player.getName() + " won a rare 64 diamonds from a loot box!");
}
```

Store a random number under the scoreboard score `randVal` for a player called `SomePlayer`, by executing the command /randomnumber
```
/execute store success score SomePlayer randVal run randomnumber
```

Check if the random number is equal to 1 (say we have some plugin which gives a reward to a player that happened to get a 1 from a random number command, thus giving them a 1/99 chance of winning)
```
/execute if score SomePlayer randVal matches 1 run givereward SomePlayer
```
