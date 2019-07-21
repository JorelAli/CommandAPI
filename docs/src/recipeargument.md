# Recipe argument

The `RecipeArgument` class lets you retrieve Bukkit's `Recipe` object.

## Example - Giving the result of a recipe

```java
LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
arguments.put("recipe", new RecipeArgument());

CommandAPI.getInstance().register("giverecipe", arguments, (sender, args) -> {
    Recipe recipe = (Recipe) args[0];
    Player player = (Player) sender;        

    player.getInventory().addItem(recipe.getResult());
});
```

