# Entity & player arguments

## Entity Selector Arguments

Minecraft's [target selectors](https://minecraft.gamepedia.com/Commands#Target_selectors) (e.g. `@a` or `@e`) are implemented using the `EntitySelectorArgument` class. This allows you to select specific entities based on certain attributes.

The `EntitySelectorArgument` constructor requires an `EntitySelector` argument to determine what type of data to return. There are 4 types of entity selections which are available:

* `EntitySelector.ONE_ENTITY` - A single entity, which returns a `Entity` object.
* `EntitySelector.MANY_ENTITIES`  - A collection of many entities, which returns a `Collection<Entity>` object.
* `EntitySelector.ONE_PLAYER` - A single player, which returns a `Player` object.
* `EntitySelector.MANY_PLAYERS` - A collection of players, which returns a `Collection<Player>` object.

The return type is the type to be cast when retrieved from the `Object[] args` in the command declaration.

### Example - Kill entities command

```java
//LinkedHashMap to store arguments for the command
LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();

//Using a collective entity selector to select multiple entities
arguments.put("entities", new EntitySelectorArgument(EntitySelector.MANY_ENTITIES));

CommandAPI.getInstance().register("kill", arguments, (sender, args) -> {
    
	//Parse the argument as a collection of entities (as stated above in the documentation)
	Collection<Entity> entities = (Collection<Entity>) args[0];
	sender.sendMessage("killed " + entities.size() + "entities");
	for(Entity e : entity)
		e.remove();
});
```

Example command usage for the above code would be:

* Kill all cows:
  ```
  /kill @e[type=cow]
  ```
* Kill the 10 furthest pigs from the command sender:
  ```
  /kill @e[type=pig,limit=10,sort=furthest]
  ```
## Player arguments

The `PlayerArgument` class is very similar to `EntitySelectorArgument`, except it's specifically for **one** player. It also allows you to select a player based on their UUID.
