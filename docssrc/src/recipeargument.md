# Recipe arguments

![](./images/arguments/recipe.png)

The `RecipeArgument` class lets you retrieve Bukkit's `Recipe` object. Unlike the other arguments, **this argument has a slightly different implementation for Minecraft 1.15+**.

> **Developer's Note:**
>
> In Minecraft 1.15 and onwards, Bukkit now has the [`ComplexRecipe`](https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/inventory/ComplexRecipe.html) class. This class is a subclass of the typical `Recipe` class, so casting still works when casting to `Recipe`. The only major difference is the `ComplexRecipe` class also inherits the `Keyed` interface, allowing you to access its `NamespacedKey`. This is used to grant the recipe to players. (This is shown in the second example below).

<div class="example">

### Example - Giving a player the result of a recipe

Say we want to give youself the result of a specific recipe. Since Bukkit's `Recipe` class contains the `getResult()` method, we will use that in our example. We want to create the following command:

```
/giverecipe <recipe>
```

As such, we easily implement it by specifying the `RecipeArgument`, casting it and adding it to the player's inventory:

```java
{{#include ../../CommandAPI/commandapi-core/src/test/java/Examples.java:recipearguments}}
```

</div>

<div class="example">

### Example - Unlocking a recipe for a player (1.15+ only)

Since 1.15 has the `ComplexRecipe` class, we will take advantage of this to unlock a recipe for a player. For this command, we'll use the following structure:

```
/unlockrecipe <player> <recipe>
```

Since the `discoverRecipe(NamespacedKey)` method for a player requires a `NamespacedKey`, we have to ensure that this recipe is of type `ComplexRecipe`. This is simply done by using `instanceof`. If this is the case, then we can unlock the recipe for the player. Otherwise, we cannot, so we fail gracefully using the `CommandAPI.fail(String)` method;

```java
{{#include ../../CommandAPI/commandapi-core/src/test/java/Examples.java:recipearguments2}}
```

</div>