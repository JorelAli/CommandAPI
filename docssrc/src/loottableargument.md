# LootTable argument

![](./images/arguments/loottable.png)

The `LootTableArgument` class can be used to get a Bukkit `LootTable` object.

<div class="example">

### Example - Filling a chest with loot table contents

Say we wanted to write a command that populates a chest with some loot table contents. For this example, we'll use the following command:

```
/giveloottable <loottable> <location>
```

We ensure that the location provided is a container (such as a chest or shulkerbox) and then update the contents of that container:

```java
{{#include ../../CommandAPI/commandapi-core/src/test/java/Examples.java:loottablearguments}}
```


</div>