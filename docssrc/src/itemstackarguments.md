# Itemstack arguments

![](./images/arguments/itemstack.png)

The `ItemStackArgument` class represents in-game items. As expected, this should be casted to Bukkit's `ItemStack` object.

<div class="example">

### Example - Giving a player an itemstack

Say we want to create a command that gives you items. For this command, we will use the following structure:

```
/item <itemstack>
```

With this structure, we can easily create our command:

```java
{{#include ../../CommandAPI/commandapi-core/src/test/java/Examples.java:itemstackarguments}}
```

</div>