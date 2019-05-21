# Location argument

The `LocationArgument` class is used to specify a location in the _command sender's current world_. This allows the user to enter three numbers as coordinates, or use relative coordinates (i.e. the `~` and `^` operators)

The `LocationArgument` constructor requires a `LocationType`, which specifies the type of location that is accepted by the command.

### `LocationType.BLOCK_POSITION`
Integer block coordinates. The suggested location is the coordinates of block you are looking at when you type the command.
  ![BLOCK_POSITION](https://user-images.githubusercontent.com/42968178/50574833-92ef9e00-0df0-11e9-9ae5-59235b1bb8cb.png)

### `LocationType.PRECISE_POSITION`
Exact coordinates. The suggested location is the exact coordinates of where your cursor is pointing at when you type the command.
  ![PRECISE_POSITION](https://user-images.githubusercontent.com/42968178/50574841-b6b2e400-0df0-11e9-9003-481b139d190f.png)

**By default, the `LocationArgument` will use `PRECISE_POSITION`**.

### Example - Break block by coordinates command

```java
LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();

//We want to target blocks in particular, so use BLOCK_POSITION
arguments.put("block", new LocationArgument(LocationType.BLOCK_POSITION));

CommandAPI.getInstance().register("break", arguments, (sender, args) -> {
	((Location) args[0]).getBlock().setType(Material.AIR);
});
```

