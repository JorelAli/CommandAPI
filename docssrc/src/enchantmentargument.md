# Enchantment argument

![](./images/arguments/enchantment.png)

The `EnchantmentArgument` class lets users input a specific enchantment. As you would expect, the cast type is Bukkit's `Enchantment` class.

<div class="example">

### Example - Giving a player an enchantment on their current item

Say we want to give a player an enchantment on the item that the player is currently holding. We will use the following command syntax:

```mccmd
/enchantitem <enchantment> <level>
```

Since most enchantment levels range between 1 and 5, we will also make use of the `IntegerArgument` to restrict the level of the enchantment by usng its range constructor.

```java
{{#include ../../CommandAPI/commandapi-core/src/test/java/Examples.java:enchantmentarguments}}
```

</div>

