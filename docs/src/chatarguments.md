# Chat arguments

## Chat color argument

The `ChatColorArgument` class is used to represent a given chat color (e.g. red or green)

### Example - Username color changing plugin

```java
LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
arguments.put("chatcolor", new ChatColorArgument());

CommandAPI.getInstance().register("namecolor", arguments, (sender, args) -> {
	Player player = (Player) sender;
	ChatColor color = (ChatColor) args[0];
	player.setDisplayName(color + player.getDisplayName());
});
```
## Chat component argument

> **Developer's Note:**
>
> The `ChatComponentArgument` class is dependent on a Spigot based server. This means that the `ChatComponentArgument` will not work on a non-Spigot based server, such as CraftBukkit. If you use this class on a non-Spigot based server, it will throw a `SpigotNotFoundException`
> 
> Spigot based servers include, but are not limited to:
> * Spigot
> * PaperSpigot
> * TacoSpigot

The `ChatComponentArgument` class accepts raw JSON as valid input. This is converted into Spigot's `BaseComponent[]` which can be used for books and raw messages. You can read more about raw JSON [here](https://minecraft.gamepedia.com/Commands#Raw_JSON_text).

### Example - Book made from raw JSON

```java
LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
arguments.put("contents", new ChatComponentArgument());

CommandAPI.getInstance().register("makebook", arguments, (sender, args) -> {
	if (sender instanceof Player) {
		Player player = (Player) sender;
		BaseComponent[] arr = (BaseComponent[]) args[0];
		
        //Create book
		ItemStack is = new ItemStack(Material.WRITTEN_BOOK);
		BookMeta meta = (BookMeta) is.getItemMeta(); 
		meta.spigot().addPage(arr);
		is.setItemMeta(meta);
		
        //Give player the book
		player.getInventory().addItem(is);
	}
});
```
