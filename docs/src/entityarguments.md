# Entity & player arguments

## Entity selector argument

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

## Player argument

The `PlayerArgument` class is very similar _(almost identical)_ to `EntitySelectorArgument`, with the EntitySelector `ONE_PLAYER`. It also allows you to select a player based on their UUID.

> **Developer's Note:** 
>
> I've not tested the `PlayerArgument` enough to recommend using it over the `EntitySelectorArgument(EntitySelector.ONE_PLAYER)`. There may be other advantages to using this than the regular EntitySelectorArgument, but as of writing this documentation, I know not of the advantages nor disadvantages to using this argument type. Internally, the `PlayerArgument` uses the `GameProfile` class from Mojang's authlib, which may be able to retrieve offline players (untested).

## Entity type argument

The `EntityTypeArgument` class is used to retrieve a type of entity as defined in the [`EntityType`](https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/entity/EntityType.html) enum. In other words, this is an entity type, for example a pig or a zombie.

### Example - Spawning entities

```java
LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();

arguments.put("entity", new EntityTypeArgument());
arguments.put("amount", new IntegerArgument(1, 100)); //Prevent spawning too many entities

CommandAPI.getInstance().register("spawnmob", arguments, (sender, args) -> {
	Player player = (Player) sender;
	for(int i = 0; i < (int) args[1]; i++) {
		player.getWorld().spawnEntity(player.getLocation(), (EntityType) args[0]);
	}
});
```
