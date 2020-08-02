# Block predicate arguments

<div class="example">

### Example - Replacing specific blocks in a radius

Say you want to replace blocks in a given radius. To do this, we'll use the following command structure:

```
/replace <radius> <fromBlock> <toBlock>
```

Of course, we could simply use a `BlockStateArgument` or even an `ItemStackArgument` as our `<fromBlock>` in order to get the material to replace, but the block predicate argument provides a test for a given block, which if satisfied, allows certain code to be executed. 

First, we declare our arguments. We want to use the `BlockPredicateArgument` since it also allows us to use Minecraft tags to identify blocks, as well as individual blocks. We then use `BlockStateArgument` to set the block to a given type. The `BlockStateArgument` also allows the user to provide any block data (e.g. contents of a chest or a stair's orientation).

```java
LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
arguments.put("radius", new IntegerArgument());
arguments.put("fromBlock", new BlockPredicateArgument());
arguments.put("toBlock", new BlockStateArgument());
```

We then register our `/replace` command. First, we parse the arguments making sure to cast to `Predicate<Block>` and `BlockData` (and not `BlockState`).

```java
new CommandAPICommand("replace")
.withArguments(arguments)
.executesPlayer((player, args) -> {
	
	// Parse the arguments
	int radius = (int) args[0];
	@SuppressWarnings("unchecked")
	Predicate<Block> predicate = (Predicate<Block>) args[1];
	BlockData blockData = (BlockData) args[2];
	
	// Find a (solid) sphere of blocks around the player with a given radius
	ArrayList<Block> sphere = new ArrayList<Block>();
	Location center = player.getLocation();
	for (int Y = -radius; Y < radius; Y++) {
		for (int X = -radius; X < radius; X++) {
			for (int Z = -radius; Z < radius; Z++) {
				if (Math.sqrt((X * X) + (Y * Y) + (Z * Z)) <= radius) {
					Block block = center.getWorld().getBlockAt(X + center.getBlockX(), Y + center.getBlockY(), Z + center.getBlockZ());
					sphere.add(block);
				}
			}
		}
	}
	
	// Iterate through the blocks in the radius
	for(Block block : sphere) {
		
		// If that block matches a block from the predicate, set it
		if(predicate.test(block)) {
			block.setType(blockData.getMaterial());
			block.setBlockData(blockData);
		}
	}
})
.register();
```



</div>

